package dev.kloenk.forest.util;

import dev.kloenk.forest.blocks.TFBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class FeaturePlacers {
    public static final BiFunction<TestableWorld, BlockPos, Boolean> VALID_TREE_POS = TreeFeature::canReplace;

    public static <T extends MobEntity> void placeEntity(EntityType<T> entityType, BlockPos pos, ServerWorldAccess worldAccess) {
        MobEntity mob = entityType.create((World) worldAccess);

        if (mob == null) return;

        mob.setPersistent();
        mob.refreshPositionAndAngles(pos, 0.0F, 0.0F);
        mob.initialize(worldAccess, worldAccess.getLocalDifficulty(pos), SpawnReason.STRUCTURE, null, null);
        worldAccess.spawnEntityAndPassengers(mob);
        worldAccess.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
    }

    /**
     * Draws a line from {x1, y1, z1} to {x2, y2, z2}
     * This takes all variables for setting Branch
     */
    public static void drawBresenhamBranch(WorldAccess world, BiConsumer<BlockPos, BlockState> trunkPlacer, Random random, BlockPos start, BlockPos end, BlockStateProvider config) {
        for (BlockPos pixel : FeatureLogic.getBresenhamArrays(start, end)) {
            placeIfValidTreePos(world, trunkPlacer, random, pixel, config);
        }
    }

    /**
     * Build a root, but don't let it stick out too far into thin air because that's weird
     */
    public static void buildRoot(WorldAccess world, BiConsumer<BlockPos, BlockState> placer, Random rand, BlockPos start, double offset, int b, BlockStateProvider config) {
        BlockPos dest = FeatureLogic.translate(start.down(b + 2), 5, 0.3 * b + offset, 0.8);

        // go through block by block and stop drawing when we head too far into open air
        for (BlockPos coord : FeatureLogic.getBresenhamArrays(start.down(), dest)) {
            if (!placeIfValidRootPos(world, placer, rand, coord, config)) return;
        }
    }

    /**
     * Draws a line from {x1, y1, z1} to {x2, y2, z2}
     * This just takes a BlockState, used to set Trunk
     */
    public static void drawBresenhamTree(BiConsumer<BlockPos, BlockState> placer, BlockPos from, BlockPos to, BlockStateProvider config, Random random) {
        for (BlockPos pixel : FeatureLogic.getBresenhamArrays(from, to)) {
            placeProvidedBlock(placer, pixel, config, random);
        }
    }

    public static void placeProvidedBlock(BiConsumer<BlockPos, BlockState> worldPlacer, BlockPos pos, BlockStateProvider config, Random random) {
        worldPlacer.accept(pos, config.getBlockState(random, pos));
    }

    public static void placeProvidedBlock(TestableWorld world, BiConsumer<BlockPos, BlockState> worldPlacer, BiFunction<TestableWorld, BlockPos, Boolean> predicate, BlockPos pos, BlockStateProvider config, Random random) {
        if (predicate.apply(world, pos)) {
            worldPlacer.accept(pos, config.getBlockState(random, pos));
        }
    }

    // Use for trunks with Odd-count widths
    public static void placeCircleOdd(TestableWorld world, BiConsumer<BlockPos, BlockState> placer, BiFunction<TestableWorld, BlockPos, Boolean> predicate, Random random, BlockPos centerPos, float radius, BlockStateProvider config) {
        // Normally, I'd use mutable pos here but there are multiple bits of logic down the line that force
        // the pos to be immutable causing multiple same BlockPos instances to exist.
        float radiusSquared = radius * radius;
        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos, config, random);

        // trace out a quadrant
        for (int x = 0; x <= radius; x++) {
            for (int z = 1; z <= radius; z++) {
                // if we're inside the blob, fill it
                if (x * x + z * z <= radiusSquared) {
                    // do four at a time for easiness!
                    FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.add(  x, 0,  z), config, random);
                    FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.add( -x, 0, -z), config, random);
                    FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.add( -z, 0,  x), config, random);
                    FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.add(  z, 0, -x), config, random);
                    // Confused how this circle pixel-filling algorithm works exactly? https://www.desmos.com/calculator/psqynhk21k
                }
            }
        }
    }

    // Use for trunks with Even-count widths
    // TODO Verify that it works correctly, haven't gotten to a compiling state yet to test
    public static void placeCircleEven(TestableWorld world, BiConsumer<BlockPos, BlockState> placer, BiFunction<TestableWorld, BlockPos, Boolean> predicate, Random random, BlockPos centerPos, float radius, BlockStateProvider config) {
        // Normally, I'd use mutable pos here but there are multiple bits of logic down the line that force
        // the pos to be immutable causing multiple same BlockPos instances to exist.
        float radiusSquared = radius * radius;
        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos, config, random);

        // trace out a quadrant
        for (int x = 0; x <= radius; x++) {
            for (int z = 0; z <= radius; z++) {
                // if we're inside the blob, fill it
                if (x * x + z * z <= radiusSquared) {
                    // do four at a time for easiness!
                    FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.add( 1+x, 0, 1+z), config, random);
                    FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.add( -x, 0, -z), config, random);
                    FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.add( -x, 0, 1+z), config, random);
                    FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.add( 1+x, 0, -z), config, random);
                    // Confused how this circle pixel-filling algorithm works exactly? https://www.desmos.com/calculator/psqynhk21k
                }
            }
        }
    }

    public static void placeSpheroid(TestableWorld world, BiConsumer<BlockPos, BlockState> placer, BiFunction<TestableWorld, BlockPos, Boolean> predicate, Random random, BlockPos centerPos, float xzRadius, float yRadius, float verticalBias, BlockStateProvider config) {
        float xzRadiusSquared = xzRadius * xzRadius;
        float yRadiusSquared = yRadius * yRadius;
        float superRadiusSquared = xzRadiusSquared * yRadiusSquared;
        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos, config, random);

        for (int y = 0; y <= yRadius; y++) {
            if (y > yRadius) continue;

            FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.add( 0,  y, 0), config, random);
            FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.add( 0, -y, 0), config, random);
        }

        for (int x = 0; x <= xzRadius; x++) {
            for (int z = 1; z <= xzRadius; z++) {
                if (x * x + z * z > xzRadiusSquared) continue;

                FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.add(  x, 0,  z), config, random);
                FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.add( -x, 0, -z), config, random);
                FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.add( -z, 0,  x), config, random);
                FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.add(  z, 0, -x), config, random);

                for (int y = 1; y <= yRadius; y++) {
                    float xzSquare = ((x * x + z * z) * yRadiusSquared);

                    if (xzSquare + (((y - verticalBias) * (y - verticalBias)) * xzRadiusSquared) <= superRadiusSquared) {
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.add(  x,  y,  z), config, random);
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.add( -x,  y, -z), config, random);
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.add( -z,  y,  x), config, random);
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.add(  z,  y, -x), config, random);
                    }

                    if (xzSquare + (((y + verticalBias) * (y + verticalBias)) * xzRadiusSquared) <= superRadiusSquared) {
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.add(  x, -y,  z), config, random);
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.add( -x, -y, -z), config, random);
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.add( -z, -y,  x), config, random);
                        FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.add(  z, -y, -x), config, random);
                    }
                }
            }
        }
    }

    // Version without the `verticalBias` unlike above
    public static void placeSpheroid(TestableWorld world, BiConsumer<BlockPos, BlockState> placer, BiFunction<TestableWorld, BlockPos, Boolean> predicate, Random random, BlockPos centerPos, float xzRadius, float yRadius, BlockStateProvider config) {
        float xzRadiusSquared = xzRadius * xzRadius;
        float yRadiusSquared = yRadius * yRadius;
        float superRadiusSquared = xzRadiusSquared * yRadiusSquared;
        FeaturePlacers.placeProvidedBlock(placer, centerPos, config, random);

        for (int y = 0; y <= yRadius; y++) {
            if (y > yRadius) continue;

            FeaturePlacers.placeProvidedBlock(placer, centerPos.add( 0,  y, 0), config, random);
            FeaturePlacers.placeProvidedBlock(placer, centerPos.add( 0, -y, 0), config, random);
        }

        for (int x = 0; x <= xzRadius; x++) {
            for (int z = 1; z <= xzRadius; z++) {
                if (x * x + z * z > xzRadiusSquared) continue;

                FeaturePlacers.placeProvidedBlock(placer, centerPos.add(  x, 0,  z), config, random);
                FeaturePlacers.placeProvidedBlock(placer, centerPos.add( -x, 0, -z), config, random);
                FeaturePlacers.placeProvidedBlock(placer, centerPos.add( -z, 0,  x), config, random);
                FeaturePlacers.placeProvidedBlock(placer, centerPos.add(  z, 0, -x), config, random);

                for (int y = 1; y <= yRadius; y++) {
                    float xzSquare = ((x * x + z * z) * yRadiusSquared);

                    if (xzSquare + (y * y) * xzRadiusSquared <= superRadiusSquared) {
                        FeaturePlacers.placeProvidedBlock(placer, centerPos.add(  x,  y,  z), config, random);
                        FeaturePlacers.placeProvidedBlock(placer, centerPos.add( -x,  y, -z), config, random);
                        FeaturePlacers.placeProvidedBlock(placer, centerPos.add( -z,  y,  x), config, random);
                        FeaturePlacers.placeProvidedBlock(placer, centerPos.add(  z,  y, -x), config, random);

                        FeaturePlacers.placeProvidedBlock(placer, centerPos.add(  x, -y,  z), config, random);
                        FeaturePlacers.placeProvidedBlock(placer, centerPos.add( -x, -y, -z), config, random);
                        FeaturePlacers.placeProvidedBlock(placer, centerPos.add( -z, -y,  x), config, random);
                        FeaturePlacers.placeProvidedBlock(placer, centerPos.add(  z, -y, -x), config, random);
                    }
                }
            }
        }
    }

    // [VanillaCopy] TrunkPlacer.placeLog - Swapped TreeConfiguration for BlockStateProvider
    // If possible, use TrunkPlacer.placeLog instead
    public static boolean placeIfValidTreePos(WorldAccess world, BiConsumer<BlockPos, BlockState> placer, Random random, BlockPos pos, BlockStateProvider config) {
        if (TreeFeature.canReplace(world, pos)) {
            placer.accept(pos, config.getBlockState(random, pos));
            return true;
        } else {
            return false;
        }
    }

    public static boolean placeIfValidRootPos(WorldAccess world, BiConsumer<BlockPos, BlockState> placer, Random random, BlockPos pos, BlockStateProvider config) {
        if (FeatureLogic.canRootGrowIn(world, pos)) {
            placer.accept(pos, config.getBlockState(random, pos));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Add a firefly at the specified height and angle.
     *
     * @param height how far up the tree
     * @param angle  from 0 - 1 rotation around the tree
     */
    public static void addFirefly(WorldAccess world, BlockPos pos, int height, double angle) {
        int iAngle = (int) (angle * 4.0);
        if (iAngle == 0) {
            // FIXME: not jar but firefly itself
            setIfEmpty(world, pos.add( 1, height,  0), TFBlocks.FIREFLY_JAR.getDefaultState().with(FacingBlock.FACING, Direction.EAST));
        } else if (iAngle == 1) {
            // FIXME: not jar but firefly itself
            setIfEmpty(world, pos.add(-1, height,  0), TFBlocks.FIREFLY_JAR.getDefaultState().with(FacingBlock.FACING, Direction.WEST));
        } else if (iAngle == 2) {
            // FIXME: not jar but firefly itself
            setIfEmpty(world, pos.add( 0, height,  1), TFBlocks.FIREFLY_JAR.getDefaultState().with(FacingBlock.FACING, Direction.SOUTH));
        } else if (iAngle == 3) {
            // FIXME: not jar but firefly itself
            setIfEmpty(world, pos.add( 0, height, -1), TFBlocks.FIREFLY_JAR.getDefaultState().with(FacingBlock.FACING, Direction.NORTH));
        }
    }

    private static void setIfEmpty(WorldAccess world, BlockPos pos, BlockState state) {
        if (world.isAir(pos)) {
            world.setBlockState(pos, state,3);
        }
    }
}
