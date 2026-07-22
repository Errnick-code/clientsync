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

            ProcessBuilder pb = new ProcessBuilder(
                    javaBin,
                    "-jar",
                    extractedJar.getAbsolutePath(),
                    clientVersion,
                    "fabric",
                    loaderVersion
            );
            pb.directory(gameDir);
            File logFile = new File(gameDir, "clientsync/installer.log");
            logFile.getParentFile().mkdirs();
            pb.redirectOutput(ProcessBuilder.Redirect.appendTo(logFile));
            pb.redirectError(ProcessBuilder.Redirect.appendTo(logFile));

            Process process = pb.start();
            ClientSyncMod.LOGGER.info("[ClientSync] Installer process started, pid={}", process.pid());

            try {
                boolean exited = process.waitFor(1200, java.util.concurrent.TimeUnit.MILLISECONDS);
                if (exited) {
                    int code = process.exitValue();
                    ClientSyncMod.LOGGER.error("[ClientSync] Installer exited immediately after launch (code {})", code);
                    return java.util.Optional.of("clientsync.error.installer_exited_early");
                }
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            return java.util.Optional.empty();
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
