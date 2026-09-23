package dev.errnicraft.clientsync.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SyncTcpClient implements AutoCloseable {

    private final String host;
    private final int port;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public SyncTcpClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect(int timeoutMs) throws IOException {
        final IOException[] error = new IOException[1];
        final Socket[] result = new Socket[1];

        Thread connectThread = new Thread(() -> {
            try {
                Socket s = new Socket();
                s.connect(new InetSocketAddress(host, port), timeoutMs);
                result[0] = s;
            } catch (IOException e) {
                error[0] = e;
            }
        }, "clientsync-connect");
        connectThread.setDaemon(true);
        connectThread.start();

        try {
            connectThread.join(timeoutMs + 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Connection interrupted", e);
        }

        if (connectThread.isAlive()) {
            connectThread.interrupt();
            throw new IOException("Connection timed out (DNS/connect hung) to " + host + ":" + port);
        }
        if (error[0] != null) {
            throw error[0];
        }
        if (result[0] == null) {
            throw new IOException("Failed to connect to " + host + ":" + port);
        }

        socket = result[0];
        socket.setTcpNoDelay(true);
        socket.setSoTimeout(timeoutMs);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public void setReadTimeout(int timeoutMs) throws IOException {
        if (socket != null) socket.setSoTimeout(timeoutMs);
    }

    @Override
    public void close() {
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
    }

    public static class PingInfo {
        public final boolean ok;
        public final String mcVersion;
        public final int maxConcurrentTasks;
        public final int chunkSizeMb;
        public final String loader;
        public final String loaderVersion;
        public final String packVersion;

        PingInfo(boolean ok, String mcVersion, int maxConcurrentTasks, int chunkSizeMb, String loader, String loaderVersion, String packVersion) {
            this.ok = ok;
            this.mcVersion = mcVersion;
            this.maxConcurrentTasks = maxConcurrentTasks;
            this.chunkSizeMb = chunkSizeMb;
            this.loader = loader;
            this.loaderVersion = loaderVersion;
            this.packVersion = packVersion;
        }
    }

    public boolean ping() throws IOException {
        return pingInfo().ok;
    }

    public PingInfo pingInfo() throws IOException {
        writeHeader(SyncProtocol.OP_PING);
        out.flush();
        byte status = in.readByte();
        if (status != SyncProtocol.STATUS_OK) return new PingInfo(false, "", 0, 0, "", "", "");
        String mod = readString(in);
        if (!"clientsync".equals(mod)) return new PingInfo(false, "", 0, 0, "", "", "");
        String mcVersion = readString(in);
        int maxConcurrentTasks = in.readInt();
        int chunkSizeMb = in.readInt();
        String loader = "";
        String loaderVersion = "";
        String packVersion = "";
        int previousTimeout = socket.getSoTimeout();
        try {
            socket.setSoTimeout(SyncProtocol.PING_OPTIONAL_TIMEOUT_MS);
            try {
                loader = readString(in);
                loaderVersion = readString(in);
                packVersion = readString(in);
            } catch (IOException ignored) {
            }
        } finally {
            socket.setSoTimeout(previousTimeout);
        }
        return new PingInfo(true, mcVersion, maxConcurrentTasks, chunkSizeMb, loader, loaderVersion, packVersion);
    }

    public List<String> getIndex() throws IOException {
        writeHeader(SyncProtocol.OP_GET_INDEX);
        out.flush();
        byte status = in.readByte();
        if (status != SyncProtocol.STATUS_OK) throw new IOException("index status=" + status);
        int count = in.readInt();
        if (count < 0 || count > SyncProtocol.MAX_INDEX_NAMES) {
            throw new IOException("index count out of bounds: " + count);
        }
        List<String> names = new ArrayList<>(count);
        for (int i = 0; i < count; i++) names.add(readString(in));
        return names;
    }

    public List<String> getAutoScope() throws IOException {
        writeHeader(SyncProtocol.OP_GET_AUTO_SCOPE);
        out.flush();
        byte status = in.readByte();
        if (status != SyncProtocol.STATUS_OK) return List.of();
        int count = in.readInt();
        if (count < 0 || count > SyncProtocol.MAX_INDEX_NAMES) {
            throw new IOException("auto scope count out of bounds: " + count);
        }
        List<String> scope = new ArrayList<>(count);
        for (int i = 0; i < count; i++) scope.add(readString(in));
        return scope;
    }

    public byte[] getManifest(String name) throws IOException {
        writeHeader(SyncProtocol.OP_GET_MANIFEST);
        writeString(out, name);
        out.flush();
        byte status = in.readByte();
        if (status == SyncProtocol.STATUS_NOT_FOUND) throw new IOException("manifest not found: " + name);
        if (status != SyncProtocol.STATUS_OK) throw new IOException("manifest status=" + status);
        int len = in.readInt();
        if (len < 0 || len > SyncProtocol.MAX_MANIFEST_BYTES) {
            throw new IOException("manifest length out of bounds: " + len);
        }
        byte[] bytes = new byte[len];
        in.readFully(bytes);
        return bytes;
    }

    public interface BatchResultHandler {
        void onFile(String rel, byte status, byte[] data) throws IOException;
    }

    public void getBatch(List<String> rels, BatchResultHandler handler) throws IOException {
        writeHeader(SyncProtocol.OP_GET_BATCH);
        out.writeInt(rels.size());
        for (String rel : rels) writeString(out, rel);
        out.flush();

        byte status = in.readByte();
        if (status != SyncProtocol.STATUS_OK) throw new IOException("batch status=" + status);
        int count = in.readInt();
        if (count < 0 || count > SyncProtocol.MAX_BATCH_FILES) {
            throw new IOException("batch count out of bounds: " + count);
        }
        for (int i = 0; i < count; i++) {
            byte fileStatus = in.readByte();
            String rel = readString(in);
            long len = in.readLong();
            byte[] data = null;
            if (fileStatus == SyncProtocol.STATUS_OK) {
                if (len < 0 || len > SyncProtocol.MAX_BATCH_FILE_BYTES) {
                    throw new IOException("batch file length out of bounds: " + len);
                }
                data = new byte[(int) len];
                in.readFully(data);
            }
            handler.onFile(rel, fileStatus, data);
        }
    }

    public void getChunk(String rel, long offset, long length, OutputStream sink) throws IOException {
        writeHeader(SyncProtocol.OP_GET_CHUNK);
        writeString(out, rel);
        out.writeLong(offset);
        out.writeLong(length);
        out.flush();

        byte status = in.readByte();
        if (status != SyncProtocol.STATUS_OK) throw new IOException("chunk status=" + status + " for " + rel);
        long len = in.readLong();
        if (len < 0) throw new IOException("chunk length negative: " + len);

        byte[] buf = new byte[64 * 1024];
        long remaining = len;
        while (remaining > 0) {
            int toRead = (int) Math.min(buf.length, remaining);
            int read = in.read(buf, 0, toRead);
            if (read < 0) throw new IOException("stream closed early for " + rel);
            sink.write(buf, 0, read);
            remaining -= read;
        }
    }

    private void writeHeader(byte op) throws IOException {
        out.writeInt(SyncProtocol.MAGIC);
        out.writeInt(SyncProtocol.VERSION);
        out.writeByte(op);
    }

    private static void writeString(DataOutputStream out, String s) throws IOException {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > SyncProtocol.MAX_STRING_BYTES) throw new IOException("string too long: " + bytes.length);
        out.writeInt(bytes.length);
        out.write(bytes);
    }

    private static String readString(DataInputStream in) throws IOException {
        int len = in.readInt();
        if (len < 0 || len > SyncProtocol.MAX_STRING_BYTES) {
            throw new IOException("invalid string length: " + len);
        }
        byte[] bytes = new byte[len];
        in.readFully(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}