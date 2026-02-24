package org.stellium.ignoring.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.stellium.ignoring.config.IgnoringConfig;
import org.stellium.ignoring.entity.EntityCaptures;
import org.stellium.ignoring.mixin.accessor.RenderLayerAccessor;
import org.stellium.ignoring.mixin.accessor.RenderSetupAccessor;
import org.stellium.ignoring.mixin.accessor.RenderSetupTextureSpecAccessor;

import java.util.Map;
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

    public static RenderLayer getArmorLayer(boolean cull, Identifier texture, Supplier<RenderLayer> original) {
        if (canReplaceRenderLayer()) {
            if (cull) {
                return RenderLayers.itemEntityTranslucentCull(texture);
            } else {
                return RenderLayers.entityTranslucent(texture);
            }
        }
        return original.get();
    }

	public static RenderLayer getLayer(Identifier texture, Supplier<RenderLayer> original) {
		if (canReplaceRenderLayer()) {
			return RenderLayers.itemEntityTranslucentCull(texture);
		}
		return original.get();
	}

	public static RenderLayer getItemLayer(RenderLayer original) {
		if (!canReplaceRenderLayer()) {
			return original;
		}

		Identifier texture = getTextureLocation(original);
		if (SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE.equals(texture)) {
			return TexturedRenderLayers.getBlockTranslucentCull();
		}

		return TexturedRenderLayers.getItemTranslucentCull();
	}

	    public static RenderLayer getItemLayer(Supplier<RenderLayer> original) {
	        return getItemLayer(original.get());
	    }

    private static Identifier getTextureLocation(RenderLayer layer) {
        try {
            Map<String, Object> textures = ((RenderSetupAccessor) (Object) ((RenderLayerAccessor) (Object) layer).ignoring$getRenderSetup()).ignoring$getTextures();
            if (textures == null || textures.isEmpty()) {
                return null;
            }
            Object textureSpec = textures.values().iterator().next();
            return ((RenderSetupTextureSpecAccessor) textureSpec).ignoring$getLocation();
        } catch (Exception ignored) {
            return null;
        }
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
		return config.shouldIgnorePlayer(entity);
	}

}
