package dev.kloenk.forest.client.renderer.entity;

import dev.kloenk.forest.ForestMod;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.Identifier;

public class TFBipedRenderer<T extends HostileEntity, M extends BipedEntityModel<T>> extends BipedEntityRenderer<T, M> {
    private final Identifier textureLoc;

    public TFBipedRenderer(EntityRendererFactory.Context ctx, M model, float shadowSize, String textureName) {
        super(ctx, model, shadowSize);

        if (textureName.startsWith("textures")) {
            textureLoc = new Identifier(textureName);
        } else {
            textureLoc = ForestMod.modelTexture(textureName);
        }
    }

    public TFBipedRenderer(EntityRendererFactory.Context ctx, M model, M armorModel, M armorModel2, float shadowSize, String textureName) {
        this(ctx, model, shadowSize, textureName);
        this.addFeature(new ArmorFeatureRenderer<>(this, armorModel, armorModel2));
    }

    @Override
    public Identifier getTexture(T mobEntity) {
        return textureLoc;
    }
}
