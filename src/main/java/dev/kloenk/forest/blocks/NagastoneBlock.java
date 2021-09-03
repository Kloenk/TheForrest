package dev.kloenk.forest.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class NagastoneBlock extends Block {

    public static final EnumProperty<Variant> VARIANT = EnumProperty.of("variant", Variant.class);

    public NagastoneBlock(Settings settings) {
        super(settings);

        this.setDefaultState(this.getStateManager().getDefaultState().with(VARIANT, Variant.SOLID));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return getVariant(world, pos);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getVariant(ctx.getWorld(), ctx.getBlockPos());
    }

    private BlockState getVariant(WorldAccess world, BlockPos pos) {
        int connectionCount = 0;
        BlockState stateOut;
        Direction[] facings = new Direction[2];

        for (Direction side : Direction.values()) {
            BlockState neighborState = world.getBlockState(pos.offset(side));
            if (neighborState.getBlock() == this || (neighborState.getBlock() == TFBlocks.NAGA_STONE_HEAD && side == neighborState.get(TFHorizontalBlock.FACING))) {
                facings[connectionCount++] = side;
                if (connectionCount >= 2) {
                    break;
                }
            }
        }

        // if there are 2 sides that don't line on same axis, use an elbow part, else use axis part
        // if there is 1 side, then use an axis part
        // if there are 0 or greater than 2 sides, use solid
        // use default if there are more than 3 connections or 0
        switch (connectionCount) {
            case 1:
                facings[1] = facings[0]; // No null, for next statement
            case 2:
                stateOut = getDefaultState().with(VARIANT, Variant.getVariantFromDoubleFacing(facings[0], facings[1]));
                break;
            default:
                stateOut = this.getDefaultState();
                break;
        }
        return stateOut;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(VARIANT);
    }

    public enum Variant implements StringIdentifiable {
        NORTH_DOWN,
        SOUTH_DOWN,
        WEST_DOWN,
        EAST_DOWN,
        NORTH_UP,
        SOUTH_UP,
        EAST_UP,
        WEST_UP,
        AXIS_X,
        AXIS_Y,
        AXIS_Z,
        SOLID; // This can act as null


        @Override
        public String asString() {
            return name().toLowerCase(Locale.ROOT);
        }

        public static Variant getVariantFromAxis(Direction.Axis axis) {
            switch (axis) {
                case X:
                    return AXIS_X;
                case Y:
                    return AXIS_Y;
                case Z:
                    return AXIS_Z;
                default:
                    return SOLID;
            }
        }

        public static Variant getVariantFromDoubleFacing(Direction facing1, Direction facing2) {
            if (facing1.getAxis() == facing2.getAxis()) // Pairs of 6 dirs and axes
                return getVariantFromAxis(facing1.getAxis()); // Both faces are on same axis
            else if (facing1.getAxis() != Direction.Axis.Y && facing2.getAxis() != Direction.Axis.Y)
                return SOLID; // Elbow connection doesn't have Y

            Direction facingYAxis = facing1.getAxis() == Direction.Axis.Y ? facing1 : facing2;
            Direction otherFace = facing1.getAxis() != Direction.Axis.Y ? facing1 : facing2;

            if (facingYAxis == Direction.UP) {
                switch (otherFace) {
                    case NORTH:
                        return NORTH_UP;
                    case SOUTH:
                        return SOUTH_UP;
                    case WEST:
                        return WEST_UP;
                    case EAST:
                        return EAST_UP;
                    default:
                        return SOLID;
                }
            } else {
                switch (otherFace) {
                    case NORTH:
                        return NORTH_DOWN;
                    case SOUTH:
                        return SOUTH_DOWN;
                    case WEST:
                        return WEST_DOWN;
                    case EAST:
                        return EAST_DOWN;
                    default:
                        return SOLID;
                }
            }
        }
    }
}
