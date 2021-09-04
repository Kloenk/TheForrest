package dev.kloenk.forest.entities.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.network.Packet;
import net.minecraft.world.World;

public abstract class TFThrowableEntity extends ThrownEntity implements ITFProjectile {
    public TFThrowableEntity(EntityType<? extends TFThrowableEntity> type, World world) {
        super(type, world);
    }

    public TFThrowableEntity(EntityType<? extends TFThrowableEntity> type, World world, double x, double y, double z) {
        super(type, x, y, z, world);
    }

    public TFThrowableEntity(EntityType<? extends TFThrowableEntity> type, World world, LivingEntity owner) {
        super(type, owner, world);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    public Packet<?> createSpawnPacket() {
        // TODO: ?
        return super.createSpawnPacket();
    }
}
