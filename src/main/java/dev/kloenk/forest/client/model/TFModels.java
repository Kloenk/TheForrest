package dev.kloenk.forest.client.model;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.client.model.entity.LichModel;
import dev.kloenk.forest.client.model.entity.TFGhastModel;
import dev.kloenk.forest.client.model.entity.UrGhastModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModels;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class TFModels {
    // Lich
    public static final EntityModelLayer LICH = registerModel("lich");
    public static final EntityModelLayer LICH_MINION = registerModel("lich_minion");

    // Ghast
    public static final EntityModelLayer UR_GHAST = registerModel("ur_ghast");
    public static final EntityModelLayer CARMINITE_GHASTGUARD = registerModel("carminite_ghastguard");
    public static final EntityModelLayer CARMINITE_GHASTLING = registerModel("carminite_ghastling");

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        // TODO: lich_MINION
        // Lic
        EntityModelLayerRegistry.registerModelLayer(LICH, LichModel::create);
        EntityModelLayerRegistry.registerModelLayer(LICH_MINION, () -> TexturedModelData.of(BipedEntityModel.getModelData(Dilation.NONE, 0F), 64, 64));

        // Ghast
        EntityModelLayerRegistry.registerModelLayer(UR_GHAST, UrGhastModel::create);
        EntityModelLayerRegistry.registerModelLayer(CARMINITE_GHASTGUARD, TFGhastModel::create);
        EntityModelLayerRegistry.registerModelLayer(CARMINITE_GHASTLING, TFGhastModel::create);
    }

    @Contract(value = "_ -> new", pure = true)
    private static @NotNull EntityModelLayer registerModel(String path) {
        return registerModel(ForestMod.path(path), "main");
    }

    @Contract(value = "_, _ -> new", pure = true)
    private static @NotNull EntityModelLayer registerModel(Identifier id, String part) {
        return new EntityModelLayer(id, part);
    }
}
