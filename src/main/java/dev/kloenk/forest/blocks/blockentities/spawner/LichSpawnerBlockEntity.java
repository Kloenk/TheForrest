package dev.kloenk.forest.blocks.blockentities.spawner;

import dev.kloenk.forest.blocks.blockentities.TFBlockEntities;
import dev.kloenk.forest.entities.TFEntities;
import dev.kloenk.forest.entities.boss.LichEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.client.model.BlockStateSupplier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class LichSpawnerBlockEntity extends BossSpawnerBlockEntity<LichEntity> {

    public LichSpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(TFBlockEntities.LICH_SPAWNER, TFEntities.LICH, pos, state);
    }

    @Override
    public boolean isPlayerInRange() {
        PlayerEntity closestPlayer = world.getClosestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, getRange(), false);
        return closestPlayer != null && closestPlayer.getY() > pos.getY() - 4;
    }
}
