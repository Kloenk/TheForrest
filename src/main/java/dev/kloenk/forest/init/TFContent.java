package dev.kloenk.forest.init;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.blocks.GiantBlock;
import dev.kloenk.forest.blocks.VanishingBlock;
import dev.kloenk.forest.entities.GiantMinerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TFContent {
    public static final ItemGroup ITEM_GROUP;

    // Vanishing
    public static final Block VANISHING_BLOCK_WOOD;
    public static final Block REAPPEARING_BLOCK_WOOD;

    // Giant
    public static final Block GIANT_COBBLESTONE_BLOCK;
    public static final Block GIANT_LEAVES_BLOCK;
    public static final Block GIANT_LOG_BLOCK;
    public static final Block GIANT_OBSIDIAN_BLOCK;
    public static final EntityType<GiantMinerEntity> GIANT_MINER;
    public static final Item GIANT_MINER_SPAWN_EGG;


    public static void register() {
        registerBlockAndItem(new Identifier(ForestMod.ID, "vanishing_block_wood"), VANISHING_BLOCK_WOOD);
        registerBlockAndItem(new Identifier(ForestMod.ID, "reappearing_block_wood"), REAPPEARING_BLOCK_WOOD);

        registerBlockAndItem(new Identifier(ForestMod.ID, "giant_cobblestone"), GIANT_COBBLESTONE_BLOCK);
        registerBlockAndItem(new Identifier(ForestMod.ID, "giant_leaves"), GIANT_LEAVES_BLOCK);
        registerBlockAndItem(new Identifier(ForestMod.ID, "giant_log"), GIANT_LOG_BLOCK);
        registerBlockAndItem(new Identifier(ForestMod.ID, "giant_obsidian"), GIANT_OBSIDIAN_BLOCK);
        FabricDefaultAttributeRegistry.register(GIANT_MINER, GiantMinerEntity.createGiantMinerAttributes());
        Registry.register(Registry.ITEM, new Identifier(ForestMod.ID, "giant_miner_spawn_egg"), GIANT_MINER_SPAWN_EGG);
    }

    private static void registerBlockAndItem(Identifier identifier, Block block) {
        registerBlockAndItem(identifier, block, new FabricItemSettings().group(ITEM_GROUP));
    }

    private static void registerBlockAndItem(Identifier identifier, Block block, FabricItemSettings itemSettings) {
        Registry.register(Registry.BLOCK, identifier, block);
        Registry.register(Registry.ITEM, identifier, new BlockItem(block, itemSettings));
    }

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        EntityRendererRegistry.register(GIANT_MINER, (ctx) -> new GiantMinerEntity.GiantMinerEntityRenderer<GiantMinerEntity>(ctx));
    }

    static {
        ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(ForestMod.ID, "general"), () -> new ItemStack(Items.DIAMOND));

        // Vanishing
        VANISHING_BLOCK_WOOD = new VanishingBlock(FabricBlockSettings.of(Material.WOOD, MapColor.YELLOW).strength(10, 35f).sounds(BlockSoundGroup.WOOD).lightLevel((state) -> state.get(VanishingBlock.ACTIVE) ? 4 : 0), true);
        REAPPEARING_BLOCK_WOOD = new VanishingBlock(FabricBlockSettings.of(Material.WOOD, MapColor.YELLOW).strength(8, 35f).sounds(BlockSoundGroup.WOOD).lightLevel((state) -> state.get(VanishingBlock.ACTIVE) ? 4 : 0), false);

        // Giant
        GIANT_COBBLESTONE_BLOCK = new GiantBlock((FabricBlockSettings.of(Material.STONE).strength(10, 15f).requiresTool().sounds(BlockSoundGroup.STONE)));
        GIANT_LEAVES_BLOCK = new GiantBlock(FabricBlockSettings.of(Material.LEAVES).strength(10, 5f).sounds(BlockSoundGroup.AZALEA_LEAVES));
        GIANT_LOG_BLOCK = new GiantBlock(FabricBlockSettings.of(Material.WOOD).strength(10, 10f).requiresTool().sounds(BlockSoundGroup.WOOD));
        GIANT_OBSIDIAN_BLOCK = new GiantBlock((FabricBlockSettings.of(Material.STONE, MapColor.BLACK).strength(10, 35f).requiresTool().sounds(BlockSoundGroup.NETHER_BRICKS)));
        GIANT_MINER = Registry.register(Registry.ENTITY_TYPE, new Identifier(ForestMod.ID, "giant_miner"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GiantMinerEntity::new).dimensions(EntityDimensions.fixed(2.4f, 7.2f)).build());
        GIANT_MINER_SPAWN_EGG = new SpawnEggItem(GIANT_MINER, 12895428, 11382189, new Item.Settings().group(ItemGroup.MISC));
    }
}
