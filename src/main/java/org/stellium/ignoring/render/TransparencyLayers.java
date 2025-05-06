package org.stellium.ignoring.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayer.MultiPhaseParameters;
import net.minecraft.client.render.RenderPhase.Texture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.equipment.EquipmentModel.LayerType;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.TriState;
import net.minecraft.util.Util;
import org.stellium.ignoring.config.IgnoringConfig;
import org.stellium.ignoring.entity.EntityCaptures;

import java.util.function.Function;
import java.util.function.Supplier;


/*
 * This file is part of Transparent-Entities(https://github.com/LopyMine/Transparent-Entities)
 * Copyright (C) LopyMine(https://github.com/LopyMine)
 *
 * Modified by stellium1 in Ignoring(https://github.com/stellium1/Ignoring).
 * Licensed under the GNU Lesser General Public License v3.0
*/
public class TransparencyLayers {

	static {
		IgnoringConfig.init();
	}

	private static final TriState triState = TriState.FALSE;


	private static final Function<Identifier, RenderLayer> ITEM_ENTITY_TRANSLUCENT_NO_CULL = Util.memoize((texture) -> {
		MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder().program(RenderLayer.ITEM_ENTITY_TRANSLUCENT_CULL_PROGRAM).texture(new Texture(texture, triState, false)).transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY).cull(RenderLayer.DISABLE_CULLING).target(RenderLayer.ITEM_ENTITY_TARGET).lightmap(RenderLayer.ENABLE_LIGHTMAP).overlay(RenderLayer.ENABLE_OVERLAY_COLOR).writeMaskState(RenderLayer.ALL_MASK).build(true);
		return RenderLayer.of("item_entity_translucent_no_cull", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, DrawMode.QUADS, 1536, true, true, multiPhaseParameters);
	});

	public static RenderLayer getLayerNoCull(LayerType layerType, Identifier texture, Supplier<RenderLayer> original) {
		if (canReplaceRenderLayer()) {
			if (layerType == LayerType.WINGS) {
				return ITEM_ENTITY_TRANSLUCENT_NO_CULL.apply(texture);
			} else {
				return RenderLayer.getItemEntityTranslucentCull(texture);
			}
		}
		return original.get();
	}

	public static RenderLayer getLayer(Identifier texture, Supplier<RenderLayer> original) {
		if (canReplaceRenderLayer()) {
			return RenderLayer.getItemEntityTranslucentCull(texture);
		}
		return original.get();
	}

	public static RenderLayer getItemLayer(RenderLayer original) {
		if (canReplaceRenderLayer()) {
			return TexturedRenderLayers.getItemEntityTranslucentCull();
		}
		return original;
	}

	public static RenderLayer getLayer(Supplier<RenderLayer> original) {
		if (canReplaceRenderLayer()) {
			return TexturedRenderLayers.getItemEntityTranslucentCull();
		}
		return original.get();
	}

	private static boolean canReplaceRenderLayer() {
		IgnoringConfig config;
		try {
			config = IgnoringConfig.get();
		} catch (Exception e) {
			return false;
		}
		config = IgnoringConfig.get();
		if (config == null) {
			return false;
		}
		if (!config.ignoreRender) {
			return false;
		}
		Entity entity = EntityCaptures.MAIN.getEntity();
		if (entity == null) {
			return false;
		}
		String entityName = entity.getNameForScoreboard();
		if (entityName == null) {
			return false;
		}
		return config.ignoredPlayerList.contains(entityName);
	}

}