package dev.kloenk.forest.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.texture.PlayerSkinTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.client.resource.DefaultClientResourcePack;

public class GiantMinerEntity extends HostileEntity {
    public GiantMinerEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createGiantMinerAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 80f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23d)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2d)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40d);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.initCustomGoals();
    }

    protected void initCustomGoals() {
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1d, false) {
            @Override
            protected double getSquaredMaxAttackDistance(LivingEntity entity) {
                return this.mob.getWidth() * this.mob.getHeight();
            }
        });
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0D));
        this.targetSelector.add(2, new FollowTargetGoal(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new FollowTargetGoal(this, MerchantEntity.class, false));
        this.targetSelector.add(3, new FollowTargetGoal(this, IronGolemEntity.class, true));
        this.targetSelector.add(1, (new RevengeGoal(this, new Class[0])).setGroupRevenge(new Class[]{ZombifiedPiglinEntity.class}));
    }

    @Override
    protected boolean canStartRiding(Entity entity) {
        return false;
    }

    @Override
    protected void initEquipment(LocalDifficulty difficulty) {
        // TODO: replace with GIANT_STONE_PICKAXE
        equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_PICKAXE));
    }

    @Environment(EnvType.CLIENT)
    public static class GiantMinerEntityRenderer<T extends GiantMinerEntity> extends MobEntityRenderer<T, PlayerEntityModel<T>> {
        public GiantMinerEntityRenderer(EntityRendererFactory.Context ctx) {
            super(ctx, new PlayerEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER), false), 1.8f);
        }

        @Override
        public void render(T mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
            if (this.model.riding) {
                matrixStack.translate(0, -2.5f, 0);
            }

            super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
        }

        @Override
        public Identifier getTexture(GiantMinerEntity entity) {
            MinecraftClient mc = MinecraftClient.getInstance();
            Identifier texture = mc.player.getSkinTexture();

            if (mc.getCameraEntity() instanceof AbstractClientPlayerEntity) {
                AbstractClientPlayerEntity client = (AbstractClientPlayerEntity) mc.getCameraEntity();
                client.getSkinTexture();
            }
            return texture;
        }

        @Override
        protected void scale(T entity, MatrixStack matrices, float amount) {
            float scale = 4f;

            matrices.scale(scale, scale, scale);
        }
    }
}
