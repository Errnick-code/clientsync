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
    public static final byte OP_GET_AUTO_SCOPE = 6;

    public static final byte STATUS_OK        = 0;
    public static final byte STATUS_NOT_FOUND = 1;
    public static final byte STATUS_ERROR     = 2;

    public static final long DEFAULT_CHUNK_BYTES = 10L * 1024 * 1024;
    public static final int DEFAULT_MAX_CONCURRENT_TASKS = 20;
    public static final int MAX_TASK_ATTEMPTS = 3;
    public static final int MAX_FILE_ATTEMPTS = 3;

    public static final int MAX_STRING_BYTES = 1024 * 1024;
    public static final int MAX_INDEX_NAMES = 10_000;
    public static final int MAX_MANIFEST_BYTES = 16 * 1024 * 1024;
    public static final int MAX_BATCH_FILES = 100_000;
    public static final long MAX_BATCH_FILE_BYTES = 256L * 1024 * 1024;
    public static final int IDLE_TIMEOUT_MS = 30_000;
    public static final int PING_OPTIONAL_TIMEOUT_MS = 1_000;
}
