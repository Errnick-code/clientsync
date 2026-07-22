package dev.errnicraft.clientsync.net;

public final class SyncProtocol {

    private SyncProtocol() {}

    public static final int MAGIC = 0x43535943;
    public static final int VERSION = 1;

    public static final byte OP_PING          = 1;
    public static final byte OP_GET_INDEX     = 2;
    public static final byte OP_GET_MANIFEST  = 3;
    public static final byte OP_GET_BATCH     = 4;
    public static final byte OP_GET_CHUNK     = 5;

    public static final byte STATUS_OK        = 0;
    public static final byte STATUS_NOT_FOUND = 1;
    public static final byte STATUS_ERROR     = 2;

    public static final long DEFAULT_CHUNK_BYTES = 10L * 1024 * 1024;
    public static final int DEFAULT_MAX_CONCURRENT_TASKS = 20;
    public static final int MAX_TASK_ATTEMPTS = 3;
    public static final int MAX_FILE_ATTEMPTS = 3;
}
