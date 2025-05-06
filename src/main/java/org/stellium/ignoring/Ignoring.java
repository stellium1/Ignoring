


package org.stellium.ignoring;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stellium.ignoring.config.IgnoringConfig;


public class Ignoring implements ModInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger("Ignoring");

    @Override
    public void onInitialize() {
        LOGGER.info("Initialized");
        KeyBinding openConfigKeybind = new KeyBinding(
          "text.ignoring.key.openConfig",
          InputUtil.Type.KEYSYM,
          GLFW.GLFW_KEY_P,
          "text.ignoring.key.category"
        );

        KeyBinding toggleIgnoreRenderKeybind = new KeyBinding(
          "text.ignoring.key.toggleIgnoreRender",
          InputUtil.Type.KEYSYM,
          GLFW.GLFW_KEY_SEMICOLON,
          "text.ignoring.key.category"
        );

        KeyBinding toggleIgnoreChatKeybind = new KeyBinding(
          "text.ignoring.key.toggleIgnoreChat",
          InputUtil.Type.KEYSYM,
          GLFW.GLFW_KEY_APOSTROPHE,
          "text.ignoring.key.category"
        );

        KeyBinding toggleIgnoreTablistKeybind = new KeyBinding(
          "text.ignoring.key.toggleIgnoreTablist",
          InputUtil.Type.KEYSYM,
          GLFW.GLFW_KEY_UNKNOWN,
          "text.ignoring.key.category"
        );

        KeyBinding toggleInteractionThroughIgnoredPlayerKeybind = new KeyBinding(
          "text.ignoring.key.toggleInteractionThroughIgnoredPlayer",
          InputUtil.Type.KEYSYM,
          GLFW.GLFW_KEY_UNKNOWN,
          "text.ignoring.key.category"
        );

        KeyBindingHelper.registerKeyBinding(openConfigKeybind);
        KeyBindingHelper.registerKeyBinding(toggleIgnoreRenderKeybind);
        KeyBindingHelper.registerKeyBinding(toggleIgnoreChatKeybind);
        KeyBindingHelper.registerKeyBinding(toggleIgnoreTablistKeybind);
        KeyBindingHelper.registerKeyBinding(toggleInteractionThroughIgnoredPlayerKeybind);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openConfigKeybind.wasPressed()) {
                client.setScreen(AutoConfig.getConfigScreen(IgnoringConfig.class, client.currentScreen).get());
            }

            if (toggleIgnoreRenderKeybind.wasPressed()) {
                boolean before = IgnoringConfig.get().ignoreRender;
                IgnoringConfig.get().ignoreRender = !before;
                AutoConfig.getConfigHolder(IgnoringConfig.class).save();

                client.inGameHud.setOverlayMessage(
                    getToggleText("ignoreRender", before),
                    false
                );
            }

            if (toggleIgnoreChatKeybind.wasPressed()) {
                boolean before = IgnoringConfig.get().ignoreChat;
                IgnoringConfig.get().ignoreChat = !before;
                AutoConfig.getConfigHolder(IgnoringConfig.class).save();

                client.inGameHud.setOverlayMessage(
                    getToggleText("ignoreChat", before),
                    false
                );
            }

            if (toggleIgnoreTablistKeybind.wasPressed()) {
                boolean before = IgnoringConfig.get().ignoreTablist;
                IgnoringConfig.get().ignoreTablist = !before;
                AutoConfig.getConfigHolder(IgnoringConfig.class).save();

                client.inGameHud.setOverlayMessage(
                    getToggleText("ignoreTablist", before),
                    false
                );
            }

            if (toggleInteractionThroughIgnoredPlayerKeybind.wasPressed()) {
                boolean before = IgnoringConfig.get().interactionThroughIgnoredPlayer;
                IgnoringConfig.get().interactionThroughIgnoredPlayer = !before;
                AutoConfig.getConfigHolder(IgnoringConfig.class).save();

                client.inGameHud.setOverlayMessage(
                    getToggleText("interactionThroughIgnoredPlayer", before),
                    false
                );
            }
        });

    }

    private Text getToggleText(String optionName, boolean original) {
        Text name = Text.translatable("text.ignoring.toggle." + optionName);
        Text status = Text.translatable(
                original
                    ? "text.ignoring.status.disabled"
                    : "text.ignoring.status.enabled"
            )
            .formatted(original ? Formatting.RED : Formatting.GREEN);

        return Text.empty()
                   .append(name)
                   .append(Text.literal(": "))
                   .append(status);
    }

}
