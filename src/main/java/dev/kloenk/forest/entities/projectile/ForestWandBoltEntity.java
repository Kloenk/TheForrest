package dev.kloenk.forest.entities.projectile;

import dev.kloenk.forest.entities.TFEntities;
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

public class ForestWandBoltEntity extends TFThrowableEntity implements FlyingItemEntity {

    public ForestWandBoltEntity(EntityType<? extends TFThrowableEntity> type, World world) {
        super(type, world);
    }

    public ForestWandBoltEntity(EntityType<? extends TFThrowableEntity> type, World world, double x, double y, double z) {
        super(type, world, x, y, z);
    }

    public ForestWandBoltEntity(EntityType<? extends TFThrowableEntity> type, World world, LivingEntity owner) {
        super(type, world, owner);
        setProperties(owner, owner.getPitch(), owner.getYaw(), 0, 1.5F, 1F);
    }

    public ForestWandBoltEntity(World world, LivingEntity owner) {
        this(TFEntities.WAND_BOLT, world, owner);
    }

    @Override
    public void tick() {
        super.tick();
        makeTrail();
    }

    protected void makeTrail() {
        for (int i = 0; i < 5; i++) {
            double dx = getX() + 0.5 * (random.nextDouble() - random.nextDouble());
            double dy = getY() + 0.5 * (random.nextDouble() - random.nextDouble());
            double dz = getZ() + 0.5 * (random.nextDouble() - random.nextDouble());

            double s1 = ((random.nextFloat() * 0.5F) + 0.5F) * 0.17F;  // color
            double s2 = ((random.nextFloat() * 0.5F) + 0.5F) * 0.80F;  // color
            double s3 = ((random.nextFloat() * 0.5F) + 0.5F) * 0.69F;  // color

            world.addParticle(ParticleTypes.ENTITY_EFFECT, dx, dy, dz, s1, s2, s3);
        }
    }

    @Override
    protected float getGravity() {
        return 0.003F;
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 3) {
            ItemStackParticleEffect particleEffect = new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Items.ENDER_PEARL));
            for (int i = 0; i < 8; i++) {
                this.world.addParticle(particleEffect, false, this.getX(), this.getY(), this.getZ(), random.nextGaussian() * 0.05D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.05D);
            }
        } else {
            super.handleStatus(status);
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (!this.world.isClient) {
            this.world.sendEntityStatus(this, (byte) 3);
        }
        super.onCollision(hitResult);
        //this.discard();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (!this.world.isClient) {
            if (entityHitResult.getEntity() instanceof LivingEntity entity) {
                entity.damage(DamageSource.magic(this, this.getOwner()), 6);
            }
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        super.damage(source, amount);

        if (!this.world.isClient && source.getAttacker() != null) {
            Vec3d velocity = source.getAttacker().getRotationVector();

            this.setVelocity(velocity);

            if (source.getSource() instanceof LivingEntity newOwner) {
                this.setOwner(newOwner);
            }
            return true;
        }

        return false;
    }

    @Override
    public ItemStack getStack() {
        return new ItemStack(Items.ENDER_PEARL);
    }
}
