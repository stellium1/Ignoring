package org.stellium.ignoring.mixin.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.stellium.ignoring.entity.EntityCaptures;
import org.stellium.ignoring.render.TransparencyLayers;
import org.stellium.ignoring.render.TransparencyManager;
import org.stellium.ignoring.util.ArgbUtils;


/*
 * This file is part of Transparent-Entities(https://github.com/LopyMine/Transparent-Entities)
 * Copyright (C) LopyMine(https://github.com/LopyMine)
 *
 * Modified by stellium1 in Ignoring(https://github.com/stellium1/Ignoring).
 * Licensed under the GNU Lesser General Public License v3.0
*/
@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

	@ModifyVariable(at = @At("HEAD"), method = "renderItem(Lnet/minecraft/item/ModelTransformationMode;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II[ILnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/item/ItemRenderState$Glint;)V", argsOnly = true, index = 7)
	private static RenderLayer modifyLayer(RenderLayer value) {
		return TransparencyLayers.getItemLayer(value);
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/ColorHelper;getAlpha(I)I"), method = "renderBakedItemQuads")
	private static int generated(int i, Operation<Integer> original) {
		Entity entity = EntityCaptures.MAIN.getEntity();
		if (entity == null) {
			return original.call(i);
		}
		return ArgbUtils.getAlpha(TransparencyManager.getTranslucentArgb(entity, i));
	}

}