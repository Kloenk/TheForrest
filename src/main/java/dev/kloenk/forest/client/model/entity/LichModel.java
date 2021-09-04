package dev.kloenk.forest.client.model.entity;

import com.google.common.collect.Iterables;
import dev.kloenk.forest.entities.boss.LichEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModels;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;

public class LichModel extends BipedEntityModel<LichEntity> {
    private boolean shadowClone;
    private final ModelPart collar;
    private final ModelPart cloak;

    public LichModel(ModelPart root) {
        super(root);
        this.collar = root.getChild("collar");
        this.cloak = root.getChild("cloak");
    }

    public static TexturedModelData create() {
        ModelData mesh = BipedEntityModel.getModelData(Dilation.NONE, 0f);
        ModelPartData partRoot = mesh.getRoot();

        partRoot.addChild("head", ModelPartBuilder.create()
                .uv(0, 0)
                .cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F),
            ModelTransform.pivot(0.0F, -4.0F, 0.0F)
        );

        partRoot.addChild("hat", ModelPartBuilder.create()
                .uv(32, 0)
                .cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5f)),
            ModelTransform.pivot(0F, -4F, 0F)
        );

        partRoot.addChild("collar", ModelPartBuilder.create()
                        .uv(32, 16)
                        .cuboid(-6.0F, -2.0F, -4.0F, 12.0F, 12.0F, 1.0F),
                ModelTransform.of(0.0F, -3.0F, -1.0F, 2.164208F, 0F, 0F));

        partRoot.addChild("cloak", ModelPartBuilder.create()
                        .uv(0, 44)
                        .cuboid(-6.0F, 2.0F, 0.0F, 12.0F, 19.0F, 1.0F),
                ModelTransform.pivot(0.0F, -4.0F, 2.5F));

        partRoot.addChild("body", ModelPartBuilder.create()
                        .uv(8, 16)
                        .cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 24.0F, 4.0F),
                ModelTransform.pivot(0.0F, -4.0F, 0.0F));

        partRoot.addChild("right_arm", ModelPartBuilder.create()
                        .uv(0, 16)
                        .cuboid(-2.0F, -2.0F, -2.0F, 2.0F, 12.0F, 2.0F),
                ModelTransform.pivot(-5.0F, -2.0F, 0.0F));

        partRoot.addChild("left_arm", ModelPartBuilder.create().mirrored()
                        .uv(0, 16)
                        .cuboid(-1.0F, -2.0F, -2.0F, 2.0F, 12.0F, 2.0F),
                ModelTransform.pivot(5.0F, 2.0F, 0.0F));

        partRoot.addChild("right_leg", ModelPartBuilder.create()
                        .uv(0, 16)
                        .cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F),
                ModelTransform.pivot(-2.0F, 12.0F, 0.0F));

        partRoot.addChild("left_leg", ModelPartBuilder.create().mirrored()
                        .uv(0, 16)
                        .cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F),
                ModelTransform.pivot(2.0F, 12.0F, 0.0F));

        return TexturedModelData.of(mesh, 64, 64);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        if (!shadowClone) {
            super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        } else {
            float shadow = 0.33f;
            super.render(matrices, vertices, light, overlay, red * shadow, green * shadow, blue * shadow, 0.8F);
        }
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        if (shadowClone) {
            return super.getBodyParts();
        } else {
            return Iterables.concat(Arrays.asList(cloak, collar), super.getBodyParts());
        }
    }

    @Override
    public void setAngles(LichEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.shadowClone = livingEntity.isShadowClone();
        super.setAngles(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        float ogSin = MathHelper.sin(handSwingProgress * 3.141593F);
        float otherSin = MathHelper.sin((1.0F - (1.0F - handSwingProgress) * (1.0F - handSwingProgress)) * 3.141593F);
        rightArm.roll = 0.0F;
        leftArm.roll = 0.5F;
        rightArm.yaw = -(0.1F - ogSin * 0.6F);
        leftArm.yaw = 0.1F - ogSin * 0.6F;
        rightArm.pitch = -1.570796F;
        leftArm.pitch = -3.141593F;
        rightArm.pitch -= ogSin * 1.2F - otherSin * 0.4F;
        leftArm.pitch -= ogSin * 1.2F - otherSin * 0.4F;
        rightArm.roll += MathHelper.cos(ageInTicks * 0.26F) * 0.15F + 0.05F;
        leftArm.roll -= MathHelper.cos(ageInTicks * 0.26F) * 0.15F + 0.05F;
        rightArm.pitch += MathHelper.sin(ageInTicks * 0.167F) * 0.15F;
        leftArm.pitch -= MathHelper.sin(ageInTicks * 0.167F) * 0.15F;

        head.pivotY = -4.0F;
        hat.pivotY = -4.0F;
    }
}
