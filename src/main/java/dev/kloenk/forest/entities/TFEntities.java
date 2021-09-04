package dev.kloenk.forest.entities;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.client.model.TFModels;
import dev.kloenk.forest.client.model.entity.LichModel;
import dev.kloenk.forest.client.renderer.entity.LichRenderer;
import dev.kloenk.forest.entities.boss.LichEntity;
import dev.kloenk.forest.entities.boss.LichMinionEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.ZombieEntityRenderer;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.registry.Registry;

// TODO: spawnrules
public class TFEntities {
    // Lich
    public static final EntityType<LichEntity> LICH = Registry.register(
            Registry.ENTITY_TYPE,
            ForestMod.path("lich"),
             FabricEntityTypeBuilder.Mob.<LichEntity>create(SpawnGroup.MONSTER, LichEntity::new)
                     .fireImmune()
                    .dimensions(EntityDimensions.fixed(1.1f, 2.1f)).build()
    );
    public static final EntityType<LichMinionEntity> LICH_MINION = Registry.register(
            Registry.ENTITY_TYPE,
            ForestMod.path("lich_minion"),
            FabricEntityTypeBuilder.Mob.<LichMinionEntity>create(SpawnGroup.MONSTER, LichMinionEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.95f)).build()
    );

    // Giant
    public static final EntityType<GiantMinerEntity> GIANT_MINER = Registry.register(
            Registry.ENTITY_TYPE,
            ForestMod.path("giant_miner"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GiantMinerEntity::new)
                    .dimensions(EntityDimensions.fixed(2.4f, 7.2f)).build()
    );


    // Lich
    public static final Item LICH_SPAWN_EGG = new SpawnEggItem(LICH, 0xaca489, 0x360472, new Item.Settings().group(ItemGroup.MISC));
    public static final Item LICH_MINION_SPAWN_EGG = new SpawnEggItem(LICH_MINION, 0xaca489, 0x360477, new Item.Settings().group(ItemGroup.MISC));

    // Giant
    public static final Item GIANT_MINER_SPAWN_EGG = new SpawnEggItem(GIANT_MINER, 0x211b52, 0x9a9a9a, new Item.Settings().group(ItemGroup.MISC));

    public static void register() {
        registerEntityAttributes();
        registerSpawnEggs();
    }

    private static void registerSpawnEggs() {
        // Lich
        Registry.register(Registry.ITEM, ForestMod.path("lich_spawn_egg"), LICH_SPAWN_EGG);
        Registry.register(Registry.ITEM, ForestMod.path("lich_minion_spawn_egg"), LICH_MINION_SPAWN_EGG);

        // Giant
        Registry.register(Registry.ITEM, ForestMod.path("giant_miner_spawn_egg"), GIANT_MINER_SPAWN_EGG);
    }

    private static void registerEntityAttributes() {
        // Lich
        FabricDefaultAttributeRegistry.register(LICH, LichEntity.registerAttributes());
        FabricDefaultAttributeRegistry.register(LICH_MINION, LichMinionEntity.createZombieAttributes());

        // Giant
        FabricDefaultAttributeRegistry.register(GIANT_MINER, GiantMinerEntity.createGiantMinerAttributes());
    }

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        TFModels.registerClient();
        registerEntityRenderers();
    }

    private static void registerEntityRenderers() {
        // Lich
        EntityRendererRegistry.register(LICH, (ctx) -> new LichRenderer(ctx, new LichModel(ctx.getPart(TFModels.LICH)), 0.6F));
        EntityRendererRegistry.register(LICH_MINION, (ctx) -> new ZombieEntityRenderer(ctx));

        // Giant
        EntityRendererRegistry.register(GIANT_MINER, (ctx) -> new GiantMinerEntity.GiantMinerEntityRenderer<GiantMinerEntity>(ctx));
    }
}
