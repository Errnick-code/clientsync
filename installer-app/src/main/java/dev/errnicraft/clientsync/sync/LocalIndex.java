package dev.errnicraft.clientsync.sync;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.errnicraft.clientsync.model.SyncManifest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class LocalIndex {

    public static class ModEntry {
        public final String modId;
        public final String version;
        public final String hash;
        public final String fileName;
        public final Path path;

        public ModEntry(String modId, String version, String hash, String fileName, Path path) {
            this.modId = modId;
            this.version = version;
            this.hash = hash;
            this.fileName = fileName;
            this.path = path;
        }
    }

    public static class FileEntry {
        public final String hash;
        public final Path path;

        public FileEntry(String hash, Path path) {
            this.hash = hash;
            this.path = path;
        }
    }

    private final Path gameDir;

    private final Map<String, ModEntry> mods = new HashMap<>();
    private final Map<String, List<ModEntry>> modsByModId = new HashMap<>();
    private final Map<String, FileEntry> files = new HashMap<>();

    public LocalIndex(Path gameDir) {
        this.gameDir = gameDir;
    }

    public void load() {
        mods.clear();
        modsByModId.clear();
        files.clear();
        scanMods(gameDir.resolve("mods"));
    }

    public void loadForManifest(SyncManifest manifest) {
        load();
        for (String folder : foldersOf(manifest)) {
            if (folder.equals("mods") || folder.isEmpty()) continue;
            scanFolder(folder, gameDir.resolve(folder));
        }
    }

    private List<String> foldersOf(SyncManifest manifest) {
        List<String> result = new ArrayList<>();
        for (var f : manifest.files) {
            int slash = f.path.indexOf('/');
            String folder = slash < 0 ? f.path : f.path.substring(0, slash);
            if (!result.contains(folder)) result.add(folder);
        }
        if (manifest.removed != null) {
            for (var r : manifest.removed) {
                if (!"FILE".equals(r.category)) continue;
                int slash = r.key.indexOf('/');
                String folder = slash < 0 ? r.key : r.key.substring(0, slash);
                if (!folder.isEmpty() && !result.contains(folder)) result.add(folder);
            }
        }
        return result;
    }

    private void scanMods(Path modsDir) {
        if (!Files.isDirectory(modsDir)) return;
        try (var stream = Files.list(modsDir)) {
            for (Path entry : stream.toList()) {
                if (!Files.isRegularFile(entry)) continue;
                String fileName = entry.getFileName().toString();
                if (!fileName.endsWith(".jar")) continue;

                String hash = safeSha256(entry);
                String modId = readModId(entry, fileName);
                String version = readModVersion(entry);

                ModEntry me = new ModEntry(modId, version, hash, fileName, entry);
                mods.put(fileName, me);
                modsByModId.computeIfAbsent(modId, k -> new ArrayList<>()).add(me);
            }
        } catch (IOException ignored) {}
    }

    private void scanFolder(String folderName, Path dir) {
        if (!Files.isDirectory(dir)) return;
        try {
            scanFolderRecursive(folderName, dir, dir);
        } catch (IOException ignored) {}
    }

    private void scanFolderRecursive(String folderName, Path root, Path current) throws IOException {
        try (var stream = Files.list(current)) {
            for (Path entry : stream.toList()) {
                if (Files.isDirectory(entry)) {
                    scanFolderRecursive(folderName, root, entry);
                } else if (Files.isRegularFile(entry)) {
                    String relPath = root.relativize(entry).toString().replace('\\', '/');
                    String key = folderName + "/" + relPath;
                    files.put(key, new FileEntry(safeSha256(entry), entry));
                }
            }
        }
    }

    private String readModId(Path jar, String fileName) {
        try (ZipFile zf = new ZipFile(jar.toFile())) {
            ZipEntry ze = zf.getEntry("fabric.mod.json");
            if (ze != null) {
                try (InputStream is = zf.getInputStream(ze)) {
                    JsonObject obj = JsonParser.parseString(new String(is.readAllBytes())).getAsJsonObject();
                    if (obj.has("id")) return obj.get("id").getAsString();
                }
            }
        } catch (Exception ignored) {}
        return fileName.replaceAll("\\.jar$", "");
    }

    private String readModVersion(Path jar) {
        try (ZipFile zf = new ZipFile(jar.toFile())) {
            ZipEntry ze = zf.getEntry("fabric.mod.json");
            if (ze != null) {
                try (InputStream is = zf.getInputStream(ze)) {
                    JsonObject obj = JsonParser.parseString(new String(is.readAllBytes())).getAsJsonObject();
                    if (obj.has("version")) return obj.get("version").getAsString();
                }
            }
        } catch (Exception ignored) {}
        return "unknown";
    }

    private String safeSha256(Path file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(Files.readAllBytes(file));
            return HexFormat.of().formatHex(digest.digest());
        } catch (Exception e) {
            return "";
        }
    }

    public ModEntry getModByFileName(String fileName) {
        return mods.get(fileName);
    }

    public List<ModEntry> getModsByModId(String modId) {
        return modsByModId.getOrDefault(modId, List.of());
    }

    public FileEntry getFile(String key) {
        return files.get(key);
    }

    public List<ModEntry> allMods() {
        return new ArrayList<>(mods.values());
    }

    public Map<String, FileEntry> allFiles() {
        return new HashMap<>(files);
    }

    public String modIdOf(ModEntry entry) {
        return entry.modId;
    }
}