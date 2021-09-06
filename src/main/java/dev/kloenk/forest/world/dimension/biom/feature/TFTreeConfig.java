package dev.kloenk.forest.world.dimension.biom.feature;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import dev.kloenk.forest.blocks.TFBlocks;
import dev.kloenk.forest.world.dimension.biom.feature.placer.tree.BranchesConfig;
import dev.kloenk.forest.world.dimension.biom.feature.placer.tree.BranchingTrunkPlacer;
import dev.kloenk.forest.world.dimension.biom.feature.placer.tree.LeafSpheroidFoliagePlacer;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;

public class TFTreeConfig {
    protected static final int canopyDistancing = 5;

    protected final static int LEAF_SHAG_FACTOR = 24;
    public static final TreeFeatureConfig CANOPY_TREE = new TreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(TFBlocks.CANOPY_LOG.getDefaultState()),
            new BranchingTrunkPlacer(20, 5, 5, 6, new BranchesConfig(3, 1, 10, 1, 0.3, 0.2), false),
            new SimpleBlockStateProvider(TFBlocks.CANOPY_LEAVES.getDefaultState()),
            new SimpleBlockStateProvider(TFBlocks.CANOPY_SAPLING.getDefaultState()),
            new LeafSpheroidFoliagePlacer(4.5f, 1.5f, ConstantIntProvider.create(0), 1, 0, -0.25F, LEAF_SHAG_FACTOR),
            new TwoLayersFeatureSize(20, 1, canopyDistancing)
    )
            // TODO
            /*.decorators(ImmutableList.of(
            TreeDecorators.LIVING_ROOTS,
               TreeDecorators.FIREFLY,
                    new TrunkSideDecorator( // A few more Fireflies!
                       4,
                            0.5f,
                       new SimpleStateProvider(TFBlocks.firefly.get().defaultBlockState().setValue(FireflyBlock.FACING, Direction.NORTH))
            ),
            new DangleFromTreeDecorator(
                            0,
                                    1,
                                    2,
                                    5,
                                    15,
                                    new SimpleStateProvider(TFBlocks.canopy_fence.get().defaultBlockState()),
            new SimpleStateProvider(TFBlocks.firefly_jar.get().defaultBlockState())
            ),
            new DangleFromTreeDecorator(
                            0,
                                    1,
                                    2,
                                    5,
                                    15,
                                    new SimpleStateProvider(Blocks.CHAIN.defaultBlockState()),
            new SimpleStateProvider(TFBlocks.firefly_jar.get().defaultBlockState())
            )
            ))*/
            .ignoreVines()
            .build();

    public static final TreeFeatureConfig CANOPY_TREE_DEAD = new TreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(TFBlocks.CANOPY_LOG.getDefaultState()),
            new BranchingTrunkPlacer(20, 5, 5, 7, new BranchesConfig(3, 1, 10, 1, 0.3, 0.2), false),
            new SimpleBlockStateProvider(Blocks.AIR.getDefaultState()),
            new SimpleBlockStateProvider(TFBlocks.CANOPY_SAPLING.getDefaultState()),
            new LeafSpheroidFoliagePlacer(0, 0, ConstantIntProvider.create(0), 0, 0, 0, 0),
            new TwoLayersFeatureSize(20, 0, canopyDistancing)
    )
            // TODO: .decorators(ImmutableList.of(TreeDecorators.FIREFLY, TreeDecorators.LIVING_ROOTS))
            .ignoreVines()
            .build();
}
