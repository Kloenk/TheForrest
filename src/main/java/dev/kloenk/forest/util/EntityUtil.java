package dev.kloenk.forest.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.DoubleUnaryOperator;

public class EntityUtil {
    public static boolean canDestroyBlock(World world, BlockPos pos, Entity entity) {
        return canDestroyBlock(world, pos, world.getBlockState(pos), entity);
    }

    public static boolean canDestroyBlock(World world, BlockPos pos, BlockState state, Entity entity) {
        float hardness = state.getHardness(world, pos);
        /*return hardness >= 0f && hardness < 50f && !state.isAir()
                && state.getBlock().canEntityDestroy(state, world, pos, entity)
                && (/* rude type limit *//*!(entity instanceof LivingEntity)
                || ForgeEventFactory.onEntityDestroyBlock((LivingEntity) entity, pos, state));*/
        /*return hardness >= 0f && hardness < 50f && !state.isAir()
                && state.getBlock().bre*/
        // TODO
        return true;
    }

    /**
     * [VanillaCopy] Entity.pick
     */
    public static BlockHitResult rayTrace(Entity entity, double range) {
        Vec3d position = entity.getEyePos(); // getCameraPosVec(1f);
        Vec3d look = entity.getCameraPosVec(1f);
        Vec3d dest = position.add(look.x * range, look.y * range, look.z * range);
        return entity.world.raycast(new RaycastContext(position, dest, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity));
    }

    public static BlockHitResult rayTrace(PlayerEntity player) {
        return rayTrace(player, null);
    }

    public static BlockHitResult rayTrace(PlayerEntity player, @Nullable DoubleUnaryOperator modifier) {
        //double range = player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
        // TODO
        double range = 5;
        return rayTrace(player, modifier == null ? range : modifier.applyAsDouble(range));
    }
}
