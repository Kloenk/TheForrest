package dev.kloenk.forest.mixin;

import net.minecraft.block.Block;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockTags.class)
public interface BlockTagsAccessor {
    @Invoker("register")
    static Tag.Identified<Block> registerInvoker(String id) {
        throw new AssertionError("Untransformed Accessor");
    }
}
