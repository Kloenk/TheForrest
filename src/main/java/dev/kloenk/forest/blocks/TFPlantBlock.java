package dev.kloenk.forest.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import java.util.Locale;

public class TFPlantBlock extends PlantBlock {
    private final PlantVariant variant;

    protected TFPlantBlock(Settings settings, PlantVariant variant) {
        super(settings);
        this.variant = variant;
    }

    public static boolean canPlaceRootAt(WorldView world, BlockPos pos) {
        BlockState state = world.getBlockState(pos.up());
        if (state.getMaterial() == Material.SOIL || state.getMaterial() == Material.SOLID_ORGANIC) {
            // can always hang below dirt blocks
            return true;
        } else {
            return (state.getBlock() == TFBlocks.ROOT_STRAND
                    || state == TFBlocks.ROOT.getDefaultState());
        }
    }

    // TODO

    public static enum PlantVariant implements StringIdentifiable {
        MOSSPATCH(),
        MAYAPPLE(),
        CLOVERPATCH(),
        FIDDLEHEAD(true),
        MUSHGLOOM(),
        TORCHBERRY(),
        ROOT_STRAND(),
        FALLEN_LEAVES(true, true);

        public final boolean isColored;
        public final boolean isLeaves;

        PlantVariant() {
            this(false);
        }

        PlantVariant(boolean isColored) {
            this(isColored, false);
        }

        PlantVariant(boolean isColored, boolean isLeaves) {
            this.isColored = isColored;
            this.isLeaves = isLeaves;
        }


        @Override
        public String asString() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
