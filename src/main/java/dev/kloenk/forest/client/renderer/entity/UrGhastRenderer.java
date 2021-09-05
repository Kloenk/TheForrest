package dev.kloenk.forest.client.renderer.entity;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.client.model.entity.UrGhastModel;
import dev.kloenk.forest.entities.boss.UrGhastEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class UrGhastRenderer extends CarminiteGhastRenderer<UrGhastEntity, UrGhastModel> {
    private final Identifier textureLocClosed = ForestMod.modelTexture("towerboss.png");
    private final Identifier textureLocOpen   = ForestMod.modelTexture("towerboss_openeyes.png");
    private final Identifier textureLocAttack = ForestMod.modelTexture("towerboss_fire.png");

    public UrGhastRenderer(EntityRendererFactory.Context ctx, UrGhastModel urGhastModel, float shadowSize, float scale) {
        super(ctx, urGhastModel, shadowSize, scale);
    }

    @Override
    public Identifier getTexture(UrGhastEntity entity) {
        return super.getTexture(entity);
        // TODO: assets
        /*switch (entity.isShooting() ? 2 : entity.getAttackStatus()) {
            default:
            case 0:
                return textureLocClosed;
            case 1:
                return textureLocOpen;
            case 2:
                return textureLocAttack;
        }*/
    }
}
