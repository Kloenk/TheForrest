package dev.kloenk.forest.blocks.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.mob.Monster;
import net.minecraft.util.math.BlockPos;

public abstract class BossSpawnerBlockEntity<T extends Monster> extends BlockEntity {
    public BossSpawnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    // TODO
}
