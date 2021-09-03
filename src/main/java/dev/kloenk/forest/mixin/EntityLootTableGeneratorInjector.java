package dev.kloenk.forest.mixin;

import dev.kloenk.forest.data.TFEntityLootTableGenerator;
import net.minecraft.data.server.EntityLootTableGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLootTableGenerator.class)
public class EntityLootTableGeneratorInjector {
    @Inject(method = "accept(Ljava/util/function/BiConsumer;)V", at = @At("HEAD"))
    private void injected(CallbackInfo ci) {
        TFEntityLootTableGenerator generator = new TFEntityLootTableGenerator((EntityLootTableGenerator) (Object) this);
        generator.register();
    }
}
