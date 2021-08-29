package dev.kloenk.forest.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class GiantBlock extends Block {

    private boolean isSelfDesctructing = false;

    public GiantBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        for (BlockPos pos: getVolume(ctx.getBlockPos())) {
            if (!ctx.getWorld().getBlockState(pos).canReplace(ctx)) {
                return null;
            }
        }
        return super.getPlacementState(ctx);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        // TODO: if playerentit in getVolume, tp ontop
        if (!world.isClient) {
            for (BlockPos iterPos: getVolume(pos)) {
                world.setBlockState(iterPos, getDefaultState());
            }
        }
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
        if (/*!this.isSelfDesctructing &&*/ !isVolumeCleared(world, pos)) {
            this.setGiantBlockToAir(world, pos);
        }
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        super.onDestroyedByExplosion(world, pos, explosion);
        if (/*!this.isSelfDesctructing && */!isVolumeCleared(world, pos)) {
            this.setGiantBlockToAir(world, pos);
        }
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.BLOCK;
    }

    private void setGiantBlockToAir(WorldAccess world, BlockPos pos) {
        this.isSelfDesctructing = true;

        for (BlockPos iterPos : getVolume(pos)) {
            if (!pos.equals(iterPos) && world.getBlockState(iterPos).getBlock() == this) {
                world.removeBlock(iterPos, false);
            }
            //world.removeBlock(iterPos, false);
        }

        this.isSelfDesctructing = false;
    }

    private boolean isVolumeCleared(WorldAccess world, BlockPos pos) {
        for (BlockPos dPos: getVolume(pos)) {
            if (world.getBlockState(dPos).getBlock() == this) {
                return false;
            }
        }
        return true;
    }

    public static Iterable<BlockPos> getVolume(BlockPos pos) {
        return BlockPos.iterate(
                pos.getX() & ~0b11, pos.getY() & ~0b11, pos.getZ() & ~0b11,
                pos.getX() | 0b11, pos.getY() | 0b11, pos.getZ() | 0b11
        );
    }
}
