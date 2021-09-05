package dev.kloenk.forest.client.model.entity;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.entities.boss.UrGhastEntity;
import net.minecraft.client.model.*;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class UrGhastModel extends TFGhastModel<UrGhastEntity> {
    private final ModelPart[][] tentacles = new ModelPart[tentacleCount][3];

    public UrGhastModel(ModelPart root) {
        super(root);

        ModelPart body = root.getChild("body");

        for (int i = 0; i< this.tentacles.length; i++) {
            this.tentacles[i][0] = body.getChild("tentacle_" + i + "");
            this.tentacles[i][1] = this.tentacles[i][0].getChild("tentacle_" + i + "_extension");
            this.tentacles[i][2] = this.tentacles[i][1].getChild("tentacle_" + i + "_tip");
        }
    }

    public static TexturedModelData create() {
        ModelData mesh = new ModelData();
        ModelPartData partRoot = mesh.getRoot();

        var body = partRoot.addChild("body", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-8.0F, -8.0F, -8.0F, 16, 16, 16),
                ModelTransform.pivot(0, 8, 0));

        for (int i = 0; i < TFGhastModel.tentacleCount; ++i) {
            makeTentacle(body, "tentacle_" + i, i);
        }

        return TexturedModelData.of(mesh, 64, 64);
    }

    private static void makeTentacle(ModelPartData parent, String name, int iteration) {
        ModelPartData tentacleBase = parent.addChild(
                name,
                ModelPartBuilder.create()
                    .cuboid(-1.5F, 0.0F, -1.5F, 3.333F, 5.333F, 3.333F),
                switch (iteration) {
                    case 0 -> ModelTransform.pivot(4.5F, 7, 4.5F);
                    case 1 -> ModelTransform.pivot(-4.5F, 7, 4.5F);
                    case 2 -> ModelTransform.pivot(0F, 7, 0F);
                    case 3 -> ModelTransform.pivot(5.5F, 7, -4.5F);
                    case 4 -> ModelTransform.pivot(-5.5F, 7, -4.5F);
                    case 5 -> ModelTransform.of(-7.5F, 3.5F, -1F, 0F, 0F, MathHelper.PI / 4.0F);
                    case 6 -> ModelTransform.of(-7.5F, -1.5F, 3.5F, 0F, 0F, MathHelper.PI / 3.0F);
                    case 7 -> ModelTransform.of(7.5F, 3.5F, -1F, 0F, 0F, -MathHelper.PI / 4.0F);
                    case 8 -> ModelTransform.of(7.5F, -1.5F, 3.5F, 0F, 0F, -MathHelper.PI / 3.0F);
                    default ->  {
                        ForestMod.LOGGER.warn("Out of bounds with Ur-Ghast limb creation: Iteration " + iteration);
                        yield ModelTransform.NONE;
                    }
                }
        );

        ModelPartData tentacleExtension = tentacleBase.addChild(
                name + "_extension",
                ModelPartBuilder.create()
                        .cuboid(-1.5F, -1.35F, -1.5F, 3.333F, 6.66F, 3.333F),
                ModelTransform.pivot(0, 6.66F, 0)
        );

        tentacleExtension.addChild(
                name + "_tip",
                ModelPartBuilder.create()
                        .cuboid(-1.5F, 1.3F, -1.5F, 3.333F, 4, 3.333F),
                ModelTransform.pivot(0, 4, 0)
        );
    }

    @Override
    public void setAngles(UrGhastEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

        // wave tentacles
        for (int i = 0; i < this.tentacles.length; ++i) {
            float wiggle = Math.min(limbDistance, 0.6F);

            float time = (animationProgress + (i * 9)) / 2.0F;

            this.tentacles[i][1].pitch = (MathHelper.cos(time * 0.6662F) - MathHelper.PI / 3.0F) * wiggle;
            this.tentacles[i][2].pitch = MathHelper.cos(time * 0.7774F) * 1.2F * wiggle;

            this.tentacles[i][1].pitch = 0.1F + MathHelper.cos(time * 0.3335F) * 0.15F;
            this.tentacles[i][2].pitch = 0.1F + MathHelper.cos(time * 0.4445F) * 0.20F;

            float yTwist = 0.4F;

            this.tentacles[i][0].yaw = yTwist * MathHelper.sin(time * 0.3F);
        }
    }
}
