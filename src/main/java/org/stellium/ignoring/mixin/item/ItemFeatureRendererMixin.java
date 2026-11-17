package org.stellium.ignoring.mixin.item;

import com.mojang.blaze3d.vertex.QuadInstance;
import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.stellium.ignoring.config.IgnoringConfig;
import org.stellium.ignoring.entity.EntityCaptures;
import org.stellium.ignoring.render.TransparencyLayers;
import org.stellium.ignoring.util.ArgbUtils;


/*
 * This file is part of Transparent-Entities(https://github.com/LopyMine/Transparent-Entities)
 * Copyright (C) LopyMine(https://github.com/LopyMine)
 *
 * Modified by stellium1 in Ignoring(https://github.com/stellium1/Ignoring).
 * Licensed under the GNU Lesser General Public License v3.0
 *
 * Ported to 26.1.2: the old ItemRenderer.renderItem / renderBakedItemQuads hooks were
 * replaced by the equivalent ItemFeatureRenderer.renderItem pipeline.
*/
@Mixin(ItemFeatureRenderer.class)
public class ItemFeatureRendererMixin {

    @ModifyVariable(
            method = "renderItem(Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/OutlineBufferSource;Lnet/minecraft/client/renderer/SubmitNodeStorage$ItemSubmit;)V",
            at = @At(value = "STORE"),
            ordinal = 0
    )
    private RenderType modifyLayer(RenderType value) {
        return TransparencyLayers.getItemLayer(value);
    }

    @ModifyArg(
            method = "renderItem(Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/OutlineBufferSource;Lnet/minecraft/client/renderer/SubmitNodeStorage$ItemSubmit;)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/QuadInstance;setColor(I)V"),
            index = 0
    )
    private int modifyQuadColor(int color) {
        Entity entity = EntityCaptures.MAIN.getEntity();
        if (entity == null) {
            return color;
        }
        return ArgbUtils.applyAlpha(color, IgnoringConfig.get().transparency);
    }

}
