package org.stellium.ignoring.mixin.player;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.stellium.ignoring.config.IgnoringConfig;
import org.stellium.ignoring.render.TransparencyRenderer;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/*
 * This file is part of Transparent-Entities(https://github.com/LopyMine/Transparent-Entities)
 * Copyright (C) LopyMine(https://github.com/LopyMine)
 *
 * Modified by stellium1 in Ignoring(https://github.com/stellium1/Ignoring).
 * Licensed under the GNU Lesser General Public License v3.0
*/
@Mixin(EntityRenderManager.class)
public class EntityRenderDispatcherMixin {

    @Unique
    private static final ThreadLocal<Map<EntityRenderState, Entity>> IGNORING$STATE_ENTITY =
        ThreadLocal.withInitial(IdentityHashMap::new);

    @Inject(method = "getAndUpdateRenderState(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/client/render/entity/state/EntityRenderState;", at = @At("RETURN"))
    private <E extends Entity> void ignoring$captureEntity(E entity, float tickDelta, CallbackInfoReturnable<EntityRenderState> cir) {
        EntityRenderState state = cir.getReturnValue();
        if (state != null) {
            IGNORING$STATE_ENTITY.get().put(state, entity);
        }
    }

    @WrapOperation(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/entity/EntityRenderer;render(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V"
        ),
        method = "render(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/render/state/CameraRenderState;DDDLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;)V"
    )
    private void handleEntityRendering(
        EntityRenderer<?, EntityRenderState> instance,
        EntityRenderState state,
        MatrixStack matrices,
        OrderedRenderCommandQueue commandQueue,
        CameraRenderState cameraState,
        Operation<Void> original
    ) {
        Entity entity = IGNORING$STATE_ENTITY.get().get(state);
        if (entity == null) {
            original.call(instance, state, matrices, commandQueue, cameraState);
            return;
        }
        TransparencyRenderer.handleEntityRendering(entity, () -> original.call(instance, state, matrices, commandQueue, cameraState));
    }

    @WrapOperation(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitShadowPieces(Lnet/minecraft/client/util/math/MatrixStack;FLjava/util/List;)V"
        ),
        method = "render(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/render/state/CameraRenderState;DDDLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;)V"
    )
    private void handleShadowRendering(
        OrderedRenderCommandQueue instance,
        MatrixStack matrices,
        float shadowRadius,
        List<EntityRenderState.ShadowPiece> shadowPieces,
        Operation<Void> original,
        @Local(argsOnly = true) EntityRenderState state
    ) {
        Entity entity = IGNORING$STATE_ENTITY.get().get(state);
        if (entity == null || !org.stellium.ignoring.render.TransparencyManager.canRenderTransparencyShadow(entity)) {
            original.call(instance, matrices, shadowRadius, shadowPieces);
            return;
        }

        float alpha = IgnoringConfig.get().transparency / 255F;
        List<EntityRenderState.ShadowPiece> adjusted = new ArrayList<>(shadowPieces.size());
        for (EntityRenderState.ShadowPiece piece : shadowPieces) {
            adjusted.add(new EntityRenderState.ShadowPiece(
                piece.relativeX(),
                piece.relativeY(),
                piece.relativeZ(),
                piece.shapeBelow(),
                alpha
            ));
        }
        original.call(instance, matrices, shadowRadius, adjusted);
    }

    @Inject(
        method = "render(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/render/state/CameraRenderState;DDDLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;)V",
        at = @At("TAIL")
    )
    private void ignoring$clearCapturedEntity(
        EntityRenderState state,
        CameraRenderState cameraState,
        double x,
        double y,
        double z,
        MatrixStack matrices,
        OrderedRenderCommandQueue commandQueue,
        CallbackInfo ci
    ) {
        IGNORING$STATE_ENTITY.get().remove(state);
    }

}
