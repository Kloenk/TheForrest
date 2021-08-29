package dev.kloenk.forest.client;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.init.TFContent;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ForestClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TFContent.registerClient();

        ForestMod.LOGGER.info("Registered Client");
    }
}
