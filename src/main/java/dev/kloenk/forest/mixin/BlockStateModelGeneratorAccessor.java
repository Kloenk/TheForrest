package dev.kloenk.forest.mixin;

import net.minecraft.block.Block;
import net.minecraft.data.client.model.BlockStateModelGenerator;
import net.minecraft.data.client.model.TexturedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockStateModelGenerator.class)
public interface BlockStateModelGeneratorAccessor {
    @Invoker("registerSimpleState")
    void registerSimpleStateInvoker(Block block);

    @Invoker("registerLog")
    BlockStateModelGenerator.LogTexturePool registerLogInvoker(Block block);

    @Invoker("registerSingleton")
    void registerSingletonInvoker(Block block, TexturedModel.Factory modelFactory);
}
