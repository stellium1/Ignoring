package org.stellium.ignoring.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import java.util.ArrayList;
import java.util.List;

@Config(name = "ignoring")
public class IgnoringConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.TransitiveObject
    public boolean ignoreChat = false;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.TransitiveObject
    public boolean ignoreRender = false;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.TransitiveObject
    public boolean ignoreTablist = false;

    @ConfigEntry.Gui.Tooltip(count = 2)
    @ConfigEntry.Gui.TransitiveObject
    public boolean interactionThroughIgnoredPlayer = false;


    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 0, max = 255)
    public int transparency = 255;

    @ConfigEntry.Gui.Tooltip
    public List<String> ignoredPlayerList = new ArrayList<>();



    public IgnoringConfig() {
        if (ignoredPlayerList.isEmpty()) {
            ignoredPlayerList.add("Insert name");
        }
    }

    static void validate(IgnoringConfig config) {
        if (config.ignoredPlayerList != null) {
            config.ignoredPlayerList.removeIf(name -> name == null || name.isBlank());
        }
        // 🔥 투명도 값도 유효성 검사 추가
        if (config.transparency < 0) config.transparency = 0;
        if (config.transparency > 255) config.transparency = 255;
    }

    public static void init() {
        AutoConfig.register(IgnoringConfig.class, GsonConfigSerializer::new);
    }

    public static IgnoringConfig get() {
        IgnoringConfig config = AutoConfig.getConfigHolder(IgnoringConfig.class).getConfig();
        validate(config);
        return config;
    }

    @Override
    public void validatePostLoad() throws ValidationException {
        validate(this);
    }
}
