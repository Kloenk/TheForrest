package dev.kloenk.forest.blocks;

import dev.kloenk.forest.blocks.blockentities.spawner.BossSpawnerBlockEntity;
import dev.kloenk.forest.entities.boss.BossVariant;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BossSpawner extends BlockWithEntity {

    private final BossVariant variant;

    protected BossSpawner(Settings settings, BossVariant variant) {
        super(settings);
        this.variant = variant;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        BlockEntityType<? extends BossSpawnerBlockEntity<?>> type = variant.getType();
        if (type == null)
            return null;

        return type.instantiate(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, variant.getType(), BossSpawnerBlockEntity::tick);
    }


    // TODO
}
