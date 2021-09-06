package dev.kloenk.forest.world.dimension.biom.feature.growers;

import dev.kloenk.forest.world.dimension.biom.feature.TFConfiguredFeatures;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class CanopyTree extends SaplingGenerator {
    @Nullable
    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random random, boolean bees) {
        return TFConfiguredFeatures.CANOPY_TREE_BASE;
    }
}
