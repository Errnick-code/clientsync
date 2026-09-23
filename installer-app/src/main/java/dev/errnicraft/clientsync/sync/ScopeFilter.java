package dev.errnicraft.clientsync.sync;

import dev.errnicraft.clientsync.model.ManifestMod;
import dev.errnicraft.clientsync.model.ManifestFile;
import dev.errnicraft.clientsync.model.ManifestRemoved;
import dev.errnicraft.clientsync.model.SyncManifest;

import java.util.ArrayList;
import java.util.List;

public class ScopeFilter {

    private ScopeFilter() {}

    public static boolean inScope(List<String> scope, String path) {
        if (scope == null || scope.isEmpty() || path == null) return false;
        for (String raw : scope) {
            String entry = normalize(raw);
            if (entry.isEmpty()) continue;
            if (path.equals(entry)) return true;
            if (path.startsWith(entry + "/")) return true;
        }
        return false;
    }

    public static boolean hasScope(List<String> scope) {
        if (scope == null) return false;
        for (String raw : scope) {
            if (!normalize(raw).isEmpty()) return true;
        }
        return false;
    }

    public static SyncManifest filter(SyncManifest source, List<String> scope) {
        List<ManifestMod> mods = new ArrayList<>();
        if (source.mods != null) {
            for (ManifestMod m : source.mods) {
                if (inScope(scope, m.remotePath != null && !m.remotePath.isEmpty() ? m.remotePath : "mods/" + m.fileName)) {
                    mods.add(m);
                }
            }
        }

        List<ManifestFile> files = new ArrayList<>();
        if (source.files != null) {
            for (ManifestFile f : source.files) {
                if (inScope(scope, f.path)) files.add(f);
            }
        }

        List<ManifestRemoved> removed = new ArrayList<>();
        if (source.removed != null) {
            for (ManifestRemoved r : source.removed) {
                if ("MOD".equals(r.category)) {
                    String p = r.fileName != null && !r.fileName.isEmpty() ? "mods/" + r.fileName : null;
                    if (p != null && inScope(scope, p)) removed.add(r);
                } else {
                    if (inScope(scope, r.key)) removed.add(r);
                }
            }
        }

        return new SyncManifest(mods, files, removed);
    }

    private static String normalize(String raw) {
        if (raw == null) return "";
        String e = raw.trim().replace('\\', '/');
        while (e.startsWith("/")) e = e.substring(1);
        while (e.endsWith("/")) e = e.substring(0, e.length() - 1);
        return e;
    }
}