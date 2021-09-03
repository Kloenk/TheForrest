package dev.kloenk.forest.world.dimension.biom;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.Map;

public class BiomeMaker extends BiomeHelper {
    public static final Map<RegistryKey<Biome>, Biome> biomes = generateBiomes();

    private static Map<RegistryKey<Biome>, Biome> generateBiomes() {
        final ImmutableMap.Builder<RegistryKey<Biome>, Biome> biomes = new ImmutableMap.Builder();

        commonBiomes(biomes);

        return biomes.build();
    }

    private static void commonBiomes(ImmutableMap.Builder<RegistryKey<Biome>, Biome> biomes) {
        biomes.put(TFBiomeKeys.FOREST, biomeWithDefaults(
                defaultAmbientBuilder(),
                defaultMobSpawning().playerSpawnFriendly(),
                theForrestGen()
        ).build());
        BuiltinRegistries.add(BuiltinRegistries.BIOME, )
    }

    public static void register() {
        for (Map.Entry<RegistryKey<Biome>, Biome> entry: biomes.entrySet()) {
            Registry.register(BuiltinRegistries.BIOME, entry.getKey().getValue(), entry.getValue());
        }
    }
}
