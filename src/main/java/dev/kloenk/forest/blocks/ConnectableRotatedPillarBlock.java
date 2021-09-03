package dev.kloenk.forest.blocks;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.sql.ConnectionBuilder;
import java.util.Set;

public class ConnectableRotatedPillarBlock extends PillarBlock {
    public static final BooleanProperty NORTH = ConnectingBlock.NORTH;
    public static final BooleanProperty SOUTH = ConnectingBlock.SOUTH;
    public static final BooleanProperty WEST = ConnectingBlock.WEST;
    public static final BooleanProperty EAST = ConnectingBlock.EAST;
    public static final BooleanProperty UP = ConnectingBlock.UP;
    public static final BooleanProperty DOWN = ConnectingBlock.DOWN;

    final double boundingBoxWidthLower;
    final double boundingBoxWidthUpper;

    private final double boundingBoxHeightLower;
    private final double boundingBoxHeightUpper;


    public ConnectableRotatedPillarBlock(Settings settings, double size) {
        this(settings, size, size);
    }

    public ConnectableRotatedPillarBlock(Settings settings, double width, double height) {
        super(settings);

        if (width >= 16d) {
            this.boundingBoxWidthLower = 0d;
            this.boundingBoxWidthUpper = 16d;
        } else {
            this.boundingBoxWidthLower = 8d-(width/2);
            this.boundingBoxWidthUpper = 16d-this.boundingBoxWidthLower;
        }

        if (height >= 16d) {
            this.boundingBoxHeightLower = 0d;
            this.boundingBoxHeightUpper = 16d;
        } else {
            this.boundingBoxHeightLower = 8d-(height/2d);
            this.boundingBoxHeightUpper = 16d-this.boundingBoxHeightLower;
        }

        setDefaultState(getStateManager().getDefaultState()
                .with(NORTH, false).with(WEST, false)
                .with(SOUTH, false).with(EAST, false)
                .with(UP, false).with(DOWN, false)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return state.with(ConnectingBlock.FACING_PROPERTIES.get(direction), this.canConnectTo(neighborState, neighborState.isSideSolid(world, neighborPos, direction.getOpposite(), SideShapeType.CENTER)));
    }

    public boolean canConnectTo(BlockState state, boolean solidSide) {
        return !cannotConnect(state) && solidSide;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockView world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        FluidState fluidState = world.getFluidState(pos);
        BlockPos blockpos1 = pos.north();
        BlockPos blockpos2 = pos.south();
        BlockPos blockpos3 = pos.west();
        BlockPos blockpos4 = pos.east();
        BlockState blockstate = world.getBlockState(blockpos1);
        BlockState blockstate1 = world.getBlockState(blockpos2);
        BlockState blockstate2 = world.getBlockState(blockpos3);
        BlockState blockstate3 = world.getBlockState(blockpos4);
        return this.getDefaultState()
                .with(AXIS, ctx.getSide().getAxis())
                .with(NORTH, this.canConnectTo(
                        blockstate, blockstate.isSideSolid(world, blockpos1, Direction.SOUTH, SideShapeType.CENTER)))
                .with(SOUTH, this.canConnectTo(
                        blockstate1, blockstate1.isSideSolid(world, blockpos2, Direction.NORTH, SideShapeType.CENTER)))
                .with(WEST, this.canConnectTo(
                        blockstate2, blockstate2.isSideSolid(world, blockpos3, Direction.EAST, SideShapeType.CENTER)))
                .with(EAST, this.canConnectTo(
                        blockstate3, blockstate3.isSideSolid(world, blockpos4, Direction.WEST, SideShapeType.CENTER)));
    }

    protected Box getSidedBoxStraight(Direction facing, Direction.Axis axis) {
        return makeQuickBox(
                facing == Direction.EAST  ? 16d : axis == Direction.Axis.X ? this.boundingBoxHeightLower : this.boundingBoxWidthLower,
                facing == Direction.UP    ? 16d : axis == Direction.Axis.Y ? this.boundingBoxHeightLower : this.boundingBoxWidthLower,
                facing == Direction.SOUTH ? 16d : axis == Direction.Axis.Z ? this.boundingBoxHeightLower : this.boundingBoxWidthLower,
                facing == Direction.WEST  ?  0d : axis == Direction.Axis.X ? this.boundingBoxHeightUpper : this.boundingBoxWidthUpper,
                facing == Direction.DOWN  ?  0d : axis == Direction.Axis.Y ? this.boundingBoxHeightUpper : this.boundingBoxWidthUpper,
                facing == Direction.NORTH ?  0d : axis == Direction.Axis.Z ? this.boundingBoxHeightUpper : this.boundingBoxWidthUpper);
    }

    private Box makeQuickBox(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new Box(
                x1/16.0d, y1/16.0d,
                z1/16.0d, x2/16.0d,
                y2/16.0d, z2/16.0d
        );
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (state.get(AXIS)) {
            case X:
                return VoxelShapes.cuboid(
                        0d,
                        state.get(NORTH) ?  0d : this.boundingBoxWidthLower,
                        state.get(WEST ) ?  0d : this.boundingBoxWidthLower,
                        16d,
                        state.get(SOUTH) ? 16d : this.boundingBoxWidthUpper,
                        state.get(EAST ) ? 16d : this.boundingBoxWidthUpper
                );
            case Z:
                return VoxelShapes.cuboid(
                        state.get(EAST ) ?  0d : this.boundingBoxWidthLower,
                        state.get(SOUTH) ?  0d : this.boundingBoxWidthLower,
                        0d,
                        state.get(WEST ) ? 16d : this.boundingBoxWidthUpper,
                        state.get(NORTH) ? 16d : this.boundingBoxWidthUpper,
                        16d
                );
            default:
                return VoxelShapes.cuboid(
                        state.get(WEST)  ?  0d : this.boundingBoxWidthLower,
                        0d,
                        state.get(NORTH) ?  0d : this.boundingBoxWidthLower,
                        state.get(EAST)  ? 16d : this.boundingBoxWidthUpper,
                        16d,
                        state.get(SOUTH) ? 16d : this.boundingBoxWidthUpper
                );
        }
    }

}
