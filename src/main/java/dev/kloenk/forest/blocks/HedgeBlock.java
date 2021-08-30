package dev.kloenk.forest.blocks;

import dev.kloenk.forest.util.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.List;
import java.util.Random;

public class HedgeBlock extends Block {
    private static final VoxelShape HEDGE_VS = VoxelShapes.cuboid(new Box(0, 0, 0, 1, 0.9375, 1));
    private static final int DAMAGE = 2;

    public HedgeBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return HEDGE_VS;
    }

    /*
    @Nullable
	@Override
	public BlockPathTypes getAiPathNodeType(BlockState state, BlockGetter world, BlockPos pos, @Nullable Mob entity) {
		return entity != null && shouldDamage(entity) ? BlockPathTypes.DAMAGE_CACTUS : null;
	}

	@Override
	@Deprecated
	public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entity) {
		if (shouldDamage(entity)) {
			entity.hurt(DamageSource.CACTUS, DAMAGE);
		}
	}
     */

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (shouldDamage(entity)) {
            entity.damage(DamageSource.CACTUS, DAMAGE);
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient) {
            world.getBlockTickScheduler().schedule(pos, this, 10);
        }
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        super.onBlockBreakStart(state, world, pos, player);
        player.damage(DamageSource.CACTUS, DAMAGE);
        world.getBlockTickScheduler().schedule(pos, this, 10);
    }

    /*
    @Override
	public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {
		super.playerDestroy(world, player, pos, state, te, stack);
		player.hurt(DamageSource.CACTUS, DAMAGE);
	}
     */

    @Override
    @Deprecated
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // find players within range
        List<PlayerEntity> nearbyPlayers = world.getEntitiesByClass(PlayerEntity.class, new Box(pos).expand(8f), (o) -> true);

        for (PlayerEntity player : nearbyPlayers) {
            // are they swinging?
            if (true) {
                BlockHitResult ray = EntityUtil.rayTrace(player);
                // are they pointing at this block?
                if (ray.getType() == HitResult.Type.BLOCK && pos.equals(ray.getBlockPos())) {
                    // prick them!  prick them hard!
                    player.damage(DamageSource.CACTUS, DAMAGE);

                    // trigger this again!
                    world.getBlockTickScheduler().schedule(pos, this, 10);
                }
            }
        }
    }

    private boolean shouldDamage(Entity entity) {
        return !(entity instanceof SpiderEntity || entity instanceof ItemEntity /*|| entity.isIgnoring */);
    }

    /*
    @Override
    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 0;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 0;
    }
     */
}
