package dev.kloenk.forest;

import dev.kloenk.forest.init.TFContent;
import dev.kloenk.forest.world.dimension.TFDimension;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ForestMod implements ModInitializer {
    public static final String ID = "forest";
    public static final Logger LOGGER = LogManager.getFormatterLogger(ID);

    public static Identifier path(String path) {
        return new Identifier(ID, path);
    }

    @Override
    public void onInitialize() {
        TFContent.register();
        TFDimension.register();

        LOGGER.info("Registered");
    }
}
