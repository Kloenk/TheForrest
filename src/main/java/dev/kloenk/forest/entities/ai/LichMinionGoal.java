package dev.kloenk.forest.entities.ai;

import dev.kloenk.forest.entities.boss.LichEntity;
import dev.kloenk.forest.entities.boss.LichMinionEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.MobSpawnerEntry;
import net.minecraft.world.ServerWorldAccess;

import java.util.EnumSet;

public class LichMinionGoal extends Goal {

    private final LichEntity lich;

    public LichMinionGoal(LichEntity lich) {
        this.lich = lich;
        setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        return lich.getPhase() == 2 && !lich.isShadowClone();
    }

    @Override
    public void start() {
        lich.setStackInHand(Hand.MAIN_HAND, new ItemStack(
                // TODO
                Items.ACACIA_BOAT
        ));
    }

    @Override
    public void tick() {
        LivingEntity targetedEntity = lich.getTarget();
        if (targetedEntity == null)
            return;
        float dist = lich.distanceTo(targetedEntity);
        // spawn minions every so often
        if (lich.getAttackCooldown() % 15 == 0) {
            checkAndSpawnMinions();
        }

        if (lich.getAttackCooldown() == 0) {
            if (dist < 2.0F) {
                // melee attack
                lich.tryAttack(targetedEntity);
                lich.setAttackCooldown(20);
            } else if (dist < 20F && lich.getVisibilityCache().canSee(targetedEntity)) {
                if (lich.getNextAttackType() == 0) {
                    lich.launchBoltAt();
                } else {
                    lich.launchBombAt();
                }

                if (lich.getRandom().nextInt(2) > 0) {
                    lich.setNextAttackType(0);
                } else {
                    lich.setNextAttackType(1);
                }
                lich.setAttackCooldown(60);
            } else {
                // if not, teleport around
                lich.teleportToSightOfEntity(targetedEntity);
                lich.setAttackCooldown(20);

            }
        }
    }

    private void checkAndSpawnMinions() {
        if (!lich.world.isClient && lich.getMinionsToSummon() > 0) {
            int minions = lich.countMyMinions();

            // if not, spawn one!
            if (minions < LichEntity.MAX_ACTIVE_MINIONS) {
                spawnMinionAt();
                lich.setMinionsToSummon(lich.getMinionsToSummon() - 1);
            }
        }
        // if there's no minions left to summon, we should move into phase 3 naturally
    }

    private void spawnMinionAt() {
        // find a good spot
        LivingEntity targetedEntity = lich.getTarget();
        Vec3d minionSpot = lich.findVecInLOSOf(targetedEntity);

        if (minionSpot != null && lich.world instanceof ServerWorldAccess) {
            // put a clone there
            LichMinionEntity minion = new LichMinionEntity(lich.world, lich);
            minion.setPos(minionSpot.x, minionSpot.y, minionSpot.z);
            minion.initialize((ServerWorldAccess) lich.world, lich.world.getLocalDifficulty(new BlockPos(minionSpot)), SpawnReason.MOB_SUMMONED, null, null);
            lich.world.spawnEntity(minion);

            minion.setTarget(targetedEntity);

            minion.playSpawnEffects();
            // TODO
            // minion.playSound(TFSounds.MINION_SUMMON, 1.0F, ((lich.getRandom().nextFloat() - lich.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);

            // make sparkles leading to it
            lich.makeBlackMagicTrail(lich.getX(), lich.getY() + lich.getStandingEyeHeight(), lich.getZ(), minionSpot.x, minionSpot.y + minion.getHeight() / 2.0, minionSpot.z);
        }
    }
}
