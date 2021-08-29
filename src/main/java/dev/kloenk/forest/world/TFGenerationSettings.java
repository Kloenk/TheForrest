package dev.kloenk.forest.world;

import dev.kloenk.forest.mixin.WorldAccessor;
import dev.kloenk.forest.util.PlayerHelper;
import dev.kloenk.forest.world.dimension.generator.TFChunkGeneratorBase;
import dev.kloenk.forest.world.feature.TFFeature;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class TFGenerationSettings {
    private static final Map<Identifier, Identifier[]> BIOME_ADVANCEMENTS = new HashMap<>();
    private static final Map<Identifier, BiConsumer<PlayerEntity, World>> BIOME_PROGRESSION_ENFORCEMENT = new HashMap<>();

    static {

        /*
        // TODO make this data-driven
		registerBiomeAdvancementRestriction(BiomeKeys.DARK_FOREST, TwilightForestMod.prefix("progress_lich"));
		registerBiomeAdvancementRestriction(BiomeKeys.DARK_FOREST_CENTER, TwilightForestMod.prefix("progress_knights"));
		registerBiomeAdvancementRestriction(BiomeKeys.FINAL_PLATEAU, TwilightForestMod.prefix("progress_troll"));
		registerBiomeAdvancementRestriction(BiomeKeys.FIRE_SWAMP, TwilightForestMod.prefix("progress_labyrinth"));
		registerBiomeAdvancementRestriction(BiomeKeys.GLACIER, TwilightForestMod.prefix("progress_yeti"));
		registerBiomeAdvancementRestriction(BiomeKeys.HIGHLANDS, TwilightForestMod.prefix("progress_merge"));
		registerBiomeAdvancementRestriction(BiomeKeys.SNOWY_FOREST, TwilightForestMod.prefix("progress_lich"));
		registerBiomeAdvancementRestriction(BiomeKeys.SWAMP, TwilightForestMod.prefix("progress_lich"));
		registerBiomeAdvancementRestriction(BiomeKeys.THORNLANDS, TwilightForestMod.prefix("progress_troll"));

		registerBiomeProgressionEnforcement(BiomeKeys.DARK_FOREST, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 60 == 0) {
				player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
				trySpawnHintMonster(player, world, TFFeature.KNIGHT_STRONGHOLD);
			}
		});
		registerBiomeProgressionEnforcement(BiomeKeys.DARK_FOREST_CENTER, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 60 == 0) {
				player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
				trySpawnHintMonster(player, world, TFFeature.DARK_TOWER);
			}
		});
		registerBiomeProgressionEnforcement(BiomeKeys.FINAL_PLATEAU, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 5 == 0) {
				player.hurt(DamageSource.MAGIC, 1.5F);
				world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.PLAYERS, 1.0F, 1.0F);
				// TODO: change this when there's a book for the castle
				trySpawnHintMonster(player, world, TFFeature.TROLL_CAVE);
			}
		});
		registerBiomeProgressionEnforcement(BiomeKeys.FIRE_SWAMP, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 60 == 0) {
				player.setSecondsOnFire(8);
			}
			trySpawnHintMonster(player, world, TFFeature.HYDRA_LAIR);
		});
		registerBiomeProgressionEnforcement(BiomeKeys.GLACIER, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 60 == 0) {
				player.addEffect(new MobEffectInstance(TFPotions.frosty.get(), 100, 3));
			}
			trySpawnHintMonster(player, world, TFFeature.ICE_TOWER);
		});
		registerBiomeProgressionEnforcement(BiomeKeys.HIGHLANDS, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 5 == 0) {
				player.hurt(DamageSource.MAGIC, 0.5F);
				world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.PLAYERS, 1.0F, 1.0F);
				trySpawnHintMonster(player, world, TFFeature.TROLL_CAVE);
			}
		});
		registerBiomeProgressionEnforcement(BiomeKeys.SNOWY_FOREST, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 60 == 0) {
				player.addEffect(new MobEffectInstance(TFPotions.frosty.get(), 100, 2));
				trySpawnHintMonster(player, world, TFFeature.YETI_CAVE);
			}
		});
		registerBiomeProgressionEnforcement(BiomeKeys.SWAMP, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 60 == 0) {
				MobEffectInstance currentHunger = player.getEffect(MobEffects.HUNGER);

				int hungerLevel = currentHunger != null ? currentHunger.getAmplifier() + 1 : 1;

				player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, hungerLevel));

				trySpawnHintMonster(player, world, TFFeature.LABYRINTH);
			}
		});
		registerBiomeProgressionEnforcement(BiomeKeys.THORNLANDS, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 5 == 0) {
				player.hurt(DamageSource.MAGIC, 1.0F);
				world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.PLAYERS, 1.0F, 1.0F);

				// hint monster?
				trySpawnHintMonster(player, world, TFFeature.TROLL_CAVE);
			}
		});
         */
    }

    private static void registerBiomeAdvancementRestriction(RegistryKey<Biome> biome, Identifier... advancements) {
        BIOME_ADVANCEMENTS.put(biome.getValue(), advancements);
    }

    private static void registerBiomeProgressionEnforcement(Registry<Biome> biome, BiConsumer<PlayerEntity, World> exec) {
        BIOME_PROGRESSION_ENFORCEMENT.put(biome.getKey().getValue(), exec);
    }

    public static void enforceBiomeProgression(PlayerEntity player, World world) {
        Biome currentBiome = world.getBiome(player.getBlockPos());
        if (isBiomeSafeFor(currentBiome, player))
            return;
        BiConsumer<PlayerEntity, World> exec = BIOME_PROGRESSION_ENFORCEMENT.get(currentBiome.toString());// .getKey().getValue());
        if (exec != null)
            exec.accept(player, world);
    }

    private static void trySpawnHintMonster(PlayerEntity player, World world, TFFeature feature) {
        if (world.random.nextInt(4) == 0) {
            feature.trySpawnHintMonster(world, player);
        }
    }

    // FIXME Why are these three here - Can we get this from the World's DimensionType itself? Document here why not, if unable
    @Deprecated // Used in places where we can't access the sea level
    public static final int SEALEVEL = 0;

    public static boolean isStrictlyTheForrest(World world) {
        //return ((WorldAccessor)world).getDimension().toString().equals(TFConfig.COMMON_CONFIG.DIMENSION.)
        // FIXME
        return false;
        //.toString().equals(TFConfig.COMMON_CONFIG.DIMENSION.portalDestinationID.get());
    }

    public static boolean usesTheForrestChungGenerator(ServerWorld world) {
        return world.getChunkManager().getChunkGenerator() instanceof TFChunkGeneratorBase;
    }

    public static boolean isProgressionEnforced(World world) {
        // FIXME
        return true;
        //return world.getGameRules().getBoolean(TwilightForestMod.ENFORCED_PROGRESSION_RULE);
    }

    public static boolean isBiomeSafeFor(Biome biome, LivingEntity entity) {
        Identifier[] advancements = BIOME_ADVANCEMENTS.get(entity.world.isClient() ? entity.world.getRegistryManager().get(Registry.BIOME_KEY).getKey(biome) : biome.toString());
        if (advancements != null && entity instanceof PlayerEntity)
            return PlayerHelper.doesPlayerHaveRequiredAdvancements((PlayerEntity) entity, advancements);
        return true;
    }

    public static void markStructureConquered(World world, BlockPos pos, TFFeature feature) {
        /*TFChunkGeneratorBase generator = WorldUtil.getChunkGenerator(world);
        if (generator != null && TFFeature.getFeatureAt(pos.getX(), pos.getZ(), (ServerLevel) world) == feature) {
            //generator.setStructureConquered(pos, true);
        }*/
        // FIXME
    }

    public static Optional<StructureStart<?>> locateTFStructureInRange(WorldAccess world, BlockPos pos, int range) {
        TFFeature featureCheck = TFFeature.getFeatureForRegionPos(pos.getX(), pos.getZ(), (StructureWorldAccess) world);

        return locateTFStructureInRange(world, featureCheck, pos, range);
    }

    public static Optional<StructureStart<?>> locateTFStructureInRange(WorldAccess world, TFFeature featureCheck, BlockPos pos, int range) {
        int cx1 = MathHelper.floor((pos.getX() - range) >> 4);
        int cx2 = MathHelper.ceil((pos.getX() + range) >> 4);
        int cz1 = MathHelper.floor((pos.getZ() - range) >> 4);
        int cz2 = MathHelper.ceil((pos.getZ() + range) >> 4);

        // FIXME
        /*for (StructureFeature<?> structureFeature : net.minecraftforge.registries.ForgeRegistries.STRUCTURE_FEATURES) {
            if (!(structureFeature instanceof TFStructureStart))
                continue;
            TFFeature feature = ((TFStructureStart<?>) structureFeature).getFeature();
            if (feature != featureCheck)
                continue;

            for (int x = cx1; x <= cx2; ++x) {
                for (int z = cz1; z <= cz2; ++z) {
                    Optional<StructureStart<?>> structure = world.getChunk(x, z, ChunkStatus.STRUCTURE_STARTS).getReferencesForFeature(structureFeature).stream().
                            map((longVal) -> SectionPos.of(new ChunkPos(longVal), 0)).<StructureStart<?>>map((sectionPos) -> world.
                            hasChunk(sectionPos.x(), sectionPos.z()) ? world.
                            getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_STARTS).getStartForFeature(structureFeature) : null).
                            filter((structureStart) -> structureStart != null && structureStart.isValid()).
                            findFirst();
                    if (structure.isPresent())
                        return structure;
                }
            }
        }*/
        return Optional.empty();
    }
}
