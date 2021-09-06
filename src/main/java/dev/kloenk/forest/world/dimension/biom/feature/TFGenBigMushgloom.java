package dev.kloenk.forest.world.dimension.biom.feature;

import com.mojang.serialization.Codec;
import dev.kloenk.forest.blocks.TFBlocks;
import dev.kloenk.forest.util.FeatureUtil;
import dev.kloenk.forest.util.HugeMushroomUtil;
import net.minecraft.block.BlockState;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

public class TFGenBigMushgloom extends Feature<DefaultFeatureConfig> {
    public TFGenBigMushgloom(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess worldAccess = context.getWorld();
        BlockPos pos = context.getOrigin();
        Random random = context.getRandom();

        int height = 3 + random.nextInt(2) + random.nextInt(2);

        if (!FeatureUtil.isAreaSuitable(worldAccess, pos.add(-1, 0, -1), 3, height, 3))
            return false;

        BlockState blockDown = worldAccess.getBlockState(pos.down());
        if (!isSoil(blockDown) && !blockDown.isIn(BlockTags.MUSHROOM_GROW_BLOCK))
            return false;

        // generate
        for (int dy = 0; dy < height - 2; dy++) {
            worldAccess.setBlockState(pos.up(dy), TFBlocks.HUGE_MUSHGLOOM_STEM.getDefaultState(), 3);
        }

        makeMushroomCap(worldAccess, pos.up(height - 2));
        if (random.nextBoolean()) {
            makeMushroomCap(worldAccess, pos.up(height - 1));
        }

        return true;
    }

    protected void makeMushroomCap(StructureWorldAccess world, BlockPos pos) {
        BlockState defState = TFBlocks.HUGE_MUSHGLOOM.getDefaultState();
        world.setBlockState(pos.add(-1, 0, -1), HugeMushroomUtil.getState(HugeMushroomUtil.HugeMushroomType.NORTH_WEST, defState), 3);
        world.setBlockState(pos.add(0, 0, -1), HugeMushroomUtil.getState(HugeMushroomUtil.HugeMushroomType.NORTH, defState), 3);
        world.setBlockState(pos.add(1, 0, -1), HugeMushroomUtil.getState(HugeMushroomUtil.HugeMushroomType.NORTH_EAST, defState), 3);
        world.setBlockState(pos.add(-1, 0, 0), HugeMushroomUtil.getState(HugeMushroomUtil.HugeMushroomType.WEST, defState), 3);
        world.setBlockState(pos, HugeMushroomUtil.getState(HugeMushroomUtil.HugeMushroomType.CENTER, defState), 3);
        world.setBlockState(pos.add(1, 0, 0), HugeMushroomUtil.getState(HugeMushroomUtil.HugeMushroomType.EAST, defState), 3);
        world.setBlockState(pos.add(-1, 0, 1), HugeMushroomUtil.getState(HugeMushroomUtil.HugeMushroomType.SOUTH_WEST, defState), 3);
        world.setBlockState(pos.add(0, 0, 1), HugeMushroomUtil.getState(HugeMushroomUtil.HugeMushroomType.SOUTH, defState), 3);
        world.setBlockState(pos.add(1, 0, 1), HugeMushroomUtil.getState(HugeMushroomUtil.HugeMushroomType.SOUTH_EAST, defState), 3);
    }
}
