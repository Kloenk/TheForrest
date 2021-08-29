package dev.kloenk.forest.world.dimension;

import com.google.common.collect.ImmutableMap;
import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.mixin.ChunkGeneratorSettingsAccessor;
import dev.kloenk.forest.world.dimension.generator.TFChunkGeneratorForest;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.chunk.*;

import java.util.Optional;

public class TFDimension {
    static RegistryKey FOREST_NOISE_CONFIG_KEY =  RegistryKey.of(Registry.CHUNK_GENERATOR_SETTINGS_KEY, ForestMod.path("forest_noise_config"));

    public static void register() {
        Registry.register(Registry.BIOME_SOURCE, ForestMod.path("grid"), TFBiomProvider.CODEC);
        Registry.register(Registry.CHUNK_GENERATOR, ForestMod.path("featured_noise"), TFChunkGeneratorForest.CODEC);
        //Registry.register(Registry.CHUNK_GENERATOR_SETTINGS_KEY, ForestMod.path("forest_noise_config"), )

        registerGenratorSettings();
    }

    private static void registerGenratorSettings() {
        ChunkGeneratorSettings forestDimmensionSettings = ChunkGeneratorSettingsAccessor.callCreate(
                new StructuresConfig(Optional.empty(), ImmutableMap.of()),
                GenerationShapeConfig.create(
                        -32,
                        128,
                        new NoiseSamplingConfig(0.9999999814507745D, 0.9999999814507745D, 80.0D, 160.0D),
                        new SlideConfig(-10, 3, 0),
                        new SlideConfig(15, 3, 0),
                        1,
                        2,
                        1D,
                        -0.95,
                        true,
                        true,
                        false,
                        false
                ),
                Blocks.STONE.getDefaultState(),
                Blocks.WATER.getDefaultState(),
                Integer.MIN_VALUE,
                0,
                0,
                -32,
                false,
                false,
                false,
                false,
                false,
                false
        );

        //Registry.register(Registry.CHUNK_GENERATOR_SETTINGS, ForestMod.path("forest_noise_config"), forestDimmensionSettings);
        BuiltinRegistries.add(BuiltinRegistries.CHUNK_GENERATOR_SETTINGS, FOREST_NOISE_CONFIG_KEY.getValue(), forestDimmensionSettings);
    }

}
