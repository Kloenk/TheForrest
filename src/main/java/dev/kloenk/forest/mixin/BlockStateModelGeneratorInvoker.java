package dev.kloenk.forest.mixin;

import dev.kloenk.forest.data.BlockStateGenerator;
import net.minecraft.data.client.model.BlockStateModelGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockStateModelGenerator.class)
public class BlockStateModelGeneratorInvoker {
    @Inject(method = "register()V", at = @At("TAIL"))
    private void injected(CallbackInfo ci) {
        BlockStateGenerator generator = new BlockStateGenerator((BlockStateModelGenerator) (Object) this);
        generator.register();
    }
}
