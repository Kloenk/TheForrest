package dev.kloenk.forest.world.dimension.biom.carvers;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import dev.kloenk.forest.blocks.TFBlocks;
import dev.kloenk.forest.mixin.CarverAccessor;
import dev.kloenk.forest.mixin.CaveCarverConfigAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.carver.CaveCarver;
import net.minecraft.world.gen.carver.CaveCarverConfig;
import net.minecraft.world.gen.chunk.AquiferSampler;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

public class TFCavesCarver extends CaveCarver {
    private final boolean isHighlands;

    public TFCavesCarver(Codec<CaveCarverConfig> configCodec, boolean isHighlands) {
        super(configCodec);
        this.isHighlands = isHighlands;
        // TODO: why?
        this.carvableFluids = ImmutableSet.of();
    }

    @Override
    public boolean carve(
            CarverContext context,
            CaveCarverConfig config,
            Chunk chunk,
            Function<BlockPos, Biome> posToBiome,
            Random random,
            AquiferSampler aquiferSampler,
            ChunkPos pos,
            BitSet carvingMask
    ) {
        // TODO: offset?
        int i = ChunkSectionPos.getOffsetPos(this.getBranchFactor() * 2 - 1, 0);
        int j = random.nextInt(random.nextInt(this.getMaxCaveCount() + 1) + 1);

        for (int k = 0; k < j; ++k) {
            double x = pos.getOffsetX(random.nextInt(16));
            double y = config.y.get(random, context);
            double z = pos.getOffsetZ(random.nextInt(16));
            double horiz = config.horizontalRadiusMultiplier.get(random);
            double vert = config.verticalRadiusMultiplier.get(random);
            double floor = ((CaveCarverConfigAccessor)config).getFloorLevel().get(random);

            SkipPredicate checker = (checkerContext, scaledRelativeX, scaledRelativeY, scaledRelativeZ, checkerY) -> shouldSkip(scaledRelativeX, scaledRelativeY, scaledRelativeZ, floor);
            int l = 1;
            if (random.nextInt(4) == 0 && isHighlands) {
                double d6 = config.yScale.get(random);
                float f1 = 1f + random.nextFloat() * 6f;
                // TODO
                //this.createRoom(ctx)
                this.createRoom(context, config, chunk, posToBiome, random.nextLong(), aquiferSampler, x, y, z, f1, d6, carvingMask, checker);
                l += random.nextInt(4);
            }

            for (int k1 = 0; k1 < l; ++k1) {
                float f = random.nextFloat() * ((float)Math.PI * 2F);
                float f3 = (random.nextFloat() - 0.5F) / 4.0F;
                float f2 = this.getTunnelSystemWidth(random);
                int i1 = i - random.nextInt(i / 4);
                this.createTunnel(context, config, chunk, posToBiome, random.nextLong(), aquiferSampler, x, y, z, horiz, vert, f2, f, f3, 0, i1, this.getTunnelSystemHeightWidthRatio(), carvingMask, checker);
            }
        }

        return false;
    }

    @Override
    protected boolean carveAtPoint(CarverContext context, CaveCarverConfig config, Chunk chunk, Function<BlockPos, Biome> posToBiome, BitSet carvingMask, Random random, BlockPos.Mutable pos, BlockPos.Mutable downPos, AquiferSampler aquifer, MutableBoolean foundSurface) {
        BlockState blockstate = chunk.getBlockState(pos);
        BlockState blockstate1 = chunk.getBlockState(downPos.set(pos, Direction.UP));
        if (blockstate.isOf(Blocks.GRASS_BLOCK) || blockstate.isOf(Blocks.MYCELIUM)) {
            foundSurface.setTrue();
        }

        //We don't want caves to go so far down you can see bedrock, so lets stop them right before
        if(pos.getY() < chunk.getBottomY() + 6) return false;

        if (!this.canCarveBlock(blockstate, blockstate1) && config.debugConfig.isDebugMode()) {
            return false;
        } else {
            BlockState blockstate2 = ((CarverAccessor)this).getStateInvoker(context, config, pos, aquifer);
            if (blockstate2 == null) {
                return false;
            } else {
                //here's the code for preventing floating water and making dirt roofs. Enjoy :)

                for (Direction facing : Direction.values()) {
                    FluidState aboveSurface = chunk.getFluidState(downPos.add(pos.add(0, 1, 0)));
                    FluidState areaAround = chunk.getFluidState(downPos.offset(facing));
                    FluidState areaAboveAround = chunk.getFluidState(downPos.add(pos.add(0, 1, 0).offset(facing)));

                    if (areaAround.isIn(FluidTags.WATER) || areaAboveAround.isIn(FluidTags.WATER) || aboveSurface.isIn(FluidTags.WATER)) {
                        return false;
                    } else {
                        if (random.nextInt(10) == 0 && chunk.getBlockState(pos).isOf(Blocks.CAVE_AIR) && chunk.getBlockState(pos.offset(facing)).isIn(BlockTags.BASE_STONE_OVERWORLD) && this.isHighlands) {
                            chunk.setBlockState(pos.offset(facing), TFBlocks.TROLLSTEIN.getDefaultState(), false);
                        }
                        chunk.setBlockState(pos, CAVE_AIR, false);

                        if ((chunk.getBlockState(pos.up()).isIn(BlockTags.BASE_STONE_OVERWORLD) || chunk.getFluidState(pos.up()).isIn(FluidTags.WATER)) && chunk.getBlockState(pos).isAir() && !this.isHighlands) {
                            switch(random.nextInt(5)) {
                                case 0, 1, 2 -> chunk.setBlockState(pos.up(), Blocks.DIRT.getDefaultState(), false);
                                case 3 -> chunk.setBlockState(pos.up(), Blocks.ROOTED_DIRT.getDefaultState(), false);
                                case 4 -> chunk.setBlockState(pos.up(), Blocks.COARSE_DIRT.getDefaultState(), false);
                            }
                        }
                        if (foundSurface.isTrue()) {
                            BlockPos posDown = pos.set(pos, Direction.DOWN).down();
                            if (chunk.getBlockState(posDown).isOf(Blocks.DIRT)) {
                                chunk.setBlockState(posDown, posToBiome.apply(pos).getGenerationSettings().getSurfaceConfig().getTopMaterial(), false);
                            }
                        }

                    }
                }
                return true;
            }
        }
    }

