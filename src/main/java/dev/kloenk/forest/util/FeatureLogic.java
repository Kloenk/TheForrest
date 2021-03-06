package dev.kloenk.forest.util;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TestableWorld;

import java.util.Random;
import java.util.function.Predicate;

public class FeatureLogic {
    static final Predicate<BlockState> IS_AIR = AbstractBlock.AbstractBlockState::isAir;

    public static boolean hasEmptyNeighbor(TestableWorld worldReader, BlockPos pos) {
        return worldReader.testBlockState(pos.up(), IS_AIR)
                || worldReader.testBlockState(pos.north(), IS_AIR)
                || worldReader.testBlockState(pos.south(), IS_AIR)
                || worldReader.testBlockState(pos.west(), IS_AIR)
                || worldReader.testBlockState(pos.east(), IS_AIR)
                || worldReader.testBlockState(pos.down(), IS_AIR);
    }

    // Slight stretch of logic: We check if the block is completely surrounded by air.
    // If it's not completely surrounded by air, then there's a solid
    public static boolean hasSolidNeighbor(TestableWorld worldReader, BlockPos pos) {
        return !(worldReader.testBlockState(pos.up(), IS_AIR)
                && worldReader.testBlockState(pos.north(), IS_AIR)
                && worldReader.testBlockState(pos.south(), IS_AIR)
                && worldReader.testBlockState(pos.west(), IS_AIR)
                && worldReader.testBlockState(pos.east(), IS_AIR)
                && worldReader.testBlockState(pos.down(), IS_AIR));
    }

    public static boolean canRootGrowIn(TestableWorld worldReader, BlockPos pos) {
        if (worldReader.testBlockState(pos, IS_AIR)) {
            // roots can grow through air if they are near a solid block
            return hasSolidNeighbor(worldReader, pos);
        } else {
            return worldReader.testBlockState(pos, FeatureLogic::canRootReplace);
        }
    }

    public static boolean canRootReplace(BlockState state) {
        Block block = state.getBlock();

        // TODO
        /*return // TODO Starting to sound like we should have a generalized no-replace tag list
                block != TFBlocks.stronghold_shield.get()
                && block != TFBlocks.trophy_pedestal.get()
                && block != TFBlocks.boss_spawner_naga.get()
                && block != TFBlocks.boss_spawner_lich.get()
                && block != TFBlocks.boss_spawner_hydra.get()
                && block != TFBlocks.boss_spawner_ur_ghast.get()
                && block != TFBlocks.boss_spawner_knight_phantom.get()
                && block != TFBlocks.boss_spawner_snow_queen.get()
                && block != TFBlocks.boss_spawner_minoshroom.get()
                && block != TFBlocks.boss_spawner_alpha_yeti.get()
                && (state.getMaterial() == Material.GRASS || state.getMaterial() == Material.DIRT || state.getMaterial() == Material.STONE || state.getMaterial() == Material.WATER);*/
        return true;
    }

    /**
     * Moves distance along the vector.
     * <p>
     * This goofy function takes a float between 0 and 1 for the angle, where 0 is 0 degrees, .5 is 180 degrees and 1 and 360 degrees.
     * For the tilt, it takes a float between 0 and 1 where 0 is straight up, 0.5 is straight out and 1 is straight down.
     */
    public static BlockPos translate(BlockPos pos, double distance, double angle, double tilt) {
        double rangle = angle * 2.0D * Math.PI;
        double rtilt = tilt * Math.PI;

        return pos.add(
                Math.round(Math.sin(rangle) * Math.sin(rtilt) * distance),
                Math.round(Math.cos(rtilt) * distance),
                Math.round(Math.cos(rangle) * Math.sin(rtilt) * distance)
        );
    }

    /**
     * Get an array of values that represent a line from point A to point B
     */
    public static BlockPos[] getBresenhamArrays(BlockPos src, BlockPos dest) {
        return getBresenhamArrays(src.getX(), src.getY(), src.getZ(), dest.getX(), dest.getY(), dest.getZ());
    }

    /**
     * Get an array of values that represent a line from point A to point B
     * todo 1.9 lazify this into an iterable?
     */
    public static BlockPos[] getBresenhamArrays(int x1, int y1, int z1, int x2, int y2, int z2) {
        int i, dx, dy, dz, absDx, absDy, absDz, x_inc, y_inc, z_inc, err_1, err_2, doubleAbsDx, doubleAbsDy, doubleAbsDz;

        BlockPos pixel = new BlockPos(x1, y1, z1);
        BlockPos[] lineArray;

        dx = x2 - x1;
        dy = y2 - y1;
        dz = z2 - z1;
        x_inc = (dx < 0) ? -1 : 1;
        absDx = Math.abs(dx);
        y_inc = (dy < 0) ? -1 : 1;
        absDy = Math.abs(dy);
        z_inc = (dz < 0) ? -1 : 1;
        absDz = Math.abs(dz);
        doubleAbsDx = absDx << 1;
        doubleAbsDy = absDy << 1;
        doubleAbsDz = absDz << 1;

        if ((absDx >= absDy) && (absDx >= absDz)) {
            err_1 = doubleAbsDy - absDx;
            err_2 = doubleAbsDz - absDx;
            lineArray = new BlockPos[absDx + 1];
            for (i = 0; i < absDx; i++) {
                lineArray[i] = pixel;
                if (err_1 > 0) {
                    pixel = pixel.up(y_inc);
                    err_1 -= doubleAbsDx;
                }
                if (err_2 > 0) {
                    pixel = pixel.south(z_inc);
                    err_2 -= doubleAbsDx;
                }
                err_1 += doubleAbsDy;
                err_2 += doubleAbsDz;
                pixel = pixel.east(x_inc);
            }
        } else if ((absDy >= absDx) && (absDy >= absDz)) {
            err_1 = doubleAbsDx - absDy;
            err_2 = doubleAbsDz - absDy;
            lineArray = new BlockPos[absDy + 1];
            for (i = 0; i < absDy; i++) {
                lineArray[i] = pixel;
                if (err_1 > 0) {
                    pixel = pixel.east(x_inc);
                    err_1 -= doubleAbsDy;
                }
                if (err_2 > 0) {
                    pixel = pixel.south(z_inc);
                    err_2 -= doubleAbsDy;
                }
                err_1 += doubleAbsDx;
                err_2 += doubleAbsDz;
                pixel = pixel.up(y_inc);
            }
        } else {
            err_1 = doubleAbsDy - absDz;
            err_2 = doubleAbsDx - absDz;
            lineArray = new BlockPos[absDz + 1];
            for (i = 0; i < absDz; i++) {
                lineArray[i] = pixel;
                if (err_1 > 0) {
                    pixel = pixel.up(y_inc);
                    err_1 -= doubleAbsDz;
                }
                if (err_2 > 0) {
                    pixel = pixel.east(x_inc);
                    err_2 -= doubleAbsDz;
                }
                err_1 += doubleAbsDy;
                err_2 += doubleAbsDx;
                pixel = pixel.south(z_inc);
            }
        }
        lineArray[lineArray.length - 1] = pixel;

        return lineArray;
    }

    /**
     * Gets either cobblestone or mossy cobblestone, randomly.  Used for ruins.
     */
    @Deprecated // Determine if we can actually remove this one and delegate to StructureProcessor
    public static BlockState randStone(Random rand, int howMuch) {
        return rand.nextInt(howMuch) >= 1 ? Blocks.COBBLESTONE.getDefaultState() : Blocks.MOSSY_COBBLESTONE.getDefaultState();
    }
}
