package dev.kloenk.forest.world.dimension.biom.feature.placer.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.kloenk.forest.util.FeaturePlacers;
import dev.kloenk.forest.world.dimension.biom.feature.TFFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.Random;
import java.util.function.BiConsumer;

public class LeafSpheroidFoliagePlacer extends FoliagePlacer {
    public static final Codec<LeafSpheroidFoliagePlacer> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.floatRange(0, 16f).fieldOf("horizontal_radius").forGetter(o -> o.horizontalRadius),
                    Codec.floatRange(0, 16f).fieldOf("vertical_radius").forGetter(o -> o.verticalRadius),
                    IntProvider.createValidatingCodec(0, 8).fieldOf("offset").forGetter(obj -> obj.offset),
                    Codec.intRange(0, 16).fieldOf("random_add_horizontal").orElse(0).forGetter(o -> o.randomHorizontal),
                    Codec.intRange(0, 16).fieldOf("random_add_vertical").orElse(0).forGetter(o -> o.randomVertical),
                    Codec.floatRange(-0.5f, 0.5f).fieldOf("vertical_filler_bias").orElse(0f).forGetter(o -> o.verticalBias),
                    Codec.intRange(0, 256).fieldOf("shag_factor").orElse(0).forGetter(o -> o.shag_factor) // Shhh
            ).apply(instance, LeafSpheroidFoliagePlacer::new)
    );

    // These two variables are floats to help round out the pixel-snapping of the sphere-filling algorithm
    // n+0.5 numbers seem to work best but messing with it is encouraged to find best results
    private final float horizontalRadius;
    private final float verticalRadius;
    private final float verticalBias;

    private final int randomHorizontal;
    private final int randomVertical;
    private final int shag_factor;

    public LeafSpheroidFoliagePlacer(float horizontalRadius, float verticalRadius, IntProvider yOffset, int randomHorizontal, int randomVertical, float verticalBias, int shag_factor) {
        super(ConstantIntProvider.create((int) horizontalRadius), yOffset);

        this.horizontalRadius = horizontalRadius;
        this.verticalRadius = verticalRadius;
        this.randomHorizontal = randomHorizontal;
        this.randomVertical = randomVertical;
        this.verticalBias = verticalBias;
        this.shag_factor = shag_factor;
    }

    @Override
    protected FoliagePlacerType<?> getType() {
        return TFFeatures.FOLIAGE_SPHEROID;
    }

    /**
     * This is the main method used to generate foliage.
     *
     * @param world
     * @param replacer
     * @param random
     * @param config
     * @param trunkHeight
     * @param treeNode
     * @param foliageHeight
     * @param radius
     * @param offset
     */
    @Override
    protected void generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, TreeFeatureConfig config, int trunkHeight, TreeNode treeNode, int foliageHeight, int radius, int offset) {
        BlockPos center = treeNode.getCenter().up(offset); // foliage.getCenter

        FeaturePlacers.placeSpheroid(world, replacer, FeaturePlacers.VALID_TREE_POS, random, center, treeNode.getFoliageRadius() + this.horizontalRadius + random.nextInt(this.randomHorizontal + 1), treeNode.getFoliageRadius() + this.verticalRadius + random.nextInt(this.randomVertical + 1), this.verticalBias, config.foliageProvider);

        if (this.shag_factor > 0) {
            for (int i = 0; i < this.shag_factor; i++) {
                float randomYaw = random.nextFloat() * MathHelper.TAU;
                float randomPitch = random.nextFloat() * 2f - 1f; //random.nextFloat() * 0.75f + 0.25f;
                float yUnit = MathHelper.sqrt(1 - randomPitch * randomPitch); // Inverse Trigonometry identity (sin(arcos(t)) == sqrt(1 - t*t))
                float xCircleOffset = yUnit * MathHelper.cos(randomYaw) * (this.horizontalRadius - 1f); // We do radius minus 1 here so the leaf 2x2 generates overlapping the existing foilage
                float zCircleOffset = yUnit * MathHelper.sin(randomYaw) * (this.horizontalRadius - 1f); // instead of making awkward 2x2 pieces of foilage jutting out

                BlockPos placement = center.add(xCircleOffset + ((int) xCircleOffset >> 31), randomPitch * (this.verticalRadius + 0.25f) + this.verticalBias, zCircleOffset + ((int) zCircleOffset >> 31));

                placeLeafCluster(replacer, random, placement.toImmutable(), config.foliageProvider);
            }
        }
    }

    protected static void placeLeafCluster(BiConsumer<BlockPos, BlockState> worldPlacer, Random random, BlockPos pos, BlockStateProvider state) {
        FeaturePlacers.placeProvidedBlock(worldPlacer, pos, state, random);
        FeaturePlacers.placeProvidedBlock(worldPlacer, pos.east(), state, random);
        FeaturePlacers.placeProvidedBlock(worldPlacer, pos.south(), state, random);
        FeaturePlacers.placeProvidedBlock(worldPlacer, pos.add(1, 0, 1), state, random);
    }

    @Override
    public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
        return 0;
    }

    /**
     * Used to exclude certain positions such as corners when creating a square of leaves.
     *
     * @param random
     * @param dx
     * @param y
     * @param dz
     * @param radius
     * @param giantTrunk
     */
    @Override
    protected boolean isInvalidForLeaves(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return false;
    }
}
