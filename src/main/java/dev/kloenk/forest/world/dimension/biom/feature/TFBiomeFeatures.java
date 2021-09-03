package dev.kloenk.forest.world.dimension.biom.feature;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.world.feature.placement.OutOfStructureFilter;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.biome.DefaultBiomeCreator;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class TFBiomeFeatures {
    public static final Feature<DefaultFeatureConfig> WOOD_ROOTS = register("wood_roots", new TFWoodRootsFeature(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> WELL = register("well", new TFWellFeature(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> WEBS = register("webs", new TFWebsFeature(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> TROLL_ROOTS = register("troll_roots", new TFTrollRoots(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> TORCH_BERRIES = register("torch_berries", new TFGenTorchBerries(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> THORNS = register("thorns", new TFGenThorns(DefaultFeatureConfig.CODEC));


    public static final Decorator<NopeDecoratorConfig> PLACEMENT_NOTFSTRUCTURE = new OutOfStructureFilter(NopeDecoratorConfig.CODEC);

    public static void register() {
        /*registerFeature("wood_roots", WOOD_ROOTS);
        registerFeature("well", WELL);
        registerFeature("webs", WEBS);
        registerFeature("troll_roots", TROLL_ROOTS);
        registerFeature("torch_berries", TORCH_BERRIES);
        registerFeature("thorns", THORNS);*/

        Registry.register(Registry.DECORATOR, ForestMod.path("nostructure"), PLACEMENT_NOTFSTRUCTURE);
    }

    protected static <C extends FeatureConfig, F extends Feature<C>> F register(String path, F feature) {
        return register(ForestMod.path(path), feature);
    }

    protected static <C extends FeatureConfig, F extends Feature<C>> F register(Identifier id, F feature) {
        return Registry.register(Registry.FEATURE, id, feature);
    }
}
