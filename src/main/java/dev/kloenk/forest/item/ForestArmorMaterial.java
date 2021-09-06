package dev.kloenk.forest.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.function.Supplier;

public enum ForestArmorMaterial implements ArmorMaterial {
    ARMOR_NAGA("naga_scale", 21, new int[]{3, 6, 7, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0.5F, () -> Ingredient.ofItems(TFItems.NAGA_SCALE));

    protected static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
    protected final String name;
    protected final int durability;
    protected final int[] protection;
    protected final int enchantability;
    protected final SoundEvent equipSound;
    protected final float toughness;
    protected final Supplier<Ingredient> repairIngredient;

    ForestArmorMaterial(String name, int durability, int damageRedultion[], int enchantability, SoundEvent sound, float thoughness, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durability = durability;
        this.protection = damageRedultion;
        this.enchantability = enchantability;
        this.equipSound = sound;
        this.toughness = thoughness;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurability(EquipmentSlot slot) {
        // FIXME: null handling
        return MAX_DAMAGE_ARRAY[slot.getEntitySlotId()] * durability;
    }

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        // FIXME: null handling
        return this.protection[slot.getEntitySlotId()];
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        // TODO: ?
        return 0;
    }
}
