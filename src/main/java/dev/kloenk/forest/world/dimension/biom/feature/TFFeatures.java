package dev.kloenk.forest.world.dimension.biom.feature;

import dev.kloenk.forest.ForestMod;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;


public class TFFeatures {


    protected static <FC extends FeatureConfig, F extends Feature<FC>>ConfiguredFeature<FC, F> registerWorldFeature(String path, ConfiguredFeature<FC, F> feature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, ForestMod.path(path), feature);
    }

    // register
    public static void register() {
        // TODO:
    }
}
