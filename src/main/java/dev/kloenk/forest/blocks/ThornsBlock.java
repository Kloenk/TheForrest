package dev.kloenk.forest.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class ThornsBlock extends ConnectableRotatedPillarBlock implements Waterloggable {
    private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final float THORN_DAMAGE = 4f;

    public ThornsBlock(Settings settings) {
        super(settings, 10);
        setDefaultState(getStateManager().getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    public boolean canConnectTo(BlockState state, boolean solidSide) {
        return (state.getBlock() instanceof ThornsBlock
                || state.getBlock() == TFBlocks.THORNS_ROSE
                || state.getBlock() == TFBlocks.THORNS_LEAVES
                || state.getMaterial() == Material.PLANT
                || state.getMaterial() == Material.SOIL);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        entity.damage(DamageSource.CACTUS, THORN_DAMAGE);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (state.getBlock() instanceof ThornsBlock && state.get(AXIS) == Direction.Axis.Y) {
            onEntityCollision(state, world, pos, entity);
        }
        super.onSteppedOn(world, pos, state, entity);
    }

    // TODO: removedByPlayer


    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.BLOCK;
    }

    /**
     * Grow thorns out of both the ends, then maybe in another direction too
     */
    private void doThornBurst(World world, BlockPos pos, BlockState state) {
        switch (state.get(AXIS)) {
            case Y:
                growThorns(world, pos, Direction.UP);
                growThorns(world, pos, Direction.DOWN);
                break;
            case X:
                growThorns(world, pos, Direction.EAST);
                growThorns(world, pos, Direction.WEST);
                break;
            case Z:
                growThorns(world, pos, Direction.NORTH);
                growThorns(world, pos, Direction.SOUTH);
                break;
        }

        // also try three random directions
        growThorns(world, pos, Direction.random(world.random));
        growThorns(world, pos, Direction.random(world.random));
        growThorns(world, pos, Direction.random(world.random));
    }

    /**
     * grow several green thorns in the specified direction
     */
    private void growThorns(World world, BlockPos pos, Direction dir) {
        int length = 1 + world.random.nextInt(3);

        for (int i = 1; i < length; i++) {
            BlockPos dPos = pos.offset(dir, i);

            if (world.isAir(dPos)) {
                world.setBlockState(dPos, TFBlocks.THORNS_GREEN.getDefaultState().with(AXIS, dir.getAxis()), 2);
            } else {
                break;
            }
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        boolean flag = fluidState.getFluid() == Fluids.WATER;
        return super.getPlacementState(ctx).with(WATERLOGGED, flag);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED);
    }
}
