package dev.kloenk.forest.entities.boss;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.blocks.TFBlocks;
import dev.kloenk.forest.entities.CarminiteGhastGuardEntity;
import dev.kloenk.forest.entities.NoClipMoveHelper;
import dev.kloenk.forest.entities.TFEntities;
import dev.kloenk.forest.loot.TFTreasure;
import dev.kloenk.forest.util.TFDamagesources;
import dev.kloenk.forest.world.TFGenerationSettings;
import dev.kloenk.forest.world.feature.TFFeature;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LightBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class UrGhastEntity extends CarminiteGhastGuardEntity {
    private static final TrackedData<Boolean> DATA_TANTRUM = DataTracker.registerData(UrGhastEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final float DEFAULT_DAMAGE_NEXT_PHASE = 10;

    private static final int HOVER_ALTITUDE = 20;

    protected List<BlockPos> trapLocations;
    protected int nextTrantumCry;

    private float damageUntilNextPhase = DEFAULT_DAMAGE_NEXT_PHASE;
    private boolean noTrapMode;
    private final ServerBossBar bossBar = new ServerBossBar(getDisplayName(), BossBar.Color.RED, BossBar.Style.PROGRESS);

    public UrGhastEntity(EntityType<? extends UrGhastEntity> type, World world) {
        super(type, world);
        this.wanderFactor = 32F;
        this.noClip = true;
        this.setInTantrum(false);
        this.experiencePoints = 317;
        this.moveControl = new NoClipMoveHelper(this);
    }

    @Override
    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);
        this.bossBar.setName(this.getDisplayName());
    }

    public static DefaultAttributeContainer.Builder registerAttributes() {
        return CarminiteGhastGuardEntity.registerAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 250)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 128D);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(DATA_TANTRUM, false);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        trapLocations = new ArrayList<BlockPos>();
        this.goalSelector.getGoals().removeIf(e -> e.getGoal() instanceof  CarminiteGhastGuardEntity.AIHomedFly);
        this.goalSelector.add(5, new AIWaypointFly(this));
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    @Override
    public void checkDespawn() {
        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            if (hasHome()) {
                world.setBlockState(getPositionTarget(), TFBlocks.BOSS_SPAWNER_UR_GHAST.getDefaultState());
            }
            discard();
        } else {
            super.checkDespawn();
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // TODO: sounds
        return super.getAmbientSound();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // TODO: sounds
        return super.getHurtSound(source);
    }

    @Override
    protected SoundEvent getDeathSound() {
        // TODO: sounds
        return super.getDeathSound();
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if (!world.isClient) {
            bossBar.setPercent(getHealth() / getMaxHealth());
        } else {
            if (this.isInTantrum()) {
                // TODO: TFParticles.BOSS_TEAR
                world.addParticle(
                        DustParticleEffect.DEFAULT,
                        this.getX() + (this.random.nextDouble() - 0.5D) * this.getWidth() * 0.75D,
                        this.getY() + this.random.nextDouble() * this.getHeight() * 0.5D,
                        this.getZ() + (this.random.nextDouble() - 0.5D)* this.getWidth() * 0.75D,
                        0, 0, 0
                );
            }

            // extra deatch explosions
            if (deathTime > 0) {
                for (int k = 0; k < 5; k++) {
                    double d = random.nextGaussian() * 0.02D;
                    double d1 = random.nextGaussian() * 0.02D;
                    double d2 = random.nextGaussian() * 0.02D;

                    world.addParticle(
                            random.nextBoolean() ? ParticleTypes.EXPLOSION_EMITTER : ParticleTypes.EXPLOSION,
                            (getX() + random.nextFloat() * getWidth() * 2F) - getWidth(),
                            getY() + random.nextFloat() * getHeight(),
                            (getZ() + random.nextFloat() * getWidth() * 2F) - getWidth(),
                            d, d1, d2
                    );
                }
            }
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return damageSource == DamageSource.IN_WALL
                || damageSource == DamageSource.IN_FIRE
                || damageSource == DamageSource.ON_FIRE
                || super.isInvulnerableTo(damageSource);
    }

    @Override
    protected void knockback(LivingEntity target) {
        // Don't take knockback
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        // in tantrum mode take only 1/10 damage
        if (this.isInTantrum()) {
            amount /= 10;
        }

        float oldHealth = getHealth();
        boolean attackSuccessful;

        if ("fireball".equals(source.getName()) && source.getAttacker() instanceof PlayerEntity) {
            // 'hide' fireball attacks so that we don't take 1000 damage.
            attackSuccessful = super.damage(DamageSource.thrownProjectile(source.getAttacker(), source.getSource()), amount);
        } else {
            attackSuccessful = super.damage(source, amount);
        }

        float lastDamage = oldHealth - getHealth();

        if (!world.isClient) {
            if (this.hurtTime == this.maxHurtTime) {
                this.damageUntilNextPhase -= lastDamage;

                ForestMod.LOGGER.debug("UrGhast Attack successful, {} damage until phase switch.", this.damageUntilNextPhase);

                if (this.damageUntilNextPhase <= 0) {
                    this.switchPhase();
                }
            } else {
                ForestMod.LOGGER.debug("UrGhast Attack failed with {} type attack for {} damage", source.getName(), amount);
            }
        }

        return attackSuccessful;
    }

    protected void switchPhase() {
        if (this.isInTantrum()) {
            this.setInTantrum(false);
        } else {
            this.startTrantum();
        }

        resetDamageUntilNextPhase();
    }

    public void resetDamageUntilNextPhase() {
        damageUntilNextPhase = DEFAULT_DAMAGE_NEXT_PHASE;
    }

    protected void startTrantum() {
        this.setInTantrum(true);

        // TODO: rain effects

        spawnGhastsAtTraps();
    }

    /**
     * Spawn ghasts at tow of the traps
     */
    protected void spawnGhastsAtTraps() {
        int numSpawns = Math.min(2, trapLocations.size());

        for (int i = 0; i < numSpawns; i++) {
            BlockPos spawnCoords = trapLocations.get(random.nextInt(trapLocations.size()));
            spawnMinionGhastsAt(spawnCoords);
        }
    }

    /**
     * Spawn up to 6 minon ghasts around the indicated area
     */
    protected void spawnMinionGhastsAt(BlockPos pos) {
        int tries = 24;
        int spawns = 0;
        int maxSpawns = 0;

        int rangeXZ = 4;
        int rangeY = 8;

        // Lightning strike
        LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
        lightning.setPos(pos.getX(), pos.getY(), pos.getZ());
        lightning.setCosmetic(true);
        world.spawnEntity(lightning);

        for (int i = 0; i < tries; i++) {
            // TODO: Ghastling
            CarminiteGhastGuardEntity minion = new CarminiteGhastGuardEntity(TFEntities.TOWER_GHAST, world);
            //CarminiteGhastlingEntity minion = new CarminiteGhastlingEntity(TFEntities.mini_ghast, level);

            double sx = pos.getX() + ((random.nextDouble() - random.nextDouble()) * rangeXZ);
            double sy = pos.getY() + (random.nextDouble() * rangeY);
            double sz = pos.getZ() + ((random.nextDouble() - random.nextDouble()) * rangeXZ);

            minion.refreshPositionAndAngles(sx, sy, sz, this.world.random.nextFloat() * 360.0F, 0.0F);
            // TODO: Ghastling
            //minion.makeBossMinion();

            if (minion.canSpawn(world, SpawnReason.MOB_SUMMONED)) {
                world.spawnEntity(minion);
                minion.playSpawnEffects();
            }

            if (++spawns >= maxSpawns) {
                break;
            }
        }
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        this.hasPositionTarget();

        // despawn mini ghasts that are in our Box
        // TODO
        /*for (CarminiteGhastlingEntity ghast : world.getEntitiesOfClass(CarminiteGhastlingEntity.class, this.getBoundingBox().inflate(1, 1, 1))) {
            ghast.spawnAnim();
            ghast.discard();
            this.heal(2);
        }*/

        // trap locations?
        if (this.trapLocations.isEmpty() && !this.noTrapMode) {
            this.scanForTrapsTwice();

            if (this.trapLocations.isEmpty()) {
                this.noTrapMode = true;
            }
        }

        if (this.isInTantrum()) {
            setTarget(null);

            // cry?
            if (--this.nextTrantumCry <= 0) {
                this.playSound(getHurtSound(null), this.getSoundVolume(), this.getSoundPitch());
                this.nextTrantumCry = 20 + random.nextInt(30);
            }

            if (this.age % 10 == 0) {
                doTantrumDamageEffects();
            }
        }
    }

    protected void doTantrumDamageEffects() {
        // harm player below
        Box below = this.getBoundingBox().offset(0, -16, 0).expand(0, 16, 0);

        for (PlayerEntity player : world.getNonSpectatingEntities(PlayerEntity.class, below)) {
            if (world.isSkyVisibleAllowingSea(player.getBlockPos())) {
                player.damage(TFDamagesources.GHAST_TEAR, 3);
            }
        }

        // also suck up mini ghasts
        // TODO: Ghastling
        /*for (CarminiteGhastlingEntity ghast : world.getNonSpectatingEntities(CarminiteGhastlingEntity.class, below)) {
            ghast.push(0, 1, 0);
        }*/
    }

    /**
     * Check if there are at least 4 ghasts near at least 2 traps.  Return false if not.
     */
    protected boolean checkGhastsAtTraps() {
        int trapsWithEnoughGhasts = 0;

        for (BlockPos trap : this.trapLocations) {
            Box box = new Box(trap, trap.add(1, 1, 1)).expand(8D, 16D, 8D);

            // TODO: ghastling
            /*List<CarminiteGhastlingEntity> nearbyGhasts = world.getNonSpectatingEntities(CarminiteGhastlingEntity.class, box);

            if (nearbyGhasts.size() >= 4) {
                trapsWithEnoughGhasts++;
            }*/
        }

        return trapsWithEnoughGhasts >= 1;
    }

    @Override
    protected void shootFireball() {
        double offsetX = this.getTarget().getX() - this.getX();
        double offsetY = this.getTarget().getBoundingBox().minY + this.getTarget().getHeight() / 2.0F - (this.getY() + this.getHeight() / 2.0F);
        double offsetZ = this.getTarget().getZ() - this.getZ();

        UrGhastFireballEntity entityFireball = new UrGhastFireballEntity(this.world, this, offsetX, offsetY, offsetZ, 1);
        double shotSpawnDistance = 8.5D;
        Vec3d lookVec = this.getRotationVec(1.0F);
        entityFireball.setPos(
                this.getX() + lookVec.x * shotSpawnDistance,
                this.getY() + this.getHeight() / 2.0F + lookVec.y * shotSpawnDistance,
                this.getZ() + lookVec.z * shotSpawnDistance
        );
        this.world.spawnEntity(entityFireball);

        for (int i = 0; i < 2; i++) {
            entityFireball = new UrGhastFireballEntity(this.world, this, offsetX + (random.nextFloat() - random.nextFloat()) * 8, offsetY, offsetZ + (random.nextFloat() - random.nextFloat()) * 8, 1);
            entityFireball.setPos(
                    this.getX() + lookVec.x * shotSpawnDistance,
                    this.getY() + this.getHeight() / 2.0F + lookVec.y * shotSpawnDistance,
                    this.getZ() + lookVec.z * shotSpawnDistance
            );
            this.world.spawnEntity(entityFireball);
        }
    }

    /**
     * Scan a few chunks around us for ghast trap blocks and if we find any, add them to our list
     */
    protected void scanForTrapsTwice() {
        int scanRangeXZ = 48;
        int scanRangeY = 32;

        scanForTraps(scanRangeXZ, scanRangeY, getBlockPos());

        if (trapLocations.size() > 0) {
            // average the location of the traps we've found, and scan again from there
            int ax = 0, ay = 0, az = 0;

            for (BlockPos trapCoords : trapLocations) {
                ax += trapCoords.getX();
                ay += trapCoords.getY();
                az += trapCoords.getZ();
            }

            ax /= trapLocations.size();
            ay /= trapLocations.size();
            az /= trapLocations.size();

            scanForTraps(scanRangeXZ, scanRangeY, new BlockPos(ax, ay, az));
        }
    }

    protected void scanForTraps(int scanRangeXZ, int scanRangeY, BlockPos pos) {
        for (int sx = -scanRangeXZ; sx <= scanRangeXZ; sx++) {
            for (int sz = -scanRangeXZ; sz <= scanRangeXZ; sz++) {
                for (int sy = -scanRangeY; sy <= scanRangeY; sy++) {
                    BlockPos trapCoords = pos.add(sx, sy, sz);
                    if (isTrapAt(trapCoords)) {
                        trapLocations.add(trapCoords);
                    }
                }
            }
        }
    }

    protected boolean isTrapAt(BlockPos pos) {
        /*BlockState inactive = TFBlocks.GHAST_TRAP.defaultBlockState().with(GhastTrapBlock.ACTIVE, false);
        BlockState active = TFBlocks.ghast_trap.get().defaultBlockState().with(GhastTrapBlock.ACTIVE, true);
        return world.isChunkLoaded(pos)
                && (world.getBlockState(pos) == inactive || world.getBlockState(pos) == active);*/
        return false;
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    public boolean isInTantrum() {
        return dataTracker.get(DATA_TANTRUM);
    }

    public void setInTantrum(boolean inTantrum) {
        dataTracker.set(DATA_TANTRUM, inTantrum);
        resetDamageUntilNextPhase();
    }

    @Override
    protected float getSoundVolume() {
        return 16F;
    }

    @Override
    public float getSoundPitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.5F;
    }

    @Override
    public void writeCustomDataToNbt(@NotNull NbtCompound nbt) {
        nbt.putBoolean("inTantrum", this.isInTantrum());
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setInTantrum(nbt.getBoolean("inTantrum"));
        if (this.hasCustomName()) {
            this.bossBar.setName(this.getDisplayName());
        }
    }

    @Override
    protected void updatePostDeath() {
        super.updatePostDeath();
        if (this.deathTime == 20 && world instanceof ServerWorldAccess serverWorldAccess) {
            // TODO: treasure chest
            //TFTreasure.
        }
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (!world.isClient) {
            // TODO: markStructureConquered
            //TFGenerationSettings.markStructureConquered(world, findChestCoords(), TFFeature.DARK_TOWER);
        }
    }

    protected BlockPos findChestCoords() {
        if (trapLocations.size() > 0) {
            int ax = 0, ay = 0, az = 0;

            for (BlockPos trapCoords : trapLocations) {
                ax += trapCoords.getX();
                ay += trapCoords.getY();
                az += trapCoords.getZ();
            }

            ax /= trapLocations.size();
            ay /= trapLocations.size();
            az /= trapLocations.size();

            return new BlockPos(ax, ay + 2, az);
        } else {
            return new BlockPos(this.getBlockPos());
        }
    }

    @Override
    protected boolean shouldAttack(LivingEntity entity) {
        return !this.isInTantrum();
    }

    @Override
    protected boolean canStartRiding(Entity entity) {
        return false;
    }

    @Override
    public boolean canUsePortals() {
        return false;
    }

    protected static class AIWaypointFly extends Goal {
        protected final UrGhastEntity entity;

        protected final List<BlockPos> pointsToVisit;
        private int currentPoint = 0;

        protected  AIWaypointFly(UrGhastEntity entity) {
            this.entity = entity;
            this.pointsToVisit = createPath();
            this.setControls(EnumSet.of(Control.MOVE));
        }


        @Override
        public boolean canStart() {
            MoveControl entityMoveHelper = this.entity.getMoveControl();

            if (!entityMoveHelper.isMoving()) {
                return true;
            } else {
                double dx = entityMoveHelper.getTargetX() - this.entity.getX();
                double dy = entityMoveHelper.getTargetY() - this.entity.getY();
                double dz = entityMoveHelper.getTargetZ() - this.entity.getZ();
                double dist = dx * dx + dy * dy + dz * dz;
                return dist < 1D || dist > 3600D;
            }
        }

        @Override
        public boolean canStop() {
            return false;
        }

        @Override
        public void start() {
            if (this.pointsToVisit.isEmpty()) {
                pointsToVisit.addAll(createPath());
            } else {
                if (this.currentPoint >= pointsToVisit.size()) {
                    this.currentPoint = 0;

                    // when we're in tantrum mode, this is a good time to check if we need to spawn more ghasts
                    if (!entity.checkGhastsAtTraps()) {
                        entity.spawnGhastsAtTraps();
                    }
                }

                // TODO reintrodue wanderFactor somehow? Would need to change move helper or add extra fields here

                double x = pointsToVisit.get(currentPoint).getX();
                double y = pointsToVisit.get(currentPoint).getY() + HOVER_ALTITUDE;
                double z = pointsToVisit.get(currentPoint).getZ();
                entity.getMoveControl().moveTo(x, y, z, 1F);
                this.currentPoint++;

                // we have reached cruising altitude, time to turn noClip off
                entity.noClip = false;
            }
        }

        protected List<BlockPos> createPath() {
            List<BlockPos> potentialPoints = new ArrayList<>();
            BlockPos pos = new BlockPos(this.entity.getBlockPos());

            if (!this.entity.noTrapMode) {
                potentialPoints.addAll(this.entity.trapLocations);
            } else {
                potentialPoints.add(pos.add(20, -HOVER_ALTITUDE, 0));
                potentialPoints.add(pos.add(0, -HOVER_ALTITUDE, -20));
                potentialPoints.add(pos.add(-20, -HOVER_ALTITUDE, 0));
                potentialPoints.add(pos.add(0, -HOVER_ALTITUDE, 20));
            }

            Collections.shuffle(potentialPoints);

            if (this.entity.noTrapMode) {
                potentialPoints.add(pos.down(HOVER_ALTITUDE));
            }

            return potentialPoints;
        }
    }
}
