package dev.kloenk.forest.blocks;

import dev.kloenk.forest.entities.boss.BossVariant;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class BossSpawner extends BlockWithEntity {

    protected BossSpawner(Settings settings, BossVariant variant) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        // TODO
        return null;
    }

    // TODO
}
