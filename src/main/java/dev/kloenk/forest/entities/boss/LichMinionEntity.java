package dev.kloenk.forest.entities.boss;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class LichMinionEntity extends ZombieEntity {
    LichEntity master;

    public LichMinionEntity(EntityType<? extends LichMinionEntity> type, World world) {
        super(type, world);
        this.master = null;
    }

    public LichMinionEntity(World world, LichEntity master) {
        super(world);
        this.master = master;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        LivingEntity prevTarget = getTarget();

        if (super.damage(source, amount)) {
            if (source.getAttacker() instanceof LichEntity) {
                // return to previous target but speed up
                setAttacker(prevTarget);
                addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200, 4));
                addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 200, 1));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        //return TFSounds.MINION_AMBIENT;
        // TODO: sound
        return super.getAmbientSound();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // TODO: sound
        //return TFSounds.MINION_HURT;
        return super.getHurtSound(source);
    }

    @Override
    protected SoundEvent getDeathSound() {
        // TODO: sound
        //return TFSounds.MINION_DEATH;
        return super.getDeathSound();
    }

    @Override
    protected SoundEvent getStepSound() {
        // TODO: sound
        //return TFSounds.MINION_STEP;
        return super.getStepSound();
    }

    @Override
    public void tickMovement() {
        if (master == null) {
            findNewMaster();
        }
        // if we still don't have a master, or ours is dead, die.
        if (master == null || !master.isAlive()) {
            this.setHealth(0);
        }
        super.tickMovement();
    }

    private void findNewMaster() {
        List<LichEntity> nearbyLiches = world.getNonSpectatingEntities(LichEntity.class, new Box(getX(), getY(), getZ(), getX() + 1, getY() + 1, getZ() + 1).expand(32D, 16D, 32D));

        for (LichEntity nearbyLich : nearbyLiches) {
            if (!nearbyLich.isShadowClone() && nearbyLich.wantsNewMinion()) {
                this.master = nearbyLich;

                // animate our new linkage!
                master.makeBlackMagicTrail(getX(), getY() + this.getStandingEyeHeight(), getZ(), master.getX(), master.getY() + master.getStandingEyeHeight(), master.getZ());

                // become angry at our masters target
                setTarget(master.getTarget());

                // quit looking
                break;
            }
        }
    }
}
