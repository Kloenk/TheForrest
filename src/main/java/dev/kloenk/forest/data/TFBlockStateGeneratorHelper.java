package dev.kloenk.forest.data;

import com.google.gson.JsonElement;
import dev.kloenk.forest.ForestMod;
import dev.kloenk.forest.blocks.NagastoneBlock;
import dev.kloenk.forest.blocks.TFBlocks;
import dev.kloenk.forest.blocks.ThornsBlock;
import dev.kloenk.forest.mixin.BlockStateModelGeneratorAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.model.*;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.lwjgl.system.CallbackI;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class TFBlockStateGeneratorHelper {
    public static final Model THORNS_MAIN = new Model(Optional.of(ForestMod.path("block/thorns_main")), Optional.empty(), TextureKey.SIDE, TextureKey.END);
    public static final Model THORNS_SECTION_BOTTOM = new Model(Optional.of(ForestMod.path("block/thorns_section_bottom")), Optional.empty(), TextureKey.SIDE, TextureKey.END);
    public static final Model THORNS_SECTION_TOP = new Model(Optional.of(ForestMod.path("block/thorns_section_top")), Optional.empty(), TextureKey.SIDE, TextureKey.END);

    public final BlockStateModelGenerator generator;

    public TFBlockStateGeneratorHelper(BlockStateModelGenerator generator) {
        this.generator = generator;
    }

    public void registerSimpleState(Block block) {
        ((BlockStateModelGeneratorAccessor)generator).registerSimpleStateInvoker(block);
    }

    public void registerItemModel(Block block) {
        ((BlockStateModelGeneratorAccessor)generator).registerItemModelInvoker(block);
    }

    public void registerStateWithModelReference(Block block, Block reference) {
        ((BlockStateModelGeneratorAccessor)generator).registerStateWithModelReferenceInvoker(block, reference);
    }

    static VariantsBlockStateSupplier createSingletonBlockState(Block block, Identifier modelId) {
        return VariantsBlockStateSupplier.create(block, BlockStateVariant.create().put(VariantSettings.MODEL, modelId));
    }

    public Consumer<BlockStateSupplier> getBlockStateCollector() {
        return ((BlockStateModelGeneratorAccessor)generator).getBlockStateCollector();
    }

    public BiConsumer<Identifier, Supplier<JsonElement>> getModelCollector() {
        return ((BlockStateModelGeneratorAccessor)generator).getModelCollector();
    }

    public BlockStateModelGenerator.LogTexturePool registerLog(Block block) {
        return ((BlockStateModelGeneratorAccessor)generator).registerLogInvoker(block);
    }

    public void registerSingleton(Block block, TexturedModel.Factory modelFactory) {
        ((BlockStateModelGeneratorAccessor)generator).registerSingletonInvoker(block, modelFactory);
    }

    public void registerParentedItemModel(Item item, Identifier parentModelId) {
        ((BlockStateModelGeneratorAccessor)generator).registerParentedItemModelInvoker(item, parentModelId);
    }

    public static List<BlockStateVariant> buildBlockStateVariants(List<Identifier> modelIds, UnaryOperator<BlockStateVariant> processor) {
        return BlockStateModelGeneratorAccessor.buildBlockStateVariantsInvoker(modelIds, processor);
    }

    public void registerSimpleCubeAll(Block block) {
        this.registerSingleton(block, TexturedModel.CUBE_ALL);
        //this.getBlockStateCollector().accept(createSingletonBlockState(block, TexturedModel.CUBE_ALL.upload(block, this.getModelCollector())));
    }

    public void registerSimpleColumn(Block block) {
        this.registerSingleton(block, TexturedModel.CUBE_COLUMN);
    }

    protected void registerWithModelReferenceAndItem(Block block, Block reference) {
        this.registerStateWithModelReference(block, reference);
        this.registerParentedItemModel(block.asItem(), Texture.getId(reference));
    }

    protected void registerSpawner(Block spawner) {
        this.registerStateWithModelReference(spawner, Blocks.SPAWNER);
        this.registerParentedItemModel(spawner.asItem(), Texture.getId(Blocks.SPAWNER));
    }

    protected void registerCrossBlock(Block block) {
        Texture texture = Texture.cross(block) ;
        Identifier identifier = Models.CROSS.upload(block, texture, this.getModelCollector());
        this.getBlockStateCollector().accept(createSingletonBlockState(block, identifier));
    }

    protected BlockStateSupplier createThorn(Block thorn, Identifier model, Identifier topModel, Identifier bottomModel) {
        return MultipartBlockStateSupplier
                .create(thorn)
                .with(
                        When.create().set(
                                ThornsBlock.AXIS,
                                Direction.Axis.Y
                        ),
                        BlockStateVariant.create().put(
                                VariantSettings.MODEL,
                                model
                        )
                )
                .with(
                        When.create().set(
                                ThornsBlock.AXIS,
                                Direction.Axis.Z
                        ),
                        BlockStateVariant.create()
                                .put(
                                        VariantSettings.MODEL,
                                        model
                                )
                                .put(
                                        VariantSettings.X,
                                        VariantSettings.Rotation.R90
                                )
                )
                .with(
                        When.create().set(
                                ThornsBlock.AXIS,
                                Direction.Axis.X
                        ),
                        BlockStateVariant.create()
                                .put(
                                        VariantSettings.MODEL,
                                        model
                                )
                                .put(
                                        VariantSettings.X,
                                        VariantSettings.Rotation.R90
                                )
                                .put(
                                        VariantSettings.Y,
                                        VariantSettings.Rotation.R90
                                )
                )
                .with(
                        When.create().set(
                                ThornsBlock.UP,
                                true
                        ),
                        BlockStateVariant.create()
                                .put(
                                        VariantSettings.MODEL,
                                        topModel
                                )
                                .put(
                                        VariantSettings.X,
                                        VariantSettings.Rotation.R90
                                )
                )
                .with(
                        When.create().set(
                                ThornsBlock.DOWN,
                                true
                        ),
                        BlockStateVariant.create()
                                .put(
                                        VariantSettings.MODEL,
                                        bottomModel
                                )
                                .put(
                                        VariantSettings.X,
                                        VariantSettings.Rotation.R90
                                )
                )
                .with(
                        When.create().set(
                                ThornsBlock.EAST,
                                true
                        ),
                        BlockStateVariant.create()
                                .put(
                                        VariantSettings.MODEL,
                                        topModel
                                )
                                .put(
                                        VariantSettings.Y,
                                        VariantSettings.Rotation.R270
                                )
                )
                .with(
                        When.create().set(
                                ThornsBlock.WEST,
                                true
                        ),
                        BlockStateVariant.create()
                                .put(
                                        VariantSettings.MODEL,
                                        bottomModel
                                )
                                .put(
                                        VariantSettings.Y,
                                        VariantSettings.Rotation.R270
                                )
                )
                .with(
                        When.create().set(
                                ThornsBlock.SOUTH,
                                true
                        ),
                        BlockStateVariant.create()
                                .put(
                                        VariantSettings.MODEL,
                                        bottomModel
                                )
                                .put(
                                        VariantSettings.Y,
                                        VariantSettings.Rotation.R180
                                )
                )
                .with(
                        When.create().set(
                                ThornsBlock.NORTH,
                                true
                        ),
                        BlockStateVariant.create()
                                .put(
                                        VariantSettings.MODEL,
                                        topModel
                                )
                                .put(
                                        VariantSettings.Y,
                                        VariantSettings.Rotation.R180
                                )
                );
    }

    protected void registerThorn(Block thorn) {
        Texture texture = Texture.sideEnd(ModelIds.getBlockSubModelId(thorn, "_side"), ModelIds.getBlockSubModelId(thorn, "_top"));
        Identifier mainModel = THORNS_MAIN.upload(thorn, texture, this.getModelCollector());
        Identifier topModel = THORNS_SECTION_TOP.upload(thorn, "_top", texture, this.getModelCollector());
        Identifier bottomModel = THORNS_SECTION_BOTTOM.upload(thorn, "_bottom", texture, this.getModelCollector());

        this.getBlockStateCollector().accept(
                createThorn(thorn, mainModel, topModel, bottomModel)
        );
        // TODO: item
    }

    /*public void registerSingleton(Block block, Texture texture, Model model) {
        ((BlockStateModelGeneratorAccessor)generator).registerSingletonInvoker(block, texture, model);
    }*/
}
