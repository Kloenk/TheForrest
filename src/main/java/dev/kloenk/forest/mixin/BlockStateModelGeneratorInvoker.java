package dev.kloenk.forest.mixin;

import dev.kloenk.forest.data.TFBlockStateGenerator;
import net.minecraft.data.client.model.BlockStateModelGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockStateModelGenerator.class)
public class BlockStateModelGeneratorInvoker {
    @Inject(method = "register()V", at = @At("TAIL"))
    private void injected(CallbackInfo ci) {
        TFBlockStateGenerator generator = new TFBlockStateGenerator((BlockStateModelGenerator) (Object) this);
        generator.register();
    }
}
