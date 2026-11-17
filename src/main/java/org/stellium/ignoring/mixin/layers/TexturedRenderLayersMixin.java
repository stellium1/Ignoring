package org.stellium.ignoring.mixin.layers;

import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.Sheets;
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
 *
 * Ported to 26.1.2: getEntityCutout -> cutoutBlockSheet. The old getEntitySolid /
 * solidBlockSheet no longer exists; its former implementation (entitySolid on the
 * block atlas) is already covered by RenderLayerMixin.
*/
@Mixin(Sheets.class)
public class TexturedRenderLayersMixin {

    @Inject(at = @At("RETURN"), method = {
            "cutoutBlockSheet"
    }, cancellable = true)
    private static void swapRenderLayer(CallbackInfoReturnable<RenderType> cir) {
        cir.setReturnValue(TransparencyLayers.getItemLayer(cir::getReturnValue));
    }

}
