package dev.kloenk.forest.entities;

import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;

public class NoClipMoveHelper extends MoveControl {
    //private final LivingEntity entity;
    private int courseChangeCooldown;

    public NoClipMoveHelper(MobEntity entity) {
        super(entity);
        //this.entity = entity;
    }

    @Override
    public void tick() {
        if (this.state == State.MOVE_TO) {
            double dx = this.getTargetX() - this.entity.getX();
            double dy = this.getTargetY() - this.entity.getY();
            double dz = this.getTargetZ() - this.entity.getZ();
            double dist = dx * dx + dy * dy + dz * dz;

            if (this.courseChangeCooldown-- <= 0) {
                this.courseChangeCooldown += this.entity.getRandom().nextInt(5) + 2;
                dist = MathHelper.sqrt((float) dist);

                this.entity.setVelocity(this.entity.getVelocity().add(
                        (dx / dist * 0.1D) * this.speed,
                        (dy / dist * 0.1D) * this.speed,
                        (dz / dist * 0.1D) * this.speed
                ));
            }
        }
    }
}
