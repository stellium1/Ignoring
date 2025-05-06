package org.stellium.ignoring.mixin.sodium;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
import net.caffeinemc.mods.sodium.client.model.quad.ModelQuadView;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.stellium.ignoring.config.IgnoringConfig;
import org.stellium.ignoring.entity.EntityCaptures;
import org.stellium.ignoring.util.ArgbUtils;

/*
 * This file is part of Transparent-Entities(https://github.com/LopyMine/Transparent-Entities)
 * Copyright (C) LopyMine(https://github.com/LopyMine)
 *
 * Modified by stellium1 in Ignoring(https://github.com/stellium1/Ignoring).
 * Licensed under the GNU Lesser General Public License v3.0
*/
@Restriction(require = @Condition("sodium"))
@Pseudo
@Mixin(value = ItemRenderer.class, priority = 2500)
public class ItemRendererMixinMixin {

	@Dynamic
	@TargetHandler(
			mixin = "net.caffeinemc.mods.sodium.mixin.features.render.model.item.ItemRendererMixin",
			name = "renderBakedItemQuads"
	)
	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/immediate/model/BakedModelEncoder;writeQuadVertices(Lnet/caffeinemc/mods/sodium/api/vertex/buffer/VertexBufferWriter;Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/caffeinemc/mods/sodium/client/model/quad/ModelQuadView;IIIZ)V"), method = "@MixinSquared:Handler")
	private static void generated(VertexBufferWriter writer, Entry entry, ModelQuadView quad, int color, int light, int overlay, boolean bl, Operation<Void> original) {
		Entity entity = EntityCaptures.MAIN.getEntity();

		if (entity == null) {
			original.call(writer, entry, quad, color, light, overlay, bl);
			return;
		}

		original.call(writer, entry, quad, ArgbUtils.applyAlpha(color, IgnoringConfig.get().transparency), light, overlay, bl);
	}

}
