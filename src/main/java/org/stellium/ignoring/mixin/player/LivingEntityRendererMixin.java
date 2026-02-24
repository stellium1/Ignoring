package org.stellium.ignoring.mixin.player;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.command.RenderCommandQueue;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.jspecify.annotations.Nullable;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.render.entity.model.EntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.stellium.ignoring.entity.EntityCaptures;
import org.stellium.ignoring.render.TransparencyManager;
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

    @WrapOperation(method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILcom/mojang/blaze3d/textures/GpuTextureView;ILcom/mojang/blaze3d/textures/GpuTextureView;)V"))
    private <S> void wrapSubmitModel(OrderedRenderCommandQueue instance, Model model, S state, MatrixStack matrices, RenderLayer renderLayer, int light, int overlay, int color, @Nullable GpuTextureView crumblingOverlay, int outlineColor, @Nullable GpuTextureView extraTexture, Operation<Void> original) {
        Entity entity = EntityCaptures.MAIN.getEntity();
        if (entity != null && ArgbUtils.getAlpha(TransparencyManager.getTranslucentArgb(entity, -1)) == 0) {
            return;
        }
        original.call(instance, model, state, matrices, renderLayer, light, overlay, color, crumblingOverlay, outlineColor, extraTexture);
    }

    @WrapOperation(method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/FeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/EntityRenderState;FF)V"))
    private void wrapFeatureRender(FeatureRenderer<?, ?> instance, MatrixStack matrixStack, OrderedRenderCommandQueue queue, int light, EntityRenderState entityRenderState, float a, float b, Operation<Void> original) {
        Entity entity = EntityCaptures.MAIN.getEntity();
        if (entity != null && ArgbUtils.getAlpha(TransparencyManager.getTranslucentArgb(entity, -1)) == 0) {
            return;
        }
        original.call(instance, matrixStack, queue, light, entityRenderState, a, b);
    }
}
