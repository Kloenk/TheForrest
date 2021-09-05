package dev.kloenk.forest.entities;

import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

import java.util.Random;

public class CarminiteGhastlingEntity extends CarminiteGhastGuardEntity {
    private boolean isMinion = false;

    public CarminiteGhastlingEntity(EntityType<? extends CarminiteGhastlingEntity> type, World world) {
        super(type, world);
        this.wanderFactor = 4F;
    }

    @Override
    public int getLimitPerChunk() {
        return 16;
    }

    public static DefaultAttributeContainer.Builder registerAttributes() {
        return CarminiteGhastGuardEntity.registerAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16D);
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.5F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // TODO: sound
        return super.getAmbientSound();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // TODO: sound
        return super.getHurtSound(source);
    }

    @Override
    protected SoundEvent getDeathSound() {
        // TODO: sound
        return super.getDeathSound();
    }

    // Loosely based on EntityEnderman.shouldAttackPlayer
    @Override
    protected boolean shouldAttack(LivingEntity entity) {
        ItemStack helmet = entity.getEquippedStack(EquipmentSlot.HEAD);
        if (!helmet.isEmpty() && helmet.getItem() == Blocks.PUMPKIN.asItem()) {
            return false;
        } else if (this.distanceTo(entity) <= 3.5F) {
            return this.canSee(entity);
        } else {
            Vec3d vec3d = entity.getRotationVec(1F).normalize();
            Vec3d vec3d1 = new Vec3d(this.getX() - entity.getX(), this.getBoundingBox().minY + this.getStandingEyeHeight() - (entity.getY() + entity.getStandingEyeHeight()), this.getZ() - entity.getZ());
            double d0 = vec3d1.length();
            vec3d1.normalize();
            double d1 = vec3d.dotProduct(vec3d1);
            return d1 > 1D - 0.025D / d0 && this.canSee(entity);
        }
    }

    public static boolean canSpawnHere(EntityType<? extends CarminiteGhastlingEntity> type, ServerWorldAccess world, SpawnReason reason, BlockPos pos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL
                && (reason == SpawnReason.MOB_SUMMONED || HostileEntity.isSpawnDark(world, pos, random))
                && canMobSpawn(type, world, reason, pos, random);
    }

    public void makeBossMinion() {
        this.wanderFactor = 0.005F;
        this.isMinion = true;

        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(6);
        this.setHealth(6);;
    }

    public boolean isMinion() {
        return isMinion;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putBoolean("isMinion", this.isMinion);
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.getBoolean("isMinion")) {
            makeBossMinion();
        }
    }
}
