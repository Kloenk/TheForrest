package dev.kloenk.forest.data;

import dev.kloenk.forest.blocks.TFBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.server.BlockLootTableGenerator;

public class TFBlockLootTableGenerator {
    public final BlockLootTableGenerator generator;

    public TFBlockLootTableGenerator(BlockLootTableGenerator generator) {
        this.generator = generator;
    }

    public void addDrop(Block block) {
        this.generator.addDrop(block);
    }

    public void register() {
        this.addDrop(TFBlocks.OAK_LOG);
        this.addDrop(TFBlocks.CANOPY_LOG);
        this.addDrop(TFBlocks.MANGROVE_LOG);
        this.addDrop(TFBlocks.DARK_LOG);
        this.addDrop(TFBlocks.OAK_LOG_STRIPPED);
        this.addDrop(TFBlocks.CANOPY_LOG_STRIPPED);
        this.addDrop(TFBlocks.MANGROVE_LOG_STRIPPED);
        this.addDrop(TFBlocks.DARK_LOG_STRIPPED);
        this.addDrop(TFBlocks.OAK_WOOD);
        this.addDrop(TFBlocks.CANOPY_WOOD);
        this.addDrop(TFBlocks.MANGROVE_WOOD);
        this.addDrop(TFBlocks.DARK_WOOD);
        this.addDrop(TFBlocks.OAK_WOOD_STRIPPED);
        this.addDrop(TFBlocks.CANOPY_WOOD_STRIPPED);
        this.addDrop(TFBlocks.MANGROVE_WOOD_STRIPPED);
        this.addDrop(TFBlocks.DARK_WOOD_STRIPPED);

        this.addDrop(TFBlocks.OAK_LEAVES);
        this.addDrop(TFBlocks.CANOPY_LEAVES);
        this.addDrop(TFBlocks.MANGROVE_LEAVES);
        this.addDrop(TFBlocks.DARK_LEAVES);
        this.addDrop(TFBlocks.RAINBOW_LEAVES);

        this.addDrop(TFBlocks.MAZE_STONE);
        this.addDrop(TFBlocks.MAZE_STONE_BRICK);
        this.addDrop(TFBlocks.MAZE_STONE_CHISELED);
        this.addDrop(TFBlocks.MAZE_STONE_CRACKED);
        this.addDrop(TFBlocks.MAZE_STONE_MOSSY);
        this.addDrop(TFBlocks.MAZE_STONE_MOSAIC);
        this.addDrop(TFBlocks.MAZE_STONE_DECORATIVE);
        this.addDrop(TFBlocks.MAZE_STONE_BORDER);

        this.addDrop(TFBlocks.HEDGE);

        this.addDrop(TFBlocks.FIREFLY_JAR);
        this.addDrop(TFBlocks.CICADA_JAR);

        this.addDrop(TFBlocks.MOSS_PATCH);
        this.addDrop(TFBlocks.CLOVER_PATCH);
        this.addDrop(TFBlocks.MAYAPPLE);
        this.addDrop(TFBlocks.FIDDLEHEAD);
        this.addDrop(TFBlocks.MUSHGLOOM);
        this.addDrop(TFBlocks.TORCHBERRY_PLANT);
        this.addDrop(TFBlocks.ROOT_STRAND);
        this.addDrop(TFBlocks.FALLEN_LEAVES);

        this.addDrop(TFBlocks.ROOT);
        this.addDrop(TFBlocks.LIVEROOT);

        this.addDrop(TFBlocks.FIRE_JET);
        this.addDrop(TFBlocks.FIRE_JET_ENCASED);

        this.addDrop(TFBlocks.TROLLVIDR);
        this.addDrop(TFBlocks.TROLLBER_UNRIPE);
        this.addDrop(TFBlocks.TROLLBER);

        this.addDrop(TFBlocks.TROLLSTEIN);

        this.addDrop(TFBlocks.THORNS_LEAVES);

        this.addDrop(TFBlocks.VANISHING_BLOCK_WOOD);
        this.addDrop(TFBlocks.REAPPEARING_BLOCK_WOOD);

        this.addDrop(TFBlocks.GIANT_COBBLESTONE_BLOCK);
        this.addDrop(TFBlocks.GIANT_LEAVES_BLOCK);
        this.addDrop(TFBlocks.GIANT_LOG_BLOCK);
        this.addDrop(TFBlocks.GIANT_OBSIDIAN_BLOCK);
    }
}
