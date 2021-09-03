package dev.kloenk.forest.loot;

import com.google.common.collect.Sets;
import dev.kloenk.forest.ForestMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;

import java.util.Set;

public class TFTreasure {
    // For easy testing:
    // /give @p chest{BlockEntityTag:{LootTable:"forest:all_bosses",CustomName:'{"text":"Master Loot Crate"}'}} 1
    private static final Set<Identifier> TF_LOOT_TABLES = Sets.newHashSet();

    public static final TFTreasure BASEMENT = new TFTreasure("basement");

    public final Identifier lootTable;

    private TFTreasure(String path) {
        lootTable = ForestMod.path(String.format("structures/%s", path));
    }

    public void generateChest(StructureWorldAccess world, BlockPos pos, Direction dir, boolean trapped) {
        this.generateLootContainer(world, pos, (trapped ? Blocks.TRAPPED_CHEST : Blocks.CHEST).getDefaultState().with(ChestBlock.FACING, dir), 2);
    }

    public void generateLootContainer(StructureWorldAccess world, BlockPos pos, BlockState state, int flags) {
        world.setBlockState(pos, state, flags);

        this.generateChestContents(world, pos);
    }

    public void generateLootContainer(StructureWorldAccess world, BlockPos pos, BlockState state, int flags, long seed) {
        world.setBlockState(pos, state, flags);

        this.generateChestContents(world, pos, seed);
    }

    public void generateChestContents(StructureWorldAccess world, BlockPos pos) {
        this.generateChestContents(world, pos, world.getSeed() * pos.getX() + pos.getY() ^ pos.getZ());
    }

    public void generateChestContents(StructureWorldAccess world, BlockPos pos, long seed) {
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof LootableContainerBlockEntity) {
            LootableContainerBlockEntity lootContainer = (LootableContainerBlockEntity) entity;
            lootContainer.setLootTable(lootTable, seed);
        }
    }

    private static Identifier register(String path) {
        return register(ForestMod.path(path));
    }

    private static Identifier register(Identifier identifier) {
        if (TF_LOOT_TABLES.add(identifier)) {
            return identifier;
        } else {
            throw new IllegalStateException(identifier + " is already a registered built-in loot table");
        }
    }
}
