package dev.kloenk.forest.client.renderer.entity;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.client.model.entity.LichModel;
import dev.kloenk.forest.entities.boss.LichEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class LichRenderer extends MobEntityRenderer<LichEntity, LichModel> {

    private static final Identifier LICH_TEXTURE = ForestMod.modelTexture("lich.png");

    public LichRenderer(EntityRendererFactory.Context ctx, LichModel model, float shadowSize) {
        super(ctx, model, shadowSize);
        addFeature(new ShieldLayer<>(this));
    }

    @Override
    public Identifier getTexture(LichEntity entity) {
        return LICH_TEXTURE;
    }
}
