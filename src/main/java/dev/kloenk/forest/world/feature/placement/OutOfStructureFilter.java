package dev.kloenk.forest.world.feature.placement;

import com.mojang.serialization.Codec;
import dev.kloenk.forest.world.dimension.generator.TFChunkGeneratorBase;
import dev.kloenk.forest.world.feature.TFFeature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorContext;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;

import java.util.Random;
import java.util.stream.Stream;

public class OutOfStructureFilter extends Decorator<NopeDecoratorConfig> {
    public OutOfStructureFilter(Codec<NopeDecoratorConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public Stream<BlockPos> getPositions(DecoratorContext context, Random random, NopeDecoratorConfig config, BlockPos pos) {
        if (!(context.getWorld().toServerWorld().getChunkManager().getChunkGenerator() instanceof  TFChunkGeneratorBase tfChunkGen))
            return Stream.of(pos);

        // Feature Center
        BlockPos.Mutable featurePos = TFFeature.getNearestCenterXYZ(pos.getX() >> 4, pos.getZ() >> 4).mutableCopy();

        final TFFeature feature = tfChunkGen.getFeatureCached(new ChunkPos(featurePos), context.getWorld());

        if (feature.areChunkDecorationsEnabled)
            return Stream.of(pos);

        // Turn Feature Center into Feature Offset
        featurePos.set(Math.abs(featurePos.getX() - pos.getX()), 0, Math.abs(featurePos.getZ() - pos.getZ()));
        int size = feature.size * 16;

        return featurePos.getX() < size && featurePos.getZ() < size ? Stream.empty() : Stream.of(pos);
    }
}
