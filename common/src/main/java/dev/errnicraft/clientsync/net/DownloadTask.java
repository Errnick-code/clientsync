package dev.errnicraft.clientsync.net;

import java.util.List;

public class DownloadTask {

    public enum Kind { SMALL_BATCH, FILE_CHUNK }

    public final Kind kind;
    public final List<RemoteFileRef> batch;
    public final RemoteFileRef file;
    public final long offset;
    public final long length;

    private DownloadTask(Kind kind, List<RemoteFileRef> batch, RemoteFileRef file, long offset, long length) {
        this.kind = kind;
        this.batch = batch;
        this.file = file;
        this.offset = offset;
        this.length = length;
    }

    public static DownloadTask smallBatch(List<RemoteFileRef> refs) {
        return new DownloadTask(Kind.SMALL_BATCH, refs, null, 0, 0);
    }

    public static DownloadTask fileChunk(RemoteFileRef file, long offset, long length) {
        return new DownloadTask(Kind.FILE_CHUNK, null, file, offset, length);
    }
}
