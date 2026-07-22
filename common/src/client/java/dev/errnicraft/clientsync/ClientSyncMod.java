package dev.errnicraft.clientsync;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientSyncMod implements ClientModInitializer {

    public static final String MOD_ID = "clientsync";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("[ClientSync] Loaded.");
    }
}
