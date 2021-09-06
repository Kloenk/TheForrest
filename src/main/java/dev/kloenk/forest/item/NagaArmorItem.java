package dev.kloenk.forest.item;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class NagaArmorItem extends ArmorItem {
    public NagaArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (isIn(group)) {
            ItemStack isStack = new ItemStack(this);
            switch (this.slot) {
                case CHEST:
                    isStack.addEnchantment(Enchantments.FIRE_PROTECTION, 3);
                    break;
                case LEGS:
                    isStack.addEnchantment(Enchantments.PROTECTION, 3);
                    break;
                default:
                    break;
            }
            stacks.add(isStack);
        }
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        switch (this.slot) {
            case CHEST -> stack.addEnchantment(Enchantments.FIRE_PROTECTION, 3);
            case LEGS -> stack.addEnchantment(Enchantments.PROTECTION, 3);
        }
    }
}
