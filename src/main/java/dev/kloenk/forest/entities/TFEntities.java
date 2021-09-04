package dev.kloenk.forest.entities;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.client.model.TFModels;
import dev.kloenk.forest.client.model.entity.LichMinionModel;
import dev.kloenk.forest.client.model.entity.LichModel;
import dev.kloenk.forest.client.model.entity.TFGhastModel;
import dev.kloenk.forest.client.renderer.entity.CarminiteGhastRenderer;
import dev.kloenk.forest.client.renderer.entity.LichRenderer;
import dev.kloenk.forest.client.renderer.entity.TFBipedRenderer;
import dev.kloenk.forest.entities.boss.LichBoltEntity;
import dev.kloenk.forest.entities.boss.LichBombEntity;
import dev.kloenk.forest.entities.boss.LichEntity;
import dev.kloenk.forest.entities.boss.LichMinionEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

// TODO: spawnrules
public class TFEntities {
    // Lich
    public static final EntityType<LichEntity> LICH = registerEntityType(
            "lich",
            FabricEntityTypeBuilder.Mob.<LichEntity>create(SpawnGroup.MONSTER, LichEntity::new)
                    .fireImmune()
                    .dimensions(EntityDimensions.fixed(1.1F, 2.1F))
                    .build()
    );
    public static final EntityType<LichMinionEntity> LICH_MINION = registerEntityType(
            "lich_minion",
            FabricEntityTypeBuilder.Mob.<LichMinionEntity>create(SpawnGroup.MONSTER, LichMinionEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
                    .build()
    );
    public static final EntityType<LichBoltEntity> LICH_BOLT = registerEntityType(
            "lich_bolt",
            FabricEntityTypeBuilder.Mob.<LichBoltEntity>create(SpawnGroup.MISC, LichBoltEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .trackRangeBlocks(150)
                    .trackedUpdateRate(2)
                    .build()
    );
    public static final EntityType<LichBombEntity> LICH_BOMB = registerEntityType(
            "lich_bomb",
            FabricEntityTypeBuilder.Mob.<LichBombEntity>create(SpawnGroup.MISC, LichBombEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .trackRangeBlocks(150)
                    .build()
    );

    // Ghast
    public static final EntityType<CarminiteGhastGuardEntity> TOWER_GHAST = registerEntityType(
            "tower_ghast",
            FabricEntityTypeBuilder.Mob.<CarminiteGhastGuardEntity>create(SpawnGroup.MONSTER, CarminiteGhastGuardEntity::new)
                    .dimensions(EntityDimensions.fixed(4F, 6F))
                    .build()
    );

    // Giant
    public static final EntityType<GiantMinerEntity> GIANT_MINER = Registry.register(
            Registry.ENTITY_TYPE,
            ForestMod.path("giant_miner"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GiantMinerEntity::new)
                    .dimensions(EntityDimensions.fixed(2.4f, 7.2f)).build()
    );


    protected static final Item.Settings MISC_ITEM_GROUP = new Item.Settings().group(ItemGroup.MISC);
    // Lich
    public static final Item LICH_SPAWN_EGG = new SpawnEggItem(LICH, 0xaca489, 0x360472, MISC_ITEM_GROUP);
    public static final Item LICH_MINION_SPAWN_EGG = new SpawnEggItem(LICH_MINION, 0xaca489, 0x360477, MISC_ITEM_GROUP);

    // Ghast
    public static final Item TOWER_GHAST_SPAWN_EGG = new SpawnEggItem(TOWER_GHAST, 0xbcbcbc, 0xb77878, MISC_ITEM_GROUP);

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

        // Ghast
        Registry.register(Registry.ITEM, ForestMod.path("tower_ghast_spawn_egg"), TOWER_GHAST_SPAWN_EGG);

        // Giant
        Registry.register(Registry.ITEM, ForestMod.path("giant_miner_spawn_egg"), GIANT_MINER_SPAWN_EGG);
    }

    private static void registerEntityAttributes() {
        // Lich
        FabricDefaultAttributeRegistry.register(LICH, LichEntity.registerAttributes());
        FabricDefaultAttributeRegistry.register(LICH_MINION, LichMinionEntity.createZombieAttributes());

        // Ghast
        FabricDefaultAttributeRegistry.register(TOWER_GHAST, CarminiteGhastGuardEntity.registerAttributes());

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
        EntityRendererRegistry.register(LICH_MINION, ctx -> new TFBipedRenderer<>(
                ctx,
                new LichMinionModel(ctx.getPart(TFModels.LICH_MINION)),
                // TODO
                //new LichMinionModel(ctx.getPart(TFModels.ZOMBIE_INNER_ARMOR)),
                //new LichMinionModel(ctx.getPart(TFModels.ZOMBIE_OUTER_ARMOR)),
                0.5F,
                "textures/entity/zombie/zombie.png"
        ));
        EntityRendererRegistry.register(LICH_BOLT, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(LICH_BOMB, FlyingItemEntityRenderer::new);

        // Ghast
        EntityRendererRegistry.register(TOWER_GHAST, ctx -> new CarminiteGhastRenderer<>(ctx, new TFGhastModel<>(ctx.getPart(TFModels.CARMINITE_GHASTGUARD)), 3F));

        // Giant
        EntityRendererRegistry.register(GIANT_MINER, (ctx) -> new GiantMinerEntity.GiantMinerEntityRenderer<GiantMinerEntity>(ctx));
    }

    protected static <E extends Entity, T extends EntityType<E>> T registerEntityType(String path, T type) {
        return registerEntityType(ForestMod.path(path), type);
    }

    protected static <E extends Entity, T extends EntityType<E>> T registerEntityType(Identifier id, T type) {
        return Registry.register(
                Registry.ENTITY_TYPE,
                id,
                type
        );
    }
}
