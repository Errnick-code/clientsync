package dev.errnicraft.clientsync.model;

public class ManifestMod {
    public final String modId;
    public final String version;
    public final String hash;
    public final String remotePath;
    public final String fileName;
    public final long size;
    public final String folder;

    public ManifestMod(String modId, String version, String hash, String remotePath, String fileName, long size, String folder) {
        this.modId = modId;
        this.version = version;
        this.hash = hash;
        this.remotePath = remotePath;
        this.fileName = fileName;
        this.size = size;
        this.folder = folder;
    }
}
