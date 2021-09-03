package dev.kloenk.forest.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BurntThornsBlock extends ThornsBlock {
    public BurntThornsBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        // dissolve
        if (!world.isClient && entity instanceof LivingEntity) {
            world.removeBlock(pos, false);
        }
    }
}
