package dev.kloenk.forest.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomBlock;

public class HugeMushroomUtil {
    public static BlockState getState(HugeMushroomType type, BlockState base) {
        return base
                .with(MushroomBlock.UP, type.top)
                .with(MushroomBlock.DOWN, type.bottom)
                .with(MushroomBlock.NORTH, type.north)
                .with(MushroomBlock.SOUTH, type.south)
                .with(MushroomBlock.EAST, type.east)
                .with(MushroomBlock.WEST, type.west);
    }

    public enum HugeMushroomType {
        CENTER(true, false, false, false, false, false),
        NORTH(true, false, true, false, false, false),
        SOUTH(true, false, false, true, false, false),
        EAST(true, false, false, false, true, false),
        WEST(true, false, false, false, false, true),
        NORTH_WEST(true, false, true, false, false, true),
        NORTH_EAST(true, false, true, false, true, false),
        SOUTH_WEST(true, false, false, true, false, true),
        SOUTH_EAST(true, false, false, true, true, false);

        private final boolean top;
        private final boolean bottom;
        private final boolean north;
        private final boolean south;
        private final boolean east;
        private final boolean west;

        HugeMushroomType(boolean t, boolean b, boolean n, boolean s, boolean e, boolean w) {
            this.top = t;
            this.bottom = b;
            this.north = n;
            this.south = s;
            this.east = e;
            this.west = w;
        }
    }
}
