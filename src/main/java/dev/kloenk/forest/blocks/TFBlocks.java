package dev.kloenk.forest.blocks;

import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.entities.boss.BossVariant;
import dev.kloenk.forest.init.TFContent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;


public class TFBlocks {
    // LOGS
    public static final Block OAK_LOG = new TFLogBlock(logProperties(MapColor.OAK_TAN, MapColor.SPRUCE_BROWN).strength(2f).sounds(BlockSoundGroup.WOOD));
    public static final Block CANOPY_LOG = new TFLogBlock(logProperties(MapColor.SPRUCE_BROWN, MapColor.BROWN).strength(2f).sounds(BlockSoundGroup.WOOD));
    public static final Block MANGROVE_LOG = new TFLogBlock(logProperties(MapColor.DIRT_BROWN, MapColor.SPRUCE_BROWN).strength(2f).sounds(BlockSoundGroup.WOOD));
    public static final Block DARK_LOG = new TFLogBlock(logProperties(MapColor.BROWN, MapColor.STONE_GRAY).strength(2f).sounds(BlockSoundGroup.WOOD));
    public static final Block OAK_LOG_STRIPPED = new TFLogBlock(logProperties(MapColor.OAK_TAN).strength(2f).sounds(BlockSoundGroup.WOOD));
    public static final Block CANOPY_LOG_STRIPPED = new TFLogBlock(logProperties(MapColor.SPRUCE_BROWN).strength(2f).sounds(BlockSoundGroup.WOOD));
    public static final Block MANGROVE_LOG_STRIPPED = new TFLogBlock(logProperties(MapColor.DIRT_BROWN).strength(2f).sounds(BlockSoundGroup.WOOD));
    public static final Block DARK_LOG_STRIPPED = new TFLogBlock(FabricBlockSettings.of(Material.WOOD, MapColor.SPRUCE_BROWN).strength(2f).sounds(BlockSoundGroup.WOOD));
    public static final Block OAK_WOOD = new TFLogBlock(logProperties(MapColor.SPRUCE_BROWN).strength(2f).sounds(BlockSoundGroup.WOOD));
    public static final Block CANOPY_WOOD = new TFLogBlock(logProperties(MapColor.BROWN).strength(2f).sounds(BlockSoundGroup.WOOD));
    public static final Block MANGROVE_WOOD = new TFLogBlock(logProperties(MapColor.SPRUCE_BROWN).strength(2f).sounds(BlockSoundGroup.WOOD));
    public static final Block DARK_WOOD = new TFLogBlock(logProperties(MapColor.STONE_GRAY).strength(2f).sounds(BlockSoundGroup.WOOD));
    public static final Block OAK_WOOD_STRIPPED = new TFLogBlock(logProperties(MapColor.OAK_TAN).strength(2f).sounds(BlockSoundGroup.WOOD));
    public static final Block CANOPY_WOOD_STRIPPED = new TFLogBlock(logProperties(MapColor.SPRUCE_BROWN).strength(2f).sounds(BlockSoundGroup.WOOD));
    public static final Block MANGROVE_WOOD_STRIPPED = new TFLogBlock(logProperties(MapColor.DIRT_BROWN).strength(2f).sounds(BlockSoundGroup.WOOD));
    public static final Block DARK_WOOD_STRIPPED = new TFLogBlock(logProperties(MapColor.ORANGE).strength(2f).sounds(BlockSoundGroup.WOOD));
    // Leaves
    public static final Block OAK_LEAVES = new TFLeavesBlock(FabricBlockSettings.of(Material.LEAVES).strength(0.2f).ticksRandomly().nonOpaque().sounds(BlockSoundGroup.GRASS));
    public static final Block CANOPY_LEAVES = new TFLeavesBlock(FabricBlockSettings.of(Material.LEAVES).strength(0.2f).ticksRandomly().nonOpaque().sounds(BlockSoundGroup.GRASS));
    public static final Block MANGROVE_LEAVES = new TFLeavesBlock(FabricBlockSettings.of(Material.LEAVES).strength(0.2f).ticksRandomly().nonOpaque().sounds(BlockSoundGroup.GRASS));
    public static final Block DARK_LEAVES = new TFLeavesBlock(FabricBlockSettings.of(Material.LEAVES).strength(0.2f).ticksRandomly().nonOpaque().sounds(BlockSoundGroup.GRASS));
    public static final Block RAINBOW_LEAVES = new TFLeavesBlock(FabricBlockSettings.of(Material.LEAVES).strength(0.2f).ticksRandomly().nonOpaque().sounds(BlockSoundGroup.GRASS));

