package dev.kloenk.forest.item;

import dev.kloenk.forest.entities.projectile.ForestWandBoltEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ForestWandItem extends Item {

    public ForestWandItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        //return super.use(world, user, hand);
        ItemStack stack = user.getStackInHand(hand);

        if (stack.getDamage() == stack.getMaxDamage()) {
            return TypedActionResult.fail(stack);
        } else {
            // TODO: sound

            if (!world.isClient) {
                world.spawnEntity(new ForestWandBoltEntity(world, user));
                stack.damage(1, world.random, null);
            }

            return TypedActionResult.success(stack);
        }
    }

    // TODO: repair experience points

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(new TranslatableText("forest.scepter_charges", stack.getMaxDamage() - stack.getDamage()).formatted(Formatting.GRAY));
    }
}
