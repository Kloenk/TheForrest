package dev.kloenk.forest.world.dimension.biom.feature.placer.tree;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.kloenk.forest.util.FeatureLogic;
import dev.kloenk.forest.world.dimension.biom.feature.TFBiomeFeatures;
import dev.kloenk.forest.world.dimension.biom.feature.TFFeatures;
import dev.kloenk.forest.world.feature.TFFeature;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class BranchingTrunkPlacer extends TrunkPlacer {
    public static final Codec<BranchingTrunkPlacer> CODEC = RecordCodecBuilder.create(instance ->
            fillTrunkPlacerFields(instance).and(instance.group(
                    Codec.intRange(0, 24).fieldOf("branch_start_offset_down").forGetter(o -> o.branchDownwardOffset),
                    BranchesConfig.CODEC.fieldOf("branch_config").forGetter(o -> o.branchesConfig),
                    Codec.BOOL.fieldOf("perpendicular_branches").forGetter(o -> o.perpendicularBranches)
            )).apply(instance, BranchingTrunkPlacer::new)
    );

    private final int branchDownwardOffset;
    private final BranchesConfig branchesConfig;
    private final boolean perpendicularBranches;


    public BranchingTrunkPlacer(int baseHeight, int randomHeightA, int randomHeightB, int branchDownwardOffset, BranchesConfig branchesConfig, boolean perpendicularBranches) {
        super(baseHeight, randomHeightA, randomHeightB);
        this.branchDownwardOffset = branchDownwardOffset;
        this.branchesConfig = branchesConfig;
        this.perpendicularBranches = perpendicularBranches;
    }

    @Override
    protected TrunkPlacerType<?> getType() {
        return TFFeatures.TRUNK_BRANCHING;
    }

    /**
     * Generates the trunk blocks and return a list of tree nodes to place foliage around
     *
     * @param world
     * @param replacer
     * @param random
     * @param height
     * @param startPos
     * @param config
     */
    @Override
    public List<FoliagePlacer.TreeNode> generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, int height, BlockPos startPos, TreeFeatureConfig config) {
        List<FoliagePlacer.TreeNode> leafAttachements = Lists.newArrayList();

        for (int y = 0; y <= height; y++) {
            if (!getAndSetState(world, replacer, random, startPos.up(y), config)) {
                height = y;
                break;
            }
        }

        leafAttachements.add(new FoliagePlacer.TreeNode(startPos.up(height), 0, false));

        int numBranches = this.branchesConfig.branchCount + random.nextInt(this.branchesConfig.randomAddBranches + 1);
        float offset = random.nextFloat();
        for (int b = 0; b < numBranches; b++) {
            buildBranch(
                    world,
                    replacer,
                    startPos,
                    leafAttachements,
                    height - this.branchDownwardOffset + b
                    , this.branchesConfig.length,
                    this.branchesConfig.spacingYaw * b + offset,
                    this.branchesConfig.downwardsPitch,
                    random,
                    config,
                    this.perpendicularBranches
            );
        }

        return leafAttachements;
    }

    protected static void buildBranch(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, BlockPos pos, List<FoliagePlacer.TreeNode> leafeBlocks, int height, double length, double angle, double tilt, Random random, TreeFeatureConfig config, boolean perpendicularBranches) {
        BlockPos src = pos.up(height);
        BlockPos dest = FeatureLogic.translate(src, length, angle, tilt);

        if (perpendicularBranches) {
            drawBresenhamBranch(world, replacer, random, src, dest.withY(src.getY()), config);

            int max = Math.max(src.getY(), dest.getY());
            int min = Math.min(src.getY(), dest.getY());

            for (int i = min; i < max + 1; i++) {
                getAndSetState(world, replacer, random, dest.withY(i), config);
            }
        } else {
            drawBresenhamBranch(world, replacer, random, src, dest, config);
        }

        getAndSetState(world, replacer, random, pos.north(), config);
        getAndSetState(world, replacer, random, pos.east(), config);
        getAndSetState(world, replacer, random, pos.south(), config);
        getAndSetState(world, replacer, random, pos.west(), config);

        leafeBlocks.add(new FoliagePlacer.TreeNode(dest, 0, false));
    }

    /**
     * Draws a line from {x1, y1, z1} to {x2, y2, z2}
     * This takes all variables for setting Branch
     */
    private static void drawBresenhamBranch(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, BlockPos from, BlockPos to, TreeFeatureConfig config) {
        for (BlockPos pixel : FeatureLogic.getBresenhamArrays(from, to)) {
            getAndSetState(world, replacer, random, pixel, config);
        }
    }
}
