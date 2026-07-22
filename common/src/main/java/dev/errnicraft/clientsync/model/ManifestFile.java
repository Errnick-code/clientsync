package dev.errnicraft.clientsync.model;

public class ManifestFile {
    public final String path;
    public final String hash;
    public final String remotePath;
    public final long size;
    public final String folder;

    public ManifestFile(String path, String hash, String remotePath, long size, String folder) {
        this.path = path;
        this.hash = hash;
        this.remotePath = remotePath;
        this.size = size;
        this.folder = folder;
    }
}
