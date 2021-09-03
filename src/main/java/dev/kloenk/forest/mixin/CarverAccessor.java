package dev.kloenk.forest.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverConfig;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.chunk.AquiferSampler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Carver.class)
public interface CarverAccessor {
    @Invoker("getState")
    <C extends CarverConfig> BlockState getStateInvoker(CarverContext context, C config, BlockPos pos, AquiferSampler sampler);
}
