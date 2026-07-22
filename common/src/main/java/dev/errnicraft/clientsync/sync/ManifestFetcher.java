package dev.errnicraft.clientsync.sync;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.errnicraft.clientsync.model.ManifestFile;
import dev.errnicraft.clientsync.model.ManifestMod;
import dev.errnicraft.clientsync.model.ManifestRemoved;
import dev.errnicraft.clientsync.model.SyncManifest;
import dev.errnicraft.clientsync.net.ServerAddress;
import dev.errnicraft.clientsync.net.SyncTcpClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ManifestFetcher {

    private static final Gson GSON = new Gson();

    private static String lastError;

    private static final Path DEBUG_LOG = Path.of("clientsync-debug.log");

    private static void debugLog(String msg) {
        try {
            Files.writeString(DEBUG_LOG,
                "[" + LocalTime.now() + "] " + msg + System.lineSeparator(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ignored) {}
    }

    public static String getLastError() {
        return lastError;
    }

    public static SyncTcpClient.PingInfo lastPingInfo;

    public static boolean ping(String serverAddress) {
        lastError = null;
        debugLog("ping() start, address=" + serverAddress);
        ServerAddress addr = ServerAddress.resolve(serverAddress);
        try (SyncTcpClient client = new SyncTcpClient(addr.host, addr.port)) {
            client.connect(5000);
            SyncTcpClient.PingInfo info = client.pingInfo();
            lastPingInfo = info;
            if (!info.ok) lastError = "Server responded, but it is not a ClientSync server";
            debugLog("ping() ok=" + info.ok);
            return info.ok;
        } catch (java.net.ConnectException e) {
            debugLog("ping() ConnectException: " + e.getMessage());
            lastError = "Connection refused (" + serverAddress + "). Check that the server port is specified, e.g. 127.0.0.1:25566";
            return false;
        } catch (java.net.UnknownHostException e) {
            debugLog("ping() UnknownHostException: " + e.getMessage());
            lastError = "Failed to resolve address: " + serverAddress;
            return false;
        } catch (java.net.SocketTimeoutException e) {
            debugLog("ping() SocketTimeoutException: " + e.getMessage());
            lastError = "Server is not responding (timeout): " + serverAddress;
            return false;
        } catch (Exception e) {
            debugLog("ping() Exception: " + e);
            lastError = "Connection error: " + e.getMessage();
            return false;
        }
    }

    public static SyncManifest fetch(String serverAddress) throws IOException {
        ServerAddress addr = ServerAddress.resolve(serverAddress);

        try (SyncTcpClient client = new SyncTcpClient(addr.host, addr.port)) {
            client.connect(5000);

            List<String> manifestNames = client.getIndex();
            return parseManifests(manifestNames, name -> {
                try {
                    return client.getManifest(name);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private interface ManifestBytesFetcher {
        byte[] fetch(String name);
    }

    private static SyncManifest parseManifests(List<String> manifestNames, ManifestBytesFetcher fetcher) throws IOException {
        List<ManifestMod> mods = new ArrayList<>();
        List<ManifestFile> files = new ArrayList<>();
        List<ManifestRemoved> removed = new ArrayList<>();

        try {
            for (String name : manifestNames) {
                byte[] bytes = fetcher.fetch(name);
                JsonObject manifest = GSON.fromJson(new String(bytes, StandardCharsets.UTF_8), JsonObject.class);

                if (manifest.has("removed") && !manifest.get("removed").isJsonNull()) {
                    JsonArray removedArr = manifest.getAsJsonArray("removed");
                    for (JsonElement el : removedArr) {
                        JsonObject o = el.getAsJsonObject();
                        removed.add(new ManifestRemoved(
                            o.has("category") ? o.get("category").getAsString() : "",
                            o.has("key") ? o.get("key").getAsString() : "",
                            o.has("fileName") ? o.get("fileName").getAsString() : ""
                        ));
                    }
                    continue;
                }

                String folder = manifest.has("folder") && !manifest.get("folder").isJsonNull()
                    ? manifest.get("folder").getAsString() : "";

                if (manifest.has("mods") && !manifest.get("mods").isJsonNull()) {
                    JsonArray modsArr = manifest.getAsJsonArray("mods");
                    for (JsonElement el : modsArr) {
                        JsonObject o = el.getAsJsonObject();
                        mods.add(new ManifestMod(
                            o.get("mod_id").getAsString(),
                            o.get("version").getAsString(),
                            o.get("hash").getAsString(),
                            o.get("path").getAsString(),
                            o.has("file_name") ? o.get("file_name").getAsString() : o.get("mod_id").getAsString() + ".jar",
                            o.has("size") ? o.get("size").getAsLong() : 0L,
                            folder
                        ));
                    }
                }

                if (manifest.has("files") && !manifest.get("files").isJsonNull()) {
                    JsonArray filesArr = manifest.getAsJsonArray("files");
                    for (JsonElement el : filesArr) {
                        JsonObject o = el.getAsJsonObject();
                        String path = o.get("path").getAsString();
                        files.add(new ManifestFile(
                            path,
                            o.get("hash").getAsString(),
                            path,
                            o.has("size") ? o.get("size").getAsLong() : 0L,
                            folder
                        ));
                    }
                }
            }
        } catch (RuntimeException e) {
            if (e.getCause() instanceof IOException io) throw io;
            throw e;
        }

        return new SyncManifest(mods, files, removed);
    }
}
