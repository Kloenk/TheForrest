package dev.kloenk.forest.item;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.init.TFContent;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class TFItems {
    // NAGA
    public static final Item NAGA_SCALE = new Item(new FabricItemSettings().group(TFContent.ITEM_GROUP).rarity(Rarity.UNCOMMON));
    public static final Item NAGA_CHESTPLATE = new NagaArmorItem(ForestArmorMaterial.ARMOR_NAGA, EquipmentSlot.CHEST, new FabricItemSettings().group(TFContent.ITEM_GROUP).rarity(Rarity.UNCOMMON));
    public static final Item NAGA_LEGGINS = new NagaArmorItem(ForestArmorMaterial.ARMOR_NAGA, EquipmentSlot.LEGS, new FabricItemSettings().group(TFContent.ITEM_GROUP).rarity(Rarity.UNCOMMON));

    // Wand
    public static final Item FOREST_SCEPTER = new ForestWandItem(new FabricItemSettings().group(TFContent.ITEM_GROUP).rarity(Rarity.UNCOMMON).maxDamage(99));

    public static void register() {
        // NAGA
        registerItem("naga_scale", NAGA_SCALE);
        registerItem("naga_chestplate", NAGA_CHESTPLATE);
        registerItem("naga_leggins", NAGA_LEGGINS);

        // Wand
        registerItem("forest_scepter", FOREST_SCEPTER);
    }

    protected static void registerItem(String path, Item item) {
        registerItem(ForestMod.path(path), item);
    }

    protected static void registerItem(Identifier identifier, Item item) {
        Registry.register(Registry.ITEM, identifier, item);
    }
}
