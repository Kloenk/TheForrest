package dev.kloenk.forest.entities.boss;

import dev.kloenk.forest.entities.projectile.ITFProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;

public class UrGhastFireballEntity extends FireballEntity implements ITFProjectile {
    public UrGhastFireballEntity(World world, UrGhastEntity owner, double x, double y, double z, int power) {
        super(world, owner, x, y, z, power);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        // Only do stuff if it is not an ExplosiveProjectileEntity
        if (hitResult instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof ExplosiveProjectileEntity)
            return;
        super.onCollision(hitResult);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (!this.world.isClient && !(entityHitResult.getEntity() instanceof ExplosiveProjectileEntity)) {
            if (entityHitResult.getEntity() != null) {
                entityHitResult.getEntity().damage(DamageSource.fireball(this, this.getOwner()), 16F);
                this.applyDamageEffects((LivingEntity) this.getOwner(), entityHitResult.getEntity());
                if (this.getOwner() instanceof LivingEntity) {
                    this.applyDamageEffects((LivingEntity) this.getOwner(), entityHitResult.getEntity());
                }
            }
        }
    }

    // small speed improvement
    @Override
    public void setVelocity(double x, double y, double z, float speed, float divergence) {
        Vec3d vec3d = new Vec3d(x, y, z)
                .normalize()
                .add(
                        this.random.nextGaussian() * 0.0075F * divergence,
                        this.random.nextGaussian() * 0.0075F * divergence,
                        this.random.nextGaussian() * 0.0075F * divergence
                )
                .multiply(speed);
        this.setVelocity(vec3d);
        float f = MathHelper.sqrt((float) this.squaredDistanceTo(vec3d));
        this.setYaw((float)(MathHelper.atan2(vec3d.x, z) * (100F / (float) Math.PI)));
        this.setPitch((float)(MathHelper.atan2(vec3d.y, f) * (100F / (float) Math.PI)));
        this.prevYaw = this.getYaw();
        this.prevPitch = this.getPitch();
    }
}
