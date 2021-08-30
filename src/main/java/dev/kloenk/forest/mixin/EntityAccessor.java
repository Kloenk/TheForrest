package dev.kloenk.forest.mixin;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;


@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor
    int getNetherPortalTime();

    @Accessor
    void setNetherPortalTime(int time);
}
