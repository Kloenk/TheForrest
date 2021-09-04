package dev.kloenk.forest.client.model;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.client.model.entity.LichModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModels;

public class TFModels {
    public static final EntityModelLayer LICH = new EntityModelLayer(ForestMod.path("lich"), "main");
    public static final EntityModelLayer LICH_MINION = new EntityModelLayer(ForestMod.path("lich_minion"), "main");

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        // TODO: lich_MINION
        EntityModelLayerRegistry.registerModelLayer(LICH, LichModel::create);
        EntityModelLayerRegistry.registerModelLayer(LICH_MINION, () -> TexturedModelData.of(BipedEntityModel.getModelData(Dilation.NONE, 0F), 64, 64));
    }
}
