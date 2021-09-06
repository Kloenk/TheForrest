package dev.kloenk.forest.world.dimension.biom;

import dev.kloenk.forest.world.dimension.biom.carvers.TFConfiguredWorldCarvers;
import dev.kloenk.forest.world.dimension.biom.feature.TFConfiguredFeatures;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

public class BiomeHelper {

    public static GenerationSettings.Builder theForrestGen() {
        GenerationSettings.Builder biome = defaultGenSettingsBuilder();
        // TODO
        addForestVegetation(biome);
        commonFeatures(biome);
        addCanopyTrees(biome);
        addForestOakTrees(biome);
        addForestOakTrees(biome);
        addHollowOakTrees(biome);
        addDefaultStructures(biome);

        return biome;

    }

    public static void addForestVegetation(GenerationSettings.Builder biome) {
        // TODO
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.HUGE_LILYPAD);
        /*biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.HUGE_WATER_LILY);
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, Features.PATCH_WATERLILLY);*/
    }

    public static void commonFeatures(GenerationSettings.Builder biome) {
        // TODO:
        commonFeaturesWithoutBuildings(biome);

        // TODO
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.WELL);
        /*biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.FOUNDATION);
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.GROVE_RUINS);
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.DRUID_HUT);*/
    }

    public static void commonFeaturesWithoutBuildings(GenerationSettings.Builder biome) {
        // TODO
        /*biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.STONE_CIRCLE);
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.OUTSIDE_STALAGMITE);
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.MONOLITH);
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.HOLLOW_STUMP);
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.HOLLOW_LOG);
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.SMALL_LOG);*/
    }

    //Canopies, trees, and anything resembling a forest thing
    public static void addCanopyTrees(GenerationSettings.Builder biome) {
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.CANOPY_TREES);
    }

    public static void addFireflyCanopyTrees(GenerationSettings.Builder biome) {
        // TODO
        //biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.FIREFLY_CANOPY_TREE_MIX);
    }

    public static void addDeadCanopyTrees(GenerationSettings.Builder biome) {
        // TODO
        // biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.DEAD_CANOPY_TREES);
    }

    public static void addCanopyMushrooms(GenerationSettings.Builder biome, boolean dense) {
        DefaultBiomeFeatures.addDefaultMushrooms(biome); // Add small mushrooms
        //Same config as DefaultBiomeFeatures.withMushroomBiomeVegetation, we just use our custom large mushrooms instead
        // TODO
        /*biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, Features.BROWN_MUSHROOM_TAIGA);
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, Features.RED_MUSHROOM_TAIGA);
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.VANILLA_TF_BIG_MUSH);

        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, dense ? TFConfiguredFeatures.CANOPY_MUSHROOMS_DENSE : ConfiguredFeatures.CANOPY_MUSHROOMS_SPARSE);
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, dense ? TFConfiguredFeatures.BIG_MUSHGLOOM : ConfiguredFeatures.MUSHGLOOM_CLUSTER);*/
    }

    public static void addRainbowOaks(GenerationSettings.Builder biome) {
        // TODO
        // biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.RAINBOW_OAK_TREES);
        // biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.LARGE_RAINBOW_OAK_TREES);
    }

    public static void addMangroveTrees(GenerationSettings.Builder biome) {
        // TOOD
        // biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.MANGROVE_TREES);
    }

    public static void addDarkwoodTrees(GenerationSettings.Builder biome) {
        // TODO
        /*
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.DARKWOOD_TREES);
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.BUSH_DARK_FOREST_TREES);
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.OAK_DARK_FOREST_TREES);
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.BIRCH_DARK_FOREST_TREES);
         */
    }

    public static void addForestOakTrees(GenerationSettings.Builder biome) {
        // TODO
        /*biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.TWILIGHT_OAK_TREES);
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.VANILLA_TF_OAK);
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.VANILLA_TF_BIRCH);
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.TWILIGHT_OAK_TREES);
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.VANILLA_TF_OAK);
        biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.VANILLA_TF_BIRCH);
         */
    }

    public static void addHollowOakTrees(GenerationSettings.Builder biome) {
        // TODO
        //biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.HOLLOW_TREE_PLACER);
    }

    public static void addRareOakTrees(GenerationSettings.Builder biome) {
        // TODO
        // biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.OAK_TREES_SPARSE);
    }

    public static void addSwampTrees(GenerationSettings.Builder biome) {
        // TODO
        //biome.feature(GenerationStep.Feature.VEGETAL_DECORATION, TFConfiguredFeatures.SWAMPY_OAK_TREES);
    }

    public static GenerationSettings.Builder addDefaultStructures(GenerationSettings.Builder biome) {
        // TODO
        /*return biome.
                structureFeature(TFStructures.CONFIGURED_HEDGE_MAZE).
                structureFeature(TFStructures.CONFIGURED_HOLLOW_HILL_SMALL).
                structureFeature(TFStructures.CONFIGURED_HOLLOW_HILL_MEDIUM).
                structureFeature(TFStructures.CONFIGURED_HOLLOW_HILL_LARGE).
                structureFeature(TFStructures.CONFIGURED_NAGA_COURTYARD).
                structureFeature(TFStructures.CONFIGURED_LICH_TOWER);*/
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
        biome.carver(GenerationStep.Carver.AIR, TFConfiguredWorldCarvers.TFCAVES_CONFIGURED);
        // TODO
        //biome.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, TFConfiguredFeatures.PLANT_ROOTS);
        biome.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, TFConfiguredFeatures.TORCH_BERRIES);
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

        // TODO
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
