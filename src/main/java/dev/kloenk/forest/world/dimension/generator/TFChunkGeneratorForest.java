package dev.kloenk.forest.world.dimension.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.kloenk.forest.mixin.NoiseChunkGeneratorAccessor;
import dev.kloenk.forest.util.IntPair;
import dev.kloenk.forest.world.feature.TFFeature;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.noise.NoiseSampler;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

import java.util.function.Supplier;

public class TFChunkGeneratorForest extends TFChunkGeneratorBase {
    public static final Codec<TFChunkGeneratorForest> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeSource),
            Codec.LONG.fieldOf("seed").stable().forGetter((obj) -> obj.seed),
            ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings").forGetter(TFChunkGeneratorForest::getDimensionSettings)
    ).apply(instance, instance.stable(TFChunkGeneratorForest::new)));

    public TFChunkGeneratorForest(BiomeSource source, long seed, Supplier<ChunkGeneratorSettings> settings) {
        super(source, seed, settings);
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return new TFChunkGeneratorForest(this.biomeSource.withSeed(seed), 1, this.dimensionSettings);
    }

    private Supplier<ChunkGeneratorSettings> getDimensionSettings() {
        return this.dimensionSettings;
    }

    @Override
    public void buildSurface(ChunkRegion region, Chunk chunk) {
        this.deformTerrainForFeature(region, chunk);

        super.buildSurface(region, chunk);

        this.addDarkForestCanopy(region, chunk);
    }

    /**
     * Adds dark forest canopy.  This version uses the "unzoomed" array of biomes used in land generation to determine how many of the nearby blocks are dark forest
     */
    // Currently this is too sophisicated to be made into a SurfaceBuilder, it looks like
    private void addDarkForestCanopy(ChunkRegion region, Chunk chunk) {
        BlockPos blockpos = region.getCenterPos().getStartPos();
        int[] thicks = new int[5 * 5];
        boolean biomeFound = false;

        for (int dZ = 0; dZ < 5; dZ++) {
            for (int dX = 0; dX < 5; dX++) {
                for (int bx = -1; bx <= 1; bx++) {
                    for (int bz = -1; bz <= 1; bz++) {
                        BlockPos p = blockpos.add((dX + bx) << 2, 0, (dZ + bz) << 2);
                        Biome biome = biomeSource.getBiomeForNoiseGen(p.getX() >> 2, 0, p.getZ() >> 2);
                        // TODO: add dark forest center
                        if (BiomeKeys.DARK_FOREST.toString().equals(biome.toString()) /*|| BiomeKeys.DARK_FOREST.toString().equals(biome.toString())*/) {
                            thicks[dX + dZ * 5]++;
                            biomeFound = true;
                        }
                    }
                }
            }
        }

        if (!biomeFound) return;

        IntPair nearCenter = new IntPair();
        TFFeature nearFeature = TFFeature.getNearestFeature(region.getCenterPos().x, region.getCenterPos().z, region, nearCenter);

        double d = 0.03125D;
        //depthBuffer = noiseGen4.generateNoiseOctaves(depthBuffer, chunkX * 16, chunkZ * 16, 0, 16, 16, 1, d * 2D, d * 2D, d * 2D);

        for (int dZ = 0; dZ < 16; dZ++) {
            for (int dX = 0; dX < 16; dX++) {
                int qx = dX >> 2;
                int qz = dZ >> 2;

                float xweight = (dX % 4) * 0.25F + 0.125F;
                float zweight = (dZ % 4) * 0.25F + 0.125F;

                float thickness = thicks[qx + (qz) * 5] * (1F - xweight) * (1F - zweight)
                        + thicks[qx + 1 + (qz) * 5] * (xweight) * (1F - zweight)
                        + thicks[qx + (qz + 1) * 5] * (1F - xweight) * (zweight)
                        + thicks[qx + 1 + (qz + 1) * 5] * (xweight) * (zweight)
                        - 4;

                // make sure we're not too close to the tower
                // TODO: enable
                /*if (nearFeature == TFFeature.DARK_TOWER) {
                    int hx = nearCenter.x;
                    int hz = nearCenter.z;

                    int rx = dX - hx;
                    int rz = dZ - hz;
                    int dist = (int) Mth.sqrt(rx * rx + rz * rz);

                    if (dist < 24) {
                        thickness -= (24 - dist);
                    }
                }*/

                // TODO Clean up this math
                if (thickness > 1) {
                    // We can use the Deltas here because the methods called will just
                    final int dY = chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE_WG, dX, dZ);
                    final int oceanFloor = chunk.sampleHeightmap(Heightmap.Type.OCEAN_FLOOR_WG, dX, dZ);
                    BlockPos pos = region.getCenterPos().getStartPos().add(dX, dY, dZ);

                    // Skip any blocks over water
                    if (chunk.getBlockState(pos).getMaterial().isLiquid())
                        continue;

                    // just use the same noise generator as the terrain uses for stones
                    //int noise = Math.min(3, (int) (depthBuffer[dZ & 15 | (dX & 15) << 4] / 1.25f));
                    NoiseSampler surfaceSampler = ((NoiseChunkGeneratorAccessor)this).getSurfaceDepthNoise();
                    int noise = Math.min(3, (int) (surfaceSampler.sample((blockpos.getX() + dX) * 0.0625D, (blockpos.getZ() + dZ) * 0.0625D, 0.0625D, dX * 0.0625D) * 15F / 1.25F));

                    // manipulate top and bottom
                    int treeBottom = pos.getY() + 12 - (int) (thickness * 0.5F);
                    int treeTop = treeBottom + (int) (thickness * 1.5F);

                    treeBottom -= noise;

                    // TODO: replace with own dark leaves
                    //BlockState darkLeaves = TFBlocks.dark_leaves.get().defaultBlockState();
                    BlockState darkLeaves = Blocks.DARK_OAK_LEAVES.getDefaultState();

                    for (int y = treeBottom; y < treeTop; y++) {
                        region.setBlockState(pos.withY(y), darkLeaves, 3);
                    }

                    // What are you gonna do, call the cops?
                    forceHeightMapLevel(chunk, Heightmap.Type.WORLD_SURFACE_WG, pos, dY);
                    forceHeightMapLevel(chunk, Heightmap.Type.WORLD_SURFACE, pos, dY);
                    forceHeightMapLevel(chunk, Heightmap.Type.OCEAN_FLOOR_WG, pos, oceanFloor);
                    forceHeightMapLevel(chunk, Heightmap.Type.OCEAN_FLOOR, pos, oceanFloor);
                }
            }
        }
    }
}
