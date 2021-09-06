package dev.kloenk.forest.mixin;

import net.minecraft.block.Block;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockLootTableGenerator.class)
public interface BlockLootTableGeneratorAccessor {
    @Invoker("addDrop")
    void addDropInvoker(Block block, LootTable.Builder lootTable);
}
