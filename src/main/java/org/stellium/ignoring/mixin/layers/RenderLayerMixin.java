package org.stellium.ignoring.mixin.layers;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.stellium.ignoring.render.TransparencyLayers;

/*
 * This file is part of Transparent-Entities(https://github.com/LopyMine/Transparent-Entities)
 * Copyright (C) LopyMine(https://github.com/LopyMine)
 *
 * Modified by stellium1 in Ignoring(https://github.com/stellium1/Ignoring).
 * Licensed under the GNU Lesser General Public License v3.0
*/
@Mixin(RenderLayer.class)
public class RenderLayerMixin {

	@Inject(at = @At("RETURN"), method = {
			"getEntityCutout",
			"getEntitySmoothCutout",
			"getEntitySolid",
			"getEntitySolidZOffsetForward",
			"getEntityNoOutline",
			"getEntityTranslucentEmissiveNoOutline",
	}, cancellable = true)
	private static void swapRenderLayer(Identifier texture, CallbackInfoReturnable<RenderLayer> cir) {
		cir.setReturnValue(TransparencyLayers.getLayer(texture, cir::getReturnValue));
	}

	@Inject(at = @At("RETURN"), method = {
		"getEntityCutoutNoCull(Lnet/minecraft/util/Identifier;Z)Lnet/minecraft/client/render/RenderLayer;",
		"getEntityCutoutNoCullZOffset(Lnet/minecraft/util/Identifier;Z)Lnet/minecraft/client/render/RenderLayer;",
		"getEntityTranslucent(Lnet/minecraft/util/Identifier;Z)Lnet/minecraft/client/render/RenderLayer;",
		"getEntityTranslucentEmissive(Lnet/minecraft/util/Identifier;Z)Lnet/minecraft/client/render/RenderLayer;"
	}, cancellable = true)
	private static void swapRenderLayerBl(Identifier texture, boolean affectsOutline, CallbackInfoReturnable<RenderLayer> cir) {
		RenderLayer oldLayer = cir.getReturnValue();
		RenderLayer newLayer = TransparencyLayers.getLayer(texture, () -> oldLayer);

		cir.setReturnValue(newLayer);
	}

}