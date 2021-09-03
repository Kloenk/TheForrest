package dev.kloenk.forest.entities.boss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;

public class NagaEntity extends HostileEntity {
    private static final int TICKS_BEFORE_HEALING = 600;
    private static final int MAX_SEGMENTS = 12;
    private static final int LEASH_X = 46;
    private static final int LEASH_Y = 7;
    private static final int LEASH_Z = 46;
    private static final double DEFAULT_SPEED = 0.3;

    private int currentSegmentCount = 0; // not including head
    private final float healthPerSegment;
    private final NagaSegmentEntity[] bodySegments = new NagaSegmentEntity[MAX_SEGMENTS];
    // FIXME
    //private AIMovementPattern movementAI;
    private int ticksSinceDamaged = 0;

    private final ServerBossBar bossInfo = new ServerBossBar(getDisplayName(), BossBar.Color.GREEN, BossBar.Style.NOTCHED_10);

    private final EntityAttributeModifier slowSpeed = new EntityAttributeModifier("Naga Slow Speed", 0.25F, EntityAttributeModifier.Operation.ADDITION);
    private final EntityAttributeModifier fastSpeed = new EntityAttributeModifier("Naga Fast Speed", 0.50F, EntityAttributeModifier.Operation.ADDITION);

    private static final TrackedData<Boolean> DATA_DAZE = DataTracker.registerData(NagaEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> DATA_CHARGE = DataTracker.registerData(NagaEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public NagaEntity(EntityType<? extends NagaEntity> type, World world) {
        super(type, world);
        this.stepHeight = 2;
        this.healthPerSegment = getMaxHealth() / 10;
        this.experiencePoints = 217;
        this.ignoreCameraFrustum = true;

        for (int i = 0; i < bodySegments.length; i++) {
            bodySegments[i] = new NagaSegmentEntity(this);
        }

        // FIXME: ?
        //this.goNormal();
    }
}
