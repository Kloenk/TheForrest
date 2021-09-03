package dev.kloenk.forest.world.dimension.biom.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

public class TFWebsFeature extends Feature<DefaultFeatureConfig> {

    public TFWebsFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    private static boolean isValidMaterial(Material material) {
        return material == Material.LEAVES || material == Material.WOOD;
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> ctx) {
        StructureWorldAccess world = ctx.getWorld();
        BlockPos pos = ctx.getOrigin();
        ChunkGenerator generator = ctx.getGenerator();

        while (pos.getY() > generator.getSpawnHeight(world) && world.isAir(pos))
            pos = pos.down();

        if (!isValidMaterial(world.getBlockState(pos).getMaterial()))
            return false;

        do {
            if (world.isAir(pos.down())) {
                world.setBlockState(pos.down(), Blocks.COBWEB.getDefaultState(), 16 | 2);
                return true;
            }
            pos = pos.down();
        } while (pos.getY() > generator.getSpawnHeight(world) && isValidMaterial(world.getBlockState(pos).getMaterial()));
        return false;
    }
}
