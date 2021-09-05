package dev.kloenk.forest.entities;

import dev.kloenk.forest.world.feature.TFFeature;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;

import java.util.Random;

public class CarminiteGhastGuardEntity extends GhastEntity {
    private static final TrackedData<Byte> ATTACK_STATUS = DataTracker.registerData(CarminiteGhastGuardEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Byte> ATTACK_TIMER = DataTracker.registerData(CarminiteGhastGuardEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Byte> ATTACK_PREVTIMER = DataTracker.registerData(CarminiteGhastGuardEntity.class, TrackedDataHandlerRegistry.BYTE);

    private AIAttack attackAi;
    protected float wanderFactor;
    private int inTrapCounter;

    public CarminiteGhastGuardEntity(EntityType<? extends CarminiteGhastGuardEntity> type, World world) {
        super(type, world);

        this.wanderFactor = 16F;
        this.inTrapCounter = 0;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ATTACK_STATUS, (byte) 0);
        this.dataTracker.startTracking(ATTACK_TIMER, (byte) 0);
        this.dataTracker.startTracking(ATTACK_PREVTIMER, (byte) 0);
    }

    @Override
    protected void initGoals() {
        // TODO: this.goalSelector.add(5, new AIHomedFly(this));
        // TODO: if (!(this instanceof UrGhastEntity)) this.goalSelector.add(5, new AiRandomFly(this));
        // FIXME: remove after UrGhastEntity is created
        this.goalSelector.add(5, new AIRandomFly(this));
        this.goalSelector.add(7, new GhastEntity.LookAtTargetGoal(this));
        this.goalSelector.add(7, attackAi = new AIAttack(this));
        this.goalSelector.add(1, new FollowTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder registerAttributes() {
        return GhastEntity.createGhastAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64D);
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

    protected boolean shouldAttack(LivingEntity entity) {
        return true;
    }

    @Override
    protected float getSoundVolume() {
        return 5F;
    }

    @Override
    public int getMinAmbientSoundDelay() {
        return 160;
    }

    @Override
    public int getLimitPerChunk() {
        return 8;
    }

    @Override
    public void tickMovement() {
        if (getBrightnessAtEyes() > 0.5F) {
            this.despawnCounter += 2;
        }

        if (this.random.nextBoolean()) {
            this.world.addParticle(
                    DustParticleEffect.DEFAULT,
                    this.getX() + (this.random.nextDouble() - 5D) * this.getHeight(),
                    this.getY() + this.random.nextDouble() * this.getHeight() - 0.25D,
                    this.getZ() + (this.random.nextDouble() - 0.5D) * this.getHeight(),
                    0,
                    0,
                    0
            );
        }

        super.tickMovement();
    }

    @Override
    protected void mobTick() {
        this.findHome();

        if (this.inTrapCounter > 0) {
            this.inTrapCounter--;
            this.setTarget(null);
        }

        byte status = (byte) (this.getTarget() != null && this.shouldAttack(this.getTarget()) ? 1 : 0);

        this.dataTracker.set(ATTACK_STATUS, status);
        this.dataTracker.set(ATTACK_TIMER, (byte) attackAi.attackTimer);
        this.dataTracker.set(ATTACK_PREVTIMER, (byte) attackAi.prevAttackTimer);
    }

    public int getAttackStatus() {
        return this.dataTracker.get(ATTACK_STATUS);
    }

    public int getAttackTimer() {
        return this.dataTracker.get(ATTACK_TIMER);
    }

    public int getPrevAttackTimer() {
        return this.dataTracker.get(ATTACK_PREVTIMER);
    }

    /**
     * FIXME: Something is deeply wrong with the calculations based off of this value, so let's set it high enough that it's ignored.
     */
    @Override
    public int getLookPitchSpeed() {
        return 500;
    }

    protected void shootFireball() {
        Vec3d vec3d = this.getRotationVec(1F);
        LivingEntity target = this.getTarget();
        double dx = target.getX() - (this.getX() + vec3d.x * 4.0D);
        double dy = target.getBodyY(0.5D) - (0.5D + this.getBodyY(0.5D));
        double dz = target.getZ() - (this.getZ() + vec3d.z * 4.0D);

        FireballEntity fireballEntity = new FireballEntity(world, this, dx, dy, dz, this.getFireballStrength());
        fireballEntity.setPos(
                this.getX() + vec3d.x * 4D,
                this.getBodyY(0.5D) + 0.5D,
                fireballEntity.getZ() + vec3d.z * 4.0D
        );

        this.world.spawnEntity(fireballEntity);

        if (this.random.nextInt(6) == 0) {
            this.setTarget(null);
        }
    }

    public static boolean ghastSpawnHandler(EntityType<? extends CarminiteGhastGuardEntity> entityType, WorldAccess world, SpawnReason reason, BlockPos pos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL
                && canMobSpawn(entityType, world, reason, pos, random);
    }

    @Override
    public boolean canSpawn(WorldView world) {
        return world.intersectsEntities(this)
                && !world.containsFluid(this.getBoundingBox());
    }

    private void findHome() {
        if (!this.hasHome()) {
            int chunkX = MathHelper.floor(this.getX()) >> 4;
            int chunkZ = MathHelper.floor(this.getZ()) >> 4;

            TFFeature nearFeature = TFFeature.getFeatureForRegion(chunkX, chunkZ, (StructureWorldAccess) this.world);

            if (/*nearFeature != TFFeature.DARK_TOWER*/ true) {
                this.hasPositionTarget();
                this.despawnCounter += 5;
            } else {
                BlockPos cc = TFFeature.getNearestCenterXYZ(chunkX, chunkZ);
                this.setPositionTarget(cc.up(128), 64);
            }
        }
    }

    public void setInTrap() {
       this.inTrapCounter = 10;
    }

    // [VanillaCopy] Home fields and methods from CreatureEntity, changes noted
    private BlockPos homePosition = BlockPos.ORIGIN;
    private float maximumHomeDistance = -1.0F;

    @Override
    public boolean isInWalkTargetRange() {
        return this.isInWalkTargetRange(getBlockPos());
    }

    @Override
    public boolean isInWalkTargetRange(BlockPos pos) {
        // TF - restrict valid y levels
        // Towers are so large, a simple radius doesn't really work, so we make it more of a cylinder
        return this.maximumHomeDistance == -1F || pos.getY() > 64
                && pos.getY() < 210
                && this.homePosition.getSquaredDistance(pos) < this.maximumHomeDistance * this.maximumHomeDistance;
    }

    @Override
    public void setPositionTarget(BlockPos target, int range) {
        this.homePosition = target;
        this.maximumHomeDistance = range;
    }

    @Override
    public BlockPos getPositionTarget() {
        return this.homePosition;
    }

    @Override
    public float getPositionTargetRange() {
        return this.maximumHomeDistance;
    }

    @Override
    public boolean hasPositionTarget() {
        this.maximumHomeDistance = -1F;
        return false;
    }

    @Override
    protected boolean canStartRiding(Entity entity) {
        return false;
    }

    public boolean hasHome() {
        return this.maximumHomeDistance != -1F;
    }
    // End copy

    public static class AIRandomFly extends Goal {
        private final CarminiteGhastGuardEntity ghastGuard;

        public AIRandomFly(CarminiteGhastGuardEntity ghastGuard) {
            this.ghastGuard = ghastGuard;
        }

        @Override
        public boolean canStart() {
            MoveControl entityMoveHelper = this.ghastGuard.getMoveControl();
            if (!entityMoveHelper.isMoving()) {
                return ghastGuard.getTarget() == null;
            } else {
                double dx = entityMoveHelper.getTargetX() - this.ghastGuard.getX();
                double dy = entityMoveHelper.getTargetY() - this.ghastGuard.getY();
                double dz = entityMoveHelper.getTargetZ() - this.ghastGuard.getZ();
                double d3 = dx * dx + dy * dy + dz * dz;
                return ghastGuard.getTarget() == null && (d3 < 1D || d3 > 3600D);
            }
        }

        @Override
        public boolean shouldContinue() {
            return false;
        }

        @Override
        public void start() {
            Random random = this.ghastGuard.getRandom();
            double dx = this.ghastGuard.getX() + (random.nextFloat() * 2F - 1F) * ghastGuard.wanderFactor;
            double dy = this.ghastGuard.getY() + (random.nextFloat() * 2F - 1F) * ghastGuard.wanderFactor;
            double dz = this.ghastGuard.getZ() + (random.nextFloat() * 2F - 1F) * ghastGuard.wanderFactor;
            this.ghastGuard.getMoveControl().moveTo(dx, dy, dz, 1D);
        }
    }

    public static class AIAttack extends Goal {
        private final CarminiteGhastGuardEntity ghast;
        public int attackTimer;
        public int prevAttackTimer;

        public AIAttack(CarminiteGhastGuardEntity ghast) {
            this.ghast = ghast;
        }

        @Override
        public boolean canStart() {
            return this.ghast.getTarget() != null && this.ghast.shouldAttack(this.ghast.getTarget());
        }

        @Override
        public void stop() {
            this.ghast.setShooting(false);
        }

        @Override
        public void tick() {
            LivingEntity ghastTarget = this.ghast.getTarget();

            if (ghastTarget != null && ghastTarget.squaredDistanceTo(this.ghast) < 4096D && this.ghast.getVisibilityCache().canSee(ghastTarget)) {
                this.prevAttackTimer = this.attackTimer;
                ++this.attackTimer;

                this.ghast.getLookControl().lookAt(ghastTarget, this.ghast.getLookYawSpeed(), this.ghast.getLookPitchSpeed());

                if (this.attackTimer == 10) {
                    this.ghast.playSound(SoundEvents.ENTITY_GHAST_WARN, this.ghast.getSoundVolume() * 2, this.ghast.getSoundPitch());
                }

                if (this.attackTimer == 20) {
                    if (this.ghast.shouldAttack(ghastTarget)) {
                        this.ghast.playSound(SoundEvents.ENTITY_GHAST_SHOOT, this.ghast.getSoundVolume() * 2, this.ghast.getSoundPitch());
                        this.ghast.shootFireball();
                        this.prevAttackTimer = attackTimer;
                    }
                    this.attackTimer = -40;
                }
            } else if (this.attackTimer > 0) {
                this.prevAttackTimer = attackTimer;
                --this.attackTimer;
            }

            this.ghast.setShooting(this.attackTimer > 10);
        }
    }

    public static class AIHomedFly extends Goal {
        // FIXME

        @Override
        public boolean canStart() {
            return false;
        }
    }
}
