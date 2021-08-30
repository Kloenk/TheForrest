package dev.kloenk.forest.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MazeStoneBlock extends Block {
    public MazeStoneBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        super.onBlockBreakStart(state, world, pos, player);
        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);

        // TODO
        if (!world.isClient && !stack.isEmpty() && stack.getItem().isDamageable() /*&& !(stack.getItem() instanceof MazeBrakerPickItem)*/) {
            stack.damage(16, player, (user) -> user.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
    }
}
