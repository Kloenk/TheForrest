package dev.kloenk.forest.data;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.mixin.BlockTagsAccessor;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.BlockTagsProvider;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import org.lwjgl.system.CallbackI;

public class BlockTagGenerator extends BlockTagsProvider {
    public static final Tag.Identified<Block> PORTAL_POOL = BlockTagsAccessor.registerInvoker(ForestMod.path("portal/fluid").toString());
    public static final Tag.Identified<Block> PORTAL_DECO = BlockTagsAccessor.registerInvoker(ForestMod.path("portal/deco").toString());
    public static final Tag.Identified<Block> PORTAL_EDGE = BlockTagsAccessor.registerInvoker(ForestMod.path("portal/edge").toString());

    public BlockTagGenerator(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void configure() {
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

}
