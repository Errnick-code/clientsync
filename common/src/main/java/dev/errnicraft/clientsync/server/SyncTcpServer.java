package dev.errnicraft.clientsync.server;

import dev.errnicraft.clientsync.net.SyncProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SyncTcpServer {

    private static final Logger LOGGER = LoggerFactory.getLogger("ClientSync/TCP");

    private final int port;
    private final Path serverDir;
    private final ServerConfig config;

    private ServerSocket serverSocket;
    private ExecutorService acceptExecutor;
    private ExecutorService workerExecutor;
    private volatile boolean running;

    public SyncTcpServer(int port, Path serverDir, ServerConfig config) {
        this.port = port;
        this.serverDir = serverDir;
        this.config = config;
    }

    public int getPort() {
        if (serverSocket != null && serverSocket.isBound()) {
            return serverSocket.getLocalPort();
        }
        return port;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        workerExecutor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r, "clientsync-tcp-worker");
            t.setDaemon(true);
            return t;
        });
        acceptExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "clientsync-tcp-accept");
            t.setDaemon(true);
            return t;
        });

        running = true;
        acceptExecutor.submit(this::acceptLoop);
        LOGGER.info("[ClientSync] TCP server started on port {}", port);
    }

    private void acceptLoop() {
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                workerExecutor.submit(() -> handleClient(socket));
            } catch (IOException e) {
                if (running) {
                    LOGGER.error("[ClientSync] Accept failed", e);
                }
            }
        }
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException ignored) {}
        if (acceptExecutor != null) acceptExecutor.shutdownNow();
        if (workerExecutor != null) workerExecutor.shutdownNow();
        LOGGER.info("[ClientSync] TCP server stopped.");
    }

    private void handleClient(Socket socket) {
        try (socket;
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            socket.setTcpNoDelay(true);
            socket.setSoTimeout(SyncProtocol.IDLE_TIMEOUT_MS);

            while (running) {
                int magic;
                try {
                    magic = in.readInt();
                } catch (IOException eof) {
                    break;
                }
                if (magic != SyncProtocol.MAGIC) break;
                int version = in.readInt();
                if (version != SyncProtocol.VERSION) break;

                byte op = in.readByte();
                switch (op) {
                    case SyncProtocol.OP_PING -> handlePing(out, socket);
                    case SyncProtocol.OP_GET_INDEX -> handleGetIndex(out);
                    case SyncProtocol.OP_GET_MANIFEST -> handleGetManifest(in, out);
                    case SyncProtocol.OP_GET_BATCH -> handleGetBatch(in, out);
                    case SyncProtocol.OP_GET_CHUNK -> handleGetChunk(in, out);
                    case SyncProtocol.OP_GET_AUTO_SCOPE -> handleGetAutoScope(out);
                    default -> {
                        writeStatus(out, SyncProtocol.STATUS_ERROR);
                        return;
                    }
                }
                out.flush();
            }
        } catch (IOException e) {
            LOGGER.error("[ClientSync] Client handling error", e);
        }
    }

    private void handlePing(DataOutputStream out, Socket socket) throws IOException {
        writeStatus(out, SyncProtocol.STATUS_OK);
        writeString(out, "clientsync");
        writeString(out, config.mc_version);
        out.writeInt(config.max_concurrent_tasks);
        out.writeInt(config.chunk_size_mb);
        writeString(out, config.loader);
        writeString(out, config.loader_version);
        writeString(out, config.pack_version == null ? "" : config.pack_version);
        String clientIp = socket.getInetAddress() != null ? socket.getInetAddress().getHostAddress() : "unknown";
        LOGGER.info("[ClientSync] Ping from {} (mc {}, loader {} {})",
                clientIp, config.mc_version, config.loader, config.loader_version);
    }

    private void handleGetAutoScope(DataOutputStream out) throws IOException {
        java.util.List<String> scope = new java.util.ArrayList<>();
        if (config.auto_update != null) {
            for (String raw : config.auto_update) {
                String entry = normalizeScopeEntry(raw);
                if (!entry.isEmpty() && !scope.contains(entry)) {
                    scope.add(entry);
                }
            }
        }
        writeStatus(out, SyncProtocol.STATUS_OK);
        out.writeInt(scope.size());
        for (String entry : scope) writeString(out, entry);
    }

    private static String normalizeScopeEntry(String raw) {
        if (raw == null) return "";
        String entry = raw.trim().replace('\\', '/');
        while (entry.startsWith("/")) entry = entry.substring(1);
        while (entry.endsWith("/")) entry = entry.substring(0, entry.length() - 1);
        return entry;
    }

    private void handleGetIndex(DataOutputStream out) throws IOException {
        Path manifestsDir = serverDir.resolve("clientsync/manifests");
        if (!Files.exists(manifestsDir)) {
            writeStatus(out, SyncProtocol.STATUS_OK);
            out.writeInt(0);
            return;
        }
        java.util.List<String> names = new java.util.ArrayList<>();
        try (var stream = Files.list(manifestsDir)) {
            for (Path p : stream.toList()) {
                if (p.getFileName().toString().endsWith(".json")) {
                    names.add(p.getFileName().toString());
                }
            }
        }
        writeStatus(out, SyncProtocol.STATUS_OK);
        out.writeInt(names.size());
        for (String name : names) writeString(out, name);
    }

    private void handleGetManifest(DataInputStream in, DataOutputStream out) throws IOException {
        String name = readString(in);
        Path base = serverDir.resolve("clientsync/manifests").normalize();
        Path file = name.isEmpty() ? null : base.resolve(name).normalize();
        if (file == null || !file.startsWith(base) || !Files.isRegularFile(file)) {
            writeStatus(out, SyncProtocol.STATUS_NOT_FOUND);
            return;
        }
        byte[] bytes = Files.readAllBytes(file);
        if (bytes.length > SyncProtocol.MAX_MANIFEST_BYTES) {
            writeStatus(out, SyncProtocol.STATUS_ERROR);
            return;
        }
        writeStatus(out, SyncProtocol.STATUS_OK);
        out.writeInt(bytes.length);
        out.write(bytes);
    }

    private void handleGetBatch(DataInputStream in, DataOutputStream out) throws IOException {
        int count = in.readInt();
        if (count < 0 || count > SyncProtocol.MAX_BATCH_FILES) {
            writeStatus(out, SyncProtocol.STATUS_ERROR);
            return;
        }
        String[] rels = new String[count];
        for (int i = 0; i < count; i++) rels[i] = readString(in);

        writeStatus(out, SyncProtocol.STATUS_OK);
        out.writeInt(count);
        for (String rel : rels) {
            Path file = resolveClientFile(rel);
            if (file == null || !Files.isRegularFile(file)) {
                out.writeByte(SyncProtocol.STATUS_NOT_FOUND);
                writeString(out, rel);
                out.writeLong(0);
                continue;
            }
            long size = Files.size(file);
            if (size > SyncProtocol.MAX_BATCH_FILE_BYTES) {
                out.writeByte(SyncProtocol.STATUS_ERROR);
                writeString(out, rel);
                out.writeLong(0);
                continue;
            }
            byte[] bytes = Files.readAllBytes(file);
            out.writeByte(SyncProtocol.STATUS_OK);
            writeString(out, rel);
            out.writeLong(bytes.length);
            out.write(bytes);
        }
    }

    private void handleGetChunk(DataInputStream in, DataOutputStream out) throws IOException {
        String rel = readString(in);
        long offset = in.readLong();
        long length = in.readLong();

        Path file = resolveClientFile(rel);
        if (file == null || !Files.isRegularFile(file)) {
            writeStatus(out, SyncProtocol.STATUS_NOT_FOUND);
            return;
        }

        long fileSize = Files.size(file);
        if (offset < 0 || length < 0 || offset > fileSize || length > fileSize - offset) {
            writeStatus(out, SyncProtocol.STATUS_ERROR);
            return;
        }

        writeStatus(out, SyncProtocol.STATUS_OK);
        out.writeLong(length);

        try (var raf = new java.io.RandomAccessFile(file.toFile(), "r")) {
            raf.seek(offset);
            byte[] buf = new byte[64 * 1024];
            long remaining = length;
            while (remaining > 0) {
                int toRead = (int) Math.min(buf.length, remaining);
                int read = raf.read(buf, 0, toRead);
                if (read < 0) break;
                out.write(buf, 0, read);
                remaining -= read;
            }
        }
    }

    private Path resolveClientFile(String rel) {
        if (rel == null || rel.isEmpty()) return null;
        Path base = serverDir.resolve("clientsync/client").normalize();
        Path file = base.resolve(rel).normalize();
        if (!file.startsWith(base)) return null;
        return file;
    }

    private static void writeStatus(DataOutputStream out, byte status) throws IOException {
        out.writeByte(status);
    }

    private static void writeString(DataOutputStream out, String s) throws IOException {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        out.writeInt(bytes.length);
        out.write(bytes);
    }

    private static String readString(DataInputStream in) throws IOException {
        int len = in.readInt();
        if (len < 0 || len > SyncProtocol.MAX_STRING_BYTES) {
            throw new IOException("Invalid string length: " + len);
        }
        byte[] bytes = new byte[len];
        in.readFully(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
