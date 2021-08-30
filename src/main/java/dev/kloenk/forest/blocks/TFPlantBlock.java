package dev.kloenk.forest.blocks;

import net.minecraft.block.PlantBlock;
import net.minecraft.util.StringIdentifiable;

import java.util.Locale;

public class TFPlantBlock extends PlantBlock {
    private final PlantVariant variant;

    protected TFPlantBlock(Settings settings, PlantVariant variant) {
        super(settings);
        this.variant = variant;
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