    protected void createRoom(CarverContext ctx, CaveCarverConfig config, Chunk chunk, Function<BlockPos, Biome> biomePos, long seed, AquiferSampler aquifer, double posX, double posY, double posZ, float radius, double horizToVertRatio, BitSet bitSet, SkipPredicate checker) {
        double d0 = 1.5D + (double)(MathHelper.sin(((float)Math.PI / 2F)) * radius);
        double d1 = d0 * horizToVertRatio;
        this.carveRegion(ctx, config, chunk, biomePos, seed, aquifer, posX + 1.0D, posY, posZ, d0, d1, bitSet, checker);
    }

    protected void createTunnel(CarverContext ctx, CaveCarverConfig config, Chunk chunk, Function<BlockPos, Biome> biomePos, long seed, AquiferSampler aquifer, double posX, double posY, double posZ, double horizMult, double vertMult, float thickness, float yaw, float pitch, int branchIndex, int branchCount, double horizToVertRatio, BitSet bitSet, SkipPredicate checker) {
        Random random = new Random(seed);
        int i = random.nextInt(branchCount / 2) + branchCount / 4;
        boolean flag = random.nextInt(6) == 0;
        float f = 0.0F;
        float f1 = 0.0F;

        for(int j = branchIndex; j < branchCount; ++j) {
            double d0 = 1.5D + (double)(MathHelper.sin((float)Math.PI * (float)j / (float)branchCount) * thickness);
            double d1 = d0 * horizToVertRatio;
            float f2 = MathHelper.cos(pitch);
            posX += MathHelper.cos(yaw) * f2;
            posY += MathHelper.sin(pitch);
            posZ += MathHelper.sin(yaw) * f2;
            pitch = pitch * (flag ? 0.92F : 0.7F);
            pitch = pitch + f1 * 0.1F;
            yaw += f * 0.1F;
            f1 = f1 * 0.9F;
            f = f * 0.75F;
            f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            f = f + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
            if (j == i && thickness > 1.0F) {
                this.createTunnel(ctx, config, chunk, biomePos, random.nextLong(), aquifer, posX, posY, posZ, horizMult, vertMult, random.nextFloat() * 0.5F + 0.5F, yaw - ((float)Math.PI / 2F), pitch / 3.0F, j, branchCount, 1.0D, bitSet, checker);
                this.createTunnel(ctx, config, chunk, biomePos, random.nextLong(), aquifer, posX, posY, posZ, horizMult, vertMult, random.nextFloat() * 0.5F + 0.5F, yaw + ((float)Math.PI / 2F), pitch / 3.0F, j, branchCount, 1.0D, bitSet, checker);
                return;
            }

            if (random.nextInt(4) != 0) {
                if (!canCarveBranch(chunk.getPos(), posX, posZ, j, branchCount, thickness)) {
                    return;
                }

                this.carveRegion(ctx, config, chunk, biomePos, seed, aquifer, posX, posY, posZ, d0 * horizMult, d1 * vertMult, bitSet, checker);
            }
        }

    }


    @Override
    public boolean shouldCarve(CaveCarverConfig config, Random random) {
        return random.nextFloat() <= config.probability;
    }

    private static boolean shouldSkip(double posX, double posY, double posZ, double minY) {
        if (posY <= minY) {
            return true;
        } else {
            return posX * posX + posY * posY + posZ * posZ >= 1.0d;
        }
    }

}
