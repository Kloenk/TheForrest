package dev.kloenk.forest.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class ThornRoseBlock extends Block {
    private static final float RADIUS = 0.4F;
    private static final VoxelShape VOXEL_SHAPE = VoxelShapes.cuboid(new Box(
            0.5F -RADIUS, 0.5F -RADIUS, 0.5F -RADIUS,
            0.5F +RADIUS, .5F +RADIUS, 0.5F +RADIUS));

    public ThornRoseBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VOXEL_SHAPE;
    }
}
