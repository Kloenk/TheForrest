package dev.kloenk.forest.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class TrollRootBlock extends Block {

    protected static final VoxelShape VOXEL_SHAPE = VoxelShapes.cuboid(0.1f, 0f, 0.1f, 0.9f, 1f, 0.9f);

    public TrollRootBlock(Settings settings) {
        super(settings);
    }

    public static boolean canPlaceRootBelow(StructureWorldAccess world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        return state.isIn(BlockTags.BASE_STONE_OVERWORLD) || block == TFBlocks.TROLLVIDR || block == TFBlocks.TROLLBER || block == TFBlocks.TROLLBER_UNRIPE;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return canPlaceRootBelow((StructureWorldAccess) world, pos.up());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VOXEL_SHAPE;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP) {
            return canPlaceAt(state, world, pos) ? state: Blocks.AIR.getDefaultState();
        }
        return state;
    }
}
