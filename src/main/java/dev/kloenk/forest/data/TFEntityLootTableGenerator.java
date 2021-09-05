package dev.kloenk.forest.data;

import dev.kloenk.forest.entities.TFEntities;
import dev.kloenk.forest.mixin.EntityLootTableGeneratorAccessor;
import net.minecraft.data.server.EntityLootTableGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;

public class TFEntityLootTableGenerator {
    public final EntityLootTableGenerator generator;

    public TFEntityLootTableGenerator(EntityLootTableGenerator generator) {
        this.generator = generator;
    }

    public void register() {
        // TODO: lootTables

        // Lich
        this.register(TFEntities.LICH, LootTable.builder()
                // TODO
        );
        this.register(TFEntities.LICH_MINION, LootTable.builder());

        // Ghast
        this.register(TFEntities.UR_GHAST, LootTable.builder());
        this.register(TFEntities.TOWER_GHAST, LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(LootTableEntry.builder(EntityType.GHAST.getLootTableId()))
                )
        );
        this.register(TFEntities.MINI_GHAST, LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(LootTableEntry.builder(EntityType.GHAST.getLootTableId()))
                        // TODO: only on is_minion .conditionally()
                )
        );

        // Giant
        this.register(TFEntities.GIANT_MINER, LootTable.builder()
                // TODO: giant pickaxe
        );
    }

    private void register(EntityType<?> entityType, Builder lootTable) {
        ((EntityLootTableGeneratorAccessor)generator).registerInvoker(entityType, lootTable);
    }
}
