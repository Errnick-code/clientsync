package dev.errnicraft.clientsync.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManifestBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger("ClientSync/Manifest");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static class ModEntry {
        public String mod_id;
        public String version;
        public String hash;
        public String path;
        public String file_name;
        public long size;
    }

    public static class FileEntry {
        public String path;
        public String hash;
        public long size;
    }

    public static class Manifest {
        public String folder;
        public List<ModEntry> mods;
        public List<FileEntry> files;
    }

    public static class GlobalManifest {
        public List<FileEntry> files = new ArrayList<>();
    }

    public static class RemovedEntry {
        public String category;
        public String key;
        public String fileName;
    }

    public static class RemovedManifest {
        public List<RemovedEntry> removed = new ArrayList<>();
    }

    private final Path serverDir;

    public ManifestBuilder(Path serverDir) {
        this.serverDir = serverDir;
    }

    public Path clientDir() {
        return serverDir.resolve("clientsync/client");
    }

    public Path manifestsDir() {
        return serverDir.resolve("clientsync/manifests");
    }

    public void rebuild() throws IOException {
        rebuild(null);
    }

    public void rebuild(ServerConfig config) throws IOException {
        Path clientDir = clientDir();
        Path manifestsDir = manifestsDir();

        if (!Files.exists(clientDir)) {
            Files.createDirectories(clientDir);
        }
        Files.createDirectories(manifestsDir);

        java.util.Map<String, String> previousModIdToFileName = readPreviousModIds(manifestsDir);
        java.util.Map<String, java.util.Set<String>> previousFilesByFolder = readPreviousFilePaths(manifestsDir);

        GlobalManifest global = new GlobalManifest();
        if (Files.exists(clientDir)) {
            try (var stream = Files.list(clientDir)) {
                List<Path> entries = stream.toList();
                for (Path entry : entries) {
                    if (Files.isRegularFile(entry)) {
                        FileEntry fe = new FileEntry();
                        fe.path = entry.getFileName().toString();
                        fe.hash = sha256(entry);
                        fe.size = Files.size(entry);
                        global.files.add(fe);
                    }
                }
            }
        }
        Files.writeString(manifestsDir.resolve("global.json"), GSON.toJson(global));

        java.util.Set<String> currentModIds = new java.util.HashSet<>();
        java.util.Map<String, java.util.Set<String>> currentFilesByFolder = new java.util.HashMap<>();
        boolean modsFolderPresent = false;

        if (Files.exists(clientDir)) {
            try (var stream = Files.list(clientDir)) {
                List<Path> dirs = stream.filter(Files::isDirectory).toList();
                for (Path dir : dirs) {
                    String folderName = dir.getFileName().toString();
                    if (folderName.equals("mods")) {
                        modsFolderPresent = true;
                        List<ModEntry> mods = buildModsManifest(dir, manifestsDir);
                        for (ModEntry me : mods) currentModIds.add(me.mod_id);
                    } else {
                        java.util.Set<String> paths = buildFileManifest(folderName, dir, manifestsDir);
                        currentFilesByFolder.put(folderName, paths);
                    }
                }
            }
        }

        java.util.List<RemovedEntry> newlyRemoved = new ArrayList<>();

        if (modsFolderPresent || !previousModIdToFileName.isEmpty()) {
            for (var entry : previousModIdToFileName.entrySet()) {
                if (!currentModIds.contains(entry.getKey())) {
                    RemovedEntry re = new RemovedEntry();
                    re.category = "MOD";
                    re.key = entry.getKey();
                    re.fileName = entry.getValue();
                    newlyRemoved.add(re);
                }
            }
        }

        for (var folderEntry : previousFilesByFolder.entrySet()) {
            String folder = folderEntry.getKey();
            java.util.Set<String> currentPaths = currentFilesByFolder.getOrDefault(folder, java.util.Set.of());
            for (String oldPath : folderEntry.getValue()) {
                if (!currentPaths.contains(oldPath)) {
                    RemovedEntry re = new RemovedEntry();
                    re.category = "FILE";
                    re.key = oldPath;
                    re.fileName = "";
                    newlyRemoved.add(re);
                }
            }
        }

        if (!newlyRemoved.isEmpty()) {
            appendRemoved(manifestsDir, newlyRemoved);
            for (RemovedEntry re : newlyRemoved) {
                LOGGER.info("[ClientSync] Removal detected on server: {} '{}' ({})",
                        re.category, re.key, re.fileName.isEmpty() ? re.key : re.fileName);
            }
        }
    }

    private java.util.Map<String, String> readPreviousModIds(Path manifestsDir) {
        java.util.Map<String, String> result = new java.util.HashMap<>();
        Path modsJson = manifestsDir.resolve("mods.json");
        if (!Files.isRegularFile(modsJson)) return result;
        try {
            String json = Files.readString(modsJson);
            Manifest manifest = GSON.fromJson(json, Manifest.class);
            if (manifest != null && manifest.mods != null) {
                for (ModEntry me : manifest.mods) {
                    result.put(me.mod_id, me.file_name);
                }
            }
        } catch (Exception ignored) {}
        return result;
    }

    private java.util.Map<String, java.util.Set<String>> readPreviousFilePaths(Path manifestsDir) {
        java.util.Map<String, java.util.Set<String>> result = new java.util.HashMap<>();
        if (!Files.isDirectory(manifestsDir)) return result;
        try (var stream = Files.list(manifestsDir)) {
            for (Path file : stream.toList()) {
                String name = file.getFileName().toString();
                if (!name.endsWith(".json") || name.equals("global.json")
                        || name.equals("mods.json") || name.equals("removed.json")) continue;
                try {
                    Manifest manifest = GSON.fromJson(Files.readString(file), Manifest.class);
                    if (manifest == null || manifest.files == null || manifest.folder == null) continue;
                    java.util.Set<String> paths = new java.util.HashSet<>();
                    for (FileEntry fe : manifest.files) paths.add(fe.path);
                    result.put(manifest.folder, paths);
                } catch (Exception ignored) {}
            }
        } catch (IOException ignored) {}
        return result;
    }

    private void appendRemoved(Path manifestsDir, List<RemovedEntry> newlyRemoved) throws IOException {
        Path removedJson = manifestsDir.resolve("removed.json");
        RemovedManifest manifest = new RemovedManifest();
        if (Files.isRegularFile(removedJson)) {
            try {
                RemovedManifest existing = GSON.fromJson(Files.readString(removedJson), RemovedManifest.class);
                if (existing != null && existing.removed != null) {
                    manifest.removed.addAll(existing.removed);
                }
            } catch (Exception ignored) {}
        }

        java.util.Set<String> existingKeys = new java.util.HashSet<>();
        for (RemovedEntry re : manifest.removed) existingKeys.add(re.category + ":" + re.key);

        for (RemovedEntry re : newlyRemoved) {
            if (existingKeys.add(re.category + ":" + re.key)) {
                manifest.removed.add(re);
            }
        }

        Files.writeString(removedJson, GSON.toJson(manifest));
    }

    private List<ModEntry> buildModsManifest(Path modsDir, Path manifestsDir) throws IOException {
        Manifest manifest = new Manifest();
        manifest.folder = "mods";
        manifest.mods = new ArrayList<>();
        manifest.files = new ArrayList<>();

        scanMods(modsDir, modsDir, manifest);
        manifest.mods = resolveDuplicateModIds(manifest.mods);
        Files.writeString(manifestsDir.resolve("mods.json"), GSON.toJson(manifest));
        return manifest.mods;
    }

    private List<ModEntry> resolveDuplicateModIds(List<ModEntry> mods) {
        java.util.Map<String, List<ModEntry>> byModId = new java.util.LinkedHashMap<>();
        for (ModEntry me : mods) {
            byModId.computeIfAbsent(me.mod_id, k -> new ArrayList<>()).add(me);
        }

        List<ModEntry> result = new ArrayList<>();
        for (var entry : byModId.entrySet()) {
            List<ModEntry> variants = entry.getValue();
            if (variants.size() == 1) {
                result.add(variants.get(0));
                continue;
            }

            ModEntry best = variants.get(0);
            for (ModEntry candidate : variants) {
                if (compareVersions(candidate.version, best.version) > 0) {
                    best = candidate;
                }
            }

            List<String> skipped = new ArrayList<>();
            for (ModEntry variant : variants) {
                if (variant != best) skipped.add(variant.file_name + " (" + variant.version + ")");
            }
            LOGGER.warn("[ClientSync] Duplicate mod_id '{}' found. Using {} ({}), skipped: {}. It is recommended to remove the extra files from the mods folder manually.",
                    entry.getKey(), best.file_name, best.version, String.join(", ", skipped));

            result.add(best);
        }
        return result;
    }

    private int compareVersions(String v1, String v2) {
        List<Long> a = parseVersionSegments(v1);
        List<Long> b = parseVersionSegments(v2);
        int len = Math.max(a.size(), b.size());
        for (int i = 0; i < len; i++) {
            long x = i < a.size() ? a.get(i) : 0;
            long y = i < b.size() ? b.get(i) : 0;
            if (x != y) return Long.compare(x, y);
        }
        return v1.compareTo(v2);
    }

    private List<Long> parseVersionSegments(String version) {
        List<Long> segments = new ArrayList<>();
        if (version == null) return segments;
        String numericPart = version.split("[+\\-]")[0];
        for (String part : numericPart.split("\\.")) {
            String digits = part.replaceAll("[^0-9]", "");
            if (digits.isEmpty()) continue;
            try {
                segments.add(Long.parseLong(digits));
            } catch (NumberFormatException ignored) {}
        }
        return segments;
    }

    private void scanMods(Path root, Path current, Manifest manifest) throws IOException {
        if (!Files.exists(current)) return;
        try (var stream = Files.list(current)) {
            for (Path entry : stream.toList()) {
                if (Files.isDirectory(entry)) {
                    scanMods(root, entry, manifest);
                } else if (Files.isRegularFile(entry)) {
                    String relPath = root.relativize(entry).toString().replace('\\', '/');
                    String fileName = entry.getFileName().toString();
                    String hash = sha256(entry);
                    if (fileName.endsWith(".jar")) {
                        ModEntry me = readModMeta(entry, hash, relPath);
                        me.size = Files.size(entry);
                        manifest.mods.add(me);
                    } else {
                        FileEntry fe = new FileEntry();
                        fe.path = relPath;
                        fe.hash = hash;
                        fe.size = Files.size(entry);
                        manifest.files.add(fe);
                    }
                }
            }
        }
    }

    private ModEntry readModMeta(Path jar, String hash, String relPath) {
        ModEntry me = new ModEntry();
        me.hash = hash;
        me.path = "mods/" + relPath;
        me.file_name = jar.getFileName().toString();
        try (ZipFile zf = new ZipFile(jar.toFile())) {
            ZipEntry ze = zf.getEntry("fabric.mod.json");
            if (ze != null) {
                try (InputStream is = zf.getInputStream(ze)) {
                    JsonObject obj = JsonParser.parseString(new String(is.readAllBytes())).getAsJsonObject();
                    me.mod_id = obj.has("id") ? obj.get("id").getAsString() : jar.getFileName().toString();
                    me.version = obj.has("version") ? obj.get("version").getAsString() : "unknown";
                }
            } else {
                me.mod_id = jar.getFileName().toString().replaceAll("\\.jar$", "");
                me.version = "unknown";
            }
        } catch (Exception e) {
            me.mod_id = jar.getFileName().toString().replaceAll("\\.jar$", "");
            me.version = "unknown";
        }
        return me;
    }

    private java.util.Set<String> buildFileManifest(String folderName, Path dir, Path manifestsDir) throws IOException {
        Manifest manifest = new Manifest();
        manifest.folder = folderName;
        manifest.mods = null;
        manifest.files = new ArrayList<>();

        scanFiles(folderName, dir, dir, manifest);
        Files.writeString(manifestsDir.resolve(folderName + ".json"), GSON.toJson(manifest));

        java.util.Set<String> paths = new java.util.HashSet<>();
        for (FileEntry fe : manifest.files) paths.add(fe.path);
        return paths;
    }

    private void scanFiles(String folderName, Path root, Path current, Manifest manifest) throws IOException {
        if (!Files.exists(current)) return;
        try (var stream = Files.list(current)) {
            for (Path entry : stream.toList()) {
                if (Files.isDirectory(entry)) {
                    scanFiles(folderName, root, entry, manifest);
                } else if (Files.isRegularFile(entry)) {
                    String relPath = root.relativize(entry).toString().replace('\\', '/');
                    FileEntry fe = new FileEntry();
                    fe.path = folderName + "/" + relPath;
                    fe.hash = sha256(entry);
                    fe.size = Files.size(entry);
                    manifest.files.add(fe);
                }
            }
        }
    }

    private static String sha256(Path file) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(Files.readAllBytes(file));
            return HexFormat.of().formatHex(digest.digest());
        } catch (Exception e) {
            throw new IOException("sha256 failed", e);
        }
    }
}
