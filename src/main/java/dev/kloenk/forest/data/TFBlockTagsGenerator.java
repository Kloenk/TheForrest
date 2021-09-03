package dev.kloenk.forest.data;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.blocks.TFBlocks;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.BlockTagsProvider;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;

public class TFBlockTagsGenerator extends BlockTagsProvider {
    public static final Tag.Identified<Block> PORTAL_POOL = TagFactory.BLOCK.create(ForestMod.path("portal/fluid"));
    public static final Tag.Identified<Block> PORTAL_DECO = TagFactory.BLOCK.create(ForestMod.path("portal/deco"));
    public static final Tag.Identified<Block> PORTAL_EDGE = TagFactory.BLOCK.create(ForestMod.path("portal/edge"));

    public static final Tag.Identified<Block> GIANT_BLOCKS = TagFactory.BLOCK.create(ForestMod.path("blocks/giant"));

    public TFBlockTagsGenerator(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void configure() {
        super.configure();
        this.configurePortal();
        this.extendVanillaTags();

        this.getOrCreateTagBuilder(GIANT_BLOCKS)
                .add(TFBlocks.GIANT_COBBLESTONE_BLOCK)
                .add(TFBlocks.GIANT_OBSIDIAN_BLOCK)
                .add(TFBlocks.GIANT_LOG_BLOCK)
                .add(TFBlocks.GIANT_LEAVES_BLOCK);

    }

    protected void configurePortal() {
        this.getOrCreateTagBuilder(PORTAL_POOL)
                .add(Blocks.WATER);

        this.getOrCreateTagBuilder(PORTAL_DECO)
                .addTag(BlockTags.FLOWERS)
                .addTag(BlockTags.LEAVES)
                .addTag(BlockTags.SAPLINGS)
                .addTag(BlockTags.CROPS)
                .add(Blocks.BAMBOO);
        // TODO
        // .add(getAllMinecraftOrTwilightBlocks(b -> (b.material == Material.PLANT || b.material == Material.REPLACEABLE_PLANT || b.material == Material.LEAVES) && !plants.contains(b)));

        this.getOrCreateTagBuilder(PORTAL_EDGE)
                .addTag(BlockTags.DIRT);

    }

    // FIXME: only extend, do not reexport
    protected void extendVanillaTags() {

        this.getOrCreateTagBuilder(BlockTags.PLANKS)
                .add(TFBlocks.OAK_WOOD)
                .add(TFBlocks.CANOPY_WOOD)
                .add(TFBlocks.MANGROVE_WOOD)
                .add(TFBlocks.DARK_WOOD);
    }

    @Override
    public String getName() {
        return "TheForrest Block Tags";
    }
}
