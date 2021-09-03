package dev.kloenk.forest;

import dev.kloenk.forest.blocks.blockentities.TFBlockEntities;
import dev.kloenk.forest.data.TFBlockTagsGenerator;
import dev.kloenk.forest.data.TFConfiguredFeatureGenerater;
import dev.kloenk.forest.init.TFContent;
import dev.kloenk.forest.world.dimension.TFDimension;
import net.fabricmc.api.ModInitializer;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.Main;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;

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
        TFBlockEntities.register();

        if ("true".equals(System.getProperty("datagenerator"))) {
            String input = System.getProperty("input");
            String ouptut = System.getProperty("output");
            generateData(input, ouptut);
        }

        LOGGER.info("Registered");
    }

    public static void generateData(String output, String input) {
        generateData(Path.of(output), Path.of(input));
    }

    public static void generateData(Path output, Path input) {
        //DataGenerator generator = new DataGenerator(output, Collections.singleton(input));
        DataGenerator generator =  Main.create(output, Collections.singleton(input), true, true, true, true, true);
        generator.install(new TFBlockTagsGenerator(generator));
        generator.install(new TFConfiguredFeatureGenerater(generator));
        // TODO: recipies

        try {
            generator.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
