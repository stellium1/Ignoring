package org.stellium.ignoring.mixin.hud;

import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(PlayerListHud.class)
public interface PlayerListHudInvoker {
    @Invoker("collectPlayerEntries")
    List<PlayerListEntry> invokeCollectPlayerEntries();
}