package dev.kloenk.forest.entities.ai;

import dev.kloenk.forest.entities.boss.LichEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class LichShadowsGoal extends Goal {

    private final LichEntity lich;

    public LichShadowsGoal(LichEntity lich) {
        this.lich = lich;
        setControls(EnumSet.of(Control.LOOK, Control.MOVE));
    }

    @Override
    public boolean canStart() {
        return lich.getPhase() == 1;
    }

    @Override
    public void start() {
        lich.setStackInHand(Hand.MAIN_HAND, new ItemStack(
                // TODO
                //TFItems.TWILIGHT_SCEPTER
                Items.DIAMOND_AXE
        ));
    }

    @Override
    public void stop() {
        despawnClones();
    }

    @Override
    public void tick() {
        if (lich.isShadowClone())
            checkForMaster();

        LivingEntity targetEntity = lich.getTarget();
        if (targetEntity == null)
            return;
        float dist = lich.distanceTo(targetEntity);

        if (lich.getAttackCooldown() == 60) {
            lich.teleportToSightOfEntity(targetEntity);

            if (!lich.isShadowClone()) {
                checkAndSpawnClones();
            }
        }

        if (lich.getVisibilityCache().canSee(targetEntity) && lich.getAttackCooldown() == 0 && dist < 20F) {
            if (lich.getNextAttackType() == 0) {
                lich.launchBoltAt();
            } else {
                lich.launchBombAt();;
            }

            if (lich.getRandom().nextInt(3) > 0) {
                lich.setNextAttackType(0);
            } else {
                lich.setNextAttackType(1);
            }
            lich.setAttackCooldown(100);
        }
    }

    private void checkForMaster() {
        if (lich.getMasterLich() == null) {
            findNewMaster();
        }
        if (!lich.world.isClient && (lich.getMasterLich() == null || !lich.getMasterLich().isAlive())) {
            lich.discard();
        }
    }

    private void checkAndSpawnClones() {
        if (lich.countMyClones() < LichEntity.MAX_SHADOW_CLONES)
            spawnShadowClone();
    }

    private void spawnShadowClone() {
        LivingEntity targetEntity = lich.getTarget();

        Vec3d cloneSpot = lich.findVecInLOSOf(targetEntity);

        if (cloneSpot != null) {
            LichEntity newClone = new LichEntity(lich.world, lich);
            newClone.setPos(cloneSpot.x, cloneSpot.y, cloneSpot.z);
            lich.world.spawnEntity(newClone);

            newClone.setTarget(targetEntity);
            newClone.setAttackCooldown(60 + lich.getRandom().nextInt(3) - lich.getRandom().nextInt(3));

            lich.makeTeleportTrail(lich.getX(), lich.getY(), lich.getZ(), cloneSpot.x, cloneSpot.y, cloneSpot.z);
        }
    }

    private void despawnClones() {
        for (LichEntity nearbyLich : lich.getNearbyLiches()) {
            if (nearbyLich.isShadowClone())
                nearbyLich.remove(Entity.RemovalReason.DISCARDED);
        }
    }

    private void findNewMaster() {
        for (LichEntity nearbyLich : lich.getNearbyLiches()) {
            if (!nearbyLich.isShadowClone() && nearbyLich.wantsNewClone(lich)) {
                lich.setMaster(nearbyLich);

                lich.makeTeleportTrail(lich.getX(), lich.getY(), lich.getZ(), nearbyLich.getX(), nearbyLich.getY(), nearbyLich.getZ());

                lich.setTarget(nearbyLich.getTarget());
                break;
            }
        }
    }
}
