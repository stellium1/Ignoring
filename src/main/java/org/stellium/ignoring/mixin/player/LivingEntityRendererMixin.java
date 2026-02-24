package org.stellium.ignoring.mixin.player;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.stellium.ignoring.config.IgnoringConfig;
import org.stellium.ignoring.entity.EntityCaptures;
import org.stellium.ignoring.util.ArgbUtils;

/*
 * This file is part of Transparent-Entities(https://github.com/LopyMine/Transparent-Entities)
 * Copyright (C) LopyMine(https://github.com/LopyMine)
 *
 * Modified by stellium1 in Ignoring(https://github.com/stellium1/Ignoring).
 * Licensed under the GNU Lesser General Public License v3.0
*/
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @WrapOperation(
        method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/texture/Sprite;ILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V"
        )
    )
    private void wrapRender(
        OrderedRenderCommandQueue instance,
        Model<?> model,
        Object renderState,
        MatrixStack matrixStack,
        RenderLayer renderLayer,
        int light,
        int overlay,
        int color,
        Sprite sprite,
        int outlineColor,
        ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlayCommand,
        Operation<Void> original
    ) {
        Entity entity = EntityCaptures.MAIN.getEntity();
        if (entity != null) {
            IgnoringConfig config = IgnoringConfig.get();
            if (config.ignoreRender && config.shouldIgnorePlayer(entity)) {
                if (config.transparency <= 0) {
                    return;
                }
                color = ArgbUtils.swapAlpha(color, config.transparency);
            }
        }
        original.call(instance, model, renderState, matrixStack, renderLayer, light, overlay, color, sprite, outlineColor, crumblingOverlayCommand);
    }

    @WrapOperation(
        method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/entity/feature/FeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/EntityRenderState;FF)V"
        )
    )
    private void wrapFeatureRender(
        FeatureRenderer<?, ?> instance,
        MatrixStack matrixStack,
        OrderedRenderCommandQueue provider,
        int i,
        EntityRenderState entityRenderState,
        float a,
        float b,
        Operation<Void> original
    ) {
        Entity entity = EntityCaptures.MAIN.getEntity();
        if (entity != null) {
            IgnoringConfig config = IgnoringConfig.get();
            if (config.ignoreRender && config.shouldIgnorePlayer(entity) && config.transparency <= 0) {
                return;
            }
        }
        original.call(instance, matrixStack, provider, i, entityRenderState, a, b);
    }
}
