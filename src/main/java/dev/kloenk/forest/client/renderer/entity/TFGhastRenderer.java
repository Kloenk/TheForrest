package dev.kloenk.forest.client.renderer.entity;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.client.model.entity.TFGhastModel;
import dev.kloenk.forest.entities.CarminiteGhastGuardEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class TFGhastRenderer<T extends CarminiteGhastGuardEntity, M extends TFGhastModel<T>> extends MobEntityRenderer<T, M> {
    public static final Identifier textureLocClosed = new Identifier("textures/entity/ghast/ghast.png");
    public static final Identifier textureLocOpen = new Identifier("textures/entity/ghast/ghast_shooting.png");
    // TODO: assets public static final Identifier textureLocClosed = ForestMod.modelTexture("towerghast.png");
    // TODO: assets public static final Identifier textureLocOpen = ForestMod.modelTexture("towerghast_openeyes.png");
    public static final Identifier textureLocAttack = ForestMod.modelTexture("towerghast_fire.png");

    public TFGhastRenderer(EntityRendererFactory.Context context, M entityModel, float f) {
        super(context, entityModel, f);
    }

    @Override
    public Identifier getTexture(T entity) {
        switch (entity.isShooting() ? 2: entity.getAttackStatus()) {
            default:
            case 0:
                return textureLocClosed;
            case 1:
                return textureLocOpen;
            case 2:
                return textureLocAttack;
        }
    }
}
