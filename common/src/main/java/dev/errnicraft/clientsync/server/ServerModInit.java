package dev.errnicraft.clientsync.server;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class ServerModInit implements ModInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger("ClientSync");

    private static SyncTcpServer tcpServer;
    private static ManifestBuilder manifestBuilder;
    private static Path serverDir;

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTING.register(this::onServerStarting);
        ServerLifecycleEvents.SERVER_STOPPED.register(this::onServerStopped);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                Commands.literal("clientsync")
                    .requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS))
                    .then(Commands.literal("rebuild")
                        .executes(this::execRebuild))
                    .then(Commands.literal("reload")
                        .executes(this::execReload))
            );
        });
    }

    private void onServerStarting(MinecraftServer server) {
        serverDir = server.getServerDirectory();
        try {
            ServerConfig config = ServerConfig.load(serverDir);
            if ("CHANGE_ME".equals(config.host)) {
                LOGGER.warn("[ClientSync] host is not configured in clientsync/config.json — clients will not be able to connect. Specify the server's public IP or domain.");
            }
            manifestBuilder = new ManifestBuilder(serverDir);
            manifestBuilder.rebuild(config);

            tcpServer = new SyncTcpServer(config.port, serverDir, config);
            tcpServer.start();
            LOGGER.info("[ClientSync] Ready to accept client connections on port {}", config.port);
        } catch (Exception e) {
            LOGGER.error("[ClientSync] Failed to start", e);
        }
    }

    private void onServerStopped(MinecraftServer server) {
        if (tcpServer != null) {
            tcpServer.stop();
            tcpServer = null;
        }
    }

    private int execRebuild(CommandContext<CommandSourceStack> ctx) {
        if (manifestBuilder == null) {
            ctx.getSource().sendFailure(Component.literal("[ClientSync] Server not ready."));
            return 0;
        }
        try {
            ServerConfig config = ServerConfig.load(serverDir);
            manifestBuilder.rebuild(config);
            ctx.getSource().sendSuccess(() -> Component.literal("[ClientSync] Manifests rebuilt."), false);
            return 1;
        } catch (Exception e) {
            LOGGER.error("[ClientSync] Rebuild failed", e);
            ctx.getSource().sendFailure(Component.literal("[ClientSync] Rebuild failed: " + e.getMessage()));
            return 0;
        }
    }

    private int execReload(CommandContext<CommandSourceStack> ctx) {
        if (serverDir == null) {
            ctx.getSource().sendFailure(Component.literal("[ClientSync] Server not ready."));
            return 0;
        }
        try {
            ServerConfig config = ServerConfig.load(serverDir);
            int oldPort = tcpServer != null ? tcpServer.getPort() : -1;

            if (oldPort != config.port) {
                if (tcpServer != null) {
                    tcpServer.stop();
                }
                tcpServer = new SyncTcpServer(config.port, serverDir, config);
                tcpServer.start();
                ctx.getSource().sendSuccess(() -> Component.literal(
                        "[ClientSync] Config reloaded, port changed: " + oldPort + " -> " + config.port), false);
            } else {
                ctx.getSource().sendSuccess(() -> Component.literal(
                        "[ClientSync] Config reloaded. Host: " + config.host + ", port unchanged: " + config.port), false);
            }

            if ("CHANGE_ME".equals(config.host)) {
                ctx.getSource().sendSuccess(() -> Component.literal(
                        "[ClientSync] Warning: host is not configured in clientsync/config.json"), false);
            }
            return 1;
        } catch (Exception e) {
            LOGGER.error("[ClientSync] Reload failed", e);
            ctx.getSource().sendFailure(Component.literal("[ClientSync] Reload failed: " + e.getMessage()));
            return 0;
        }
    }
}
