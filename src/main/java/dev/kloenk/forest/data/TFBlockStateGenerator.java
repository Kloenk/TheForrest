package dev.kloenk.forest.data;

import dev.kloenk.forest.blocks.*;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.model.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class TFBlockStateGenerator extends TFBlockStateGeneratorHelper {
    public TFBlockStateGenerator(BlockStateModelGenerator generator) {
        super(generator);
    }


    public void register() {
        // TODO: block family and filter over simple blocks
        registerLogs();
        registerLeaves();
        registerSaplings();
        registerGiantBlocks();
        registerNagaStone();
        registerReapearingBlocks();
        registerFireJet();
        registerMazeStones();
        registerSpawners();
        registerThorns();

        registerCrosses();
        registerCubes();
        registerColumns();
        registerSimpleSimple();

        registerWithModelReferenceAndItem(TFBlocks.THORNS_LEAVES, Blocks.OAK_LEAVES);
    }

    @Deprecated
    private void registerSimpleSimple() {

        // TODO: Entities??
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

        this.registerSimpleState(TFBlocks.LIVEROOT);

        this.registerSimpleState(TFBlocks.TROLLBER);
        this.registerSimpleState(TFBlocks.TROLLBER_UNRIPE);

        // FIXME: All this is not a simple block!!!
        this.registerSimpleState(TFBlocks.FOREST_PORTAL);

        this.registerSimpleState(TFBlocks.TROLLSTEIN);
    }

    private void registerCubes() {
        this.registerSimpleCubeAll(TFBlocks.MAZE_STONE);
        this.registerSimpleCubeAll(TFBlocks.MAZE_STONE_BRICK);
        this.registerSimpleCubeAll(TFBlocks.MAZE_STONE_CRACKED);
        this.registerSimpleCubeAll(TFBlocks.MAZE_STONE_MOSSY);

        this.registerSimpleCubeAll(TFBlocks.HEDGE);

        this.registerSimpleCubeAll(TFBlocks.ROOT);
    }

    private void registerCrosses() {
        this.registerCrossBlock(TFBlocks.TROLLVIDR);
        this.registerCrossBlock(TFBlocks.THORNS_ROSE);
    }

    private void registerColumns() {
    }

    private void registerThorns() {
        registerThorn(TFBlocks.THORNS_GREEN);
        registerThorn(TFBlocks.THORNS_BROWN);
    }

    private void registerMazeStones() {
        registerMazeStoneBorder();
        registerMazeStoneChiseled();
        registerMazeStoneDecorative();
        registerMazeStoneMosaic();
    }

    private void registerMazeStoneBorder() {
        Texture texture = Texture.sideEnd(Texture.getId(TFBlocks.MAZE_STONE_BRICK), Texture.getId(TFBlocks.MAZE_STONE_BORDER));
        Identifier identifier = Models.CUBE_COLUMN.upload(TFBlocks.MAZE_STONE_BORDER, texture, this.getModelCollector());
        this.getBlockStateCollector().accept(createSingletonBlockState(TFBlocks.MAZE_STONE_BORDER, identifier));
    }

    private void registerMazeStoneChiseled() {
        Texture texture = Texture.sideEnd(Texture.getId(TFBlocks.MAZE_STONE_CHISELED), Texture.getId(TFBlocks.MAZE_STONE));
        Identifier identifier = Models.CUBE_COLUMN.upload(TFBlocks.MAZE_STONE_CHISELED, texture, this.getModelCollector());
        this.getBlockStateCollector().accept(createSingletonBlockState(TFBlocks.MAZE_STONE_CHISELED, identifier));
    }

    private void registerMazeStoneDecorative() {
        Texture texture = Texture.sideEnd(Texture.getId(TFBlocks.MAZE_STONE_DECORATIVE), Texture.getId(TFBlocks.MAZE_STONE));
        Identifier identifier = Models.CUBE_COLUMN.upload(TFBlocks.MAZE_STONE_DECORATIVE, texture, this.getModelCollector());
        this.getBlockStateCollector().accept(createSingletonBlockState(TFBlocks.MAZE_STONE_DECORATIVE, identifier));
    }

    private void registerMazeStoneMosaic() {
        Texture texture = Texture.sideEnd(Texture.getId(TFBlocks.MAZE_STONE_BRICK), Texture.getId(TFBlocks.MAZE_STONE_MOSAIC));
        Identifier identifier = Models.CUBE_COLUMN.upload(TFBlocks.MAZE_STONE_MOSAIC, texture, this.getModelCollector());
        this.getBlockStateCollector().accept(createSingletonBlockState(TFBlocks.MAZE_STONE_MOSAIC, identifier));
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

    protected void registerSaplings() {
        this.registerFlowerPotPlant(TFBlocks.CANOPY_SAPLING, TFBlocks.POTTED_CANOPY_SAPLING, TintType.NOT_TINTED);
    }

    public void registerSpawners() {
        this.registerSpawner(TFBlocks.BOSS_SPAWNER_NAGA);
        this.registerSpawner(TFBlocks.BOSS_SPAWNER_LICH);
        this.registerSpawner(TFBlocks.BOSS_SPAWNER_HYDRA);
        this.registerSpawner(TFBlocks.BOSS_SPAWNER_UR_GHAST);
        this.registerSpawner(TFBlocks.BOSS_SPAWNER_KNIGHT_PHANTOM);
        this.registerSpawner(TFBlocks.BOSS_SPAWNER_SNOW_QUEEN);
        this.registerSpawner(TFBlocks.BOSS_SPAWNER_MINOSHROOM);
        this.registerSpawner(TFBlocks.BOSS_SPAWNER_ALHPA_YETI);
        this.registerSpawner(TFBlocks.BOSS_SPAWNER_FINAL_BOSS);
    }

    private void registerGiantBlocks() {
        this.registerStateWithModelReference(TFBlocks.GIANT_COBBLESTONE_BLOCK, Blocks.COBBLESTONE);
        this.registerStateWithModelReference(TFBlocks.GIANT_OBSIDIAN_BLOCK, Blocks.OBSIDIAN);

        this.registerParentedItemModel(TFBlocks.GIANT_COBBLESTONE_BLOCK.asItem(), new Identifier("minecraft", "block/cobblestone"));
        this.registerParentedItemModel(TFBlocks.GIANT_OBSIDIAN_BLOCK.asItem(), new Identifier("minecraft", "block/obsidian"));

        // TODO: this should have states and a wood alterantive
        //this.registerSimpleState(TFBlocks.GIANT_LOG_BLOCK);
        this.registerStateWithModelReference(TFBlocks.GIANT_LOG_BLOCK, Blocks.OAK_LOG);
        this.registerParentedItemModel(TFBlocks.GIANT_LOG_BLOCK.asItem(), new Identifier("minecraft", "block/oak_log"));

        this.registerStateWithModelReference(TFBlocks.GIANT_LEAVES_BLOCK, Blocks.OAK_LEAVES);
        this.registerParentedItemModel(TFBlocks.GIANT_LEAVES_BLOCK.asItem(), new Identifier("minecraft", "block/oak_leaves"));
    }

    private void registerReapearingBlocks() {
        // FIXME: better blockstates for VanishingBlocks
        this.registerSimpleState(TFBlocks.VANISHING_BLOCK_WOOD);
        this.registerSimpleState(TFBlocks.REAPPEARING_BLOCK_WOOD);
        /*this.getBlockStateCollector()
                .accept(
                        VariantsBlockStateSupplier.create(
                                TFBlocks.REAPPEARING_BLOCK_WOOD,
                                BlockStateVariant.create().put(
                                        VariantSettings.MODEL,
                                        ModelIds.getBlockModelId(TFBlocks.REAPPEARING_BLOCK_WOOD)
                                )
                        ).coordinate(
                                BlockStateVariantMap
                                        .create(VanishingBlock.)
                        )
                );*/
    }

    private void registerFireJet() {
        this.registerSimpleState(TFBlocks.FIRE_JET);

        this.getBlockStateCollector()
                .accept(
                        VariantsBlockStateSupplier.create(
                                TFBlocks.FIRE_JET_ENCASED,
                                BlockStateVariant.create().put(
                                        VariantSettings.MODEL,
                                        ModelIds.getBlockModelId(TFBlocks.FIRE_JET_ENCASED)
                                )
                        ).coordinate(
                                BlockStateVariantMap
                                        .create(FireJetBlock.STATE)
                                        .register(
                                                FireJetBlock.FireJetVariant.IDLE,
                                                BlockStateVariant.create()
                                        )
                                        .register(
                                                FireJetBlock.FireJetVariant.POPPING,
                                                BlockStateVariant.create().put(
                                                        VariantSettings.MODEL,
                                                        ModelIds.getBlockSubModelId(TFBlocks.FIRE_JET_ENCASED, "_on")
                                                )
                                        )
                                        .register(
                                                FireJetBlock.FireJetVariant.FLAME,
                                                BlockStateVariant.create().put(
                                                        VariantSettings.MODEL,
                                                        ModelIds.getBlockSubModelId(TFBlocks.FIRE_JET_ENCASED, "_on")
                                                )
                                        )
                        )
                );
    }

    private void registerNagaStone() {
        this.getBlockStateCollector()
                .accept(
                        VariantsBlockStateSupplier.create(
                                TFBlocks.NAGA_STONE,
                                BlockStateVariant.create().put(
                                        VariantSettings.MODEL,
                                        ModelIds.getBlockModelId(TFBlocks.NAGA_STONE)
                                )
                        ).coordinate(
                                BlockStateVariantMap
                                        .create(NagastoneBlock.VARIANT)
                                        .register(
                                                NagastoneBlock.Variant.SOLID,
                                                BlockStateVariant.create()
                                        )
                                        .register(
                                                NagastoneBlock.Variant.NORTH_DOWN,
                                                BlockStateVariant.create()
                                                        .put(
                                                                VariantSettings.MODEL,
                                                                ModelIds.getBlockSubModelId(TFBlocks.NAGA_STONE, "_down")
                                                        ).put(
                                                                VariantSettings.Y,
                                                                VariantSettings.Rotation.R270
                                                        )
                                        )
                                        .register(
                                                NagastoneBlock.Variant.SOUTH_DOWN,
                                                BlockStateVariant.create()
                                                        .put(
                                                                VariantSettings.MODEL,
                                                                ModelIds.getBlockSubModelId(TFBlocks.NAGA_STONE, "_down")
                                                        )
                                                        .put(
                                                                VariantSettings.Y,
                                                                VariantSettings.Rotation.R90
                                                        )
                                        )
                                        .register(
                                                NagastoneBlock.Variant.WEST_DOWN,
                                                BlockStateVariant.create()
                                                        .put(
                                                                VariantSettings.MODEL,
                                                                ModelIds.getBlockSubModelId(TFBlocks.NAGA_STONE, "_down")
                                                        )
                                                        .put(
                                                                VariantSettings.Y,
                                                                VariantSettings.Rotation.R180
                                                        )
                                        )
                                        .register(
                                                NagastoneBlock.Variant.EAST_DOWN,
                                                BlockStateVariant.create()
                                                        .put(
                                                                VariantSettings.MODEL,
                                                                ModelIds.getBlockSubModelId(TFBlocks.NAGA_STONE, "_down")
                                                        )
                                        )
                                        .register(
                                                NagastoneBlock.Variant.NORTH_UP,
                                                BlockStateVariant.create()
                                                        .put(
                                                                VariantSettings.MODEL,
                                                                ModelIds.getBlockSubModelId(TFBlocks.NAGA_STONE, "_up")
                                                        )
                                                        .put(
                                                                VariantSettings.Y,
                                                                VariantSettings.Rotation.R270
                                                        )
                                        )
                                        .register(
                                                NagastoneBlock.Variant.SOUTH_UP,
                                                BlockStateVariant.create()
                                                        .put(
                                                                VariantSettings.MODEL,
                                                                ModelIds.getBlockSubModelId(TFBlocks.NAGA_STONE, "_up")
                                                        )
                                                        .put(
                                                                VariantSettings.Y,
                                                                VariantSettings.Rotation.R90
                                                        )
                                        )
                                        .register(
                                                NagastoneBlock.Variant.WEST_UP,
                                                BlockStateVariant.create()
                                                        .put(
                                                                VariantSettings.MODEL,
                                                                ModelIds.getBlockSubModelId(TFBlocks.NAGA_STONE, "_up")
                                                        )
                                                        .put(
                                                                VariantSettings.Y,
                                                                VariantSettings.Rotation.R180
                                                        )
                                        )
                                        .register(
                                                NagastoneBlock.Variant.EAST_UP,
                                                BlockStateVariant.create()
                                                        .put(
                                                                VariantSettings.MODEL,
                                                                ModelIds.getBlockSubModelId(TFBlocks.NAGA_STONE, "_up")
                                                        )
                                        )
                                        .register(
                                                NagastoneBlock.Variant.AXIS_X,
                                                BlockStateVariant.create()
                                                        .put(
                                                                VariantSettings.MODEL,
                                                                ModelIds.getBlockSubModelId(TFBlocks.NAGA_STONE, "_herizontal")
                                                        )
                                        )
                                        .register(
                                                NagastoneBlock.Variant.AXIS_Y,
                                                BlockStateVariant.create()
                                                        .put(
                                                                VariantSettings.MODEL,
                                                                ModelIds.getBlockSubModelId(TFBlocks.NAGA_STONE, "_herizontal")
                                                        )
                                                        .put(
                                                                VariantSettings.Y,
                                                                VariantSettings.Rotation.R90
                                                        )
                                        )
                                        .register(
                                                NagastoneBlock.Variant.AXIS_Z,
                                                BlockStateVariant.create()
                                                        .put(
                                                                VariantSettings.MODEL,
                                                                ModelIds.getBlockSubModelId(TFBlocks.NAGA_STONE, "vertical")
                                                        )
                                        )
                        )
                );

        this.getBlockStateCollector()
                .accept(
                        VariantsBlockStateSupplier.create(
                                TFBlocks.NAGA_STONE_HEAD,
                                BlockStateVariant.create().put(
                                        VariantSettings.MODEL,
                                        ModelIds.getBlockModelId(TFBlocks.NAGA_STONE_HEAD)
                                )
                        ).coordinate(
                                BlockStateVariantMap
                                        .create(Properties.HORIZONTAL_FACING)
                                        .register(
                                                Direction.NORTH,
                                                BlockStateVariant.create()
                                        )
                                        .register(
                                                Direction.SOUTH,
                                                BlockStateVariant.create()
                                                        .put(
                                                                VariantSettings.Y,
                                                                VariantSettings.Rotation.R180
                                                        )
                                        )
                                        .register(
                                                Direction.WEST,
                                                BlockStateVariant.create()
                                                        .put(
                                                                VariantSettings.Y,
                                                                VariantSettings.Rotation.R270
                                                        )
                                        )
                                        .register(
                                                Direction.EAST,
                                                BlockStateVariant.create()
                                                        .put(
                                                                VariantSettings.Y,
                                                                VariantSettings.Rotation.R90
                                                        )
                                        )
                        )
                );

    }

}
