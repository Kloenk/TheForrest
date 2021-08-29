package dev.kloenk.forest.world.feature;

import com.google.common.collect.ImmutableMap;
import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.util.IntPair;
import dev.kloenk.forest.util.PlayerHelper;
import dev.kloenk.forest.world.TFGenerationSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MobSpawnerEntry;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public enum TFFeature {
    NOTHING(0, "no_feature", false) {{
        this.enableDecorations().disableStructure();
    }},
    ;

    // functions
    private static final Map<RegistryKey, TFFeature> BIOME_FEATURES = new ImmutableMap.Builder<RegistryKey, TFFeature>()
            //.put()
            .build();

    public final int size;
    public final String name;
    public final boolean centerBounds;
    public boolean areChunkDecorationsEnabled;
    public boolean isStructureEnabled;
    private final Identifier[] requiredAdvancements;
    public boolean hasProtectionAura;

    public TerraformType terraform;
    public boolean requiresTerraform() {
        return this.terraform != TerraformType.None;
    }

    private List<List<SpawnSettings.SpawnEntry>> spawnableMobList;
    private List<SpawnSettings.SpawnEntry> ambientCreatureList;
    private List<SpawnSettings.SpawnEntry> waterCreatureList;

    private long lastSpawnedHintMobTime;

    private static final String BOOK_AUTHOR = "A Forgotten Explorer";

    private static final TFFeature[] VALUES = values();

    private static final int maxSize = Arrays.stream(VALUES).mapToInt(v -> v.size).max().orElse(0);

    TFFeature(int size, String name, boolean featureGeneator, Identifier... requiredAdvancements) {
        this(size, name, featureGeneator, false, requiredAdvancements);
    }

    TFFeature(int size, String name, boolean featureGenerator, boolean centerBounds, Identifier... requiredAdvancements) {
        this.size = size;
        this.name = name;
        this.areChunkDecorationsEnabled = false;
        this.isStructureEnabled = true;
        this.terraform = TerraformType.None;
        this.spawnableMobList = new ArrayList<>();
        this.ambientCreatureList = new ArrayList<>();
        this.waterCreatureList = new ArrayList<>();
        this.hasProtectionAura = true;

        if (!name.equals("hydra_lair"))
            ambientCreatureList.add(new SpawnSettings.SpawnEntry(EntityType.BAT, 10, 8, 8));

        this.requiredAdvancements = requiredAdvancements;

        this.centerBounds = centerBounds;
    }

    public void init() {}

    public static int getCount() {
        return VALUES.length;
    }

    public static int getMaxSize() {
        return maxSize;
    }

    public static TFFeature getFeature(String name) {
        for (TFFeature feature: VALUES) {
            if (feature.name.equals(name))
                return feature;
        }
        return NOTHING;
    }

    public static TFFeature getFeature(Identifier name) {
        if (name.getNamespace().equalsIgnoreCase(ForestMod.ID))
            return getFeature(name.getPath());
        return NOTHING;
    }

    public static TFFeature getFeature(int id) {
        return id < VALUES.length ? VALUES[id] : NOTHING;
    }

    public static TFFeature getFeatureAt(int regionX, int regionZ, StructureWorldAccess world) {
        return generateFeature(regionX >> 4, regionZ >> 4, world);
    }

    public static boolean isInFeatureChunk(int regionX, int regionZ) {
        int chunkX = regionX >> 4;
        int chunkZ = regionZ >> 4;
        BlockPos cc = getNearestCenterXYZ(chunkX, chunkZ);

        return chunkX == (cc.getX() >> 4) && chunkZ == (cc.getZ() >> 4);
    }

    /**
    * Turns on biome-specific decorations like grass and trees near this feature.
    */
    public TFFeature enableDecorations() {
        this.areChunkDecorationsEnabled = true;
        return this;
    }

    /**
     * Tell the chunkgenerator that we don't have an associated structure.
     */
    public TFFeature disableStructure() {
        this.enableDecorations();
        this.isStructureEnabled = false;
        return this;
    }

    /**
     * Tell the chunkgenerator that we want the terrain changed nearby.
     */
    public TFFeature enableTerrainAlterations() {
        return enableTerrainAlternations(TerraformType.Flat);
    }

    public TFFeature enableTerrainAlternations(TerraformType type) {
        this.terraform = type;
        return this;
    }

    public TFFeature disableProtectionAura() {
        this.hasProtectionAura = false;
        return this;
    }

    /**
     * Add a monster to spawn list 0
     */
    public TFFeature addMonster(EntityType<? extends LivingEntity> monsterClass, int weight, int minGroup, int maxGroup) {
        this.addMonster(0, monsterClass, weight, minGroup, maxGroup);
        return this;
    }

    /**
     * Add a monster to a specific spawn list
     */
    public TFFeature addMonster(int listIndex, EntityType<? extends LivingEntity> monsterClass, int weight, int minGroup, int maxGroup) {
        List<SpawnSettings.SpawnEntry> monsterList;
        if (this.spawnableMobList.size() > listIndex) {
            monsterList = this.spawnableMobList.get(listIndex);
        } else {
            monsterList = new ArrayList<>();
            this.spawnableMobList.add(listIndex, monsterList);
        }

        monsterList.add(new SpawnSettings.SpawnEntry(monsterClass, weight, minGroup, maxGroup));
        return this;
    }

    /**
     * Add a water creature
     */
    public TFFeature addWaterCreature(EntityType<? extends LivingEntity> monsterClass, int weight, int minGroup, int maxGroup) {
        this.waterCreatureList.add(new SpawnSettings.SpawnEntry(monsterClass, weight, minGroup, maxGroup));
        return this;
    }

    /**
     * @return The type of feature directly at the specified Chunk coordinates
     */
    public static TFFeature getFeatureDirectlyAt(int chunkX, int chunkZ, StructureWorldAccess world) {
        if (isInFeatureChunk(chunkX << 4, chunkZ << 4)) {
            return getFeatureAt(chunkX << 4, chunkZ << 4, world);
        }
        return NOTHING;
    }

    /**
     * What feature would go in this chunk.  Called when we know there is a feature, but there is no cache data,
     * either generating this chunk for the first time, or using the magic map to forecast beyond the edge of the world.
     */
    public static TFFeature generateFeature(int chunkX, int chunkZ, StructureWorldAccess world) {
        // set the chunkX and chunkZ to the center of the biome
        chunkX = Math.round(chunkX / 16F) * 16;
        chunkZ = Math.round(chunkZ / 16F) * 16;

        // what biome is at the center of the chunk?
        Biome biomeAt = world.getBiome(new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8));
        return generateFeature(chunkX, chunkZ, biomeAt, world.getSeed());
    }

    public static TFFeature generateFeature(int chunkX, int chunkZ, Biome biome, long seed) {

        // set the chunkX and chunkZ to the center of the biome in case they arent already
        chunkX = Math.round(chunkX / 16F) * 16;
        chunkZ = Math.round(chunkZ / 16F) * 16;

        // does the biome have a feature?
        TFFeature biomeFeature = BIOME_FEATURES.get(biome);
        if(biomeFeature != null && biomeFeature.isStructureEnabled)
            return biomeFeature;

        int regionOffsetX = Math.abs((chunkX + 64 >> 4) % 8);
        int regionOffsetZ = Math.abs((chunkZ + 64 >> 4) % 8);

        /*
        // plant two lich towers near the center of each 2048x2048 map area
        if ((regionOffsetX == 4 && regionOffsetZ == 5) || (regionOffsetX == 4 && regionOffsetZ == 3)) {
            return LICH_TOWER;
        }

        // also two nagas
        if ((regionOffsetX == 5 && regionOffsetZ == 4) || (regionOffsetX == 3 && regionOffsetZ == 4)) {
            return NAGA_COURTYARD;
        }

        // get random value
        // okay, well that takes care of most special cases
        switch (new Random(seed + chunkX * 25117L + chunkZ * 151121L).nextInt(16)) {
            default:
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return SMALL_HILL;
            case 6:
            case 7:
            case 8:
                return MEDIUM_HILL;
            case 9:
                return LARGE_HILL;
            case 10:
            case 11:
                return HEDGE_MAZE;
            case 12:
            case 13:
                return NAGA_COURTYARD;
            case 14:
            case 15:
                return LICH_TOWER;
        }*/
        return NOTHING;
    }

    /**
     * Returns the feature nearest to the specified chunk coordinates.
     */
    public static TFFeature getNearestFeature(int cx, int cz, StructureWorldAccess world) {
        return getNearestFeature(cx, cz, world, null);
    }

    /**
     * Returns the feature nearest to the specified chunk coordinates.
     *
     * If a non-null {@code center} is provided and a valid feature is found,
     * it will be set to relative block coordinates indicating the center of
     * that feature relative to the current chunk block coordinate system.
     */
    public static TFFeature getNearestFeature(int cx, int cz, StructureWorldAccess world, @Nullable IntPair center) {

        int diam = maxSize * 2 + 1;
        TFFeature[] features = new TFFeature[diam * diam];

        for (int rad = 1; rad <= maxSize; rad++) {
            for (int x = -rad; x <= rad; x++) {
                for (int z = -rad; z <= rad; z++) {

                    int idx = (x + maxSize) * diam + (z + maxSize);
                    TFFeature directlyAt = features[idx];
                    if (directlyAt == null) {
                        features[idx] = directlyAt = getFeatureDirectlyAt(x + cx, z + cz, world);
                    }

                    if (directlyAt.size == rad) {
                        if (center != null) {
                            center.x = (x << 4) + 8;
                            center.z = (z << 4) + 8;
                        }
                        return directlyAt;
                    }
                }
            }
        }

        return NOTHING;
    }

    // [Vanilla Copy] from MapGenStructure#findNearestStructurePosBySpacing; changed 2nd param to be TFFeature instead of MapGenStructure
    //TODO: Second parameter doesn't exist in Structure.findNearest
    @Nullable
    public static BlockPos findNearestFeaturePosBySpacing(StructureWorldAccess worldIn, TFFeature feature, BlockPos blockPos, int p_191069_3_, int p_191069_4_, int p_191069_5_, boolean p_191069_6_, int p_191069_7_, boolean findUnexplored) {
        int i = blockPos.getX() >> 4;
        int j = blockPos.getZ() >> 4;
        int k = 0;

        for (Random random = new Random(); k <= p_191069_7_; ++k) {
            for (int l = -k; l <= k; ++l) {
                boolean flag = l == -k || l == k;

                for (int i1 = -k; i1 <= k; ++i1) {
                    boolean flag1 = i1 == -k || i1 == k;

                    if (flag || flag1) {
                        int j1 = i + p_191069_3_ * l;
                        int k1 = j + p_191069_3_ * i1;

                        if (j1 < 0) {
                            j1 -= p_191069_3_ - 1;
                        }

                        if (k1 < 0) {
                            k1 -= p_191069_3_ - 1;
                        }

                        int l1 = j1 / p_191069_3_;
                        int i2 = k1 / p_191069_3_;
//						Random random1 = worldIn.setRandomSeed(l1, i2, p_191069_5_);
                        Random random1 = new Random();
                        l1 = l1 * p_191069_3_;
                        i2 = i2 * p_191069_3_;

                        if (p_191069_6_) {
                            l1 = l1 + (random1.nextInt(p_191069_3_ - p_191069_4_) + random1.nextInt(p_191069_3_ - p_191069_4_)) / 2;
                            i2 = i2 + (random1.nextInt(p_191069_3_ - p_191069_4_) + random1.nextInt(p_191069_3_ - p_191069_4_)) / 2;
                        } else {
                            l1 = l1 + random1.nextInt(p_191069_3_ - p_191069_4_);
                            i2 = i2 + random1.nextInt(p_191069_3_ - p_191069_4_);
                        }

                        //MapGenBase.setupChunkSeed(worldIn.getSeed(), random, l1, i2);
                        random.nextInt();

                        // Check changed for TFFeature
                        if (getFeatureAt(l1 << 4, i2 << 4, worldIn) == feature) {
                            if (!findUnexplored || !worldIn.isChunkLoaded(l1, i2)) {
                                return new BlockPos((l1 << 4) + 8, 64, (i2 << 4) + 8);
                            }
                        } else if (k == 0) {
                            break;
                        }
                    }
                }

                if (k == 0) {
                    break;
                }
            }
        }

        return null;
    }

    /**
     * @return The feature in the chunk "region"
     */
    public static TFFeature getFeatureForRegion(int chunkX, int chunkZ, StructureWorldAccess world) {
        //just round to the nearest multiple of 16 chunks?
        int featureX = Math.round(chunkX / 16F) * 16;
        int featureZ = Math.round(chunkZ / 16F) * 16;

        return generateFeature(featureX, featureZ, world);
    }

    /**
     * @return The feature in the chunk "region"
     */
    public static TFFeature getFeatureForRegionPos(int posX, int posZ, StructureWorldAccess world) {
        return getFeatureForRegion(posX >> 4, posZ >> 4, world);
    }

    /**
     * Given some coordinates, return the center of the nearest feature.
     * <p>
     * At the moment, with how features are distributed, just get the closest multiple of 256 and add +8 in both directions.
     * <p>
     * Maybe in the future we'll have to actually search for a feature chunk nearby, but for now this will work.
     */
    public static BlockPos getNearestCenterXYZ(int chunkX, int chunkZ) {
        // generate random number for the whole biome area
        int regionX = (chunkX + 8) >> 4;
        int regionZ = (chunkZ + 8) >> 4;

        long seed = regionX * 3129871 ^ regionZ * 116129781L;
        seed = seed * seed * 42317861L + seed * 7L;

        int num0 = (int) (seed >> 12 & 3L);
        int num1 = (int) (seed >> 15 & 3L);
        int num2 = (int) (seed >> 18 & 3L);
        int num3 = (int) (seed >> 21 & 3L);

        // slightly randomize center of biome (+/- 3)
        int centerX = 8 + num0 - num1;
        int centerZ = 8 + num2 - num3;

        // centers are offset strangely depending on +/-
        int ccz;
        if (regionZ >= 0) {
            ccz = (regionZ * 16 + centerZ - 8) * 16 + 8;
        } else {
            ccz = (regionZ * 16 + (16 - centerZ) - 8) * 16 + 9;
        }

        int ccx;
        if (regionX >= 0) {
            ccx = (regionX * 16 + centerX - 8) * 16 + 8;
        } else {
            ccx = (regionX * 16 + (16 - centerX) - 8) * 16 + 9;
        }

        return new BlockPos(ccx, TFGenerationSettings.SEALEVEL, ccz);//  Math.abs(chunkX % 16) == centerX && Math.abs(chunkZ % 16) == centerZ; FIXME (set sea level hard)
    }

    public List<SpawnSettings.SpawnEntry> getCombinedMonsterSpawnableList() {
        List<SpawnSettings.SpawnEntry> list = new ArrayList<>();
        spawnableMobList.forEach(l -> {
            if(l != null)
                list.addAll(l);
        });
        return list;
    }

    public List<SpawnSettings.SpawnEntry> getCombinedCreatureSpawnableList() {
        List<SpawnSettings.SpawnEntry> list = new ArrayList<>();
        list.addAll(ambientCreatureList);
        list.addAll(waterCreatureList);
        return list;
    }

    /**
     * Returns a list of hostile monsters.  Are we ever going to need passive or water creatures?
     */
    public List<SpawnSettings.SpawnEntry> getSpawnableList(SpawnGroup creatureType) {
        switch (creatureType) {
            case MONSTER:
                return this.getSpawnableMobList(0);
            case AMBIENT:
                return this.ambientCreatureList;
            case WATER_CREATURE:
                return this.waterCreatureList;
            default:
                return new ArrayList<>();
        }
    }

    /**
     * Returns a list of hostile monsters in the specified indexed category
     */
    public List<SpawnSettings.SpawnEntry> getSpawnableMobList(int index) {
        if (index >= 0 && index < this.spawnableMobList.size()) {
            return this.spawnableMobList.get(index);
        }
        return new ArrayList<>();
    }

    public boolean doesPlayerHaveRequiredAdvancements(PlayerEntity player) {
        return PlayerHelper.doesPlayerHaveRequiredAdvancements(player, requiredAdvancements);
    }

    /**
     * Try to spawn a hint monster near the specified player
     */
    public void trySpawnHintMonster(World world, PlayerEntity player) {
        this.trySpawnHintMonster(world, player, player.getBlockPos());
    }

    /**
     * Try several times to spawn a hint monster
     */
    public void trySpawnHintMonster(World world, PlayerEntity player, BlockPos pos) {
        // check if the timer is valid
        long currentTime = world.getTime();

        // if someone set the time backwards, fix the spawn timer
        if (currentTime < this.lastSpawnedHintMobTime) {
            this.lastSpawnedHintMobTime = 0;
        }

        if (currentTime - this.lastSpawnedHintMobTime > 1200) {
            // okay, time is good, try several times to spawn one
            for (int i = 0; i < 20; i++) {
                if (didSpawnHintMonster(world, player, pos)) {
                    this.lastSpawnedHintMobTime = currentTime;
                    break;
                }
            }
        }
    }

    /**
     * Try once to spawn a hint monster near the player.  Return true if we did.
     * <p>
     * We could change up the monster depending on what feature this is, but we currently are not doing that
     */
    private boolean didSpawnHintMonster(World world, PlayerEntity player, BlockPos pos) {
        // find a target point
        int dx = world.random.nextInt(16) - world.random.nextInt(16);
        int dy = world.random.nextInt( 4) - world.random.nextInt( 4);
        int dz = world.random.nextInt(16) - world.random.nextInt(16);

        // make our hint monster
        // FIXME: hint
        /*KoboldEntity hinty = new KoboldEntity(TFEntities.kobold, world);
        hinty.moveTo(pos.offset(dx, dy, dz), 0f, 0f);

        // check if the bounding box is clear
        if (hinty.checkSpawnObstruction(world) && hinty.getSensing().hasLineOfSight(player)) {

            // add items and hint book
            ItemStack book = this.createHintBook();

            hinty.setItemSlot(EquipmentSlot.MAINHAND, book);
            hinty.setDropChance(EquipmentSlot.MAINHAND, 1.0F);
            //hinty.setDropItemsWhenDead(true);

            world.addFreshEntity(hinty);
            return true;
        }*/

        return false;
    }

    /**
     * Create a hint book for the specified feature.  Only features with block protection will need this.
     */
    public ItemStack createHintBook() {
        ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
        this.addBookInformation(book, new NbtList());
        return book;
    }

    protected void addBookInformation(ItemStack book, NbtList bookPages) {

        addTranslatedPages(bookPages, ForestMod.ID + ".book.unknown", 2);

        book.setSubNbt("pages", bookPages);
        book.setSubNbt("author", NbtString.of(BOOK_AUTHOR));
        book.setSubNbt("title", NbtString.of("Notes on the Unexplained"));
    }

    @Nullable
    public StructurePiece provideStructureStart(StructureManager structureManager, ChunkGenerator chunkGenerator, Random rand, int x, int y, int z) {
        return null;
    }

    public GenerationStep.Feature getDecorationStage() {
        return GenerationStep.Feature.SURFACE_STRUCTURES;
    }

    private static void addTranslatedPages(NbtList bookPages, String translationKey, int pageCount) {
        for (int i = 1; i <= pageCount; i++) {
            bookPages.add(NbtString.of(Text.Serializer.toJson(new TranslatableText(translationKey + "." + i))));
        }
    }

    public static StructurePieceType registerPiece(String name, StructurePieceType piece) {
        return Registry.register(Registry.STRUCTURE_PIECE, ForestMod.path(name.toLowerCase(Locale.ROOT)), piece);
    }

    public final BlockBox getComponentToAddBoundingBox(int x, int y, int z, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, @Nullable Direction dir) {
        if(centerBounds) {
            x += (maxX + minX) / 4;
            y += (maxY + minY) / 4;
            z += (maxZ + minZ) / 4;
        }
        switch (dir) {

            case SOUTH: // '\0'
            default:
                return new BlockBox(x + minX, y + minY, z + minZ, x + maxX + minX, y + maxY + minY, z + maxZ + minZ);

            case WEST: // '\001'
                return new BlockBox(x - maxZ + minZ, y + minY, z + minX, x + minZ, y + maxY + minY, z + maxX + minX);

            case NORTH: // '\002'
                return new BlockBox(x - maxX - minX, y + minY, z - maxZ - minZ, x - minX, y + maxY + minY, z - minZ);

            case EAST: // '\003'
                return new BlockBox(x + minZ, y + minY, z - maxX, x + maxZ + minZ, y + maxY + minY, z + minX);
        }
    }


    public enum TerraformType {
        None,
        Flat,
        Envelop,
    }
}
