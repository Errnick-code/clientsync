package dev.errnicraft.clientsync.model;

import java.nio.file.Path;

public class DiffEntry {

    public enum Type {
        MISSING,
        UPDATE,
        REPAIR,
        STALE,
        OK
    }

    public enum Category {
        MOD,
        FILE
    }

    public final Category category;
    public final Type type;
    public final String key;
    public final String remotePath;
    public final String fileName;
    public final long size;
    public final Path staleLocalPath;

    public DiffEntry(Category category, Type type, String key, String remotePath, String fileName, long size) {
        this(category, type, key, remotePath, fileName, size, null);
    }

    public DiffEntry(Category category, Type type, String key, String remotePath, String fileName, long size, Path staleLocalPath) {
        this.category = category;
        this.type = type;
        this.key = key;
        this.remotePath = remotePath;
        this.fileName = fileName;
        this.size = size;
        this.staleLocalPath = staleLocalPath;
    }
}