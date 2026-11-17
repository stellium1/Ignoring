package org.stellium.ignoring.mixin.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.QuadInstance;
import net.minecraft.client.renderer.block.BlockQuadOutput;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.stellium.ignoring.config.IgnoringConfig;
import org.stellium.ignoring.entity.EntityCaptures;
import org.stellium.ignoring.util.ArgbUtils;

/*
 * Ported to 26.1.2: this replaces the old MixinSquared hook into Sodium's
 * ModelBlockRendererMixin (which no longer exists in Sodium 0.9.x). Sodium now
 * leaves the vanilla ModelBlockRenderer path untouched, so the alpha is applied
 * directly on the vanilla quad output instead.
 */
@Mixin(ModelBlockRenderer.class)
public class ModelBlockRendererMixin {

    @WrapOperation(
        method = "putQuadWithTint",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/block/BlockQuadOutput;put(FFFLnet/minecraft/client/resources/model/geometry/BakedQuad;Lcom/mojang/blaze3d/vertex/QuadInstance;)V"
        )
    )
    private void ignoring$applyBlockQuadAlpha(
        BlockQuadOutput output, float x, float y, float z, BakedQuad quad, QuadInstance instance, Operation<Void> original
    ) {
        Entity entity = EntityCaptures.MAIN.getEntity();
        if (entity == null) {
            original.call(output, x, y, z, quad, instance);
            return;
        }

        int transparency = IgnoringConfig.get().transparency;
        int[] oldColors = new int[4];
        for (int vertex = 0; vertex < 4; vertex++) {
            oldColors[vertex] = instance.getColor(vertex);
            instance.setColor(vertex, ArgbUtils.applyAlpha(oldColors[vertex], transparency));
        }
        try {
            original.call(output, x, y, z, quad, instance);
        } finally {
            for (int vertex = 0; vertex < 4; vertex++) {
                instance.setColor(vertex, oldColors[vertex]);
            }
        }
    }
}
