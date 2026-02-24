package org.stellium.ignoring.mixin.layers;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
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
@Mixin(RenderLayers.class)
public class RenderLayerMixin {

    @Inject(at = @At("RETURN"), method = {
            "entityCutout(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;",
            "entitySmoothCutout",
            "entitySolid",
            "entitySolidZOffsetForward",
            "entityNoOutline",
            "entityTranslucentEmissiveNoOutline",
    }, cancellable = true)
    private static void swapRenderLayer(Identifier texture, CallbackInfoReturnable<RenderLayer> cir) {
        cir.setReturnValue(TransparencyLayers.getLayer(texture, cir::getReturnValue));
    }

    @Inject(at = @At("RETURN"), method = {
            "entityCutoutNoCull(Lnet/minecraft/util/Identifier;Z)Lnet/minecraft/client/render/RenderLayer;",
            "entityCutoutNoCullZOffset(Lnet/minecraft/util/Identifier;Z)Lnet/minecraft/client/render/RenderLayer;",
            "entityTranslucent(Lnet/minecraft/util/Identifier;Z)Lnet/minecraft/client/render/RenderLayer;",
            "entityTranslucentEmissive(Lnet/minecraft/util/Identifier;Z)Lnet/minecraft/client/render/RenderLayer;"
    }, cancellable = true)
    private static void swapRenderLayerWithBoolean(Identifier texture, boolean affectsOutline, CallbackInfoReturnable<RenderLayer> cir) {
        cir.setReturnValue(TransparencyLayers.getLayer(texture, cir::getReturnValue));
    }

}
