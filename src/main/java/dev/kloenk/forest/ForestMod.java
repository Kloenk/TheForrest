package dev.kloenk.forest;

import dev.kloenk.forest.blocks.blockentities.TFBlockEntities;
import dev.kloenk.forest.data.TFBlockTagsGenerator;
import dev.kloenk.forest.data.TFConfiguredCaverGenerator;
import dev.kloenk.forest.data.TFConfiguredFeatureGenerater;
import dev.kloenk.forest.init.TFContent;
import dev.kloenk.forest.world.dimension.TFDimension;
import net.fabricmc.api.ModInitializer;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.Main;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;

public class ForestMod implements ModInitializer {
    public static final String ID = "forest";
    public static final Logger LOGGER = LogManager.getFormatterLogger(ID);

    public static final String MODEL_DIR = "textures/model";

    @Contract("_ -> new")
    public static @NotNull Identifier path(String path) {
        return new Identifier(ID, path);
    }

    @Contract("_ -> new")
    public static @NotNull Identifier modelTexture(String path) {
        return path(MODEL_DIR + "/" + path);
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
        generator.install(new TFConfiguredCaverGenerator(generator));
        // TODO: recipies

        try {
            generator.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
