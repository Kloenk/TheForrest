package dev.kloenk.forest.data;

import dev.kloenk.forest.blocks.TFBlocks;
import dev.kloenk.forest.mixin.BlockStateModelGeneratorAccessor;
import dev.kloenk.forest.mixin.BlockStateModelGeneratorInvoker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.client.BlockStateDefinitionProvider;
import net.minecraft.data.client.model.BlockStateModelGenerator;
import net.minecraft.data.client.model.TexturedModel;

public class BlockStateGenerator {
    public final BlockStateModelGenerator generator;

    public BlockStateGenerator(BlockStateModelGenerator generator) {
        this.generator = generator;
    }

    public void registerSimpleState(Block block) {
        ((BlockStateModelGeneratorAccessor)generator).registerSimpleStateInvoker(block);
    }

    public BlockStateModelGenerator.LogTexturePool registerLog(Block block) {
        return ((BlockStateModelGeneratorAccessor)generator).registerLogInvoker(block);
    }

    public void registerSingleton(Block block, TexturedModel.Factory modelFactory) {
        ((BlockStateModelGeneratorAccessor)generator).registerSingletonInvoker(block, modelFactory);
    }

    public void register() {
        // TODO: block family and filter over simple blocks
        registerLogs();
        registerLeaves();
        registerGiantBlocks();

        registerSimpleSimple();
    }

    private void registerLogs() {
        this.registerLog(TFBlocks.OAK_LOG).log(TFBlocks.OAK_LOG).wood(TFBlocks.OAK_WOOD);
        this.registerLog(TFBlocks.OAK_LOG_STRIPPED).log(TFBlocks.OAK_LOG_STRIPPED).wood(TFBlocks.OAK_WOOD_STRIPPED);
        this.registerLog(TFBlocks.CANOPY_LOG).log(TFBlocks.CANOPY_LOG).wood(TFBlocks.CANOPY_WOOD);
        this.registerLog(TFBlocks.CANOPY_LOG_STRIPPED).log(TFBlocks.CANOPY_LOG_STRIPPED).wood(TFBlocks.CANOPY_WOOD_STRIPPED);
        this.registerLog(TFBlocks.DARK_LOG).log(TFBlocks.DARK_LOG).wood(TFBlocks.DARK_WOOD);
        this.registerLog(TFBlocks.DARK_LOG_STRIPPED).log(TFBlocks.DARK_LOG_STRIPPED).wood(TFBlocks.DARK_WOOD_STRIPPED);
        this.registerLog(TFBlocks.MANGROVE_LOG).log(TFBlocks.MANGROVE_LOG).wood(TFBlocks.MANGROVE_WOOD);
        this.registerLog(TFBlocks.MANGROVE_LOG_STRIPPED).log(TFBlocks.MANGROVE_LOG_STRIPPED).wood(TFBlocks.MANGROVE_WOOD_STRIPPED);
    }

    private void registerLeaves() {
        this.registerSingleton(TFBlocks.OAK_LEAVES, TexturedModel.LEAVES);
        this.registerSingleton(TFBlocks.CANOPY_LEAVES, TexturedModel.LEAVES);
        this.registerSingleton(TFBlocks.MANGROVE_LEAVES, TexturedModel.LEAVES);
        this.registerSingleton(TFBlocks.DARK_LEAVES, TexturedModel.LEAVES);
        this.registerSingleton(TFBlocks.RAINBOW_LEAVES, TexturedModel.LEAVES);
    }

    private void registerGiantBlocks() {
        this.registerSimpleState(TFBlocks.GIANT_COBBLESTONE_BLOCK);
        this.registerSimpleState(TFBlocks.GIANT_OBSIDIAN_BLOCK);

        // TODO: this should have states and a wood alterantive
        this.registerSimpleState(TFBlocks.GIANT_LOG_BLOCK);

        this.registerSingleton(TFBlocks.GIANT_LEAVES_BLOCK, TexturedModel.LEAVES);
    }

    @Deprecated
    private void registerSimpleSimple() {
        this.registerSimpleState(TFBlocks.MAZE_STONE);
        this.registerSimpleState(TFBlocks.MAZE_STONE_BORDER);
        this.registerSimpleState(TFBlocks.MAZE_STONE_BRICK);
        this.registerSimpleState(TFBlocks.MAZE_STONE_CHISELED);
        this.registerSimpleState(TFBlocks.MAZE_STONE_CRACKED);
        this.registerSimpleState(TFBlocks.MAZE_STONE_DECORATIVE);
        this.registerSimpleState(TFBlocks.MAZE_STONE_MOSAIC);
        this.registerSimpleState(TFBlocks.MAZE_STONE_MOSSY);

        this.registerSimpleState(TFBlocks.HEDGE);

        this.registerSimpleState(TFBlocks.BOSS_SPAWNER_NAGA);
        this.registerSimpleState(TFBlocks.BOSS_SPAWNER_LICH);
        this.registerSimpleState(TFBlocks.BOSS_SPAWNER_HYDRA);
        this.registerSimpleState(TFBlocks.BOSS_SPAWNER_UR_GHAST);
        this.registerSimpleState(TFBlocks.BOSS_SPAWNER_KNIGHT_PHANTOM);
        this.registerSimpleState(TFBlocks.BOSS_SPAWNER_SNOW_QUEEN);
        this.registerSimpleState(TFBlocks.BOSS_SPAWNER_MINOSHROOM);
        this.registerSimpleState(TFBlocks.BOSS_SPAWNER_ALHPA_YETI);
        this.registerSimpleState(TFBlocks.BOSS_SPAWNER_FINAL_BOSS);

        this.registerSimpleState(TFBlocks.FIREFLY_JAR);
        this.registerSimpleState(TFBlocks.CICADA_JAR);

        this.registerSimpleState(TFBlocks.MOSS_PATCH);
        this.registerSimpleState(TFBlocks.CLOVER_PATCH);
        this.registerSimpleState(TFBlocks.MAYAPPLE);
        this.registerSimpleState(TFBlocks.FIDDLEHEAD);
        this.registerSimpleState(TFBlocks.MUSHGLOOM);
        this.registerSimpleState(TFBlocks.TORCHBERRY_PLANT);
        this.registerSimpleState(TFBlocks.ROOT_STRAND);
        this.registerSimpleState(TFBlocks.FALLEN_LEAVES);

        this.registerSimpleState(TFBlocks.ROOT);
        this.registerSimpleState(TFBlocks.LIVEROOT);

        this.registerSimpleState(TFBlocks.TROLLVIDR);
        this.registerSimpleState(TFBlocks.TROLLBER);
        this.registerSimpleState(TFBlocks.TROLLBER_UNRIPE);

        // FIXME: All this is not a simple block!!!
        this.registerSimpleState(TFBlocks.FIRE_JET);
        this.registerSimpleState(TFBlocks.FIRE_JET_ENCASED);
        this.registerSimpleState(TFBlocks.FOREST_PORTAL);
        this.registerSimpleState(TFBlocks.THORNS_BROWN);
        this.registerSimpleState(TFBlocks.THORNS_GREEN);
        this.registerSimpleState(TFBlocks.THORNS_LEAVES);
        this.registerSimpleState(TFBlocks.THORNS_ROSE);
        this.registerSimpleState(TFBlocks.VANISHING_BLOCK_WOOD);
        this.registerSimpleState(TFBlocks.REAPPEARING_BLOCK_WOOD);
    }
}
