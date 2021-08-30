package dev.kloenk.forest.blocks.blockentities;

import dev.kloenk.forest.blocks.FireJetBlock;
import dev.kloenk.forest.blocks.TFBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class FireJetBlockEntity extends BlockEntity {
    private int counter = 0;

    public FireJetBlockEntity(BlockPos pos, BlockState state) {
        super(TFBlockEntities.FIRE_JET, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, FireJetBlockEntity be) {
        if (state.getBlock() == TFBlocks.FIRE_JET || state.getBlock() == TFBlocks.FIRE_JET_ENCASED) {
            switch (state.get(FireJetBlock.STATE)) {
                case POPPING: tickPopping(world, pos, state, be); break;
                case FLAME: tickFlame(world, pos, state, be); break;
            }
        }
    }

    public static void tickPopping(World world, BlockPos pos, BlockState state, FireJetBlockEntity be) {
        if (++be.counter >= 80) {
            be.counter = 0;
            // turn to flame
            if (!world.isClient) {
                if (state.getBlock() == TFBlocks.FIRE_JET || state.getBlock() == TFBlocks.FIRE_JET_ENCASED) {
                    world.setBlockState(pos, state.with(FireJetBlock.STATE, FireJetBlock.FireJetVariant.FLAME));
                } else {
                    world.removeBlock(pos, false);
                }
            }
        } else {
            if (be.counter % 20 == 0) {
                for (int i = 0; i < 8; i++)
                {
                    world.addParticle(ParticleTypes.LAVA, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
                }
                // TODO: sounds
                //world.playSound(null, pos, TFSounds.JET_POP, SoundSource.BLOCKS, 0.2F + level.random.nextFloat() * 0.2F, 0.9F + level.random.nextFloat() * 0.15F);
            }
        }
    }

    public static void tickFlame(World world, BlockPos pos, BlockState state, FireJetBlockEntity be) {
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();

        if (++be.counter > 60) {
            be.counter = 0;
            // idle again
            if (!world.isClient) {
                if (state.getBlock() == TFBlocks.FIRE_JET || state.getBlock() == TFBlocks.FIRE_JET_ENCASED) {
                    world.setBlockState(pos, state.with(FireJetBlock.STATE, FireJetBlock.FireJetVariant.IDLE));
                } else {
                    world.removeBlock(pos, false);
                }
            }
        }

        if (world.isClient) {
            if (be.counter % 2 == 0) {
                world.addParticle(ParticleTypes.LARGE_SMOKE, x + 0.5, y + 1.0, z + 0.5, 0.0D, 0.0D, 0.0D);
                // TODO
                /*world.addParticle(TFParticleType.LARGE_FLAME.get(), x + 0.5, y + 1.0, z + 0.5, 0.0D, 0.5D, 0.0D);
                world.addParticle(TFParticleType.LARGE_FLAME.get(), x - 0.5, y + 1.0, z + 0.5, 0.05D, 0.5D, 0.0D);
                world.addParticle(TFParticleType.LARGE_FLAME.get(), x + 0.5, y + 1.0, z - 0.5, 0.0D, 0.5D, 0.05D);
                world.addParticle(TFParticleType.LARGE_FLAME.get(), x + 1.5, y + 1.0, z + 0.5, -0.05D, 0.5D, 0.0D);
                world.addParticle(TFParticleType.LARGE_FLAME.get(), x + 0.5, y + 1.0, z + 1.5, 0.0D, 0.5D, -0.05D);*/
            }

            // sounds
            if (be.counter % 4 == 0) {
                // TODO
                // world.playLocalSound(x + 0.5, y + 0.5, z + 0.5, TFSounds.JET_ACTIVE, SoundSource.BLOCKS, 1.0F + level.random.nextFloat(), level.random.nextFloat() * 0.7F + 0.3F, false);

            } else if (be.counter == 1) {
                // TODO
                // world.playLocalSound(x + 0.5, y + 0.5, z + 0.5, TFSounds.JET_START, SoundSource.BLOCKS, 1.0F + level.random.nextFloat(), level.random.nextFloat() * 0.7F + 0.3F, false);
            }
        }

        // actual fire effects
        if (!world.isClient) {
            if (be.counter % 5 == 0) {
                // find entities in the area of effect
                List<Entity> entitiesInRange = world.getEntitiesByClass(Entity.class, new Box(pos.add(-2, 0, -2), pos.add(2, 4, 2)), (o) -> true);
                for (Entity entity : entitiesInRange) {
                    if (!entity.isFireImmune()) {
                        // TODO
                        // entity.damage(TFDamageSources.FIRE_JET, 2);
                        entity.setOnFireFor(15);
                    }
                }
            }
        }
    }
}
