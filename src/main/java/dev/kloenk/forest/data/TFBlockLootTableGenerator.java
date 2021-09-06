package dev.kloenk.forest.data;

import com.google.common.collect.ImmutableSet;
import dev.kloenk.forest.blocks.TFBlocks;
import dev.kloenk.forest.mixin.BlockLootTableGeneratorAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.ExplosionDecayLootFunction;
import net.minecraft.loot.function.LootFunctionConsumingBuilder;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.ItemPredicate;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class TFBlockLootTableGenerator {
    public final BlockLootTableGenerator generator;

    public TFBlockLootTableGenerator(BlockLootTableGenerator generator) {
        this.generator = generator;
    }

    public void addDrop(Block block) {
        this.generator.addDrop(block);
    }

    public void addDrop(Block block, Function<Block, LootTable.Builder> lootTableFunction) {
        ((BlockLootTableGeneratorAccessor)this.generator).addDropInvoker(block, lootTableFunction.apply(block));
    }

    public void register() {
        this.addDrop(TFBlocks.OAK_LOG);
        this.addDrop(TFBlocks.CANOPY_LOG);
        this.addDrop(TFBlocks.MANGROVE_LOG);
        this.addDrop(TFBlocks.DARK_LOG);
        this.addDrop(TFBlocks.OAK_LOG_STRIPPED);
        this.addDrop(TFBlocks.CANOPY_LOG_STRIPPED);
        this.addDrop(TFBlocks.MANGROVE_LOG_STRIPPED);
        this.addDrop(TFBlocks.DARK_LOG_STRIPPED);
        this.addDrop(TFBlocks.OAK_WOOD);
        this.addDrop(TFBlocks.CANOPY_WOOD);
        this.addDrop(TFBlocks.MANGROVE_WOOD);
        this.addDrop(TFBlocks.DARK_WOOD);
        this.addDrop(TFBlocks.OAK_WOOD_STRIPPED);
        this.addDrop(TFBlocks.CANOPY_WOOD_STRIPPED);
        this.addDrop(TFBlocks.MANGROVE_WOOD_STRIPPED);
        this.addDrop(TFBlocks.DARK_WOOD_STRIPPED);

        this.addDrop(TFBlocks.OAK_LEAVES);
        this.addDrop(TFBlocks.CANOPY_LEAVES, (blockx) -> {
            return leavesDrop(blockx, TFBlocks.CANOPY_SAPLING, SAPLING_DROP_CHANCE);
        });
        this.addDrop(TFBlocks.MANGROVE_LEAVES);
        this.addDrop(TFBlocks.DARK_LEAVES);
        this.addDrop(TFBlocks.RAINBOW_LEAVES);

        this.addDrop(TFBlocks.CANOPY_SAPLING);
        this.addDrop(TFBlocks.POTTED_CANOPY_SAPLING);

        this.addDrop(TFBlocks.MAZE_STONE);
        this.addDrop(TFBlocks.MAZE_STONE_BRICK);
        this.addDrop(TFBlocks.MAZE_STONE_CHISELED);
        this.addDrop(TFBlocks.MAZE_STONE_CRACKED);
        this.addDrop(TFBlocks.MAZE_STONE_MOSSY);
        this.addDrop(TFBlocks.MAZE_STONE_MOSAIC);
        this.addDrop(TFBlocks.MAZE_STONE_DECORATIVE);
        this.addDrop(TFBlocks.MAZE_STONE_BORDER);

        this.addDrop(TFBlocks.HEDGE);

        this.addDrop(TFBlocks.FIREFLY_JAR);
        this.addDrop(TFBlocks.CICADA_JAR);

        this.addDrop(TFBlocks.MOSS_PATCH);
        this.addDrop(TFBlocks.CLOVER_PATCH);
        this.addDrop(TFBlocks.MAYAPPLE);
        this.addDrop(TFBlocks.FIDDLEHEAD);
        this.addDrop(TFBlocks.MUSHGLOOM);
        this.addDrop(TFBlocks.TORCHBERRY_PLANT);
        this.addDrop(TFBlocks.ROOT_STRAND);
        this.addDrop(TFBlocks.FALLEN_LEAVES);

        this.addDrop(TFBlocks.ROOT);
        this.addDrop(TFBlocks.LIVEROOT);

        this.addDrop(TFBlocks.FIRE_JET);
        this.addDrop(TFBlocks.FIRE_JET_ENCASED);

        this.addDrop(TFBlocks.TROLLVIDR);
        this.addDrop(TFBlocks.TROLLBER_UNRIPE);
        this.addDrop(TFBlocks.TROLLBER);

        this.addDrop(TFBlocks.TROLLSTEIN);

        this.addDrop(TFBlocks.NAGA_STONE);
        this.addDrop(TFBlocks.NAGA_STONE_HEAD);

        this.addDrop(TFBlocks.THORNS_LEAVES);

        this.addDrop(TFBlocks.VANISHING_BLOCK_WOOD);
        this.addDrop(TFBlocks.REAPPEARING_BLOCK_WOOD);

        this.addDrop(TFBlocks.GIANT_COBBLESTONE_BLOCK);
        this.addDrop(TFBlocks.GIANT_LEAVES_BLOCK);
        this.addDrop(TFBlocks.GIANT_LOG_BLOCK);
        this.addDrop(TFBlocks.GIANT_OBSIDIAN_BLOCK);
    }

    // Helper functions (Copied from vanila)
    protected static final LootCondition.Builder WITH_SILK_TOUCH = MatchToolLootCondition.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, NumberRange.IntRange.atLeast(1))));
    protected static final LootCondition.Builder WITHOUT_SILK_TOUCH = WITH_SILK_TOUCH.invert();
    protected static final LootCondition.Builder WITH_SHEARS = MatchToolLootCondition.builder(ItemPredicate.Builder.create().items(Items.SHEARS));
    protected static final LootCondition.Builder WITH_SILK_TOUCH_OR_SHEARS = WITH_SHEARS.or(WITH_SILK_TOUCH);
    protected static final LootCondition.Builder WITHOUT_SILK_TOUCH_NOR_SHEARS = WITH_SILK_TOUCH_OR_SHEARS.invert();
    protected static final Set<Blocks> EXPLOSION_IMMUNE = (Set)Stream.of(Blocks.DRAGON_EGG, Blocks.BEACON, Blocks.CONDUIT, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL, Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.CREEPER_HEAD, Blocks.DRAGON_HEAD, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX).map(ItemConvertible::asItem).collect(ImmutableSet.toImmutableSet());
    protected static final float[] SAPLING_DROP_CHANCE = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};


    protected static LootTable.Builder leavesDrop(Block leaves, Block drop, float... chance) {
        return dropsWithSilkTouchOrShears(leaves, ((LeafEntry.Builder)addSurvivesExplosionCondition(leaves, ItemEntry.builder(drop))).conditionally(TableBonusLootCondition.builder(Enchantments.FORTUNE, chance))).pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).conditionally(WITHOUT_SILK_TOUCH_NOR_SHEARS).with(((LeafEntry.Builder)applyExplosionDecay(leaves, ItemEntry.builder(Items.STICK).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))))).conditionally(TableBonusLootCondition.builder(Enchantments.FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F))));
    }

    protected static LootTable.Builder dropsWithSilkTouchOrShears(Block drop, LootPoolEntry.Builder<?> child) {
        return drops(drop, WITH_SILK_TOUCH_OR_SHEARS, child);
    }

    protected static LootTable.Builder drops(Block drop, LootCondition.Builder conditionBuilder, LootPoolEntry.Builder<?> child) {
        return LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(((LeafEntry.Builder)ItemEntry.builder(drop).conditionally(conditionBuilder)).alternatively(child)));
    }

    protected static <T> T addSurvivesExplosionCondition(ItemConvertible drop, LootConditionConsumingBuilder<T> builder) {
        return !EXPLOSION_IMMUNE.contains(drop.asItem()) ? builder.conditionally(SurvivesExplosionLootCondition.builder()) : builder.getThis();
    }

    protected static <T> T applyExplosionDecay(ItemConvertible drop, LootFunctionConsumingBuilder<T> builder) {
        return !EXPLOSION_IMMUNE.contains(drop.asItem()) ? builder.apply(ExplosionDecayLootFunction.builder()) : builder.getThis();
    }
}
