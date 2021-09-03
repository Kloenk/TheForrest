package dev.kloenk.forest.mixin;

import dev.kloenk.forest.data.TFBlockTagsGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Main.class)
public class DataMainAccessor {
    @Inject(method = "create(Ljava/nio/file/Path;Ljava/util/Collection;ZZZZZ)Lnet/minecraft/data/DataGenerator;", at = @At("RETURN"), cancellable = true)
    private static void injected(CallbackInfoReturnable<DataGenerator> cir) {
        DataGenerator generator = cir.getReturnValue();
        //generator.install(new TFBlockTagsGenerator(generator));
        cir.setReturnValue(generator);
    }
}
