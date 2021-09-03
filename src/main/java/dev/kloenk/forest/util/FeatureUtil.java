package dev.kloenk.forest.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldView;

public final class FeatureUtil {
    /**
     * Checks an area to see if it consists of flat natural ground below and air above
     */
    public static boolean isAreaSuitable(StructureWorldAccess world, BlockPos pos, int width, int height, int depth) {
        boolean flag = true;

        // check if there's anything within the diameter
        for (int cx = 0; cx < width; cx++) {
            for (int cz = 0; cz < depth; cz++) {
                BlockPos pos_ = pos.add(cx, 0, cz);
                // check if the blocks even exist?
                if (world.isChunkLoaded(pos_)) {
                    // is there grass, dirt or stone below?
                    Material m = world.getBlockState(pos_.down()).getMaterial();
                    if (m != Material.SOIL && m != Material.SOLID_ORGANIC && m != Material.STONE) {
                        flag = false;
                    }

                    for (int cy = 0; cy < height; cy++) {
                        // blank space above?
                        if (!world.isAir(pos.up(cy))) {
                            flag = false;
                        }
                    }
                } else {
                    flag = false;
                }
            }
        }

        // Okie dokie
        return flag;
    }

    /**
     * Draw a giant blob of whatevs.
     */
    public static void drawBlob(StructureWorldAccess world, BlockPos pos, int rad, BlockState state) {
        // then trace out a quadrant
        for (byte dx = 0; dx <= rad; dx++) {
            for (byte dy = 0; dy <= rad; dy++) {
                for (byte dz = 0; dz <= rad; dz++) {
                    // determine how far we are from the center.
                    int dist;
                    if (dx >= dy && dx >= dz) {
                        dist = dx + (Math.max(dy, dz) >> 1) + (Math.min(dy, dz) >> 2);
                    } else if (dy >= dx && dy >= dz) {
                        dist = dy + (Math.max(dx, dz) >> 1) + (Math.min(dx, dz) >> 2);
                    } else {
                        dist = dz + (Math.max(dx, dy) >> 1) + (Math.min(dx, dy) >> 2);
                    }


                    // if we're inside the blob, fill it
                    if (dist <= rad) {
                        // do eight at a time for easiness!
                        world.setBlockState(pos.add(+dx, +dy, +dz), state, 3);
                        world.setBlockState(pos.add(+dx, +dy, -dz), state, 3);
                        world.setBlockState(pos.add(-dx, +dy, +dz), state, 3);
                        world.setBlockState(pos.add(-dx, +dy, -dz), state, 3);
                        world.setBlockState(pos.add(+dx, -dy, +dz), state, 3);
                        world.setBlockState(pos.add(+dx, -dy, -dz), state, 3);
                        world.setBlockState(pos.add(-dx, -dy, +dz), state, 3);
                        world.setBlockState(pos.add(-dx, -dy, -dz), state, 3);
                    }
                }
            }
        }
    }

    /**
     * Does the block have at least 1 air block adjacent
     */
    private static final Direction[] directionsExceptDown = new Direction[]{Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};

    public static boolean hasAirAround(StructureWorldAccess world, BlockPos pos) {
        for (Direction e : directionsExceptDown) {
            if (world.isAir(pos.offset(e))) {
                return true;
            }
        }

        return false;
    }

    public static boolean isNearSolid(WorldView world, BlockPos pos) {
        for (Direction e : Direction.values()) {
            if (world.isChunkLoaded(pos.offset(e))
                    && world.getBlockState(pos.offset(e)).getMaterial().isSolid()) {
                return true;
            }
        }

        return false;
    }
}
