package org.stellium.ignoring.mixin.network;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.stellium.ignoring.config.IgnoringConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Shadow
    @Mutable
    @Final
    private Set<PlayerListEntry> listedPlayerListEntries;

    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        IgnoringConfig config = IgnoringConfig.get();

        Text original = packet.content();
        String raw = original.getString();

        if (config.ignoreChat) {
            if (config.ignoreEveryone) {
                ci.cancel();
                return;
            }
            for (String playerName : config.ignoredPlayerList) {
                if (raw.contains(playerName)) {
                    ci.cancel();
                    return;
                }
            }
        }

        if (!config.ignoreSpecialCharacter) return;

        Text modified = trimTailOne(original);
        if (modified == null) return;

        ci.cancel();
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(modified);
    }

    private static Text trimTailOne(Text original) {
        List<Style> styles = new ArrayList<>();
        List<String> strings = new ArrayList<>();

        original.visit((style, string) -> {
            styles.add(style);
            strings.add(string);
            return Optional.empty();
        }, Style.EMPTY);

        int pi = strings.size() - 1;
        int off = (pi >= 0 && strings.get(pi) != null) ? strings.get(pi).length() : 0;
        if (pi < 0) return null;

        int[] pos = prevNonWs(strings, pi, off);
        pi = pos[0];
        off = pos[1];
        if (pi < 0) return null;

        String s = strings.get(pi);
        int cp = s.codePointBefore(off);
        if (!isRemovable(cp)) return null;

        int start = s.offsetByCodePoints(off, -1);
        strings.set(pi, s.substring(0, start) + s.substring(off));

        MutableText out = Text.empty();
        for (int k = 0; k < strings.size(); k++) {
            String part = strings.get(k);
            if (part == null || part.isEmpty()) continue;
            out.append(Text.literal(part).setStyle(styles.get(k)));
        }
        return out;
    }

    private static int[] prevNonWs(List<String> strings, int pi, int off) {
        int p = pi;
        int o = off;
        while (p >= 0) {
            String s = strings.get(p);
            if (s == null || o <= 0) {
                p--;
                o = (p >= 0 && strings.get(p) != null) ? strings.get(p).length() : 0;
                continue;
            }
            int cp = s.codePointBefore(o);
            if (!Character.isWhitespace(cp)) return new int[]{p, o};
            o = s.offsetByCodePoints(o, -1);
        }
        return new int[]{-1, 0};
    }

    private static boolean isRemovable(int cp) {
        if (cp >= 0x30 && cp <= 0x39) return false;
        if (cp >= 0x41 && cp <= 0x5A) return false;
        if (cp >= 0x61 && cp <= 0x7A) return false;
        if (cp >= 0xAC00 && cp <= 0xD7A3) return false;

        if (cp == '[' || cp == ']' || cp == '(' || cp == ')' || cp == '{' || cp == '}' || cp == '<' || cp == '>') return false;
        if (cp == '.' || cp == ',' || cp == '!' || cp == '?' || cp == ':' || cp == ';') return false;
        if (cp == '\'' || cp == '"' || cp == '-' || cp == '_' || cp == '~') return false;

        int type = Character.getType(cp);
        return type == Character.OTHER_SYMBOL || type == Character.MATH_SYMBOL || type == Character.MODIFIER_SYMBOL;
    }
}
