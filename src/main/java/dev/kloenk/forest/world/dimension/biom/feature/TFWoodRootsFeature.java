package dev.kloenk.forest.world.dimension.biom.feature;

import com.mojang.serialization.Codec;
import dev.kloenk.forest.blocks.TFBlocks;
import dev.kloenk.forest.util.FeatureLogic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

public class TFWoodRootsFeature extends Feature<DefaultFeatureConfig> {
    // TODO
    private final BlockState rootBlock = TFBlocks.ROOT.getDefaultState();
    private final BlockState oreBlock = TFBlocks.LIVEROOT.getDefaultState();

    public TFWoodRootsFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> ctx) {
        StructureWorldAccess world = ctx.getWorld();
        BlockPos pos = ctx.getOrigin();
        Random rand = ctx.getRandom();

        // start must be in stone
        if (world.getBlockState(pos).getBlock() != Blocks.STONE) {
            return false;
        }

        float length = rand.nextFloat() * 6f + rand.nextFloat() * 6f * 4f;
        if (length > pos.getY()) {
            length = pos.getY();
        }

        // tilt between 0.6 and 0.9
        float tilt = 0.6F + rand.nextFloat() * 0.3F;

        return generateRoot(world, rand, pos, length, rand.nextFloat(), tilt);
    }

    private boolean generateRoot(StructureWorldAccess world, Random rand, BlockPos pos, float length, float angle, float tilt) {
        return generateRoot(world, rand, pos, pos, length, angle, tilt);
    }

    private boolean generateRoot(StructureWorldAccess world, Random rand, BlockPos oPos, BlockPos pos, float length, float angle, float tilt) {
        BlockPos dest = FeatureLogic.translate(pos, length, angle, tilt);

        int limit = 6;
        if (oPos.getX() + limit < dest.getX()) {
            dest = new BlockPos(oPos.getX() + limit, dest.getY(), dest.getZ());
        }
        if (oPos.getX() - limit > dest.getX()) {
            dest = new BlockPos(oPos.getX() - limit, dest.getY(), dest.getZ());
        }
        if (oPos.getZ() + limit < dest.getZ()) {
            dest = new BlockPos(dest.getX(), dest.getY(), oPos.getZ() + limit);
        }
        if (oPos.getZ() - limit > dest.getZ()) {
            dest = new BlockPos(dest.getX(), dest.getY(), oPos.getZ() - limit);
        }

        // end must be in stone
        if (world.getBlockState(dest).getBlock() != Blocks.STONE) {
            return false;
        }

        // if both the start and the end are in stone, put a root there
        BlockPos[] lineArray = FeatureLogic.getBresenhamArrays(pos, dest);
        for (BlockPos coord : lineArray) {
            this.placeRootBlock(world, coord, rootBlock);
        }

        // if we are long enough, make either another root or an oreball
        if (length > 8) {
            if (rand.nextInt(3) > 0) {
                // length > 8, usually split off into another root half as long
                BlockPos nextSrc = FeatureLogic.translate(pos, length / 2, angle, tilt);
                float nextAngle = (angle + 0.25F + (rand.nextFloat() * 0.5F)) % 1.0F;
                float nextTilt = 0.6F + rand.nextFloat() * 0.3F;
                generateRoot(world, rand, oPos, nextSrc, length / 2.0F, nextAngle, nextTilt);
            }
        }

        if (length > 6) {
            if (rand.nextInt(4) == 0) {
                // length > 6, potentially make oreball
                BlockPos ballSrc = FeatureLogic.translate(pos, length / 2, angle, tilt);
                BlockPos ballDest = FeatureLogic.translate(ballSrc, 1.5, (angle + 0.5F) % 1.0F, 0.75);

                this.placeRootBlock(world, ballSrc, oreBlock);
                this.placeRootBlock(world, new BlockPos(ballSrc.getX(), ballSrc.getY(), ballDest.getZ()), oreBlock);
                this.placeRootBlock(world, new BlockPos(ballDest.getX(), ballSrc.getY(), ballSrc.getZ()), oreBlock);
                this.placeRootBlock(world, new BlockPos(ballSrc.getX(), ballSrc.getY(), ballDest.getZ()), oreBlock);
                this.placeRootBlock(world, new BlockPos(ballSrc.getX(), ballDest.getY(), ballSrc.getZ()), oreBlock);
                this.placeRootBlock(world, new BlockPos(ballSrc.getX(), ballDest.getY(), ballDest.getZ()), oreBlock);
                this.placeRootBlock(world, new BlockPos(ballDest.getX(), ballDest.getY(), ballSrc.getZ()), oreBlock);
                this.placeRootBlock(world, ballDest, oreBlock);
            }
        }

        return true;
    }

    /**
     * Function used to actually place root blocks if they're not going to break anything important
     */
    protected void placeRootBlock(StructureWorldAccess world, BlockPos pos, BlockState state) {
        if (FeatureLogic.canRootGrowIn(world, pos)) {
            world.setBlockState(pos, state, 3);
        }
    }
}
