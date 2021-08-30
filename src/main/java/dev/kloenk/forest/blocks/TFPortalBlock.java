package dev.kloenk.forest.blocks;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.TFConfig;
import dev.kloenk.forest.data.BlockTagGenerator;
import dev.kloenk.forest.mixin.EntityAccessor;
import dev.kloenk.forest.world.TFGenerationSettings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TFPortalBlock extends TransparentBlock /*implements FluidFillable*/ {
    public static final BooleanProperty DISALLOW_RETURN = BooleanProperty.of("is_one_way");

    private static final VoxelShape VOXEL_SHAPE = VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.8125f, 1f);

    private static final int MIN_PORTAL_SIZE = 4;
    private static final int MAX_PORTAL_SIZE = 64;

    protected TFPortalBlock(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(DISALLOW_RETURN, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(DISALLOW_RETURN);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VOXEL_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        // TODO: only empty in theforest dimension?
        return state.get(DISALLOW_RETURN) ? VOXEL_SHAPE : VoxelShapes.empty();
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return Fluids.WATER.getFlowing(1, false);
    }

    public boolean tryToCreatePortal(World world, BlockPos pos, ItemEntity catalyst, @Nullable PlayerEntity player) {

        BlockState state = world.getBlockState(pos);

        if (canFormPortal(state) && world.getBlockState(pos.down()).isOpaque()) {
            Map<BlockPos, Boolean> blocksChecked = new HashMap<>();
            blocksChecked.put(pos, true);

            MutableInt size = new MutableInt(0);

            if (recursivelyValidatePortal(world, pos, blocksChecked, size, state) && size.intValue() >= MIN_PORTAL_SIZE) {

                // FIXME: this shoul not be commented out
                /*if (TFConfig.COMMON_CONFIG.checkPortalDestination.get()) {
                    boolean checkProgression = TFGenerationSettings.isProgressionEnforced(catalyst.world);
                    if (!TFTeleporter.isSafeAround(world, pos, catalyst, checkProgression)) {
                        // TODO: "failure" effect - particles?
                        if (player != null) {
                            player.displayClientMessage(new TranslatableComponent(TwilightForestMod.ID + ".twilight_portal.unsafe"), true);
                        }
                        return false;
                    }
                }*/

                catalyst.getStack().decrement(1);

                // TODO: use config
                causeLightning(world, pos, false);
                //causeLightning(world, pos, TFConfig.COMMON_CONFIG.portalLightning.get());

                for (Map.Entry<BlockPos, Boolean> checkedPos : blocksChecked.entrySet()) {
                    if (checkedPos.getValue()) {
                        world.setBlockState(checkedPos.getKey(), TFBlocks.FOREST_PORTAL.getDefaultState(), 2);
                    }
                }

                return true;
            }
        }

        return false;
    }

    public boolean canFormPortal(BlockState state) {
        return state.isIn(BlockTagGenerator.PORTAL_POOL) || state.getBlock() == this && state.get(DISALLOW_RETURN);
    }

    private static void causeLightning(World world, BlockPos pos, boolean fake) {
        LightningEntity bolt = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
        bolt.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        bolt.setCosmetic(fake);
        world.spawnEntity(bolt);

        if (fake && world instanceof ServerWorld) {
            double range = 3.0D;
            List<Entity> list = world.getEntitiesByClass(Entity.class, new Box(pos).expand(range), (o) -> true);

            for (Entity victim : list) {
                // TODO
                //victim.onStruckByLightning((ServerWorld) world, bolt);
                /*if (!ForgeEventFactory.onEntityStruckByLightning(victim, bolt)) {
                    victim.thunderHit((ServerLevel) world, bolt);
                }*/
            }
        }
    }

    private static boolean recursivelyValidatePortal(World world, BlockPos pos, Map<BlockPos, Boolean> blocksChecked, MutableInt portalSize, BlockState poolBlock) {
        if (portalSize.incrementAndGet() > MAX_PORTAL_SIZE) return false;

        boolean isPoolProbablyEnclosed = true;

        for (int i = 0; i < 4 /* FIXME Screw it, 4. Thanks Mojang /* Direction.Plane.HORIZONTAL .length*/ && portalSize.intValue() <= MAX_PORTAL_SIZE; i++) {
            BlockPos positionCheck = pos.offset(Direction.fromHorizontal(i));

            if (!blocksChecked.containsKey(positionCheck)) {
                BlockState state = world.getBlockState(positionCheck);

                if (state == poolBlock && world.getBlockState(positionCheck.down()).isOpaque()) {
                    blocksChecked.put(positionCheck, true);
                    if (isPoolProbablyEnclosed) {
                        isPoolProbablyEnclosed = recursivelyValidatePortal(world, positionCheck, blocksChecked, portalSize, poolBlock);
                    }

                } else if (isGrassOrDirt(state) && isNatureBlock(world.getBlockState(positionCheck.up()))) {
                    blocksChecked.put(positionCheck, false);

                } else return false;
            }
        }

        return isPoolProbablyEnclosed;
    }

    private static boolean isNatureBlock(BlockState state) {
        return BlockTagGenerator.PORTAL_DECO.contains(state.getBlock());
    }

    private static boolean isGrassOrDirt(BlockState state) {
        return BlockTagGenerator.PORTAL_EDGE.contains(state.getBlock());
    }

    @Override
    @Deprecated
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        boolean good = world.getBlockState(pos.down()).isOpaque();

        for (Direction facing : Direction.Type.HORIZONTAL) {
            if (!good) break;

            BlockState neighboringState = world.getBlockState(pos.offset(facing));

            good = isGrassOrDirt(neighboringState) || neighboringState == state;
        }

        if (!good) {
            world.syncWorldEvent(2001, pos, Block.getRawIdFromState(state));
            world.setBlockState(pos, Blocks.WATER.getDefaultState(), 0b11);
        }
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (state == this.getDefaultState()) {
            attemptSendPlayer(entity, false);
        }
    }

    private static RegistryKey<World> getDestination(Entity entity) {
        // FIXME: this is not the best way to do this
        RegistryKey<World> theforrest = RegistryKey.of(Registry.WORLD_KEY, ForestMod.path("theforest"));

        return !entity.getEntityWorld().getDimension().toString().equals(theforrest.toString())
                ? theforrest : RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft", "overworld"));
    }

    public static void attemptSendPlayer(Entity entity, boolean forcedEntry) {
        if (!entity.isAlive() || entity.world.isClient) {
            return;
        }

        if (entity.hasVehicle() || entity.hasPassengers() || !entity.canUsePortals()) {
            return;
        }

        if (!forcedEntry && ((EntityAccessor)entity).getNetherPortalTime() > 0) {
            return;
        }

        // set a cooldown before this can run again
        ((EntityAccessor)entity).setNetherPortalTime(10);

        RegistryKey<World> destination = getDestination(entity);
        ServerWorld serverWorld = entity.getEntityWorld().getServer().getWorld(destination);

        if(serverWorld == null)
            return;

        entity.moveToWorld(serverWorld);

        // FIXME TFTeleporter?
        // entity.changeDimension(serverWorld, new TFTeleporter(forcedEntry));

        if (destination == RegistryKey.of(Registry.WORLD_KEY, ForestMod.path("theforest")) && entity instanceof ServerPlayerEntity && forcedEntry) {
            ServerPlayerEntity playerMP = (ServerPlayerEntity) entity;
            playerMP.setSpawnPoint(destination, playerMP.getBlockPos(), playerMP.bodyYaw, true, false);
        }
    }

    // Full [VanillaCopy] of BlockPortal.randomDisplayTick
    // TODO Eeeh... Let's look at changing this too alongside a new model.
    @Override
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(100) == 0) {
            world.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
        }

        for(int i = 0; i < 4; ++i) {
            double d = (double)pos.getX() + random.nextDouble();
            double e = (double)pos.getY() + random.nextDouble();
            double f = (double)pos.getZ() + random.nextDouble();
            double g = ((double)random.nextFloat() - 0.5D) * 0.5D;
            double h = ((double)random.nextFloat() - 0.5D) * 0.5D;
            double j = ((double)random.nextFloat() - 0.5D) * 0.5D;
            int k = random.nextInt(2) * 2 - 1;
            if (!world.getBlockState(pos.west()).isOf(this) && !world.getBlockState(pos.east()).isOf(this)) {
                d = (double)pos.getX() + 0.5D + 0.25D * (double)k;
                g = (double)(random.nextFloat() * 2.0F * (float)k);
            } else {
                f = (double)pos.getZ() + 0.5D + 0.25D * (double)k;
                j = (double)(random.nextFloat() * 2.0F * (float)k);
            }

            world.addParticle(ParticleTypes.PORTAL, d, e, f, g, h, j);
        }

    }


    // TODO
    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return false;
    }

    /*
    @Override
    public boolean canPlaceLiquid(BlockGetter iBlockReader, BlockPos blockPos, BlockState blockState, Fluid fluid) {
        return false;
    }

    @Override
    public boolean placeLiquid(LevelAccessor iWorld, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
        return false;
    }*/


}
