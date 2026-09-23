package dev.errnicraft.clientsync.installer;

import dev.errnicraft.clientsync.ClientSyncMod;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class InstallerLauncher {

    private static final String INSTALLER_RESOURCE = "/META-INF/jars/clientsync-installer.jar";

    public static java.util.Optional<String> launch(String clientVersion) {
        return launch(clientVersion, null);
    }

    public static java.util.Optional<String> launch(String clientVersion, String autoUpdateAddress) {
        try {
            File gameDir = Path.of("").toAbsolutePath().toFile();
            File extractedJar = extractInstallerJar(gameDir);
            if (extractedJar == null) {
                ClientSyncMod.LOGGER.error("[ClientSync] Embedded installer not found: {}", INSTALLER_RESOURCE);
                return java.util.Optional.of("clientsync.error.installer_not_found");
            }

            String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";

            String loaderVersion = net.fabricmc.loader.api.FabricLoader.getInstance()
                    .getModContainer("fabricloader")
                    .map(c -> c.getMetadata().getVersion().getFriendlyString())
                    .orElse("unknown");

            java.util.List<String> command = new java.util.ArrayList<>();
            command.add(javaBin);
            command.add("-jar");
            command.add(extractedJar.getAbsolutePath());
            command.add(clientVersion);
            command.add("fabric");
            command.add(loaderVersion);
            if (autoUpdateAddress != null && !autoUpdateAddress.isBlank()) {
                command.add("--auto-update");
                command.add(autoUpdateAddress);
                command.add("--game-pid");
                command.add(String.valueOf(ProcessHandle.current().pid()));
            }
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(gameDir);
            File logFile = new File(gameDir, "clientsync/installer.log");
            logFile.getParentFile().mkdirs();
            pb.redirectOutput(ProcessBuilder.Redirect.appendTo(logFile));
            pb.redirectError(ProcessBuilder.Redirect.appendTo(logFile));

            Process process = pb.start();
            ClientSyncMod.LOGGER.info("[ClientSync] Installer process started, pid={}", process.pid());

            if (autoUpdateAddress == null) {
                return java.util.Optional.empty();
            }

            try {
                long deadline = System.currentTimeMillis() + 45_000;
                boolean exited = process.waitFor(1200, java.util.concurrent.TimeUnit.MILLISECONDS);
                while (!exited && System.currentTimeMillis() < deadline) {
                    exited = process.waitFor(500, java.util.concurrent.TimeUnit.MILLISECONDS);
                }
                if (!exited) {
                    ClientSyncMod.LOGGER.error("[ClientSync] Installer did not finish auto-update within 45s");
                    return java.util.Optional.of("clientsync.error.installer_timeout");
                }
                int code = process.exitValue();
                if (code == 0) {
                    ClientSyncMod.LOGGER.info("[ClientSync] Installer auto-update exit 0: game up-to-date, starting normally");
                    return java.util.Optional.empty();
                }
                ClientSyncMod.LOGGER.error("[ClientSync] Installer auto-update failed with exit code {}", code);
                return java.util.Optional.of("clientsync.error.installer_failed:exit=" + code);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            return java.util.Optional.of("clientsync.error.installer_interrupted");
        } catch (Exception e) {
            ClientSyncMod.LOGGER.error("[ClientSync] Failed to launch the installer", e);
            return java.util.Optional.of("clientsync.error.installer_launch_failed");
        }
    }

    private static File extractInstallerJar(File gameDir) throws Exception {
        File cacheDir = new File(gameDir, "clientsync/cache");
        cacheDir.mkdirs();
        File target = new File(cacheDir, "clientsync-installer.jar");

        try (InputStream in = InstallerLauncher.class.getResourceAsStream(INSTALLER_RESOURCE)) {
            if (in == null) {
                return null;
            }
            Files.copy(in, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return target;
    }
}
