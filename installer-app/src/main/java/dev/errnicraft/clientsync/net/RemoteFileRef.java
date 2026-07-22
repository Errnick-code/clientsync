package dev.errnicraft.clientsync.net;

public class RemoteFileRef {

    public final String remotePath;
    public final String localKey;
    public final long size;
    public final String expectedHash;

    public RemoteFileRef(String remotePath, String localKey, long size, String expectedHash) {
        this.remotePath = remotePath;
        this.localKey = localKey;
        this.size = size;
        this.expectedHash = expectedHash;
    }
}
