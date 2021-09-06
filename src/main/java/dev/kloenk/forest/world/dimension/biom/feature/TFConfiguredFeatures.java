package dev.kloenk.forest.world.dimension.biom.feature;

import dev.kloenk.forest.ForestMod;
import net.minecraft.block.SaplingBlock;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.ConfiguredDecorator;
import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.minecraft.world.gen.feature.*;

public class TFConfiguredFeatures {
    public static final ConfiguredFeature<?, ?> WOOD_ROOTS_SPREAD;
    public static final ConfiguredFeature<?, ?> WELL;
    public static final ConfiguredFeature<?, ?> WEBS;
    public static final ConfiguredFeature<?, ?> TROLL_ROOTS;
    public static final ConfiguredFeature<?, ?> TORCH_BERRIES;
    public static final ConfiguredFeature<?, ?> THORNS;

    public static final ConfiguredFeature<?, ?> HUGE_LILYPAD;

    // Base
    public static final ConfiguredFeature<TreeFeatureConfig, ?> CANOPY_TREE_BASE;
    public static final ConfiguredFeature<TreeFeatureConfig, ?> DEAD_CANOPY_TREE_BASE;

    // Trees
    public static final ConfiguredFeature<?, ?> CANOPY_TREES;
    public static final ConfiguredFeature<?, ?> DEAD_CANOPY_TREES;

    // Thinks to find
    public static final ConfiguredFeature<?, ?> BIG_MUSHGLOOM;
    public static final ConfiguredFeature<?, ?> FOUNDATION;

    public static final ConfiguredDecorator<?> PLACEMENT_NOTFSTRUCTURE;
    private static final ConfiguredDecorator<?> DEFAULT_TREE_PLACEMENT;

    private static <FC extends FeatureConfig>ConfiguredFeature<FC, ?> register(String path, ConfiguredFeature<FC, ?> configuredFeature) {
        return register(ForestMod.path(path), configuredFeature);
    }

    private static <FC extends FeatureConfig>ConfiguredFeature<FC, ?> register(Identifier id, ConfiguredFeature<FC, ?> configuredFeature) {
        return (ConfiguredFeature) Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, configuredFeature);
    }

    static {
        PLACEMENT_NOTFSTRUCTURE = TFBiomeFeatures.PLACEMENT_NOTFSTRUCTURE.configure(NopeDecoratorConfig.INSTANCE);
        DEFAULT_TREE_PLACEMENT = PLACEMENT_NOTFSTRUCTURE.decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_OCEAN_FLOOR_NO_WATER).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(1, 0.4F, 1)));

        WOOD_ROOTS_SPREAD = register("ore/wood_roots_spread", TFBiomeFeatures.WOOD_ROOTS.configure(new DefaultFeatureConfig())
                .decorate(PLACEMENT_NOTFSTRUCTURE)
                .range(ConfiguredFeatures.Decorators.BOTTOM_TO_60)
                .applyChance(30)
                .spreadHorizontally()
                .repeat(20));

        WELL = register("well", TFBiomeFeatures.WELL.configure(new DefaultFeatureConfig())
                .decorate(PLACEMENT_NOTFSTRUCTURE)
                .decorate(ConfiguredFeatures.Decorators.HEIGHTMAP_WORLD_SURFACE)
                .applyChance(90)
        );

        WEBS = register("webs", TFBiomeFeatures.WEBS.configure(new DefaultFeatureConfig())
                .decorate(ConfiguredFeatures.Decorators.HEIGHTMAP_WORLD_SURFACE)
                .spreadHorizontally()
                .applyChance(60)
        );

        TROLL_ROOTS = register("troll_roots", TFBiomeFeatures.TROLL_ROOTS.configure(new DefaultFeatureConfig())
                .decorate(ConfiguredFeatures.Decorators.SQUARE_TOP_SOLID_HEIGHTMAP)
                .range(ConfiguredFeatures.Decorators.BOTTOM_TO_60)
                .applyChance(4)
                .spreadHorizontally()
        );

        TORCH_BERRIES = register("torch_berries", TFBiomeFeatures.TORCH_BERRIES.configure(new DefaultFeatureConfig())
                .decorate(ConfiguredFeatures.Decorators.SQUARE_TOP_SOLID_HEIGHTMAP)
                .range(ConfiguredFeatures.Decorators.BOTTOM_TO_60)
                .applyChance(4)
        );

        THORNS = register("thorns", TFBiomeFeatures.THORNS.configure(new DefaultFeatureConfig())
                // .decorated(TFFeatures.CONFIGURED_THORNLANDS_BLANKETING)
        );

        HUGE_LILYPAD = register("huge_lilypad", TFBiomeFeatures.HUGE_LILYPADS.configure(new DefaultFeatureConfig())
                .decorate(ConfiguredFeatures.Decorators.HEIGHTMAP_WORLD_SURFACE)
                .applyChance(20)
                .spreadHorizontally()
        );

        // Base
        CANOPY_TREE_BASE = register("tree/base/canopy_tree", Feature.TREE.configure(TFTreeConfig.CANOPY_TREE));
        DEAD_CANOPY_TREE_BASE = register("tree/base/dead_canopy_tree", Feature.TREE.configure(TFTreeConfig.CANOPY_TREE_DEAD));

        // Trees
        CANOPY_TREES = register("tree/canopy_tree", CANOPY_TREE_BASE.decorate(DEFAULT_TREE_PLACEMENT));
        DEAD_CANOPY_TREES = register("tree/dead_canopy_tree", DEAD_CANOPY_TREE_BASE.decorate(DEFAULT_TREE_PLACEMENT).applyChance(20));

        // Things to find
        BIG_MUSHGLOOM = register("big_mushgloom", TFBiomeFeatures.BIG_MUSHGLOOM.configure(DefaultFeatureConfig.DEFAULT)
                .decorate(PLACEMENT_NOTFSTRUCTURE)
                .decorate(ConfiguredFeatures.Decorators.HEIGHTMAP_WORLD_SURFACE)
                .spreadHorizontally()
        );
        FOUNDATION = register("structure/foundation", TFBiomeFeatures.FOUNDATION.configure(DefaultFeatureConfig.DEFAULT)
                .decorate(PLACEMENT_NOTFSTRUCTURE)
                .decorate(ConfiguredFeatures.Decorators.HEIGHTMAP_WORLD_SURFACE)
                .applyChance(90)
                .spreadHorizontally()
        );
    }

    // TODO
    //public static final MusicSound TFMUSICTYPE = new MusicSound(TFSounds.MUSIC, 1200, 12000, true);
    public static final MusicSound TFMUSICTYPE = new MusicSound(SoundEvents.AMBIENT_CAVE, 1200, 12000, true);
}
