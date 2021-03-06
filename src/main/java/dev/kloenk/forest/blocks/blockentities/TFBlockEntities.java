package dev.kloenk.forest.blocks.blockentities;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.blocks.TFBlocks;
import dev.kloenk.forest.blocks.blockentities.spawner.LichSpawnerBlockEntity;
import dev.kloenk.forest.blocks.blockentities.spawner.UrGhastSpawnerBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TFBlockEntities {
    // Jets
    public static final BlockEntityType<FireJetBlockEntity> FIRE_JET = FabricBlockEntityTypeBuilder.create(FireJetBlockEntity::new, TFBlocks.FIRE_JET).build(null);

    // BossSpawners
    public static final BlockEntityType<LichSpawnerBlockEntity> LICH_SPAWNER = FabricBlockEntityTypeBuilder.create(LichSpawnerBlockEntity::new, TFBlocks.BOSS_SPAWNER_LICH).build(null);
    public static final BlockEntityType<UrGhastSpawnerBlockEntity> Ur_GHAST_SPAWNER = FabricBlockEntityTypeBuilder.create(UrGhastSpawnerBlockEntity::new, TFBlocks.BOSS_SPAWNER_UR_GHAST).build(null);

    public static void register() {
        registerEntity("fire_jet_entity", FIRE_JET);

        // boss spawner
        registerEntity("lich_spawner_entity", LICH_SPAWNER);
        registerEntity("ur_ghast_spawner_entity", Ur_GHAST_SPAWNER);
    }

    private static void registerEntity(String path, BlockEntityType<?> type) {
        registerEntity(ForestMod.path(path), type);
    }

    private static void registerEntity(Identifier id, BlockEntityType<?> type) {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, id, type);
    }
}
