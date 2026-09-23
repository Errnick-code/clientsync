package dev.errnicraft.clientsync.installer;

import dev.errnicraft.clientsync.ClientSyncMod;
import net.fabricmc.loader.api.FabricLoader;

import java.util.prefs.Preferences;

public class AutoUpdateChecker {

    private static final String PREF_NODE = "dev/errnicraft/clientsync/installer";
    private static final String KEY_AUTO_UPDATE = "auto_update_on_launch";
    private static final String KEY_ADDRESS = "server_address";

    public static boolean autoUpdateEnabled() {
        return "true".equalsIgnoreCase(Preferences.userRoot().node(PREF_NODE).get(KEY_AUTO_UPDATE, "true"));
    }

    public static String savedAddress() {
        return Preferences.userRoot().node(PREF_NODE).get(KEY_ADDRESS, "");
    }

    public static void runPreLaunchCheck() {
        if (!autoUpdateEnabled()) {
            return;
        }
        String address = savedAddress();
        if (address.isBlank()) {
            return;
        }

        String mcVersion = minecraftVersion();
        java.util.Optional<String> error = InstallerLauncher.launch(mcVersion, address);
        if (error.isPresent()) {
            ClientSyncMod.LOGGER.error("[ClientSync] auto-update failed ({}): aborting game launch", error.get());
            throw new RuntimeException("[ClientSync] auto-update failed: " + error.get());
        }
    }

    private static String minecraftVersion() {
        return FabricLoader.getInstance()
                .getModContainer("minecraft")
                .map(c -> c.getMetadata().getVersion().getFriendlyString())
                .orElse("unknown");
    }
}