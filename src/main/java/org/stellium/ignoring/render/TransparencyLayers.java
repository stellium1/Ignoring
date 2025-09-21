package org.stellium.ignoring.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.stellium.ignoring.config.IgnoringConfig;
import org.stellium.ignoring.entity.EntityCaptures;

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
        TransparencyRenderPipelines.register();
		IgnoringConfig.init();
	}

    public static RenderLayer getArmorLayer(boolean cull, Identifier texture, Supplier<RenderLayer> original) {
        if (canReplaceRenderLayer()) {
            if (cull) {
                return RenderLayer.getItemEntityTranslucentCull(texture);
            } else {
                return TransparencyItemEntityNoCullLayer.ITEM_ENTITY_TRANSLUCENT_NO_CULL.apply(texture);
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

    public static RenderLayer getItemLayer(Supplier<RenderLayer> original) {
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