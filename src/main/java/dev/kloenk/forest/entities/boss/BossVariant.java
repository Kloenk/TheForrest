package dev.kloenk.forest.entities.boss;

import dev.kloenk.forest.blocks.blockentities.TFBlockEntities;
import dev.kloenk.forest.blocks.blockentities.spawner.BossSpawnerBlockEntity;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.Supplier;

public enum BossVariant implements StringIdentifiable, SkullBlock.SkullType {
    NAGA          (TrophyType.GOLD    , null),
    LICH          (TrophyType.GOLD    , () -> TFBlockEntities.LICH_SPAWNER),
    HYDRA         (TrophyType.GOLD    , null),
    UR_GHAST      (TrophyType.GOLD    , () -> TFBlockEntities.Ur_GHAST_SPAWNER),
    KNIGHT_PHANTOM(TrophyType.IRON    , null),
    SNOW_QUEEN    (TrophyType.GOLD    , null),
    MINOSHROOM    (TrophyType.IRON    , null),
    ALPHA_YETI    (TrophyType.IRON    , null),
    QUEST_RAM     (TrophyType.IRONWOOD, null),
    FINAL_BOSS    (TrophyType.GOLD    , null);
    /*NAGA          (TrophyType.GOLD    , TFTileEntities.NAGA_SPAWNER::get),
    LICH          (TrophyType.GOLD    , TFTileEntities.LICH_SPAWNER::get),
    HYDRA         (TrophyType.GOLD    , TFTileEntities.HYDRA_SPAWNER::get),
    UR_GHAST      (TrophyType.GOLD    , TFTileEntities.UR_GHAST_SPAWNER::get),
    KNIGHT_PHANTOM(TrophyType.IRON    , TFTileEntities.KNIGHT_PHANTOM_SPAWNER::get),
    SNOW_QUEEN    (TrophyType.GOLD    , TFTileEntities.SNOW_QUEEN_SPAWNER::get),
    MINOSHROOM    (TrophyType.IRON    , TFTileEntities.MINOSHROOM_SPAWNER::get),
    ALPHA_YETI    (TrophyType.IRON    , TFTileEntities.ALPHA_YETI_SPAWNER::get),
    QUEST_RAM     (TrophyType.IRONWOOD, null),
    FINAL_BOSS    (TrophyType.GOLD    , TFTileEntities.FINAL_BOSS_SPAWNER::get);*/

    private final TrophyType trophyType;
    private final Supplier<BlockEntityType<? extends BossSpawnerBlockEntity<?>>> blockEntityTypeSupplier;

    private static final BossVariant[] VARIANTS = values();

    BossVariant(TrophyType trophyType, @Nullable Supplier<BlockEntityType<? extends BossSpawnerBlockEntity<?>>> blockEntityTypeSupplier) {
        this.trophyType = trophyType;
        this.blockEntityTypeSupplier = blockEntityTypeSupplier;
    }

    @Override
    public String asString() {
        return name().toLowerCase(Locale.ROOT);
    }

    public TrophyType getTrophyType() {
        return this.trophyType;
    }

    @Nullable
    public BlockEntityType<? extends BossSpawnerBlockEntity<?>> getType() {
        return blockEntityTypeSupplier != null ? blockEntityTypeSupplier.get() : null;
    }

    public enum TrophyType {
        GOLD("trophy"),
        IRON("trophy_minor"),
        IRONWOOD("trophy_quest");

        private final String modelName;

        TrophyType(String modelName) {
            this.modelName = modelName;
        }

        public String getModelName() {
            return this.modelName;
        }
    }
}
