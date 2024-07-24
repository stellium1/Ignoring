package org.stellium.ignoring;

import net.fabricmc.api.ModInitializer;
import org.stellium.ignoring.config.IgnoringConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Ignoring implements ModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("Ignoring");

    @Override
    public void onInitialize() {
        LOGGER.info("Initialized");
        IgnoringConfig.init();
    }
}
