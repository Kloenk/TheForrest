package dev.kloenk.forest.blocks.blockentities.spawner;

import dev.kloenk.forest.blocks.blockentities.TFBlockEntities;
import dev.kloenk.forest.entities.TFEntities;
import dev.kloenk.forest.entities.boss.UrGhastEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class UrGhastSpawnerBlockEntity extends BossSpawnerBlockEntity<UrGhastEntity> {
    public UrGhastSpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(TFBlockEntities.Ur_GHAST_SPAWNER, TFEntities.UR_GHAST, pos, state);
    }

    @Override
    public boolean isPlayerInRange() {
        PlayerEntity closestPlayer = world.getClosestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, getRange(), false);
        return closestPlayer != null && closestPlayer.getY() > pos.getY() - 4;
    }
}
