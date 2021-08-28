package dev.kloenk.forest.init;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.blocks.VanishingBlock;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TFContent {
    public static final ItemGroup ITEM_GROUP;

    public static final Block VANISHING_BLOCK_WOOD;
    public static final Block REAPPEARING_BLOCK_WOOD;

    public static void register() {
        registerBlockAndItem(new Identifier(ForestMod.ID, "vanishing_block_wood"), VANISHING_BLOCK_WOOD);
        registerBlockAndItem(new Identifier(ForestMod.ID, "reappearing_block_wood"), REAPPEARING_BLOCK_WOOD);
    }

    private static void registerBlockAndItem(Identifier identifier, Block block) {
        registerBlockAndItem(identifier, block, new FabricItemSettings().group(ITEM_GROUP));
    }

    private static void registerBlockAndItem(Identifier identifier, Block block, FabricItemSettings itemSettings) {
        Registry.register(Registry.BLOCK, identifier, block);
        Registry.register(Registry.ITEM, identifier, new BlockItem(block, itemSettings));
    }

    static {
        ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(ForestMod.ID, "general"), () -> new ItemStack(Items.DIAMOND));

        VANISHING_BLOCK_WOOD = new VanishingBlock(FabricBlockSettings.of(Material.WOOD, MapColor.YELLOW).strength(-1f, 35f).sounds(BlockSoundGroup.WOOD).lightLevel((state) -> state.get(VanishingBlock.ACTIVE) ? 4 : 0), true);
        REAPPEARING_BLOCK_WOOD = new VanishingBlock(FabricBlockSettings.of(Material.WOOD, MapColor.YELLOW).strength(-1f, 35f).sounds(BlockSoundGroup.WOOD).lightLevel((state) -> state.get(VanishingBlock.ACTIVE) ? 4 : 0), false);
    }
}
