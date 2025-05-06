package org.stellium.ignoring.mixin.network;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.stellium.ignoring.config.IgnoringConfig;

import java.util.Set;


@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Shadow
    @Mutable
    @Final
    private Set<PlayerListEntry> listedPlayerListEntries;

    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        if (!IgnoringConfig.get().ignoreChat) return;

        String message = packet.content().getString();
        for (String playerName : IgnoringConfig.get().ignoredPlayerList) {
            if (message.contains(playerName)) {
                ci.cancel();
                break;
            }
        }
    }


}
