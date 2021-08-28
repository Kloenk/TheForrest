package dev.kloenk.forest.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class VanishingBlock extends Block {
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
    public static final BooleanProperty VANISH = BooleanProperty.of("vanish");
    public static final BooleanProperty CURRENT = BooleanProperty.of("current");

    private static final VoxelShape VANISHED_SHAPE = VoxelShapes.cuboid(6f, 6f, 6f, 10f, 10f, 10f);

    public VanishingBlock(Settings settings, boolean vanish) {
        super(settings);

        setDefaultState(getStateManager().getDefaultState().with(ACTIVE, false).with(VANISH, vanish).with(CURRENT, false));
    }

    private void setActive(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        setActive(world, pos, state);
    }

    private void setActive(World world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof VanishingBlock && !state.get(CURRENT)) {
            world.setBlockState(pos, state.with(ACTIVE, true));
            world.getBlockTickScheduler().schedule(pos, this, 2 + world.random.nextInt(5));
        }
    }

    // Copied for redstoneblock
    private static void spawnParticles(World world, BlockPos pos) {
        double d = 0.5625D;
        Random random = world.random;
        Direction[] var5 = Direction.values();
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Direction direction = var5[var7];
            BlockPos blockPos = pos.offset(direction);
            if (!world.getBlockState(blockPos).isOpaqueFullCube(world, blockPos)) {
                Direction.Axis axis = direction.getAxis();
                double e = axis == Direction.Axis.X ? 0.5D + 0.5625D * (double)direction.getOffsetX() : (double)random.nextFloat();
                double f = axis == Direction.Axis.Y ? 0.5D + 0.5625D * (double)direction.getOffsetY() : (double)random.nextFloat();
                double g = axis == Direction.Axis.Z ? 0.5D + 0.5625D * (double)direction.getOffsetZ() : (double)random.nextFloat();
                world.addParticle(DustParticleEffect.DEFAULT, (double)pos.getX() + e, (double)pos.getY() + f, (double)pos.getZ() + g, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
        builder.add(VANISH);
        builder.add(CURRENT);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(ACTIVE)) {
            spawnParticles(world, pos);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.isClient) {
            return;
        }

        if (state.get(CURRENT)) {
            if (state.get(ACTIVE)) {
                world.setBlockState(pos, state.with(ACTIVE, false).with(CURRENT, false));
            } else {
                world.setBlockState(pos, state.with(CURRENT, false));
            }
        } else {
            if (state.get(ACTIVE)) {
                if (state.get(VANISH)) {
                    world.removeBlock(pos, false);
                } else {
                    world.setBlockState(pos, state.with(CURRENT, true).with(ACTIVE, false));
                    world.getBlockTickScheduler().schedule(pos, this, 80);
                }

                // play sound

                for (Direction d: Direction.values()) {
                    setActive(world, pos.offset(d));
                }
            }
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!state.get(ACTIVE)) {
            // TODO: add locked check and play sound if locked
            setActive(world, pos, state);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(CURRENT) ? VANISHED_SHAPE : super.getOutlineShape(state, world, pos, context);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(CURRENT) ? VoxelShapes.empty() : super.getCollisionShape(state, world, pos, context);
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(CURRENT) ? 14 : 0;
    }
}
