package org.stellium.ignoring.mixin.player;

import net.minecraft.client.render.command.LabelCommandRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.stellium.ignoring.config.IgnoringConfig;
import org.stellium.ignoring.entity.EntityCaptures;

import static org.stellium.ignoring.util.ArgbUtils.swapAlpha;

@Mixin(LabelCommandRenderer.Commands.class)
public class EntityRendererMixin {

    @ModifyArg(
        method = "add(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Vec3d;ILnet/minecraft/text/Text;ZIDLnet/minecraft/client/render/state/CameraRenderState;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/command/OrderedRenderCommandQueueImpl$LabelCommand;<init>(Lorg/joml/Matrix4f;FFLnet/minecraft/text/Text;IIID)V"
        ),
        index = 5
    )
    private int ignoring$adjustLabelColor(int color) {
        if (EntityCaptures.MAIN.getEntity() == null) {
            return color;
        }

        return swapAlpha(color, IgnoringConfig.get().transparency);
    }

    @ModifyArg(
        method = "add(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Vec3d;ILnet/minecraft/text/Text;ZIDLnet/minecraft/client/render/state/CameraRenderState;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/command/OrderedRenderCommandQueueImpl$LabelCommand;<init>(Lorg/joml/Matrix4f;FFLnet/minecraft/text/Text;IIID)V"
        ),
        index = 6
    )
    private int ignoring$adjustLabelBackgroundColor(int backgroundColor) {
        if (EntityCaptures.MAIN.getEntity() == null) {
            return backgroundColor;
        }

        return swapAlpha(backgroundColor, IgnoringConfig.get().transparency);
    }

}
