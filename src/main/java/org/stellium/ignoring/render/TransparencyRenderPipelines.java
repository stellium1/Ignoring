package org.stellium.ignoring.render;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.VertexFormats;

import com.mojang.blaze3d.pipeline.*;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;


public class TransparencyRenderPipelines {

	public static final RenderPipeline RENDER_TYPE_ITEM_ENTITY_TRANSLUCENT_NO_CULL =
			RenderPipeline.builder(RenderPipelines.TRANSFORMS_PROJECTION_FOG_LIGHTING_SNIPPET)
                    .withLocation("pipeline/item_entity_translucent_cull")
					.withVertexShader("core/rendertype_item_entity_translucent_cull")
					.withFragmentShader("core/rendertype_item_entity_translucent_cull")
					.withSampler("Sampler0")
					.withSampler("Sampler2")
					.withBlend(BlendFunction.TRANSLUCENT)
					.withCull(false)
					.withVertexFormat(VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, DrawMode.QUADS)
					.build();

	public static void register() {
		RenderPipelines.register(RENDER_TYPE_ITEM_ENTITY_TRANSLUCENT_NO_CULL);
	}

}
