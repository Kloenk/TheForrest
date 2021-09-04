package dev.kloenk.forest.entities.boss;

import dev.kloenk.forest.entities.projectile.TFThrowableEntity;
import dev.kloenk.forest.util.TFDamagesources;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class LichBombEntity extends TFThrowableEntity implements FlyingItemEntity {

    public LichBombEntity(EntityType<? extends LichBombEntity> type, World world) {
        super(type, world);
    }

    public LichBombEntity(EntityType<? extends LichBombEntity> type, World world, LivingEntity owner) {
        super(type, world, owner);
    }

    @Override
    public void tick() {
        super.tick();
        makeTrail();
    }

    private void makeTrail() {
        for (int i = 0; i < 1; i++) {
            double sx = 0.5 * (random.nextDouble() - random.nextDouble()) + this.getVelocity().x;
            double sy = 0.5 * (random.nextDouble() - random.nextDouble()) + this.getVelocity().y;
            double sz = 0.5 * (random.nextDouble() - random.nextDouble()) + this.getVelocity().z;

            double dx = getX() + sx;
            double dy = getY() + sy;
            double dz = getZ() + sz;

            world.addParticle(ParticleTypes.FLAME, dx, dy, dz, sx * -0.25, sy * -0.25, sz * -0.25);
        }
    }

    @Override
    public boolean isOnFire() {
        return true;
    }

    @Override
    public boolean collides() {
        return true;
    }

    @Override
    public float getTargetingMargin() {
        return 1F;
    }

    private void explode() {
        if (!this.world.isClient) {
            this.world.createExplosion(this, TFDamagesources.LICH_BOMB, null, this.getX(), this.getY(), this.getZ(), 2F, false, Explosion.DestructionType.NONE);
            this.discard();
        }
    }

    @Override
    protected float getGravity() {
        return 0.001F;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (hitResult instanceof EntityHitResult result) {
            if (result.getEntity() instanceof LichBoltEntity
                    || result.getEntity() instanceof LichBombEntity
                    || result.getEntity() instanceof LichEntity) {
                return;
            }
        }
        explode();
    }

    @Override
    public ItemStack getStack() {
        return new ItemStack(Items.MAGMA_CREAM);
    }
}
