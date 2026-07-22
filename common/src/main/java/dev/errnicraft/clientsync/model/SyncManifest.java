package dev.errnicraft.clientsync.model;

import java.util.List;

public class SyncManifest {
    public final List<ManifestMod> mods;
    public final List<ManifestFile> files;
    public final List<ManifestRemoved> removed;

    public SyncManifest(List<ManifestMod> mods, List<ManifestFile> files) {
        this(mods, files, new java.util.ArrayList<>());
    }

    public SyncManifest(List<ManifestMod> mods, List<ManifestFile> files, List<ManifestRemoved> removed) {
        this.mods = mods;
        this.files = files;
        this.removed = removed;
    }
}
