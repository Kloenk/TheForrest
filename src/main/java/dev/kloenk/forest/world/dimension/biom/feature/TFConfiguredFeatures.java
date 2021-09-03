package dev.kloenk.forest.world.dimension.biom.feature;

import dev.kloenk.forest.ForestMod;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.ConfiguredDecorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.minecraft.world.gen.feature.*;

public class TFConfiguredFeatures {
    public static final ConfiguredFeature<?, ?> WOOD_ROOTS_SPREAD;
    public static final ConfiguredFeature<?, ?> WELL;
    public static final ConfiguredFeature<?, ?> WEBS;
    public static final ConfiguredFeature<?, ?> TROLL_ROOTS;
    public static final ConfiguredFeature<?, ?> TORCH_BERRIES;
    public static final ConfiguredFeature<?, ?> THORNS;

    public static final ConfiguredDecorator<?> PLACEMENT_NOTFSTRUCTURE;

    private static <FC extends FeatureConfig>ConfiguredFeature<FC, ?> register(String path, ConfiguredFeature<FC, ?> configuredFeature) {
        return register(ForestMod.path(path), configuredFeature);
    }

    private static <FC extends FeatureConfig>ConfiguredFeature<FC, ?> register(Identifier id, ConfiguredFeature<FC, ?> configuredFeature) {
        return (ConfiguredFeature) Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, configuredFeature);
    }

    static {
        WOOD_ROOTS_SPREAD = register("ore/wood_roots_spread", TFBiomeFeatures.WOOD_ROOTS.configure(new DefaultFeatureConfig()));
        WELL = register("well", TFBiomeFeatures.WELL.configure(new DefaultFeatureConfig()));
        WEBS = register("webs", TFBiomeFeatures.WEBS.configure(new DefaultFeatureConfig()));
        TROLL_ROOTS = register("troll_roots", TFBiomeFeatures.TROLL_ROOTS.configure(new DefaultFeatureConfig()));
        TORCH_BERRIES = register("torch_berries", TFBiomeFeatures.TORCH_BERRIES.configure(new DefaultFeatureConfig()));
        THORNS = register("thorns", TFBiomeFeatures.THORNS.configure(new DefaultFeatureConfig()));

        PLACEMENT_NOTFSTRUCTURE = TFBiomeFeatures.PLACEMENT_NOTFSTRUCTURE.configure(NopeDecoratorConfig.INSTANCE);

    }

    // TODO
    //public static final MusicSound TFMUSICTYPE = new MusicSound(TFSounds.MUSIC, 1200, 12000, true);
    public static final MusicSound TFMUSICTYPE = new MusicSound(SoundEvents.AMBIENT_CAVE, 1200, 12000, true);
}
