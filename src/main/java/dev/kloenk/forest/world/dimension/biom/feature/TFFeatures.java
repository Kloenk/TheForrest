package dev.kloenk.forest.world.dimension.biom.feature;

import com.mojang.serialization.Codec;
import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.mixin.FoliagePlacerTypeAccessor;
import dev.kloenk.forest.mixin.TrunkPlacerTypeAccessor;
import dev.kloenk.forest.world.dimension.biom.feature.placer.tree.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;


public class TFFeatures {
    public static final TrunkPlacerType<BranchingTrunkPlacer> TRUNK_BRANCHING = registerTrunk("branching_trunk_place", BranchingTrunkPlacer.CODEC);

    public static final FoliagePlacerType<LeafSpheroidFoliagePlacer> FOLIAGE_SPHEROID = registerFoliage("spheroid_foliage_placer", LeafSpheroidFoliagePlacer.CODEC);

    protected static <FC extends FeatureConfig, F extends Feature<FC>>ConfiguredFeature<FC, F> registerWorldFeature(String path, ConfiguredFeature<FC, F> feature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, ForestMod.path(path), feature);
    }

    protected static <P extends TrunkPlacer> TrunkPlacerType<P> registerTrunk(String path, Codec<P> codec) {
        return registerTrunk(ForestMod.path(path), codec);
    }

    protected static <P extends TrunkPlacer> TrunkPlacerType<P> registerTrunk(Identifier id, Codec<P> codec) {
        return Registry.register(Registry.TRUNK_PLACER_TYPE, id, TrunkPlacerTypeAccessor.of(codec));
    }

    protected static <P extends FoliagePlacer> FoliagePlacerType<P> registerFoliage(String path, Codec<P> codec) {
        return registerFoliage(ForestMod.path(path), codec);
    }

    protected static <P extends FoliagePlacer> FoliagePlacerType<P> registerFoliage(Identifier id, Codec<P> codec) {
        FoliagePlacerType<P> type = FoliagePlacerTypeAccessor.of(codec);
        // TODO: TREE_DECORATOR_TYPES.add(type);?
        return Registry.register(Registry.FOLIAGE_PLACER_TYPE, id, type);
    }

    // register
    public static void register() {
        TFBiomeFeatures.register();

        //Registry.register(Registry.DECORATOR, TFBiomeFeatures.PLACEMENT_NOTFSTRUCTURE.);
        // TODO:
    }
}
