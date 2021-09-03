package dev.kloenk.forest.world.dimension.biom.carvers;

import dev.kloenk.forest.ForestMod;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverDebugConfig;
import net.minecraft.world.gen.carver.CaveCarverConfig;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;

public class TFConfiguredWorldCarvers {
    public static final TFCavesCarver TFCAVES = new TFCavesCarver(CaveCarverConfig.CAVE_CODEC, false);
    public static final TFCavesCarver HIGHLANDCAVES = new TFCavesCarver(CaveCarverConfig.CAVE_CODEC, true);


    public static final ConfiguredCarver<CaveCarverConfig> TFCAVES_CONFIGURED = TFCAVES.configure(
            new CaveCarverConfig(
                    0.1f,
                    UniformHeightProvider.create(YOffset.aboveBottom(5), YOffset.fixed(-5)),
                    ConstantFloatProvider.create(0.6f),
                    YOffset.getBottom(),
                    false,
                    // FIXME
                    CarverDebugConfig.create(true, Blocks.GLASS.getDefaultState(), Blocks.BLUE_STAINED_GLASS.getDefaultState(), Blocks.RED_STAINED_GLASS.getDefaultState(), Blocks.RED_WOOL.getDefaultState()),
                    ConstantFloatProvider.create(1f),
                    ConstantFloatProvider.create(1f),
                    ConstantFloatProvider.create(-0.7f)
            )
    );
    public static final ConfiguredCarver<CaveCarverConfig> HIGHLANDS_CONFIGURED = HIGHLANDCAVES.configure(
            new CaveCarverConfig(
                    0.1f,
                    UniformHeightProvider.create(YOffset.aboveBottom(5), YOffset.fixed(64)),
                    ConstantFloatProvider.create(0.75f),
                    YOffset.getBottom(),
                    false,
                    // FIXME
                    CarverDebugConfig.create(false, Blocks.GLASS.getDefaultState(), Blocks.BLUE_STAINED_GLASS.getDefaultState(), Blocks.RED_STAINED_GLASS.getDefaultState(), Blocks.RED_WOOL.getDefaultState()),
                    ConstantFloatProvider.create(1f),
                    ConstantFloatProvider.create(1f),
                    ConstantFloatProvider.create(-0.7f)
            )
    );

    public static void register() {
        register("tf_caves", TFCAVES);
        register("highland_caves", HIGHLANDCAVES);

        Registry.register(BuiltinRegistries.CONFIGURED_CARVER, ForestMod.path("tf_caves"), TFCAVES_CONFIGURED);
        Registry.register(BuiltinRegistries.CONFIGURED_CARVER, ForestMod.path("highland_caves"), HIGHLANDS_CONFIGURED);
    }

    protected static <C extends Carver> C register(String path, C carver) {
        return register(ForestMod.path(path), carver);
    }

    protected static <C extends Carver> C register(Identifier id, C carver) {
        return (C)Registry.register(Registry.CARVER, id, (Carver)carver);
    }
}
