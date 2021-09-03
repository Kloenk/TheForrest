package dev.kloenk.forest.world.dimension.biom.feature;

import com.mojang.serialization.Codec;
import dev.kloenk.forest.blocks.TFBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.PillarBlock;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.BlockSource;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

public class TFGenThorns extends Feature<DefaultFeatureConfig> {

    private static final int MAX_SPREAD = 7;
    private static final int CHANCE_OF_BRANCH = 3;
    private static final int CHANCE_OF_LEAF = 3;
    private static final int CHANCE_LEAF_IS_ROSE = 50;

    public TFGenThorns(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> ctx) {
        StructureWorldAccess world = ctx.getWorld();
        BlockPos pos = ctx.getOrigin();
        Random rand = ctx.getRandom();


        // make a 3-5 long stack going up
        int nextLength = 2 + rand.nextInt(4);
        int maxLength = 2 + rand.nextInt(4) + rand.nextInt(4) + rand.nextInt(4);

        generateThorns(world, rand, pos, nextLength, Direction.UP, maxLength, pos);

        return true;
    }

    private void generateThorns(StructureWorldAccess world, Random rand, BlockPos pos, int length, Direction direction, int maxLength, BlockPos oPos) {
        boolean complete = false;
        for (int i = 0; i < length; i++) {
            BlockPos dPos = pos.offset(direction, i);

            if (world.isSkyVisibleAllowingSea(pos)) {
                if (Math.abs(dPos.getX() - oPos.getX()) < MAX_SPREAD && Math.abs(dPos.getZ() - oPos.getZ()) < MAX_SPREAD && canPlaceThorns(world, dPos)) {
                    world.setBlockState(dPos, TFBlocks.THORNS_BROWN.getDefaultState().with(PillarBlock.AXIS, direction.getAxis()), 1 | 2);

                    // did we make it to the end?
                    if (i == length - 1) {
                        complete = true;
                        // maybe a leaf?  or a rose?
                        if (rand.nextInt(CHANCE_OF_LEAF) == 0 && world.isAir(dPos.offset(direction))) {
                            if (rand.nextInt(CHANCE_LEAF_IS_ROSE) > 0) {
                                // leaf
                                world.setBlockState(dPos.offset(direction), TFBlocks.THORNS_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), 3);
                            } else {
                                // rose
                                world.setBlockState(dPos.offset(direction), TFBlocks.THORNS_ROSE.getDefaultState(), 3);
                            }
                        }
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        // add another off the end
        if (complete && maxLength > 1) {

            Direction nextDir = Direction.random(rand);

            BlockPos nextPos = pos.offset(direction, length - 1).offset(nextDir);
            int nextLength = 1 + rand.nextInt(maxLength);

            this.generateThorns(world, rand, nextPos, nextLength, nextDir, maxLength - 1, oPos);
        }

        // maybe another branch off the middle
        if (complete && length > 3 && rand.nextInt(CHANCE_OF_BRANCH) == 0) {

            int middle = rand.nextInt(length);

            Direction nextDir = Direction.random(rand);

            BlockPos nextPos = pos.offset(direction, middle).offset(nextDir);
            int nextLength = 1 + rand.nextInt(maxLength);

            this.generateThorns(world, rand, nextPos, nextLength, nextDir, maxLength - 1, oPos);
        }

        // maybe a leaf
        if (complete && length > 3 && rand.nextInt(CHANCE_OF_LEAF) == 0) {

            int middle = rand.nextInt(length);

            Direction nextDir = Direction.random(rand);

            BlockPos nextPos = pos.offset(direction, middle).offset(nextDir);

            if (world.isAir(nextPos)) {
                world.setBlockState(nextPos, TFBlocks.THORNS_LEAVES.getDefaultState(), 3/*.with(LeavesBlock.CHECK_DECAY, false)*/);
            }
        }
    }

    private boolean canPlaceThorns(StructureWorldAccess world, BlockPos dPos) {
        BlockState state = world.getBlockState(dPos);
        return state.isAir()|| state.isIn(BlockTags.LEAVES);
    }


}
