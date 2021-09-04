package dev.kloenk.forest.data;

import dev.kloenk.forest.entities.TFEntities;
import dev.kloenk.forest.mixin.EntityLootTableGeneratorAccessor;
import net.minecraft.data.server.EntityLootTableGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTable.Builder;

public class TFEntityLootTableGenerator {
    public final EntityLootTableGenerator generator;

    public TFEntityLootTableGenerator(EntityLootTableGenerator generator) {
        this.generator = generator;
    }

    public void register() {
        // TODO: lootTables

        // Lich
        this.register(TFEntities.LICH, LootTable.builder());
        this.register(TFEntities.LICH_MINION, LootTable.builder());

        // Ghast
        this.register(TFEntities.TOWER_GHAST, LootTable.builder());

        // Giant
        this.register(TFEntities.GIANT_MINER, LootTable.builder());
    }

    private void register(EntityType<?> entityType, Builder lootTable) {
        ((EntityLootTableGeneratorAccessor)generator).registerInvoker(entityType, lootTable);
    }
}
