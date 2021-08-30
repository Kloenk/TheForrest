package dev.kloenk.forest.blocks.blockentities;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.blocks.FireJetBlock;
import dev.kloenk.forest.blocks.TFBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TFBlockEntities {
    // Jets
    public static final BlockEntityType<FireJetBlockEntity> FIRE_JET = FabricBlockEntityTypeBuilder.create(FireJetBlockEntity::new, TFBlocks.FIRE_JET).build(null);


    public static void register() {
        registerEntity("fire_jet_entity", FIRE_JET);
    }

    private static void registerEntity(String path, BlockEntityType<?> type) {
        registerEntity(ForestMod.path(path), type);
    }

    private static void registerEntity(Identifier id, BlockEntityType<?> type) {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, id, type);
    }
}
