package dev.kloenk.forest.world.dimension.biom.feature;

import com.mojang.serialization.Codec;
import dev.kloenk.forest.blocks.TFBlocks;
import dev.kloenk.forest.blocks.TFPlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

public class TFGenTorchBerries extends Feature<DefaultFeatureConfig> {

    public TFGenTorchBerries(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> ctx) {
        StructureWorldAccess world = ctx.getWorld();
        BlockPos pos = ctx.getOrigin();
        Random random = ctx.getRandom();

        int copyX = pos.getX();
        int copyZ = pos.getZ();

        for (; pos.getY() > 5; pos = pos.down()) {
            if (world.isAir(pos) && TFPlantBlock.canPlaceRootAt(world, pos) && random.nextInt(6) > 0) {
                world.setBlockState(pos, TFBlocks.TORCHBERRY_PLANT.getDefaultState(), 16 | 2);
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
