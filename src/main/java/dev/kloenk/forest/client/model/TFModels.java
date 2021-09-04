package dev.kloenk.forest.client.model;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.client.model.entity.LichModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class TFModels {
    public static final EntityModelLayer LICH = new EntityModelLayer(ForestMod.path("lich"), "main");
    public static final EntityModelLayer LICH_MINION = new EntityModelLayer(ForestMod.path("lich_minion"), "main");

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        // TODO: lich_MINION
        EntityModelLayerRegistry.registerModelLayer(LICH, LichModel::create);
    }
}
