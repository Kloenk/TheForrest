package dev.kloenk.forest.mixin;

import com.google.gson.JsonElement;
import net.minecraft.block.Block;
import net.minecraft.data.client.model.*;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.IdentityHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mixin(BlockStateModelGenerator.class)
public interface BlockStateModelGeneratorAccessor {
    @Invoker("registerSimpleState")
    void registerSimpleStateInvoker(Block block);

    @Invoker("registerLog")
    BlockStateModelGenerator.LogTexturePool registerLogInvoker(Block block);

    @Invoker("registerSingleton")
    void registerSingletonInvoker(Block block, TexturedModel.Factory modelFactory);

    /*@Invoker("registerSingleton")
    void registerSingletonInvoker(Block block, Texture texture, Model model);*/

    @Invoker("registerStateWithModelReference")
    void registerStateWithModelReferenceInvoker(Block block, Block reference);

    @Invoker("registerItemModel")
    void registerItemModelInvoker(Block block);

    @Invoker("registerParentedItemModel")
    void registerParentedItemModelInvoker(Item item, Identifier parentModelId);

    @Accessor
    Consumer<BlockStateSupplier> getBlockStateCollector();

    @Accessor
    BiConsumer<Identifier, Supplier<JsonElement>> getModelCollector();
}
