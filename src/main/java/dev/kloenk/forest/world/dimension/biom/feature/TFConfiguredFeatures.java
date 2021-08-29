package dev.kloenk.forest.world.dimension.biom.feature;

import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;

public class TFConfiguredFeatures {
    public static final ConfiguredFeature<?, ?> WOOD_ROOTS_SPREAD;

    static {
        WOOD_ROOTS_SPREAD = TFFeatures.registerWorldFeature("ore/wood_roots_spread", TFBiomeFeatures.WOOD_ROOTS.configure(new DefaultFeatureConfig()));
    }

    // TODO
    //public static final MusicSound TFMUSICTYPE = new MusicSound(TFSounds.MUSIC, 1200, 12000, true);
    public static final MusicSound TFMUSICTYPE = new MusicSound(SoundEvents.AMBIENT_CAVE, 1200, 12000, true);
}
