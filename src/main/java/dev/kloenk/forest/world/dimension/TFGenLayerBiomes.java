package dev.kloenk.forest.world.dimension;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.layer.type.InitLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;

import java.util.List;

public class TFGenLayerBiomes implements InitLayer {
    //INSTANCE;
    public static TFGenLayerBiomes INSTANCE = new TFGenLayerBiomes();

    private static final int RARE_BIOME_CHANCE = 15;

    protected static final List<RegistryKey<Biome>> commonBiomes = ImmutableList.of(
            BiomeKeys.JUNGLE
    );

    protected static final List<RegistryKey<Biome>> rareBiomes = ImmutableList.of(
            BiomeKeys.BEACH
    );

    private Registry<Biome> registry;

    public TFGenLayerBiomes setup(Registry<Biome> registry) {
        this.registry = registry;
        return this;
    }

    TFGenLayerBiomes() {
        registry = null;
    }

    @Override
    public int sample(LayerRandomnessSource context, int x, int y) {
        if (context.nextInt(RARE_BIOME_CHANCE) == 0) {
            return getBiome(context, rareBiomes);
        } else {
            return getBiome(context, commonBiomes);
        }
    }

    private int getBiome(LayerRandomnessSource random, List<RegistryKey<Biome>> biomes) {
        return TFBiomProvider.getBiom(biomes.get(random.nextInt(biomes.size())), registry);
    }
}
