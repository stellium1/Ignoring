package org.stellium.ignoring.render;

import java.util.function.Function;
import net.minecraft.client.render.*;
import net.minecraft.client.render.RenderLayer.MultiPhaseParameters;
import net.minecraft.client.render.RenderPhase.Texture;

import net.minecraft.util.*;

public class TransparencyItemEntityNoCullLayer {

	private static final TriState STATE = TriState.FALSE;

    public static final Function<Identifier, RenderLayer> ITEM_ENTITY_TRANSLUCENT_NO_CULL = Util.memoize((texture) -> {
        MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder().texture(new Texture(texture, false)).target(RenderLayer.ITEM_ENTITY_TARGET).lightmap(RenderLayer.ENABLE_LIGHTMAP).overlay(RenderLayer.ENABLE_OVERLAY_COLOR).build(true);
        return RenderLayer.of("item_entity_translucent_no_cull", 1536, true, true, TransparencyRenderPipelines.RENDER_TYPE_ITEM_ENTITY_TRANSLUCENT_NO_CULL, multiPhaseParameters);
    });

}
