package dev.kloenk.forest.world.dimension.biom.feature;

import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class TFBiomeFeatures {
    public static final Feature<DefaultFeatureConfig> WOOD_ROOTS = new TFWoodRootsFeature(DefaultFeatureConfig.CODEC);
}
