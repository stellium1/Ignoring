package org.stellium.ignoring.mixin.item;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
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
*/
@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @ModifyVariable(at = @At("HEAD"), method = "renderItem(Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II[ILjava/util/List;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/item/ItemRenderState$Glint;)V", argsOnly = true, index = 7)
    private static RenderLayer modifyLayer(RenderLayer value) {
        return TransparencyLayers.getItemLayer(value);
    }

	@ModifyVariable(
            method = "renderBakedItemQuads",
            at = @At(value = "STORE"),
            name = "f",
            ordinal = 0
    )
    private static float generated(float originalAlpha) {
        Entity entity = EntityCaptures.MAIN.getEntity();
        if (entity == null) {
            return originalAlpha;
        }
        int originalColor = ArgbUtils.getArgb((int) (originalAlpha * 255F), 255, 255, 255);
        return ArgbUtils.getAlpha(ArgbUtils.applyAlpha(originalColor, IgnoringConfig.get().transparency)) / 255F;
    }
}