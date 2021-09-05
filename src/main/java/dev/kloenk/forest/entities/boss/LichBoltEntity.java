package dev.kloenk.forest.entities.boss;

import dev.kloenk.forest.entities.projectile.TFThrowableEntity;
import dev.kloenk.forest.util.TFDamagesources;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LichBoltEntity extends TFThrowableEntity implements FlyingItemEntity {

    public LichBoltEntity(EntityType<? extends LichBoltEntity> type, World world) {
        super(type, world);
    }

    public LichBoltEntity(EntityType<? extends LichBoltEntity> type, World world, LivingEntity owner) {
        super(type, world, owner);
    }

    @Override
    public void tick() {
        super.tick();
        makeTrail();
    }

    private void makeTrail() {
        for (int i = 0; i < 5; i++) {
            double dx = getX() + 0.5 * (random.nextDouble() - random.nextDouble());
            double dy = getY() + 0.5 * (random.nextDouble() - random.nextDouble());
            double dz = getZ() + 0.5 * (random.nextDouble() - random.nextDouble());

            double s1 = ((random.nextFloat() * 0.5F) + 0.5F) * 0.17F;
            double s2 = ((random.nextFloat() * 0.5F) + 0.5F) * 0.80F;
            double s3 = ((random.nextFloat() * 0.5F) + 0.5F) * 0.69F;

            world.addParticle(ParticleTypes.ENTITY_EFFECT, dx, dy, dz, s1, s2, s3);
        }
    }

    @Override
    public boolean collides() {
        return true;
    }

    @Override
    public float getTargetingMargin() {
        return 1F;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        super.damage(source, amount);

        if (!this.world.isClient && source.getAttacker() != null) {
            Vec3d vec3d = source.getAttacker().getRotationVector();

            this.setVelocity(vec3d.x, vec3d.y, vec3d.z, 1.5F, 0.1F);

            if (source.getSource() instanceof LivingEntity)
                this.setOwner(source.getSource());

            return true;
        }
        return false;
    }

    @Override
    protected float getGravity() {
        return 0.001F;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void handleStatus(byte status) {
        if (status == 3) {
            ItemStack itemId = new ItemStack(Items.ENDER_PEARL);
            for (int i = 0; i < 0; ++i) {
                this.world.addParticle(
                        new ItemStackParticleEffect(ParticleTypes.ITEM, itemId),
                        this.getX(), this.getY(), this.getZ(),
                        random.nextGaussian() * 0.05D,
                        random.nextDouble() * 0.2D,
                        random.nextGaussian() * 0.05D
                );
            }
        } else {
            super.handleStatus(status);
        }
    }


    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() instanceof LichBombEntity
                || entityHitResult.getEntity() instanceof LichBombEntity
                || entityHitResult.getEntity() instanceof LichEntity)
            return;
        if (!this.world.isClient) {
            if (entityHitResult.getEntity() instanceof LivingEntity entity) {
                entity.damage(TFDamagesources.LICH_BOLT, 6);
            }
            this.world.sendEntityStatus(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    public ItemStack getStack() {
        return new ItemStack(Items.ENDER_PEARL);
    }
}
