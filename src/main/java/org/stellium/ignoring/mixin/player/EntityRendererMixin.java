package org.stellium.ignoring.mixin.player;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.stellium.ignoring.config.IgnoringConfig;
import org.stellium.ignoring.entity.EntityCaptures;

import static org.stellium.ignoring.util.ArgbUtils.swapAlpha;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Redirect(
            method = "renderLabelIfPresent",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)V",
                    ordinal = 0
            )
    )
    private void injected$sneaking(TextRenderer instance, Text text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, TextRenderer.TextLayerType layerType, int backgroundColor, int light) {
        if (EntityCaptures.MAIN.getEntity() == null) {
            instance.draw(text, x, y, color, shadow, matrix, vertexConsumers, layerType, backgroundColor, light);
        } else {
            instance.draw(text, x, y, swapAlpha(color, IgnoringConfig.get().transparency), shadow, matrix, vertexConsumers, layerType, swapAlpha(backgroundColor, IgnoringConfig.get().transparency), light);
        }
    }

    @Redirect(
            method = "renderLabelIfPresent",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)V",
                    ordinal = 1
            )
    )
    private void injected$notSneaking(TextRenderer instance, Text text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, TextRenderer.TextLayerType layerType, int backgroundColor, int light) {
        if (EntityCaptures.MAIN.getEntity() == null) {
            instance.draw(text, x, y, color, shadow, matrix, vertexConsumers, layerType, backgroundColor, light);
        } else {
            instance.draw(text, x, y, swapAlpha(color, IgnoringConfig.get().transparency), shadow, matrix, vertexConsumers, layerType, swapAlpha(backgroundColor, IgnoringConfig.get().transparency), light);
        }
    }

}