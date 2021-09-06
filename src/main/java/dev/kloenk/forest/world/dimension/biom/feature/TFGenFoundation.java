package dev.kloenk.forest.world.dimension.biom.feature;

import com.mojang.serialization.Codec;
import dev.kloenk.forest.loot.TFTreasure;
import dev.kloenk.forest.mixin.BlockStateModelGeneratorAccessor;
import dev.kloenk.forest.util.FeatureLogic;
import dev.kloenk.forest.util.FeatureUtil;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

public class TFGenFoundation extends Feature<DefaultFeatureConfig> {
    public TFGenFoundation(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos pos = context.getOrigin();
        Random random = context.getRandom();

        int sx = 5 + random.nextInt(5);
        int sz = 5 + random.nextInt(5);

        if (!FeatureUtil.isAreaSuitable(world, pos, sx, 4, sz))
            return false;

        for (int cx = 0; cx <= sx; cx++) {
            for (int cz = 0; cz <= sz; cz++) {
                if (cx == 0 || cx == sx || cz == 00 || cz == sz) {
                    // stone on the edges
                    int ht = random.nextInt(4) + 1;

                    for (int cy = 0; cy <= ht; cy++) {
                        world.setBlockState(pos.add(cx, cy -1, cz), FeatureLogic.randStone(random, cy + 1), 3);
                    }
                } else {
                    if (random.nextInt(3) != 0) {
                        world.setBlockState(pos.add(cx, -1, cz), Blocks.OAK_PLANKS.getDefaultState(), 3);

                        // Remove grass above oak planks
                        if (world.getBlockState(pos.add(cx, 0, cz)) == Blocks.GRASS.getDefaultState())
                            world.setBlockState(pos.add(cx, 0, cz), Blocks.AIR.getDefaultState(), 3);
                    }
                }
            }
        }

        // TODO: chimney?

        // 50% basement change
        if (random.nextBoolean()) {
            // clear basement
            for (int cx = 0; cx < sx; cx++) {
                for (int cz = 0; cz < sz; cz++) {
                    world.setBlockState(pos.add(cx, -2, cz), Blocks.AIR.getDefaultState(), 3);
                    world.setBlockState(pos.add(cx, -3, cz), Blocks.AIR.getDefaultState(), 3);
                    world.setBlockState(pos.add(cx, -4, cz), Blocks.AIR.getDefaultState(), 3);
                }
            }

            // make chest
            int cx = random.nextInt(sx - 1) + 1;
            int cz = random.nextInt(sz - 1) + 1;
            TFTreasure.BASEMENT.generateChest(world, pos.add(cx, -4, cz), Direction.NORTH, false);
        }

        return true;
    }
}
