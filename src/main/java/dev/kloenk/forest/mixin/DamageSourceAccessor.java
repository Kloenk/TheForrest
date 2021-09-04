package dev.kloenk.forest.mixin;

import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DamageSource.class)
public interface DamageSourceAccessor {
    @Invoker("<init>")
    static DamageSource of(String name) {
        throw new AssertionError("Untransformed Accessor");
    }

    @Invoker("setBypassesArmor")
    DamageSource setBypassesArmorInvoker();
}
