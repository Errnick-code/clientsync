package dev.errnicraft.clientsync.sync;

import dev.errnicraft.clientsync.model.DiffEntry;
import dev.errnicraft.clientsync.model.SyncManifest;
import dev.errnicraft.clientsync.net.DownloadTask;
import dev.errnicraft.clientsync.net.RemoteFileRef;
import dev.errnicraft.clientsync.net.ServerAddress;
import dev.errnicraft.clientsync.net.SyncProtocol;
import dev.errnicraft.clientsync.net.SyncTcpClient;
import dev.errnicraft.clientsync.net.TaskBuilder;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class FileInstaller {

    public interface ProgressCallback {
        void onProgress(int done, int total, String currentKey);
        void onError(String key, String message);
        default void onBytesDownloaded(long bytes) {}
    }

    private final Path gameDir;
    private final Path cacheDir;
    private final SyncManifest manifest;
    private final String host;
    private final int port;
    private final int maxConcurrentTasks;
    private final long chunkBytes;

    public FileInstaller(Path gameDir, SyncManifest manifest, String serverAddress) {
        this(gameDir, manifest, serverAddress, SyncProtocol.DEFAULT_MAX_CONCURRENT_TASKS,
                SyncProtocol.DEFAULT_CHUNK_BYTES);
    }

    public FileInstaller(Path gameDir, SyncManifest manifest, String serverAddress,
                          int maxConcurrentTasks, long chunkBytes) {
        this.gameDir  = gameDir;
        this.cacheDir = gameDir.resolve("clientsync/cache");
        this.manifest = manifest;

        ServerAddress addr = ServerAddress.resolve(serverAddress);
        this.host = addr.host;
        this.port = addr.port;
        this.maxConcurrentTasks = maxConcurrentTasks > 0 ? maxConcurrentTasks : SyncProtocol.DEFAULT_MAX_CONCURRENT_TASKS;
        this.chunkBytes = chunkBytes > 0 ? chunkBytes : SyncProtocol.DEFAULT_CHUNK_BYTES;
    }

    public void downloadToCache(List<DiffEntry> diff, ProgressCallback callback) throws IOException {
        Files.createDirectories(cacheDir);

        List<DiffEntry> toDownload = new ArrayList<>();
        List<InstallPlan.PlanEntry> plan = new ArrayList<>();

        int total = diff.size();
        AtomicInteger done = new AtomicInteger(0);

        for (DiffEntry entry : diff) {
            if (entry.type == DiffEntry.Type.STALE) {
                plan.add(InstallPlan.PlanEntry.delete(entry));
                done.incrementAndGet();
                callback.onProgress(done.get(), total, entry.key);
            } else {
                toDownload.add(entry);
            }
        }

        Map<String, DiffEntry> byRemotePath = new HashMap<>();
        List<RemoteFileRef> refs = new ArrayList<>();
        for (DiffEntry entry : toDownload) {
            String cachedName = sanitize(entry.key) + "_" + (entry.fileName.isEmpty() ? entry.key.hashCode() : entry.fileName);
            Path cachedPath = cacheDir.resolve(cachedName);
            preallocate(cachedPath, entry.size);
            byRemotePath.put(entry.remotePath, entry);
            refs.add(new RemoteFileRef(entry.remotePath, entry.remotePath, entry.size, resolveHash(entry)));
        }

        List<DownloadTask> tasks = TaskBuilder.build(refs, chunkBytes);

        Map<String, Path> cachedPaths = new ConcurrentHashMap<>();
        for (DiffEntry entry : toDownload) {
            String cachedName = sanitize(entry.key) + "_" + (entry.fileName.isEmpty() ? entry.key.hashCode() : entry.fileName);
            cachedPaths.put(entry.remotePath, cacheDir.resolve(cachedName));
        }

        int workerCount = Math.min(maxConcurrentTasks, Math.max(1, tasks.size()));
        ExecutorService pool = Executors.newFixedThreadPool(workerCount, r -> {
            Thread t = new Thread(r, "clientsync-download-worker");
            t.setDaemon(true);
            return t;
        });
        Semaphore permits = new Semaphore(maxConcurrentTasks);

        List<java.util.concurrent.Future<?>> futures = new ArrayList<>();

        for (DownloadTask task : tasks) {
            futures.add(pool.submit(() -> {
                try {
                    permits.acquire();
                    Exception lastError = null;
                    int maxAttempts = SyncProtocol.MAX_TASK_ATTEMPTS;
                    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                        try (SyncTcpClient client = new SyncTcpClient(host, port)) {
                            client.connect(5000);
                            client.setReadTimeout(30000);
                            runTask(task, client, cachedPaths, callback, done, total);
                            lastError = null;
                            break;
                        } catch (Exception e) {
                            lastError = e;
                            if (attempt < maxAttempts) {
                                try {
                                    Thread.sleep(300L * attempt);
                                } catch (InterruptedException ie) {
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            }
                        }
                    }
                    if (lastError != null) {
                        String key = task.kind == DownloadTask.Kind.FILE_CHUNK
                            ? task.file.localKey : "batch";
                        callback.onError(key, lastError.getMessage() + " (after " + maxAttempts + " attempts)");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    permits.release();
                }
            }));
        }

        for (var f : futures) {
            try {
                f.get();
            } catch (Exception e) {
                callback.onError("task", e.getMessage());
            }
        }
        pool.shutdown();

        List<DiffEntry> failedEntries = verifyAndCollectPlan(toDownload, cachedPaths, plan, callback, false);

        for (int fullAttempt = 1; fullAttempt <= SyncProtocol.MAX_FILE_ATTEMPTS && !failedEntries.isEmpty(); fullAttempt++) {
            List<DiffEntry> retryBatch = failedEntries;

            for (DiffEntry entry : retryBatch) {
                Path cached = cachedPaths.get(entry.remotePath);
                try (SyncTcpClient client = new SyncTcpClient(host, port)) {
                    client.connect(5000);
                    client.setReadTimeout(30000);
                    preallocate(cached, entry.size);
                    try (RandomAccessFile raf = new RandomAccessFile(cached.toFile(), "rw")) {
                        raf.seek(0);
                        client.getChunk(entry.remotePath, 0, entry.size, new RandomAccessOutputStream(raf));
                    }
                } catch (Exception e) {
                    callback.onError(entry.key, "retry " + fullAttempt + " failed: " + e.getMessage());
                }
            }

            failedEntries = verifyAndCollectPlan(retryBatch, cachedPaths, plan, callback, true);
        }

        callback.onProgress(total, total, "");
        InstallPlan.write(gameDir, plan);
    }

    private List<DiffEntry> verifyAndCollectPlan(List<DiffEntry> entries, Map<String, Path> cachedPaths,
                                                  List<InstallPlan.PlanEntry> plan, ProgressCallback callback,
                                                  boolean isRetryPass) {
        List<DiffEntry> failed = new ArrayList<>();
        for (DiffEntry entry : entries) {
            Path cached = cachedPaths.get(entry.remotePath);
            try {
                String actualHash = sha256(cached);
                String expectedHash = resolveHash(entry);
                if (!actualHash.equalsIgnoreCase(expectedHash)) {
                    failed.add(entry);
                    continue;
                }
                String version = entry.category == DiffEntry.Category.MOD
                        ? resolveModVersion(entry.key) : "";
                String destination = resolveDestination(entry);
                if (isRetryPass) {
                    plan.removeIf(pe -> pe.key != null && pe.key.equals(entry.key));
                }
                plan.add(InstallPlan.PlanEntry.install(entry, expectedHash, version, cached.toString(), destination));
            } catch (Exception e) {
                failed.add(entry);
            }
        }
        for (DiffEntry entry : failed) {
            String suffix = isRetryPass ? "hash mismatch after retry" : "hash mismatch, will retry";
            callback.onError(entry.key, suffix);
        }
        return failed;
    }

    private void runTask(DownloadTask task, SyncTcpClient client, Map<String, Path> cachedPaths,
                          ProgressCallback callback, AtomicInteger done, int total) throws IOException {
        if (task.kind == DownloadTask.Kind.FILE_CHUNK) {
            RemoteFileRef ref = task.file;
            Path cached = cachedPaths.get(ref.remotePath);
            try (RandomAccessFile raf = new RandomAccessFile(cached.toFile(), "rw")) {
                raf.seek(task.offset);
                java.io.OutputStream sink = new RandomAccessOutputStream(raf);
                client.getChunk(ref.remotePath, task.offset, task.length, sink);
            }
            callback.onBytesDownloaded(task.length);
            callback.onProgress(done.incrementAndGet(), total, ref.localKey);
        } else {
            List<String> rels = new ArrayList<>();
            for (RemoteFileRef ref : task.batch) rels.add(ref.remotePath);

            client.getBatch(rels, (rel, status, data) -> {
                Path cached = cachedPaths.get(rel);
                if (status != SyncProtocol.STATUS_OK || data == null) {
                    callback.onError(rel, "batch fetch failed, status=" + status);
                } else {
                    Files.write(cached, data);
                    callback.onBytesDownloaded(data.length);
                }
                callback.onProgress(done.incrementAndGet(), total, rel);
            });
        }
    }

    private void preallocate(Path path, long size) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(path.toFile(), "rw")) {
            raf.setLength(size);
        }
    }

    private static class RandomAccessOutputStream extends java.io.OutputStream {
        private final RandomAccessFile raf;
        RandomAccessOutputStream(RandomAccessFile raf) { this.raf = raf; }
        @Override public void write(int b) throws IOException { raf.write(b); }
        @Override public void write(byte[] b, int off, int len) throws IOException { raf.write(b, off, len); }
    }

    private String sha256(Path file) throws IOException, NoSuchAlgorithmException {
        MessageDigest d = MessageDigest.getInstance("SHA-256");
        d.update(Files.readAllBytes(file));
        return HexFormat.of().formatHex(d.digest());
    }

    private String resolveHash(DiffEntry entry) {
        if (entry.category == DiffEntry.Category.MOD) {
            return manifest.mods.stream().filter(m -> m.remotePath.equals(entry.remotePath))
                    .findFirst().map(m -> m.hash).orElse("");
        }
        return manifest.files.stream().filter(f -> f.path.equals(entry.key))
                .findFirst().map(f -> f.hash).orElse("");
    }

    private String resolveModVersion(String modId) {
        return manifest.mods.stream().filter(m -> m.modId.equals(modId))
                .findFirst().map(m -> m.version).orElse("");
    }

    private String resolveDestination(DiffEntry entry) {
        if (entry.category == DiffEntry.Category.MOD) {
            return gameDir.resolve("mods/" + entry.fileName).toString();
        }
        return gameDir.resolve(entry.key).toString();
    }

    private String sanitize(String key) {
        return key.replace("/", "_").replace("\\", "_");
    }
}
