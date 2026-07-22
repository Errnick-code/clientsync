package dev.errnicraft.clientsync.sync;

import dev.errnicraft.clientsync.model.DiffEntry;
import dev.errnicraft.clientsync.model.ManifestFile;
import dev.errnicraft.clientsync.model.ManifestMod;
import dev.errnicraft.clientsync.model.ManifestRemoved;
import dev.errnicraft.clientsync.model.SyncManifest;

import java.util.ArrayList;
import java.util.List;

public class DiffCalculator {

    public static List<DiffEntry> calculate(SyncManifest manifest, LocalIndex index) {
        List<DiffEntry> diff = new ArrayList<>();

        for (ManifestMod serverMod : manifest.mods) {
            List<LocalIndex.ModEntry> localVariants = index.getModsByModId(serverMod.modId);

            LocalIndex.ModEntry matching = null;
            for (LocalIndex.ModEntry local : localVariants) {
                if (local.hash.equalsIgnoreCase(serverMod.hash)) {
                    matching = local;
                    break;
                }
            }

            if (matching == null) {
                DiffEntry.Type type = localVariants.isEmpty() ? DiffEntry.Type.MISSING : DiffEntry.Type.REPAIR;
                diff.add(new DiffEntry(DiffEntry.Category.MOD, type, serverMod.modId,
                        serverMod.remotePath, serverMod.fileName, serverMod.size));
            }

            for (LocalIndex.ModEntry local : localVariants) {
                if (local == matching) continue;
                diff.add(new DiffEntry(DiffEntry.Category.MOD, DiffEntry.Type.STALE,
                        serverMod.modId, null, local.fileName, 0, local.path));
            }
        }

        for (ManifestFile serverFile : manifest.files) {
            LocalIndex.FileEntry local = index.getFile(serverFile.path);
            DiffEntry.Type type;
            if (local == null) {
                type = DiffEntry.Type.MISSING;
            } else if (!local.hash.equalsIgnoreCase(serverFile.hash)) {
                type = DiffEntry.Type.REPAIR;
            } else {
                type = DiffEntry.Type.OK;
            }
            if (type != DiffEntry.Type.OK) {
                diff.add(new DiffEntry(DiffEntry.Category.FILE, type, serverFile.path,
                        serverFile.remotePath, "", serverFile.size));
            }
        }

        return diff;
    }

    public static List<DiffEntry> calculateRemoved(SyncManifest manifest, LocalIndex index) {
        List<DiffEntry> removed = new ArrayList<>();
        if (manifest.removed == null) return removed;

        for (ManifestRemoved re : manifest.removed) {
            if ("MOD".equals(re.category)) {
                for (LocalIndex.ModEntry local : index.getModsByModId(re.key)) {
                    removed.add(new DiffEntry(DiffEntry.Category.MOD, DiffEntry.Type.STALE,
                            re.key, null, local.fileName, 0, local.path));
                }
            } else if ("FILE".equals(re.category)) {
                LocalIndex.FileEntry local = index.getFile(re.key);
                if (local == null) continue;
                removed.add(new DiffEntry(DiffEntry.Category.FILE, DiffEntry.Type.STALE,
                        re.key, null, "", 0, local.path));
            }
        }

        return removed;
    }

    public static List<DiffEntry> calculateCleanExtras(SyncManifest manifest, LocalIndex index) {
        List<DiffEntry> extras = new ArrayList<>();

        java.util.Set<String> serverModIds = new java.util.HashSet<>();
        for (ManifestMod m : manifest.mods) serverModIds.add(m.modId);

        for (LocalIndex.ModEntry local : index.allMods()) {
            if (serverModIds.contains(local.modId)) continue;
            extras.add(new DiffEntry(DiffEntry.Category.MOD, DiffEntry.Type.STALE,
                    local.modId, null, local.fileName, 0, local.path));
        }

        java.util.Set<String> serverFilePaths = new java.util.HashSet<>();
        for (ManifestFile f : manifest.files) serverFilePaths.add(f.path);

        for (var entry : index.allFiles().entrySet()) {
            if (serverFilePaths.contains(entry.getKey())) continue;
            extras.add(new DiffEntry(DiffEntry.Category.FILE, DiffEntry.Type.STALE,
                    entry.getKey(), null, "", 0, entry.getValue().path));
        }

        return extras;
    }
}
