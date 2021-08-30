package dev.kloenk.forest.blocks;

import dev.kloenk.forest.blocks.blockentities.FireJetBlockEntity;
import dev.kloenk.forest.blocks.blockentities.TFBlockEntities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Random;

public class FireJetBlock extends BlockWithEntity {
    public static final EnumProperty<FireJetVariant> STATE = EnumProperty.of("state", FireJetVariant.class);

    protected FireJetBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(STATE, FireJetVariant.IDLE));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(STATE);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (state.get(STATE) != FireJetVariant.IDLE) {
            if (!entity.isFireImmune() && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entity)) {
                entity.damage(DamageSource.HOT_FLOOR, 1.0F);
            }
        }

        super.onSteppedOn(world, pos, state, entity);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!world.isClient && state.get(STATE) == FireJetVariant.IDLE) {
            BlockPos lavaPos = findLavaAround(world, pos.down());

            if (isLava(world, lavaPos)) {
                world.setBlockState(lavaPos, Blocks.AIR.getDefaultState());
                world.setBlockState(pos, state.with(STATE, FireJetVariant.POPPING));
            }
        }
    }

    private BlockPos findLavaAround(ServerWorld world, BlockPos pos) {
        if (isLava(world, pos))
            return pos;

        for (int i = 0; i < 3; i++) {
            BlockPos randPos = pos.add(world.random.nextInt(3) -1, 0, world.random.nextInt(3) -1);
            if (isLava(world, randPos))
                return randPos;
        }

        return pos;
    }

    private boolean isLava(ServerWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        // TODO: registry
         return true;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FireJetBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, TFBlockEntities.FIRE_JET, FireJetBlockEntity::tick);
    }

    public enum FireJetVariant implements StringIdentifiable {
        IDLE,
        POPPING,
        FLAME;

        @Override
        public String toString() {
            return asString();
        }

        @Override
        public String asString() {
            return name().toLowerCase(Locale.ROOT);
        }

    }
}
