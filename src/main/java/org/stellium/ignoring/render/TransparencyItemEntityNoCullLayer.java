package org.stellium.ignoring.render;

import java.util.function.Function;
import net.minecraft.client.render.*;
import net.minecraft.util.*;

public class TransparencyItemEntityNoCullLayer {

    public static final Function<Identifier, RenderLayer> ITEM_ENTITY_TRANSLUCENT_NO_CULL = Util.memoize((texture) -> {
        RenderSetup renderSetup = RenderSetup.builder(TransparencyRenderPipelines.RENDER_TYPE_ITEM_ENTITY_TRANSLUCENT_NO_CULL)
                .texture("Sampler0", texture)
                .outputTarget(OutputTarget.ITEM_ENTITY_TARGET)
                .useLightmap()
                .useOverlay()
                .crumbling()
                .translucent()
                .outlineMode(RenderSetup.OutlineMode.AFFECTS_OUTLINE)
                .build();
        return RenderLayer.of("item_entity_translucent_no_cull", renderSetup);
    });

}
