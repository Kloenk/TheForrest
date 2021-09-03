package dev.kloenk.forest.mixin;

import net.minecraft.data.server.EntityLootTableGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityLootTableGenerator.class)
public interface EntityLootTableGeneratorAccessor {
    @Invoker("register")
    void registerInvoker(EntityType<?> entityType, LootTable.Builder lootTable);
}
