package dev.kloenk.forest.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FoliagePlacerType.class)
public interface FoliagePlacerTypeAccessor {
    @Invoker("<init>")
    static <P extends FoliagePlacer> FoliagePlacerType<P> of(Codec<P> codec) {
        throw new AssertionError("Untransformed Accessor");
    }
}
