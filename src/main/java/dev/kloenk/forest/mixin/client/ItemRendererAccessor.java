package dev.kloenk.forest.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public interface ItemRendererAccessor {
    @Invoker("renderBakedItemQuads")
    void renderBakedItemQuadsInvoker(MatrixStack matrices, VertexConsumer vertices, List<BakedQuad> quads, ItemStack stack, int light, int overlay);
}
