package dev.kloenk.forest.mixin;

import net.minecraft.util.math.noise.NoiseSampler;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NoiseChunkGenerator.class)
public interface NoiseChunkGeneratorAccessor {
    @Accessor
    NoiseSampler getSurfaceDepthNoise();
}
