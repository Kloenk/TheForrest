package dev.kloenk.forest.world.dimension.biom;

import dev.kloenk.forest.world.dimension.biom.feature.TFConfiguredFeatures;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

public class BiomeHelper {

    public static GenerationSettings.Builder theForrestGen() {
        GenerationSettings.Builder biome = defaultGenSettingsBuilder();
        // TODO
        /*addForestVegetation(biome);
        commonFeatures(biome);
        addCanopyTrees(biome);
        addTwilightOakTrees(biome);
        addTwilightOakTrees(biome);
        addHollowOakTrees(biome);
        addDefaultStructures(biome);*/

        return biome;

    }

    public static GenerationSettings.Builder defaultGenSettingsBuilder() {
        GenerationSettings.Builder biome = new GenerationSettings.Builder()
                .surfaceBuilder(ConfiguredSurfaceBuilders.OCEAN_SAND);

        DefaultBiomeFeatures.addClayDisk(biome);
        DefaultBiomeFeatures.addForestGrass(biome);
        DefaultBiomeFeatures.addSavannaGrass(biome);
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_SUGAR_CANE);

        addSmallStoneClusters(biome);
        withWoodRoots(biome);
        addCaves(biome);
        return biome;
    }

    // Helpers
    public static void addSmallStoneClusters(GenerationSettings.Builder biom) {
        biom.feature(GenerationStep.Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_ANDESITE);
        biom.feature(GenerationStep.Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_DIORITE);
        biom.feature(GenerationStep.Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_GRANITE);
    }

    public static void withWoodRoots(GenerationSettings.Builder biom) {
        // TODO:
        biom.feature(GenerationStep.Feature.UNDERGROUND_ORES, TFConfiguredFeatures.WOOD_ROOTS_SPREAD);
    }

    public static void addCaves(GenerationSettings.Builder biome) {
        // TODO
        //biome.carver(GenerationStep.Carver.AIR, TCConfiguredCarvers.TFCAVES_CONFIGURED);
        //biome.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, TFConfiguredFeatures.TROLL_ROOTS);
        DefaultBiomeFeatures.addDefaultOres(biome);
    }

    // builders
    public static Biome.Builder biomeWithDefaults(BiomeEffects.Builder biomeEffects, SpawnSettings.Builder spawnBuilder, GenerationSettings.Builder biomSettingsBuilder) {
        return new Biome.Builder()
                .precipitation(Biome.Precipitation.RAIN)
                .category(Biome.Category.FOREST)
                .depth(0.05f)
                .scale(0.1f)
                .temperature(0.5f)
                .downfall(0.5f)
                .spawnSettings(spawnBuilder.build())
                .effects(biomeEffects.build())
                .generationSettings(biomSettingsBuilder.build())
                .temperatureModifier(Biome.TemperatureModifier.NONE);
    }

    public static BiomeEffects.Builder defaultAmbientBuilder() {
        return new BiomeEffects.Builder()
                .fogColor(0xC0FFD8) // TODO Change based on Biome. Not previously done before
                .waterColor(0x3F76E4)
                .waterFogColor(0x050533)
                .skyColor(0x20224A) //TODO Change based on Biome. Not previously done before
                .moodSound(BiomeMoodSound.CAVE)
                .music(TFConfiguredFeatures.TFMUSICTYPE);

    }

    public static SpawnSettings.Builder defaultMobSpawning() {
        SpawnSettings.Builder spawner = new SpawnSettings.Builder();

        spawner.creatureSpawnProbability(0.1f);

        //spawnInfo.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(TFEntities.bighorn_sheep, 12, 4, 4));
        //spawnInfo.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(TFEntities.wild_boar, 10, 4, 4));
        spawner.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.CHICKEN, 10, 2, 4));
        //spawnInfo.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(TFEntities.deer, 15, 4, 5));
        spawner.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.WOLF, 5, 4, 4));
        //spawnInfo.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(TFEntities.tiny_bird, 15, 4, 8));
        //spawnInfo.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(TFEntities.squirrel, 10, 2, 4));
        //spawnInfo.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(TFEntities.bunny, 10, 4, 5));
        //spawnInfo.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(TFEntities.raven, 10, 1, 2));

        spawner.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.SPIDER, 10, 4, 4));
        spawner.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.ZOMBIE, 10, 4, 4));
        spawner.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.SKELETON, 10, 4, 4));
        spawner.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.CREEPER, 1, 4, 4));
        spawner.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.SLIME, 10, 4, 4));
        spawner.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.ENDERMAN, 1, 1, 4));
        //spawner.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(TFEntities.kobold, 10, 2, 4));

        //not a monster, but we want them to go underground
        spawner.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.BAT, 10, 1, 2));

        return spawner;
    }
}
