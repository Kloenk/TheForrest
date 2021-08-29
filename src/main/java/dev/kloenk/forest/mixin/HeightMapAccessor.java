package dev.kloenk.forest.mixin;

import net.minecraft.world.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Heightmap.class)
public interface HeightMapAccessor {
    @Invoker("set")
    void invokeSet(int x, int z, int height);
}
