package dev.errnicraft.clientsync.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ServerConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public int port = 25566;
    public String host = "CHANGE_ME";
    public String mc_version = "";
    public String loader = "fabric";
    public String loader_version = "";
    public int max_concurrent_tasks = 20;
    public int chunk_size_mb = 10;

    public static ServerConfig load(Path serverDir) throws IOException {
        Path path = configPath(serverDir);
        if (!Files.exists(path)) {
            ServerConfig defaults = new ServerConfig();
            defaults.mc_version = net.minecraft.SharedConstants.getCurrentVersion().name();
            defaults.loader = "fabric";
            defaults.loader_version = ">=" + currentLoaderVersion();
            defaults.save(serverDir);
            return defaults;
        }
        String json = Files.readString(path);
        ServerConfig cfg = GSON.fromJson(json, ServerConfig.class);
        if (cfg == null) cfg = new ServerConfig();
        if (cfg.mc_version == null || cfg.mc_version.isBlank()) {
            cfg.mc_version = net.minecraft.SharedConstants.getCurrentVersion().name();
        }
        if (cfg.loader == null || cfg.loader.isBlank()) {
            cfg.loader = "fabric";
        }
        if (cfg.loader_version == null || cfg.loader_version.isBlank()) {
            cfg.loader_version = ">=" + currentLoaderVersion();
        }
        if (cfg.max_concurrent_tasks <= 0) cfg.max_concurrent_tasks = 20;
        if (cfg.chunk_size_mb <= 0) cfg.chunk_size_mb = 10;
        return cfg;
    }

    private static String currentLoaderVersion() {
        return net.fabricmc.loader.api.FabricLoader.getInstance()
                .getModContainer("fabricloader")
                .map(c -> c.getMetadata().getVersion().getFriendlyString())
                .orElse("0.0.0");
    }

    public void save(Path serverDir) throws IOException {
        Path path = configPath(serverDir);
        Files.createDirectories(path.getParent());
        Files.writeString(path, GSON.toJson(this));
    }

    private static Path configPath(Path serverDir) {
        return serverDir.resolve("clientsync/config.json");
    }
}
