package dev.kloenk.forest.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class JarBlock extends Block {
    private static final VoxelShape JAR = VoxelShapes.cuboid(4d, 0d, 3d, 13d, 14d, 13d);
    private static final VoxelShape LID = VoxelShapes.cuboid(4d, 14d, 4d, 12d, 16d, 12d);
    // TODO: should be Voxelschapes.or, but is `-`
    private static final VoxelShape VOXEL_SHAPE = VoxelShapes.union(JAR, LID);

    public JarBlock(Settings settings) {
        super(settings);
    }

    /*
    TODO: enable whene assets are available
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VOXEL_SHAPE;
    }*/

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);

        // need to counter a higher random tick speed resulting in so many sounds, so here we go
        // TODO
        /*if(!TFConfig.CLIENT_CONFIG.silentCicadas.get() && random.nextInt(worldIn.getGameRules().getRule(GameRules.RULE_RANDOMTICKING).get()) <= 3) {
            worldIn.playSound(null, pos, TFSounds.CICADA, SoundSource.BLOCKS, 1.0F, 1.0F);
        }*/
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {
        if(this == TFBlocks.FIREFLY_JAR) {
            for (int i = 0; i < 2; i++) {
                double dx = pos.getX() + ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);
                double dy = pos.getY() + 0.4F + ((rand.nextFloat() - rand.nextFloat()) * 0.3F);
                double dz = pos.getZ() + ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);

                // TODO: particle
                //world.addParticle(TFParticleType.FIREFLY.get(), dx, dy, dz, 0, 0, 0);
            }
        } else {
            double dx = pos.getX() + ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);
            double dy = pos.getY() + 0.4F + ((rand.nextFloat() - rand.nextFloat()) * 0.2F);
            double dz = pos.getZ() + ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);

            // TODO: particle
            //world.addParticle(ParticleTypes.NOTE, dx, dy, dz, 0, 0, 0);
        }
    }

}
