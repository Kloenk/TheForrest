package dev.kloenk.forest.client.renderer.entity;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.entities.boss.LichEntity;
import dev.kloenk.forest.mixin.client.ItemRendererAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Random;

public class ShieldLayer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    public static final ModelIdentifier LOC = new ModelIdentifier(ForestMod.path("shield"), "inventory");

    private static final Direction[] DIRS = ArrayUtils.add(Direction.values(), null);
    private static final Random RAND = new Random();

    public ShieldLayer(FeatureRendererContext<T, M> renderer) {
        super(renderer);
    }

    private int getShieldCount(T entity) {
        // TODO: read from some entity attribute
        return entity instanceof LichEntity
                ? ((LichEntity) entity).getShieldStrength()
                : 0;
    }

    private void renderShields(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, float tickDelta) {
        float age = entity.age + tickDelta;
        float rotateAngleY = age / 5.0F;
        float rotateAngleX = MathHelper.sin(age / 5.0F) / 4.0F;
        float rotateAngleZ = MathHelper.cos(age / 5.0F) / 4.0F;

        int count = getShieldCount(entity);
        for (int c = 0; c < count; c++) {
            matrices.push();

            // shift to the torso
            matrices.translate(-0.5, 0.5, -0.5);

            // invert Y
            matrices.scale(1, -1, 1);

            // perform the rotations, accounting for the fact that baked models are corner-based
            matrices.translate(0.5, 0.5, 0.5);
            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(rotateAngleZ * (180F / (float) Math.PI)));
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotateAngleY * (180F / (float) Math.PI) + (c * (360F / count))));
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(rotateAngleX * (180F / (float) Math.PI)));
            matrices.translate(-0.5, -0.5, -0.5);

            // push the shields outwards from the center of rotation
            matrices.translate(0F, 0F, -0.7F);

            BakedModel model = MinecraftClient.getInstance().getBakedModelManager().getModel(LOC);
            for (Direction dir : DIRS) {
                RAND.setSeed(42L);
                ((ItemRendererAccessor)MinecraftClient.getInstance().getItemRenderer()).renderBakedItemQuadsInvoker(
                        matrices,
                        vertexConsumers.getBuffer(TexturedRenderLayers.getEntityTranslucentCull()),
                        model.getQuads(null, dir, RAND),
                        ItemStack.EMPTY,
                        0xF000F0,
                        OverlayTexture.DEFAULT_UV
                );
            }

            matrices.pop();
        }
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (getShieldCount(entity) > 0) {
            renderShields(matrices, vertexConsumers, entity, tickDelta);
        }
    }
}
