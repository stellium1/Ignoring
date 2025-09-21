package org.stellium.ignoring.mixin.player;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
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

    @WrapOperation(method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"))
    private void wrapRender(EntityModel<?> instance, MatrixStack matrixStack, VertexConsumer vertexConsumer, int a, int b, int c, Operation<Void> original, @Local(argsOnly = true) LivingEntityRenderState state) {
        Entity entity = EntityCaptures.MAIN.getEntity();
        if (entity != null && ArgbUtils.getAlpha(TransparencyManager.getTranslucentArgb(entity, -1)) == 0) {
            return;
        }
        original.call(instance, matrixStack, vertexConsumer, a, b, c);
    }

    @WrapOperation(method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/FeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/EntityRenderState;FF)V"))
    private void wrapFeatureRender(FeatureRenderer<?, ?> instance, MatrixStack matrixStack, VertexConsumerProvider provider, int i, EntityRenderState entityRenderState, float a, float b, Operation<Void> original, @Local(argsOnly = true) LivingEntityRenderState state) {
        Entity entity = EntityCaptures.MAIN.getEntity();
        if (entity != null && ArgbUtils.getAlpha(TransparencyManager.getTranslucentArgb(entity, -1)) == 0) {
            return;
        }
        original.call(instance, matrixStack, provider, i, entityRenderState, a, b);
    }
}