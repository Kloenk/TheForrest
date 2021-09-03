package dev.kloenk.forest.world.dimension.biom.feature;

import com.mojang.serialization.Codec;
import dev.kloenk.forest.blocks.TFBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

public class TFGenHugeLilyPad extends Feature<DefaultFeatureConfig> {
    public TFGenHugeLilyPad(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos pos = context.getOrigin();
        Random random = context.getRandom();

        for (int i = 0; i < 10; i++) {
            BlockPos dPos = pos.add(
                    random.nextInt(8) - random.nextInt(8),
                    random.nextInt(4) - random.nextInt(4),
                    random.nextInt(8) - random.nextInt(8)
            );

            // TODO: isValidForSetBlock?
            if (shouldPlaceAt(world, dPos) && world.isValidForSetBlock(dPos)) {
                final Direction horizontal = Direction.fromHorizontal(random.nextInt(4));
                // FIXME!!
                //final BlockState lilypad = TFBlocks.HUGE_LILYPADS.getDefaultState().with(FACING, horizontal)
                final BlockState lilypad = TFBlocks.CANOPY_LEAVES.getDefaultState();

                world.setBlockState(dPos, lilypad, 16 | 2);
                world.setBlockState(dPos.east(), lilypad, 16 | 2);
                world.setBlockState(dPos.east().south(), lilypad, 16 | 2);
                world.setBlockState(dPos.south(), lilypad, 16 | 2);

                /*world.setBlockState(dPos, lilypad.with(PIECE, NW), 16 | 2);
                world.setBlockState(dPos.east(), lilypad.with(PIECE, NE), 16 | 2);
                world.setBlockState(dPos.east().south(), lilypad.with(PIECE, SE), 16 | 2);
                world.setBlockState(dPos.south(), lilypad.with(PIECE, SW), 16 | 2);*/
            }
        }

        return true;
    }

    private boolean shouldPlaceAt(WorldAccess world, BlockPos pos) {
        return isSpaceValid(world, pos)
                && isSpaceValid(world, pos.east())
                && isSpaceValid(world, pos.south())
                && isSpaceValid(world, pos.east().south());
    }

    private boolean isSpaceValid(WorldAccess world, BlockPos pos) {
        return world.isAir(pos) && world.getBlockState(pos.down()).getMaterial() == Material.WATER;
    }
}
