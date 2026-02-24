package org.stellium.ignoring.mixin.player;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.stellium.ignoring.config.IgnoringConfig;
import org.stellium.ignoring.render.TransparencyManager;
import org.stellium.ignoring.render.TransparencyRenderer;
import org.stellium.ignoring.util.ArgbUtils;

/*
 * This file is part of Transparent-Entities(https://github.com/LopyMine/Transparent-Entities)
 * Copyright (C) LopyMine(https://github.com/LopyMine)
 *
 * Modified by stellium1 in Ignoring(https://github.com/stellium1/Ignoring).
 * Licensed under the GNU Lesser General Public License v3.0
*/
@Mixin(EntityRenderer.class)
public class EntityRenderDispatcherShadowMixin {

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;getShadowOpacity(Lnet/minecraft/client/render/entity/state/EntityRenderState;)F"), method = "updateShadow(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/world/World;)V")
    private float swapShadowTransparency(EntityRenderer instance, EntityRenderState entityRenderState, Operation<Float> original) {
        float call = original.call(instance, entityRenderState);
        Entity entity = TransparencyRenderer.getCurrentEntity();
        if (entity == null || !TransparencyManager.canRenderTransparencyShadow(entity)) {
            return call;
        }
        int originalColor = ArgbUtils.getArgb((int) (call * 255F), 255, 255, 255);
        return ArgbUtils.getAlpha(ArgbUtils.applyAlpha(originalColor, IgnoringConfig.get().transparency)) / 255F;
    }
}
