package dev.kloenk.forest.mixin;

import dev.kloenk.forest.data.TFBlockLootTableGenerator;
import net.minecraft.data.server.BlockLootTableGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockLootTableGenerator.class)
public class BlockLootTableGeneratorInjector {
    @Inject(method = "accept(Ljava/util/function/BiConsumer;)V", at = @At("HEAD"))
    private void injected(CallbackInfo ci) {
        TFBlockLootTableGenerator generator = new TFBlockLootTableGenerator((BlockLootTableGenerator) (Object) this);
        generator.register();
    }
}
