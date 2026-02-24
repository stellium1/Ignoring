package org.stellium.ignoring.render;

import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.stellium.ignoring.config.IgnoringConfig;
import org.stellium.ignoring.entity.EntityCaptures;

/*
 * This file is part of Transparent-Entities(https://github.com/LopyMine/Transparent-Entities)
 * Copyright (C) LopyMine(https://github.com/LopyMine)
 *
 * Modified by stellium1 in Ignoring(https://github.com/stellium1/Ignoring).
 * Licensed under the GNU Lesser General Public License v3.0
*/
public class TransparencyRenderer {

    private static final ThreadLocal<Entity> CURRENT_ENTITY = new ThreadLocal<>();

    public static void setCurrentEntity(Entity entity) {
        IgnoringConfig cfg = IgnoringConfig.get();
        if (cfg.ignoreRender && cfg.shouldIgnorePlayer(entity)) {
            CURRENT_ENTITY.set(entity);
            EntityCaptures.MAIN.setEntity(entity);
        }
    }

    public static void clearCurrentEntity() {
        if (CURRENT_ENTITY.get() != null) {
            CURRENT_ENTITY.remove();
            EntityCaptures.MAIN.clearEntity();
        }
    }

    @Nullable
    public static Entity getCurrentEntity() {
        return CURRENT_ENTITY.get();
    }

    public static void handleEntityRendering(Entity entity, Runnable renderCall) {
        IgnoringConfig cfg = IgnoringConfig.get();

        if (!cfg.ignoreRender) {
            renderCall.run();
            return;
        }

        if (!cfg.shouldIgnorePlayer(entity)) {
            renderCall.run();
            return;
        }

        EntityCaptures.MAIN.setEntity(entity);
        renderCall.run();
        EntityCaptures.MAIN.clearEntity();
    }

}
