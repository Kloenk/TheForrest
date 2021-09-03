package dev.kloenk.forest.blocks.blockentities.spawner;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public abstract class BossSpawnerBlockEntity<T extends MobEntity> extends BlockEntity {
    protected static final int SHORT_RANGE = 9, LONG_RANGE = 50;

    protected final EntityType<T> entityType;
    protected boolean spanwedBoss = false;

    protected BossSpawnerBlockEntity(BlockEntityType<?> type, EntityType<T> entityType, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.entityType = entityType;
    }

    public boolean isPlayerInRange() {
        return world.isPlayerInRange(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, getRange());
    }

    public static void tick(World world, BlockPos pos, BlockState state, BossSpawnerBlockEntity<?> blockEntity) {
        if (blockEntity.spanwedBoss || !blockEntity.isPlayerInRange())
            return;

        if (world.isClient) {
            double rx = pos.getX() + world.random.nextFloat();
            double ry = pos.getY() + world.random.nextFloat();
            double rz = pos.getZ() + world.random.nextFloat();

            world.addParticle(ParticleTypes.SMOKE, rx, ry, rz, 0D, 0D, 0D);
            world.addParticle(ParticleTypes.FLAME, rx, ry, rz, 0D, 0D, 0D);
        } else {
            if (world.getDifficulty() != Difficulty.PEACEFUL) {
                if (blockEntity.spawnBoss((ServerWorldAccess) world)) {
                    world.removeBlock(pos, false);
                    blockEntity.spanwedBoss = true;
                }
            }
        }
    }

    protected boolean spawnBoss(ServerWorldAccess worldAccess) {
        T myCreature = createCreature();

        myCreature.refreshPositionAndAngles(pos, worldAccess.getRandom().nextFloat() * 360F, 0F);
        myCreature.initialize(worldAccess, worldAccess.getLocalDifficulty(pos), SpawnReason.SPAWNER, null, null);

        initializeCreature(myCreature);

        return worldAccess.spawnEntity(myCreature);
    }

    protected void initializeCreature(T myCreature) {
        myCreature.setPositionTarget(pos, 46);
    }

    protected int getRange() {
        return SHORT_RANGE;
    }

    protected T createCreature() {
        return entityType.create(world);
    }
}
