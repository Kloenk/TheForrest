package dev.kloenk.forest.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EncasedFireJetBlock extends FireJetBlock {
    protected EncasedFireJetBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        FireJetVariant variant = state.get(STATE);
        boolean powered = world.isReceivingRedstonePower(pos);

        if (variant == FireJetVariant.IDLE && powered) {
            world.setBlockState(pos, state.with(STATE, FireJetVariant.POPPING));
            // TODO: play sound
        }
    }
}
