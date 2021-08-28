package dev.kloenk.forest;

import dev.kloenk.forest.init.TFContent;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ForestMod implements ModInitializer {
    public static final String ID = "forest";
    public static final Logger LOGGER = LogManager.getFormatterLogger(ID);

    @Override
    public void onInitialize() {
        TFContent.register();

        LOGGER.info("Registered");
    }
}
