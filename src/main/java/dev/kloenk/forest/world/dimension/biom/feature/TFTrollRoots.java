package dev.kloenk.forest.world.dimension.biom.feature;

import com.mojang.serialization.Codec;
import dev.kloenk.forest.blocks.TFBlocks;
import dev.kloenk.forest.blocks.TrollRootBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

public class TFTrollRoots extends Feature<DefaultFeatureConfig> {
    public TFTrollRoots(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> ctx) {
        StructureWorldAccess world = ctx.getWorld();
        BlockPos pos = ctx.getOrigin();
        Random random = ctx.getRandom();

        int copyX = pos.getX();
        int copyZ = pos.getZ();

        for (; pos.getY() > 5; pos = pos.down()) {
            if (world.isAir(pos) && TrollRootBlock.canPlaceRootBelow(world, pos.up()) && random.nextInt(6) > 0) {
                if (random.nextInt(10) == 0) {
                    world.setBlockState(pos, TFBlocks.TROLLBER_UNRIPE.getDefaultState(), 16 | 2);
                } else {
                    world.setBlockState(pos, TFBlocks.TROLLVIDR.getDefaultState(), 16 | 2);
                }
            } else {
                pos = new BlockPos(
                        copyX + random.nextInt(4) - random.nextInt(4),
                        pos.getY(),
                        copyZ + random.nextInt(4) - random.nextInt(4)
                );
            }
        }

        return true;
    }
}
