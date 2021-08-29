package dev.kloenk.forest.world.dimension;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.kloenk.forest.mixin.BiomeLayerSamplerAccessor;
import dev.kloenk.forest.world.dimension.biom.TFBiomeKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.dynamic.RegistryLookupCodec;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.DefaultBiomeCreator;
import net.minecraft.world.biome.layer.BiomeLayers;
import net.minecraft.world.biome.layer.util.*;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.biome.source.BiomeSource;

import java.util.List;
import java.util.Optional;
import java.util.function.LongFunction;

public class TFBiomProvider extends BiomeSource {
    public static final Codec<TFBiomProvider> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.LONG.fieldOf("seed").stable().forGetter(obj -> obj.seed),
            RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(obj -> obj.registry)
    ).apply(instance, instance.stable(TFBiomProvider::new)));

    // TODO: biome list
    private static final List<RegistryKey<Biome>> BIOMES = ImmutableList.of(
            TFBiomeKeys.FOREST,
            BiomeKeys.BEACH,
            BiomeKeys.JUNGLE
    );

    private final Registry<Biome> registry;
    private final BiomeLayerSampler layers;
    private final long seed;

    private TFBiomProvider(long seed, Registry<Biome> biomeRegistry) {
        super(BIOMES
                .stream()
                .map(biomeRegistry::getOrEmpty)
                .filter(Optional::isPresent)
                .map(opt -> opt::get)
        );
        this.seed = seed;
        this.registry = biomeRegistry;
        this.layers = makeLayers(seed, registry);
    }

    public static int getBiom(RegistryKey<Biome> biome, Registry<Biome> registry) {
        return registry.getRawId(registry.get(biome));
    }

    private static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> makeLayers(LongFunction<C> seed, Registry<Biome> registry, long rawSeed) {
        LayerFactory<T> biomes = TFGenLayerBiomes.INSTANCE.setup(registry).create(seed.apply(1L));

        // TODO: much

        return biomes;
    }

    public static BiomeLayerSampler makeLayers(long seed, Registry<Biome> registry) {
        LayerFactory<CachingLayerSampler> areaFactory = makeLayers((context) -> new CachingLayerContext(25, seed, context), registry, seed);

        return new BiomeLayerSampler(areaFactory) {
            @Override
            public Biome sample(Registry<Biome> biomeRegistry, int x, int z) {
                //int i = this.sampler.get(x, z);
                int i = ((BiomeLayerSamplerAccessor) this).getSampler().sample(x, z);
                Biome biome = registry.get(i);
                if (biome == null) {
                    throw new IllegalStateException("Unknown Biome id emitted by Sampler: " + i);
                }
                return biome;
            }
        };
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return CODEC;
    }

    @Override
    public BiomeSource withSeed(long seed) {
        return new TFBiomProvider(seed, registry);
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return layers.sample(registry, biomeY, biomeZ);
    }
}
