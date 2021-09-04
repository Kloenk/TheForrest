package dev.kloenk.forest.client.model.entity;

import dev.kloenk.forest.entities.CarminiteGhastGuardEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class TFGhastModel<T extends CarminiteGhastGuardEntity> extends SinglePartEntityModel<T> {
    protected final static int tentacleCount = 9;
    private final ModelPart root, body;
    private final ModelPart[] tentacles = new ModelPart[tentacleCount];

    public TFGhastModel(ModelPart root) {
        this.root = root;
        this.body = this.root.getChild("body");

        for (int i = 0; i < this.tentacles.length; i++) {
            this.tentacles[i] = this.body.getChild("tentacle_" + i);
        }
    }

    public static TexturedModelData create() {
        ModelData mesh = new ModelData();
        ModelPartData partRoot = mesh.getRoot();

        var body = partRoot.addChild("body", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-8.0F, -8.0F, -8.0F, 16, 16, 16),
                ModelTransform.pivot(0, 8, 0));

        Random rand = new Random(1660L);

        for (int i = 0; i < TFGhastModel.tentacleCount; ++i) {
            makeTentacle(body, "tentacle_" + i, rand, i);
        }

        return TexturedModelData.of(mesh, 64, 64);
    }

    private static ModelPartData makeTentacle(ModelPartData parent, String name, Random random, int i) {
        final int length = random.nextInt(7) + 8;

        // Please ensure the model is working accurately before we port
        float xPoint = ((i % 3 - i / 3 % 2 * 0.5F + 0.25F) / 2.0F * 2.0F - 1.0F) * 5.0F;
        float zPoint = (i / 3 / 2.0F * 2.0F - 1.0F) * 5.0F;

        return parent.addChild(name, ModelPartBuilder.create()
                        .cuboid(-1.0F, 0.0F, -1.0F, 2, length, 2),
                ModelTransform.pivot(xPoint, 7, zPoint));
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        // wave tentacles
        for (int i = 0; i < this.tentacles.length; ++i) {
            this.tentacles[i].pitch = 0.2F * MathHelper.sin(animationProgress * 0.3F + i) + 0.4F;
        }

        // make body face what we're looking at
        this.body.pitch = headPitch / (180F / (float) Math.PI);
        this.body.yaw = headYaw / (180F / (float) Math.PI);
    }
}
