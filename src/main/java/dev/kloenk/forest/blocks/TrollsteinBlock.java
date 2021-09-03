package dev.kloenk.forest.blocks;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Random;

public class TrollsteinBlock extends Block {
    private static final BooleanProperty DOWN_LIT = BooleanProperty.of("down");
    private static final BooleanProperty UP_LIT = BooleanProperty.of("up");
    private static final BooleanProperty NORTH_LIT = BooleanProperty.of("north");
    private static final BooleanProperty SOUTH_LIT = BooleanProperty.of("south");
    private static final BooleanProperty WEST_LIT  = BooleanProperty.of("west");
    private static final BooleanProperty EAST_LIT  = BooleanProperty.of("east");
    private static final Map<Direction, BooleanProperty> PROPERTY_MAP = ImmutableMap.<Direction, BooleanProperty>builder()
            .put(Direction.DOWN, DOWN_LIT)
            .put(Direction.UP, UP_LIT)
            .put(Direction.NORTH, NORTH_LIT)
            .put(Direction.SOUTH, SOUTH_LIT)
            .put(Direction.WEST, WEST_LIT)
            .put(Direction.EAST, EAST_LIT).build();

    private static final int LIGHT_THRESHHOLD = 7;

    public TrollsteinBlock(Settings settings) {
        super(settings);

        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(DOWN_LIT, false).with(UP_LIT, false)
                .with(NORTH_LIT, false).with(SOUTH_LIT, false)
                .with(WEST_LIT, false).with(EAST_LIT, false)
        );
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        boolean lit = world.getLightLevel(neighborPos) > LIGHT_THRESHHOLD;
        return state.with(PROPERTY_MAP.get(direction), lit);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState ret = getDefaultState();
        for (Map.Entry<Direction, BooleanProperty> e: PROPERTY_MAP.entrySet()) {
            int light = ctx.getWorld().getLightLevel(ctx.getBlockPos().offset(e.getKey()));
            ret = ret.with(e.getValue(), light >LIGHT_THRESHHOLD);
        }

        return ret;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(2) == 0) {
            spawnParticles(world, pos);
        }
    }

    // Copied for redstoneblock
    private static void spawnParticles(World world, BlockPos pos) {
        double offset = 0.0625D;
        Random random = world.getRandom();
        int threshold = LIGHT_THRESHHOLD;

        for (Direction side: Direction.values()) {
            double rx = pos.getX() + random.nextFloat();
            double ry = pos.getY() + random.nextFloat();
            double rz = pos.getZ() + random.nextFloat();

            if (side == Direction.DOWN && !world.getBlockState(pos.down()).isSolidBlock(world, pos) && world.getLightLevel(pos.down()) <= threshold) {
                ry = pos.getY() - offset;
            }

            if (side == Direction.UP && !world.getBlockState(pos.up()).isSolidBlock(world, pos) && world.getLightLevel(pos.up()) <= threshold) {
                ry = pos.getY() + offset + 1d;
            }

            if (side == Direction.NORTH && !world.getBlockState(pos.north()).isSolidBlock(world, pos) && world.getLightLevel(pos.north()) <= threshold) {
                rz = pos.getZ() - offset;
            }

            if (side == Direction.SOUTH && !world.getBlockState(pos.south()).isSolidBlock(world, pos) && world.getLightLevel(pos.south()) <= threshold) {
                rz = pos.getZ() + offset + 1d;
            }

            if (side == Direction.WEST && !world.getBlockState(pos.west()).isSolidBlock(world, pos) && world.getLightLevel(pos.west()) <= threshold) {
                rx = pos.getX() - offset;
            }

            if (side == Direction.EAST && !world.getBlockState(pos.east()).isSolidBlock(world, pos) && world.getLightLevel(pos.east()) <= threshold) {
                rx = pos.getX() + offset + 1d;
            }

            if (rx < pos.getX() || rx > pos.getX() + 1 || ry < 0.0D || ry > pos.getY() + 1 || rz < pos.getZ() || rz > pos.getZ() + 1) {
                world.addParticle(new DustParticleEffect(new Vec3f(0.0F, random.nextFloat(), 1.0F), 1.0F), rx, ry, rz, 0.25D, -1.0D, 0.5D);
            }
        }

        /*double d = 0.5625D;
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
        }*/
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(DOWN_LIT, UP_LIT, NORTH_LIT, SOUTH_LIT, WEST_LIT, EAST_LIT);
    }
}
