package dev.errnicraft.clientsync.model;

public class ManifestRemoved {
    public final String category;
    public final String key;
    public final String fileName;

    public ManifestRemoved(String category, String key, String fileName) {
        this.category = category;
        this.key = key;
        this.fileName = fileName;
    }
}
