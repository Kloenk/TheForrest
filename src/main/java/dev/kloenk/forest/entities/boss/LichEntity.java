package dev.kloenk.forest.entities.boss;

import com.google.common.collect.ImmutableSet;
import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.blocks.TFBlocks;
import dev.kloenk.forest.entities.TFEntities;
import dev.kloenk.forest.entities.ai.LichMinionGoal;
import dev.kloenk.forest.entities.ai.LichShadowsGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;


// FIXME: check all dmaageSource (getAttacker)
// FIXME: seems to not take damage at all
public class LichEntity extends HostileEntity {

    public static final Identifier LOOT_TABLE = ForestMod.path("entities/lich");
    //TODO: Think these could be EntityType?
    private static final Set<Class<? extends Entity>> POPPABLE = ImmutableSet.of(
            SkeletonEntity.class,
            ZombieEntity.class,
            EndermanEntity.class,
            SpiderEntity.class,
            CreeperEntity.class
            // TODO: SwarmSpiderEntity
            //SwarmSpiderEntity.class
    );

    private static final TrackedData<Boolean> DATA_ISCLONE = DataTracker.registerData(LichEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Byte> DATA_SHIELDSTRENGTH = DataTracker.registerData(LichEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Byte> DATA_MINIONSLEFT = DataTracker.registerData(LichEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Byte> DATA_ATTACKTYPE = DataTracker.registerData(LichEntity.class, TrackedDataHandlerRegistry.BYTE);

    public static final int MAX_SHADOW_CLONES = 2;
    public static final int INITIAL_SHIELD_STRENGTH = 5;
    public static final int MAX_ACTIVE_MINIONS = 3;
    public static final int INITIAL_MINIONS_TO_SUMMON = 9;
    public static final int MAX_HEALTH = 100;

    private LichEntity masterLich;
    private int attackCooldown;
    private final ServerBossBar bossInfo = new ServerBossBar(getDisplayName(), BossBar.Color.YELLOW, BossBar.Style.NOTCHED_6);

    public LichEntity(EntityType<? extends HostileEntity> type, World world) {
        super(type, world);

        setShadowClone(false);
        this.masterLich = null;
        this.experiencePoints = 217;
    }

    public LichEntity(World world, LichEntity masterLich) {
        this(TFEntities.LICH, world);

        setShadowClone(true);
        this.masterLich = masterLich;
    }

    @Nullable
    public LichEntity getMasterLich() {
        return masterLich;
    }

    public int getAttackCooldown() {
        return attackCooldown;
    }

    public void setAttackCooldown(int attackCooldown) {
        this.attackCooldown = attackCooldown;
    }

    @Override
    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new LichShadowsGoal(this));
        this.goalSelector.add(2, new LichMinionGoal(this));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 0.7D, true) {
            @Override
            public boolean canStart() {
                return getPhase() == 3 && super.canStart();
            }

            @Override
            public void start() {
                super.start();
                setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.GOLDEN_SWORD));
            }
        });

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new FollowTargetGoal<>(this, PlayerEntity.class, false));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(DATA_ISCLONE, false);
        dataTracker.startTracking(DATA_SHIELDSTRENGTH, (byte) INITIAL_SHIELD_STRENGTH);
        dataTracker.startTracking(DATA_MINIONSLEFT, (byte) INITIAL_MINIONS_TO_SUMMON);
        dataTracker.startTracking(DATA_ATTACKTYPE, (byte) 0);
    }

    public static DefaultAttributeContainer.Builder registerAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, MAX_HEALTH)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.45000001788139344D);
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    @Override
    public void checkDespawn() {
        if (world.getDifficulty() == Difficulty.PEACEFUL && !isShadowClone()) {
            if (hasPositionTarget()) {
                world.setBlockState(getPositionTarget(), TFBlocks.BOSS_SPAWNER_LICH.getDefaultState());
                super.checkDespawn();
            }
        } else {
            super.checkDespawn();
        }
    }

    /**
     * What phase of the fight are we on?
     * <p>
     * 1 - reflecting bolts, shield up
     * 2 - summoning minions
     * 3 - melee
     */
    public int getPhase() {
        if (isShadowClone() || getShieldStrength() > 0) {
            return 1;
        } else if (getMinionsToSummon() > 0 || countMyMinions() > 0) {
            return 2;
        } else {
            return 3;
        }
    }

    @Override
    public void tickMovement() {
        // determine the hand position
        float angle = ((bodyYaw * 3.141593F) / 180F);

        double dx = getX() + (MathHelper.cos(angle) * 0.65);
        double dy = getY() + (getHeight() * 0.94);
        double dz = getZ() + (MathHelper.sin(angle) * 0.65);


        // add particles!

        // how many particles do we want to add?!
        int factor = (80 - attackCooldown) / 10;
        int particles = factor > 0 ? random.nextInt(factor) : 1;


        for (int j1 = 0; j1 < particles; j1++) {
            float sparkle = 1.0F - (attackCooldown + 1.0F) / 60.0F;
            sparkle *= sparkle;

            float red = 0.37F * sparkle;
            float grn = 0.99F * sparkle;
            float blu = 0.89F * sparkle;

            // change color for fireball
            if (this.getNextAttackType() != 0) {
                red = 0.99F * sparkle;
                grn = 0.47F * sparkle;
                blu = 0.00F * sparkle;
            }

            world.addParticle(ParticleTypes.ENTITY_EFFECT, dx + (random.nextGaussian() * 0.025), dy + (random.nextGaussian() * 0.025), dz + (random.nextGaussian() * 0.025), red, grn, blu);
        }

        if (this.getPhase() == 3)
            world.addParticle(ParticleTypes.ANGRY_VILLAGER,
                    this.getX() + this.random.nextFloat() * this.getHeight() * 2.0F - this.getHeight(),
                    this.getY() + 1.0D + this.random.nextFloat() * this.getHeight(),
                    this.getZ() + this.random.nextFloat() * this.getHeight() * 2.0F - this.getHeight(),
                    this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D);

        if (!world.isClient) {
            if (this.getPhase() == 1) {
                bossInfo.setPercent((float) (getShieldStrength() + 1) / (float) (INITIAL_SHIELD_STRENGTH + 1));
            } else {
                bossInfo.setStyle(BossBar.Style.PROGRESS);
                bossInfo.setPercent(getHealth() / getMaxHealth());
                if (this.getPhase() == 2)
                    bossInfo.setColor(BossBar.Color.PURPLE);
                else
                    bossInfo.setColor(BossBar.Color.RED);
            }
        }


        super.tickMovement();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        // if we're in a wall, teleport for gosh sakes
        if ("inWall".equals(source.getName()) && getTarget() != null) {
            teleportToSightOfEntity(getTarget());
        }

        if (isShadowClone() && source != DamageSource.OUT_OF_WORLD) {
            // TODO: sound
            //playSound(TFSounds.LICH_CLONE_HURT, 1.0F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            return false;
        }

        // ignore all bolts that are not reflected
        if (source.getAttacker() instanceof LichEntity) {
            return false;
        }

        // if our shield is up, ignore any damage that can be blocked.
        if (source != DamageSource.OUT_OF_WORLD && getShieldStrength() > 0) {
            if (source.isMagic() && amount > 2) {
                // reduce shield for magic damage greater than 1 heart
                if (getShieldStrength() > 0) {
                    setShieldStrength(getShieldStrength() - 1);
                    // TODO: sound
                    //playSound(TFSounds.SHIELD_BREAK, 1.0F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                }
            } else {
                // TODO: sound
                // playSound(TFSounds.SHIELD_BREAK, 1.0F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                if (source.getAttacker() instanceof LivingEntity) {
                    setAttacker((LivingEntity) source.getAttacker());
                }
            }

            return false;
        }

        if (super.damage(source, amount)) {
            // Prevent AIHurtByTarget from targeting our own companions
            if (getAttacker() instanceof LichEntity && ((LichEntity) getAttacker()).masterLich == this.masterLich) {
                setAttacker(null);
            }

            if (this.getPhase() < 3 || random.nextInt(4) == 0) {
                this.teleportToSightOfEntity(getTarget());
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void mobTick() {
        super.mobTick();

        if (getTarget() == null) {
            return;
        }

        if (attackCooldown > 0) {
            attackCooldown--;
        }

        // TODO: AI task?
        if (!isShadowClone() && attackCooldown % 15 == 0) {
            popNearbyMob();
        }

        // always watch our target
        // TODO: make into AI task
        this.getLookControl().lookAt(getTarget(), 100F, 100F);
    }

    public void launchBoltAt() {
        float bodyFacingAngle = ((bodyYaw * 3.141593F) / 180F);
        double sx = getX() + (MathHelper.cos(bodyFacingAngle) * 0.65);
        double sy = getY() + (getHeight() * 0.82);
        double sz = getZ() + (MathHelper.sin(bodyFacingAngle) * 0.65);

        double tx = getTarget().getX() - sx;
        double ty = (getTarget().getBoundingBox().minY + getTarget().getHeight() / 2.0F) - (getY() + getHeight() / 2.0F);
        double tz = getTarget().getZ() - sz;

        // TODO: sounds
        //playSound(TFSounds.LICH_SHOOT, getSoundVolume(), (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);

        LichBoltEntity projectile = new LichBoltEntity(TFEntities.LICH_BOLT, world, this);
        projectile.refreshPositionAndAngles(sx, sy, sz, getYaw(), getPitch());
        projectile.setVelocity(tx, ty, tz, 0.5F, 1F);

        world.spawnEntity(projectile);
    }

    public void launchBombAt() {
        float bodyFacingAngle = ((bodyYaw * 3.141593F) / 180F);
        double sx = getX() + (MathHelper.cos(bodyFacingAngle) * 0.65);
        double sy = getY() + (getHeight() * 0.82);
        double sz = getZ() + (MathHelper.sin(bodyFacingAngle) * 0.65);

        double tx = getTarget().getX() - sx;
        double ty = (getTarget().getBoundingBox().minY + getTarget().getHeight() / 2.0F) - (getY() + getHeight() / 2.0F);
        double tz = getTarget().getZ() - sz;

        // TODO: sounds
        //playSound(TFSounds.LICH_SHOOT, getSoundVolume(), (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);

        // TODO: lichBombEntity
        LichBombEntity projectile = new LichBombEntity(TFEntities.LICH_BOMB, world, this);
        projectile.refreshPositionAndAngles(sx, sy, sz, getYaw(), getPitch());
        projectile.setVelocity(tx, ty, tz, 0.35F, 1.0F);

        world.spawnEntity(projectile);
    }

    private void popNearbyMob() {
        List<MobEntity> nearbyMobs = world.getEntitiesByClass(MobEntity.class, new Box(getX(), getY(), getZ(), getX() + 1, getY() + 1, getZ() + 1).expand(32D, 16D, 32D), e -> POPPABLE.contains(e.getClass()));

        for (MobEntity mob : nearbyMobs) {
            if (getVisibilityCache().canSee(mob)) {
                mob.playSpawnEffects();
                mob.discard();
                // play death sound
//					world.playSoundAtEntity(mob, mob.getDeathSound(), mob.getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);

                // make trail so it's clear that we did it
                makeRedMagicTrail(mob.getX(), mob.getY() + mob.getHeight() / 2.0, mob.getZ(), this.getX(), this.getY() + this.getHeight() / 2.0, this.getZ());

                break;
            }
        }
    }

    public boolean wantsNewClone(LichEntity clone) {
        return clone.isShadowClone() && countMyClones() < LichEntity.MAX_SHADOW_CLONES;
    }

    public void setMaster(LichEntity lich) {
        masterLich = lich;
    }

    public int countMyClones() {
        // check if there are enough clones.  we check a 32x16x32 area
        int count = 0;

        for (LichEntity nearbyLich : getNearbyLiches()) {
            if (nearbyLich.isShadowClone() && nearbyLich.getMasterLich() == this) {
                count++;
            }
        }

        return count;
    }

    public List<? extends LichEntity> getNearbyLiches() {
        return world.getNonSpectatingEntities(getClass(), new Box(getX(), getY(), getZ(), getX() +1, getY()+1, getZ()+1).expand(32D, 16D, 32D));
    }

    public boolean wantsNewMinion() {
        return countMyMinions() < LichEntity.MAX_ACTIVE_MINIONS;
    }

    public int countMyMinions() {
        return (int) world.getEntitiesByClass(LichMinionEntity.class, new Box(getX(), getY(), getZ(), getX() + 1, getY() + 1, getZ() + 1).expand(32D, 16D, 32D),
                m -> m.master == this).stream().count();
    }

    public void teleportToSightOfEntity(Entity entity) {
        Vec3d dest = findVecInLOSOf(entity);
        double srcX = getX();
        double srcY = getY();
        double srcZ = getZ();

        if (dest != null) {
            teleportToNoChecks(dest.x, dest.y, dest.z);
            this.getLookControl().lookAt(entity, 100F, 100F);
            this.bodyYaw = this.prevYaw;

            if (!this.getVisibilityCache().canSee(entity)) {
                teleportToNoChecks(srcX, srcY, srcZ);
            }
        }
    }

    /**
     * Returns coords that would be good to teleport to.
     * Returns null if we can't find anything
     */
    @Nullable
    public Vec3d findVecInLOSOf(Entity targetEntity) {
        if (targetEntity == null) return null;
        double origX = getX();
        double origY = getY();
        double origZ = getZ();

        int tries = 100;
        for (int i = 0; i < tries; i++) {
            // we abuse LivingEntity.attemptTeleport, which does all the collision/ground checking for us, then teleport back to our original spot
            double tx = targetEntity.getX() + random.nextGaussian() * 16D;
            double ty = targetEntity.getY();
            double tz = targetEntity.getZ() + random.nextGaussian() * 16D;

            boolean destClear = teleport(tx, ty, tz, true);
            boolean canSeeTargetAtDest = canSee(targetEntity); // Don't use senses cache because we're in a temporary position
            teleport(origX, origY, origZ);

            if (destClear && canSeeTargetAtDest) {
                return new Vec3d(tx, ty, tz);
            }
        }

        return null;
    }

    /**
     * Does not check that the teleport destination is valid, we just go there
     */
    private void teleportToNoChecks(double destX, double destY, double destZ) {
        // save original position
        double srcX = getX();
        double srcY = getY();
        double srcZ = getZ();

        // change position
        teleport(destX, destY, destZ);

        makeTeleportTrail(srcX, srcY, srcZ, destX, destY, destZ);
        // TODO: sound
        //this.world.playSound(null, srcX, srcY, srcZ, TFSounds.LICH_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
        //this.playSound(TFSounds.LICH_TELEPORT, 1.0F, 1.0F);

        // sometimes we need to do this
        this.jumping = false;
    }

    public void makeTeleportTrail(double srcX, double srcY, double srcZ, double destX, double destY, double destZ) {
        // make particle trail
        int particles = 128;
        for (int i = 0; i < particles; i++) {
            double trailFactor = i / (particles - 1.0D);
            float f = (random.nextFloat() - 0.5F) * 0.2F;
            float f1 = (random.nextFloat() - 0.5F) * 0.2F;
            float f2 = (random.nextFloat() - 0.5F) * 0.2F;
            double tx = srcX + (destX - srcX) * trailFactor + (random.nextDouble() - 0.5D) * getHeight() * 2D;
            double ty = srcY + (destY - srcY) * trailFactor + random.nextDouble() * getHeight();
            double tz = srcZ + (destZ - srcZ) * trailFactor + (random.nextDouble() - 0.5D) * getHeight() * 2D;
            world.addParticle(ParticleTypes.EFFECT, tx, ty, tz, f, f1, f2);
        }
    }

    private void makeRedMagicTrail(double srcX, double srcY, double srcZ, double destX, double destY, double destZ) {
        int particles = 32;
        for (int i = 0; i < particles; i++) {
            double trailFactor = i / (particles - 1.0D);
            float f = 1.0F;
            float f1 = 0.5F;
            float f2 = 0.5F;
            double tx = srcX + (destX - srcX) * trailFactor + random.nextGaussian() * 0.005;
            double ty = srcY + (destY - srcY) * trailFactor + random.nextGaussian() * 0.005;
            double tz = srcZ + (destZ - srcZ) * trailFactor + random.nextGaussian() * 0.005;
            world.addParticle(ParticleTypes.ENTITY_EFFECT, tx, ty, tz, f, f1, f2);
        }
    }

    public void makeBlackMagicTrail(double srcX, double srcY, double srcZ, double destX, double destY, double destZ) {
        // make particle trail
        int particles = 32;
        for (int i = 0; i < particles; i++) {
            double trailFactor = i / (particles - 1.0D);
            float f = 0.2F;
            float f1 = 0.2F;
            float f2 = 0.2F;
            double tx = srcX + (destX - srcX) * trailFactor + random.nextGaussian() * 0.005;
            double ty = srcY + (destY - srcY) * trailFactor + random.nextGaussian() * 0.005;
            double tz = srcZ + (destZ - srcZ) * trailFactor + random.nextGaussian() * 0.005;
            world.addParticle(ParticleTypes.ENTITY_EFFECT, tx, ty, tz, f, f1, f2);
        }
    }

    public boolean isShadowClone() {
        return dataTracker.get(DATA_ISCLONE);
    }

    public void setShadowClone(boolean shadowClone) {
        bossInfo.setVisible(!shadowClone);
        dataTracker.set(DATA_ISCLONE, shadowClone);
    }

    public byte getShieldStrength() {
        return dataTracker.get(DATA_SHIELDSTRENGTH);
    }

    public void setShieldStrength(int shieldStrength) {
        dataTracker.set(DATA_SHIELDSTRENGTH, (byte) shieldStrength);
    }

    public byte getMinionsToSummon() {
        return dataTracker.get(DATA_MINIONSLEFT);
    }

    public void setMinionsToSummon(int minionsToSummon) {
        dataTracker.set(DATA_MINIONSLEFT, (byte) minionsToSummon);
    }

    public byte getNextAttackType() {
        return dataTracker.get(DATA_ATTACKTYPE);
    }

    public void setNextAttackType(int attackType) {
        dataTracker.set(DATA_ATTACKTYPE, (byte) attackType);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // TODO: sound
        //return TFSounds.LICH_AMBIENT;
        return super.getAmbientSound();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // TODO: sound
        //return TFSounds.LICH_HURT;
        return super.getHurtSound(source);
    }

    @Override
    protected SoundEvent getDeathSound() {
        // TODO: sound
        //return TFSounds.LICH_DEATH;
        return super.getDeathSound();
    }


    @Override
    protected Identifier getLootTableId() {
        return !isShadowClone() ? LOOT_TABLE : null;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putBoolean("ShadowClone", isShadowClone());
        nbt.putByte("ShieldStrength", getShieldStrength());
        nbt.putByte("MinionsToSummon", getMinionsToSummon());

        return nbt;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        setShadowClone(nbt.getBoolean("ShadowClone"));
        setShieldStrength(nbt.getByte("ShieldStrength"));
        setMinionsToSummon(nbt.getByte("MinionsToSummon"));
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);

        // mark the tower as defeated
        if (!world.isClient && !this.isShadowClone()) {
            // TODO
            //TFGenerationSettings.markStructureConquered(level, new BlockPos(this.blockPosition()), TFFeature.LICH_TOWER);
        }
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.UNDEAD;
    }

    @Override
    protected boolean canStartRiding(Entity entity) {
        return false;
    }

    @Override
    public boolean canUsePortals() {
        return false;
    }
}
