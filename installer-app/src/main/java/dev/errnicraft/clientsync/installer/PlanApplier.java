package dev.errnicraft.clientsync.installer;

import dev.errnicraft.clientsync.sync.InstallPlan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

public class PlanApplier {

    public interface ProgressCallback {
        void onProgress(int done, int total, String currentKey);
        void onError(String key, String message);
    }

    private final Path gameDir;

    public PlanApplier(Path gameDir) {
        this.gameDir = gameDir;
    }

    public void apply(List<InstallPlan.PlanEntry> plan, ProgressCallback callback) {
        int total = plan.size();
        int done = 0;

        for (InstallPlan.PlanEntry entry : plan) {
            callback.onProgress(done, total, entry.key);
            try {
                if (entry.delete) {
                    applyDelete(entry);
                } else {
                    applyInstall(entry);
                }
            } catch (Exception e) {
                callback.onError(entry.key, e.getMessage());
            }
            done++;
        }

        callback.onProgress(total, total, "");
        cleanupCache();
    }

    private void applyInstall(InstallPlan.PlanEntry entry) throws IOException {
        if (entry.cachedFile == null || entry.cachedFile.isEmpty()) return;
        Path cached = Path.of(entry.cachedFile);
        if (!Files.exists(cached)) return;

        Path destination = resolveInsideGameDir(entry.destination);
        if (destination == null) {
            throw new IOException("install destination outside game directory: " + entry.destination);
        }
        Files.createDirectories(destination.getParent());
        Files.move(cached, destination, StandardCopyOption.REPLACE_EXISTING);
    }

    private void applyDelete(InstallPlan.PlanEntry entry) throws IOException {
        if (entry.destination == null || entry.destination.isEmpty()) return;
        Path target = resolveInsideGameDir(entry.destination);
        if (target == null) {
            throw new IOException("delete destination outside game directory: " + entry.destination);
        }
        Files.deleteIfExists(target);
    }

    private Path resolveInsideGameDir(String destination) {
        Path base = gameDir.toAbsolutePath().normalize();
        Path d = Path.of(destination);
        Path target;
        if (d.isAbsolute()) {
            target = d.normalize();
        } else {
            if (d.getNameCount() > 0 && d.getName(0).toString().matches("^[A-Za-z]:.*")) {
                return null;
            }
            target = base.resolve(d).normalize();
        }
        if (!target.startsWith(base)) return null;
        return target;
    }

    private void cleanupCache() {
        Path cacheDir = gameDir.resolve("clientsync/cache");
        if (!Files.isDirectory(cacheDir)) return;
        try (Stream<Path> stream = Files.walk(cacheDir)) {
            stream.sorted((a, b) -> b.getNameCount() - a.getNameCount())
                  .forEach(p -> {
                      try {
                          Files.deleteIfExists(p);
                      } catch (IOException ignored) {}
                  });
        } catch (IOException ignored) {}
    }
}
