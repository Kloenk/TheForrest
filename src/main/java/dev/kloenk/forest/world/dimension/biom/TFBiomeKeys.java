package dev.kloenk.forest.world.dimension.biom;

import dev.kloenk.forest.ForestMod;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class TFBiomeKeys {
    //public static final RegistryKey<Biome> LAKE = makeKey("lake");
    public static final RegistryKey<Biome> FOREST = makeKey("forest");
    public static final RegistryKey<Biome> DENSE_FOREST = makeKey("dense_forest");

    private static RegistryKey<Biome> makeKey(String name) {
        return RegistryKey.of(Registry.BIOME_KEY, ForestMod.path(name));
    }
}
