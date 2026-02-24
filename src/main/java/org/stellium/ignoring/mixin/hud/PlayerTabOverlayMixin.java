package org.stellium.ignoring.mixin.hud;

import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.stellium.ignoring.config.IgnoringConfig;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerListHud.class)
public class PlayerTabOverlayMixin {
    @Redirect(
            method = "render",
        at = @At(
            value = "INVOKE",
                target = "Lnet/minecraft/client/gui/hud/PlayerListHud;collectPlayerEntries()Ljava/util/List;"
        )
    )
    private List<PlayerListEntry> redirectCollect(PlayerListHud self) {
        List<PlayerListEntry> original = ((PlayerListHudInvoker) self).invokeCollectPlayerEntries();
        IgnoringConfig config = IgnoringConfig.get();
        if (!config.ignoreTablist) {
            return original;
        }
        List<PlayerListEntry> copy = new ArrayList<>(original);

        copy.removeIf(entry -> {
            String displayName = entry.getDisplayName() != null
                ? entry.getDisplayName().getString()
                : null;

            String profileName = entry.getProfile().name();

            return (displayName != null && config.isPlayerIgnored(displayName))
                || config.isPlayerIgnored(profileName);
        });

        return copy;
    }
}
