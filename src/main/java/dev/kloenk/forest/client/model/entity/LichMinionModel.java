package dev.kloenk.forest.client.model.entity;

import dev.kloenk.forest.entities.boss.LichMinionEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffects;

public class LichMinionModel extends ZombieEntityModel<LichMinionEntity> {
    private boolean hasStrength;

    public LichMinionModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setAngles(LichMinionEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.hasStrength = entity.getStatusEffect(StatusEffects.STRENGTH) != null;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        if (hasStrength) {
            super.render(matrices, vertices, light, overlay, red * 0.25F, green, blue * 0.25F, alpha);
        } else {
            super.render(matrices, vertices, light, overlay, red * 0.25F, green * 0.25F, blue, alpha);
        }
    }
}