    // Portal
    public static final Block FOREST_PORTAL = new TFPortalBlock(FabricBlockSettings.of(Material.PORTAL).strength(-1f).sounds(BlockSoundGroup.GLASS).lightLevel(11).noCollision().nonOpaque().dropsNothing());

    // MazeStone
    public static final Block MAZE_STONE = new MazeStoneBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(100f, 5f).sounds(BlockSoundGroup.STONE));
    public static final Block MAZE_STONE_BRICK = new MazeStoneBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(100f, 5f).sounds(BlockSoundGroup.STONE));
    public static final Block MAZE_STONE_CHISELED = new MazeStoneBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(100f, 5f).sounds(BlockSoundGroup.STONE));
    public static final Block MAZE_STONE_DECORATIVE = new MazeStoneBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(100f, 5f).sounds(BlockSoundGroup.STONE));
    public static final Block MAZE_STONE_CRACKED = new MazeStoneBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(100f, 5f).sounds(BlockSoundGroup.STONE));
    public static final Block MAZE_STONE_MOSSY = new MazeStoneBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(100f, 5f).sounds(BlockSoundGroup.STONE));
    public static final Block MAZE_STONE_MOSAIC = new MazeStoneBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(100f, 5f).sounds(BlockSoundGroup.STONE));
    public static final Block MAZE_STONE_BORDER = new MazeStoneBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(100f, 5f).sounds(BlockSoundGroup.STONE));

    // Hedge
    public static final Block HEDGE = new HedgeBlock(FabricBlockSettings.of(Material.CACTUS).strength(2f, 10f).sounds(BlockSoundGroup.GRASS));

    // BossSpawner
    public static final Block BOSS_SPAWNER_NAGA = new BossSpawner(FabricBlockSettings.of(Material.STONE).strength(-1f).nonOpaque().dropsNothing(), BossVariant.NAGA);
    public static final Block BOSS_SPAWNER_LICH = new BossSpawner(FabricBlockSettings.of(Material.STONE).strength(-1f).nonOpaque().dropsNothing(), BossVariant.LICH);
    public static final Block BOSS_SPAWNER_HYDRA = new BossSpawner(FabricBlockSettings.of(Material.STONE).strength(-1f).nonOpaque().dropsNothing(), BossVariant.HYDRA);
    public static final Block BOSS_SPAWNER_UR_GHAST = new BossSpawner(FabricBlockSettings.of(Material.STONE).strength(-1f).nonOpaque().dropsNothing(), BossVariant.UR_GHAST);
    public static final Block BOSS_SPAWNER_KNIGHT_PHANTOM = new BossSpawner(FabricBlockSettings.of(Material.STONE).strength(-1f).nonOpaque().dropsNothing(), BossVariant.KNIGHT_PHANTOM);
    public static final Block BOSS_SPAWNER_SNOW_QUEEN = new BossSpawner(FabricBlockSettings.of(Material.STONE).strength(-1f).nonOpaque().dropsNothing(), BossVariant.SNOW_QUEEN);
    public static final Block BOSS_SPAWNER_MINOSHROOM = new BossSpawner(FabricBlockSettings.of(Material.STONE).strength(-1f).nonOpaque().dropsNothing(), BossVariant.MINOSHROOM);
    public static final Block BOSS_SPAWNER_ALHPA_YETI = new BossSpawner(FabricBlockSettings.of(Material.STONE).strength(-1f).nonOpaque().dropsNothing(), BossVariant.ALPHA_YETI);
    public static final Block BOSS_SPAWNER_FINAL_BOSS = new BossSpawner(FabricBlockSettings.of(Material.STONE).strength(-1f).nonOpaque().dropsNothing(), BossVariant.FINAL_BOSS);

    // Jars
    public static final Block FIREFLY_JAR = new JarBlock(FabricBlockSettings.of(Material.GLASS).strength(0.3f, 0f).sounds(BlockSoundGroup.WOOD).lightLevel(15).nonOpaque());
    public static final Block CICADA_JAR = new JarBlock(FabricBlockSettings.of(Material.GLASS).strength(0.3f, 0f).sounds(BlockSoundGroup.WOOD).nonOpaque().ticksRandomly());

    // Plants
    public static final Block MOSS_PATCH = new TFPlantBlock(FabricBlockSettings.of(Material.PLANT).strength(0f).sounds(BlockSoundGroup.GRASS).nonOpaque().noCollision(), TFPlantBlock.PlantVariant.MOSSPATCH);
    public static final Block CLOVER_PATCH = new TFPlantBlock(FabricBlockSettings.of(Material.PLANT).strength(0f).sounds(BlockSoundGroup.GRASS).noCollision().nonOpaque(), TFPlantBlock.PlantVariant.CLOVERPATCH);
    public static final Block MAYAPPLE = new TFPlantBlock(FabricBlockSettings.of(Material.PLANT).strength(0f).sounds(BlockSoundGroup.GRASS).nonOpaque().noCollision(), TFPlantBlock.PlantVariant.MAYAPPLE);
    public static final Block FIDDLEHEAD = new TFPlantBlock(FabricBlockSettings.of(Material.PLANT).strength(0f).sounds(BlockSoundGroup.GRASS).nonOpaque().noCollision(), TFPlantBlock.PlantVariant.FIDDLEHEAD);
    public static final Block MUSHGLOOM = new TFPlantBlock(FabricBlockSettings.of(Material.PLANT).strength(0f).sounds(BlockSoundGroup.GRASS).noCollision().nonOpaque().lightLevel(3), TFPlantBlock.PlantVariant.MUSHGLOOM);
    public static final Block TORCHBERRY_PLANT = new TFPlantBlock(FabricBlockSettings.of(Material.PLANT).strength(0f).sounds(BlockSoundGroup.GRASS).noCollision().nonOpaque().lightLevel(8), TFPlantBlock.PlantVariant.TORCHBERRY);
    public static final Block ROOT_STRAND = new TFPlantBlock(FabricBlockSettings.of(Material.PLANT).strength(0f).sounds(BlockSoundGroup.GRASS).noCollision().nonOpaque(), TFPlantBlock.PlantVariant.ROOT_STRAND);
    public static final Block FALLEN_LEAVES = new TFPlantBlock(FabricBlockSettings.of(Material.REPLACEABLE_PLANT).strength(0f).sounds(BlockSoundGroup.GRASS).nonOpaque(), TFPlantBlock.PlantVariant.FALLEN_LEAVES);

    // Roots
    public static final Block ROOT = new Block(FabricBlockSettings.of(Material.WOOD).strength(2f).sounds(BlockSoundGroup.WOOD));
    public static final Block LIVEROOT = new Block(FabricBlockSettings.of(Material.WOOD).strength(2f).sounds(BlockSoundGroup.WOOD));

    // Crafting Blocks
    // TODO

    // FireJets
    // TODO: lightlevel
    public static final Block FIRE_JET = new FireJetBlock(FabricBlockSettings.of(Material.STONE, MapColor.PALE_GREEN).strength(1.5f, 0f).sounds(BlockSoundGroup.WOOD).ticksRandomly());
    public static final Block FIRE_JET_ENCASED = new EncasedFireJetBlock(FabricBlockSettings.of(Material.WOOD, MapColor.PALE_YELLOW).requiresTool().strength(1.5f, 0f).sounds(BlockSoundGroup.WOOD).lightLevel(15));

    public static final Block NAGA_STONE = new NagastoneBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(1.5f, 10f).sounds(BlockSoundGroup.STONE));
    public static final Block NAGA_STONE_HEAD = new TFHorizontalBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(1.5f, 10f).sounds(BlockSoundGroup.STONE));

    // Troll Roots
    public static final Block TROLLVIDR = new TrollRootBlock(FabricBlockSettings.of(Material.PLANT).sounds(BlockSoundGroup.GRASS).noCollision());
    // TODO: replace with new UnripeTorchClusterBlock(BlockBehaviour.Properties.of(Material.PLANT).sound(SoundType.GRASS).noCollission().randomTicks()));
    public static final Block TROLLBER_UNRIPE = new TrollRootBlock(FabricBlockSettings.of(Material.PLANT).sounds(BlockSoundGroup.GRASS).noCollision().ticksRandomly());
    public static final Block TROLLBER = new TrollRootBlock(FabricBlockSettings.of(Material.PLANT).sounds(BlockSoundGroup.GRASS).noCollision().luminance(15));

    public static final Block TROLLSTEIN = new TrollsteinBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2f, 15f).sounds(BlockSoundGroup.STONE));

    // Thorns
    public static final Block THORNS_BROWN = new ThornsBlock(FabricBlockSettings.of(Material.WOOD, MapColor.SPRUCE_BROWN).strength(50f, 20000f).sounds(BlockSoundGroup.WOOD).dropsNothing());
    public static final Block THORNS_GREEN = new ThornsBlock(FabricBlockSettings.of(Material.WOOD, MapColor.DARK_GREEN).strength(50f, 20000f).sounds(BlockSoundGroup.WOOD).dropsNothing());
    public static final Block THORNS_ROSE = new ThornRoseBlock(FabricBlockSettings.of(Material.WOOD, MapColor.STONE_GRAY).strength(0.1f, 0f).sounds(BlockSoundGroup.SAND).dropsNothing());
    public static final Block THORNS_LEAVES = new TFLeavesBlock(FabricBlockSettings.of(Material.LEAVES).strength(0.2f).ticksRandomly().nonOpaque().sounds(BlockSoundGroup.GRASS));

    // Vanishing
    public static final Block VANISHING_BLOCK_WOOD = new VanishingBlock(FabricBlockSettings.of(Material.WOOD, MapColor.YELLOW).strength(10, 35f).sounds(BlockSoundGroup.WOOD).lightLevel((state) -> state.get(VanishingBlock.ACTIVE) ? 4 : 0), true);
    public static final Block REAPPEARING_BLOCK_WOOD = new VanishingBlock(FabricBlockSettings.of(Material.WOOD, MapColor.YELLOW).strength(8, 35f).sounds(BlockSoundGroup.WOOD).lightLevel((state) -> state.get(VanishingBlock.ACTIVE) ? 4 : 0), false);

    // Giant Blocks
    public static final Block GIANT_COBBLESTONE_BLOCK = new GiantBlock((FabricBlockSettings.of(Material.STONE).strength(10, 15f).requiresTool().sounds(BlockSoundGroup.STONE)));
    public static final Block GIANT_LEAVES_BLOCK = new GiantBlock(FabricBlockSettings.of(Material.LEAVES, MapColor.GREEN).strength(10, 5f).sounds(BlockSoundGroup.AZALEA_LEAVES));
    public static final Block GIANT_LOG_BLOCK = new GiantBlock(FabricBlockSettings.of(Material.WOOD).strength(10, 10f).requiresTool().sounds(BlockSoundGroup.WOOD));
    public static final Block GIANT_OBSIDIAN_BLOCK = new GiantBlock((FabricBlockSettings.of(Material.STONE, MapColor.BLACK).strength(10, 35f).requiresTool().sounds(BlockSoundGroup.NETHER_BRICKS)));
    // Giant Miner

    public static void register() {
        // LOGS
        registerBlockAndItem("oak_log", OAK_LOG);
        registerBlockAndItem("canopy_log", CANOPY_LOG);
        registerBlockAndItem("mangrove_log", MANGROVE_LOG);
        registerBlockAndItem("dark_log", DARK_LOG);
        registerBlockAndItem("oak_log_stripped", OAK_LOG_STRIPPED);
        registerBlockAndItem("canopy_log_stripped", CANOPY_LOG_STRIPPED);
        registerBlockAndItem("mangrove_log_stripped", MANGROVE_LOG_STRIPPED);
        registerBlockAndItem("dark_log_stripped", DARK_LOG_STRIPPED);
        registerBlockAndItem("oak_wood", OAK_WOOD);
        registerBlockAndItem("canopy_wood", CANOPY_WOOD);
        registerBlockAndItem("mangrove_wood", MANGROVE_WOOD);
        registerBlockAndItem("dark_wood", DARK_WOOD);
        registerBlockAndItem("oak_wood_stripped", OAK_WOOD_STRIPPED);
        registerBlockAndItem("canopy_wood_stripped", CANOPY_WOOD_STRIPPED);
        registerBlockAndItem("mangrove_wood_stripped", MANGROVE_WOOD_STRIPPED);
        registerBlockAndItem("dark_wood_stripped", DARK_WOOD_STRIPPED);
        // Leaves
        registerBlockAndItem("oak_leaves", OAK_LEAVES);
        FlammableBlockRegistry.getDefaultInstance().add(OAK_LEAVES, 60, 30);
        registerBlockAndItem("canopy_leaves", CANOPY_LEAVES);
        FlammableBlockRegistry.getDefaultInstance().add(CANOPY_LEAVES, 60, 30);
        registerBlockAndItem("mangrove_leaves", MANGROVE_LEAVES);
        FlammableBlockRegistry.getDefaultInstance().add(MANGROVE_LEAVES, 60, 30);
        registerBlockAndItem("dark_leaves", DARK_LEAVES);
        FlammableBlockRegistry.getDefaultInstance().add(DARK_LEAVES, 60, 30);
        registerBlockAndItem("rainbow_leaves", RAINBOW_LEAVES);
        FlammableBlockRegistry.getDefaultInstance().add(RAINBOW_LEAVES, 60, 30);

        // Portal
        registerBlockAndItem("forest_portal", FOREST_PORTAL);

        // MazeStone
        registerBlockAndItem("maze_stone", MAZE_STONE);
        registerBlockAndItem("maze_stone_brick", MAZE_STONE_BRICK);
        registerBlockAndItem("maze_stone_chiseled", MAZE_STONE_CHISELED);
        registerBlockAndItem("maze_stone_decorative", MAZE_STONE_DECORATIVE);
        registerBlockAndItem("maze_stone_cracked", MAZE_STONE_CRACKED);
        registerBlockAndItem("maze_stone_mossy", MAZE_STONE_MOSSY);
        registerBlockAndItem("maze_stone_mosaic", MAZE_STONE_MOSAIC);
        registerBlockAndItem("maze_stone_border", MAZE_STONE_BORDER);

        // Hedge
        registerBlockAndItem("hedge", HEDGE);

        // BossSpawner
        registerBlockAndItem("boss_spawner_naga", BOSS_SPAWNER_NAGA);
        registerBlockAndItem("boss_spawner_lich", BOSS_SPAWNER_LICH);
        registerBlockAndItem("boss_spawner_hydra", BOSS_SPAWNER_HYDRA);
        registerBlockAndItem("boss_spawner_ur_ghast", BOSS_SPAWNER_UR_GHAST);
        registerBlockAndItem("boss_spawner_knight_phantom", BOSS_SPAWNER_KNIGHT_PHANTOM);
        registerBlockAndItem("boss_spawner_snow_queen", BOSS_SPAWNER_SNOW_QUEEN);
        registerBlockAndItem("boss_spawner_minoshroom", BOSS_SPAWNER_MINOSHROOM);
        registerBlockAndItem("boss_spawner_alpha_yeti", BOSS_SPAWNER_ALHPA_YETI);
        registerBlockAndItem("boss_spawner_final_boss", BOSS_SPAWNER_FINAL_BOSS);

        // Jars
        registerBlockAndItem("firefly_jar", FIREFLY_JAR);
        registerBlockAndItem("cicada_jar", CICADA_JAR);

        // Plants
        registerBlockAndItem("moss_patch", MOSS_PATCH);
        registerBlockAndItem("clover_patch", CLOVER_PATCH);
        registerBlockAndItem("mayapple", MAYAPPLE);
        registerBlockAndItem("fiddlehead", FIDDLEHEAD);
        registerBlockAndItem("mushgloom", MUSHGLOOM);
        registerBlockAndItem("torchberry_plant", TORCHBERRY_PLANT);
        registerBlockAndItem("root_strand", ROOT_STRAND);
        registerBlockAndItem("fallen_leaves", FALLEN_LEAVES);

        // Roots
        registerBlockAndItem("root", ROOT);
        registerBlockAndItem("livingroot", LIVEROOT);

        // Crafting Blocks
        // TODO

        // FireJets
        registerBlockAndItem("fire_jet", FIRE_JET);
        registerBlockAndItem("fire_jet_encased", FIRE_JET_ENCASED);

        // Naga
        registerBlockAndItem("naga_stone", NAGA_STONE);
        registerBlockAndItem("naga_stone_head", NAGA_STONE_HEAD);

        // Troll Roots
        registerBlockAndItem("trollvidr", TROLLVIDR);
        registerBlockAndItem("trollber_unripe", TROLLBER_UNRIPE);
        registerBlockAndItem("trollber", TROLLBER);

        registerBlockAndItem("trollstein", TROLLSTEIN);

        // Thorns
        registerBlockAndItem("thorns_brown", THORNS_BROWN);
        registerBlockAndItem("thorns_green", THORNS_GREEN);
        registerBlockAndItem("thorns_rose", THORNS_ROSE);
        registerBlockAndItem("thorns_leaves", THORNS_LEAVES);

        // Vanishing
        registerBlockAndItem("vanishing_block_wood", VANISHING_BLOCK_WOOD);
        registerBlockAndItem("reappearing_block_wood", REAPPEARING_BLOCK_WOOD);

        // Giant Blocks
        registerBlockAndItem("giant_cobblestone", GIANT_COBBLESTONE_BLOCK);
        registerBlockAndItem("giant_leaves", GIANT_LEAVES_BLOCK);
        registerBlockAndItem("giant_log", GIANT_LOG_BLOCK);
        registerBlockAndItem("giant_obsidian", GIANT_OBSIDIAN_BLOCK);
    }

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
    }

    private static void registerBlockAndItem(String path, Block block) {
        registerBlockAndItem(ForestMod.path(path), block, new FabricItemSettings().group(TFContent.ITEM_GROUP));
    }

    private static void registerBlockAndItem(String path, Block block, FabricItemSettings itemSettings) {
        registerBlockAndItem(ForestMod.path(path), block, itemSettings);
    }

    private static void registerBlockAndItem(Identifier identifier, Block block, FabricItemSettings itemSettings) {
        Registry.register(Registry.BLOCK, identifier, block);
        Registry.register(Registry.ITEM, identifier, new BlockItem(block, itemSettings));
    }

    private static AbstractBlock.Settings logProperties(MapColor color) {
        return FabricBlockSettings.of(Material.WOOD, color);
        //return logProperties(color, color);
    }

    private static AbstractBlock.Settings logProperties(MapColor top, MapColor side) {
        return FabricBlockSettings.of(Material.WOOD, (state) ->
                state.get(PillarBlock.AXIS) == Direction.Axis.Y ? top : side);
    }
}
