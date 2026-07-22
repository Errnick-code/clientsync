package dev.errnicraft.clientsync.net;

import java.util.ArrayList;
import java.util.List;

public final class TaskBuilder {

    private TaskBuilder() {}

    public static List<DownloadTask> build(List<RemoteFileRef> files) {
        return build(files, SyncProtocol.DEFAULT_CHUNK_BYTES);
    }

    public static List<DownloadTask> build(List<RemoteFileRef> files, long chunkBytes) {
        List<DownloadTask> tasks = new ArrayList<>();

        List<RemoteFileRef> smallBuffer = new ArrayList<>();
        long smallBufferSize = 0;

        for (RemoteFileRef ref : files) {
            if (ref.size >= chunkBytes) {
                for (long offset = 0; offset < ref.size; offset += chunkBytes) {
                    long length = Math.min(chunkBytes, ref.size - offset);
                    tasks.add(DownloadTask.fileChunk(ref, offset, length));
                }
                continue;
            }

            if (smallBufferSize + ref.size > chunkBytes && !smallBuffer.isEmpty()) {
                tasks.add(DownloadTask.smallBatch(new ArrayList<>(smallBuffer)));
                smallBuffer.clear();
                smallBufferSize = 0;
            }

            smallBuffer.add(ref);
            smallBufferSize += ref.size;
        }

        if (!smallBuffer.isEmpty()) {
            tasks.add(DownloadTask.smallBatch(smallBuffer));
        }

        return tasks;
    }
}
