package org.stellium.ignoring.mixin.entity;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.stellium.ignoring.config.IgnoringConfig;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(
            method = "canHit",
            at = @At("RETURN"),
            cancellable = true
    )
    private void injected(CallbackInfoReturnable<Boolean> cir) {
        if (!IgnoringConfig.get().interactionThroughIgnoredPlayer) {
            return;
        }
        LivingEntity entity = (LivingEntity) (Object) this;
        if (IgnoringConfig.get().ignoredPlayerList.contains(entity.getNameForScoreboard())) {
            cir.setReturnValue(false);
        }

    }

}
