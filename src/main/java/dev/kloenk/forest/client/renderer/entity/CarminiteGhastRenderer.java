package dev.kloenk.forest.client.renderer.entity;

import dev.kloenk.forest.client.model.entity.TFGhastModel;
import dev.kloenk.forest.entities.CarminiteGhastGuardEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

/**
 * This is a copy of the RenderGhast class that changes the model
 */
public class CarminiteGhastRenderer<T extends CarminiteGhastGuardEntity, M extends TFGhastModel<T>> extends TFGhastRenderer<T, M> {
    public float ghastScale = 8F;

    public CarminiteGhastRenderer(EntityRendererFactory.Context ctx, M modelTFGhast, float f) {
        super(ctx, modelTFGhast, f);
    }

    public CarminiteGhastRenderer(EntityRendererFactory.Context ctx, M modelTFGhast, float f, float ghastScale) {
        this(ctx, modelTFGhast, f);
        this.ghastScale = ghastScale;
    }

    @Override
    protected void scale(T entity, MatrixStack matrices, float amount) {
        int attackTimer = entity.getAttackTimer();
        int prevAttackTimer = entity.getPrevAttackTimer();
        float scaleVariable = (prevAttackTimer + (attackTimer - prevAttackTimer) * amount) / 20.0F;
        if (scaleVariable < 0.0F) {
            scaleVariable = 0.0F;
        }

        scaleVariable = 1.0F / (scaleVariable * scaleVariable * scaleVariable * scaleVariable * scaleVariable * 2.0F + 1.0F);
        float yScale = (ghastScale + scaleVariable) / 2.0F;
        float xzScale = (ghastScale + 1.0F / scaleVariable) / 2.0F;
        matrices.scale(xzScale, yScale, xzScale);
    }
}
