package dev.kloenk.forest.init;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.blocks.TFBlocks;
import dev.kloenk.forest.entities.GiantMinerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TFContent {
    public static final ItemGroup ITEM_GROUP;

    public static final EntityType<GiantMinerEntity> GIANT_MINER = Registry.register(Registry.ENTITY_TYPE, new Identifier(ForestMod.ID, "giant_miner"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GiantMinerEntity::new).dimensions(EntityDimensions.fixed(2.4f, 7.2f)).build());
    public static final Item GIANT_MINER_SPAWN_EGG = new SpawnEggItem(GIANT_MINER, 12895428, 11382189, new Item.Settings().group(ItemGroup.MISC));

    /*public static final EntityType<GiantMinerEntity> GIANT_MINER = FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GiantMinerEntity::new).dimensions(EntityDimensions.fixed(2.4f, 7.2f)).build();
    public static final Item GIANT_MINER_SPAWN_EGG = new SpawnEggItem(GIANT_MINER, 12895428, 11382189, new Item.Settings().group(ItemGroup.MISC));*/

    public static void register() {
        TFBlocks.register();

        // Giant Miner
        //Registry.register(Registry.ENTITY_TYPE, ForestMod.path("giant_miner"), GIANT_MINER);
        Registry.register(Registry.ITEM, ForestMod.path("giant_miner_spawn_egg"), GIANT_MINER_SPAWN_EGG);
    }

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        TFBlocks.registerClient();

        EntityRendererRegistry.register(GIANT_MINER, (ctx) -> new GiantMinerEntity.GiantMinerEntityRenderer<GiantMinerEntity>(ctx));
    }

    static {
        ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(ForestMod.ID, "general"), () -> new ItemStack(Items.DIAMOND));
    }
}
