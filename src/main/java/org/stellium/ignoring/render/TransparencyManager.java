package org.stellium.ignoring.render;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.stellium.ignoring.config.IgnoringConfig;
import org.stellium.ignoring.util.ArgbUtils;

/*
 * This file is part of Transparent-Entities(https://github.com/LopyMine/Transparent-Entities)
 * Copyright (C) LopyMine(https://github.com/LopyMine)
 *
 * Modified by stellium1 in Ignoring(https://github.com/stellium1/Ignoring).
 * Licensed under the GNU Lesser General Public License v3.0
*/
public class TransparencyManager {

	public static int getTranslucentArgb(Entity entity, int original) {
		if (!IgnoringConfig.get().ignoreRender) {
			return original;
		}

		MinecraftClient client = MinecraftClient.getInstance();
		ClientPlayerEntity player = client.player;
		ClientWorld world = client.world;
		Vec3d cameraPos = client.gameRenderer.getCamera().getPos();
		Vec3d entityPos = entity.getPos();

		if (player == null || world == null) {
			return original;
		}

		if (player.equals(entity)) {
			return getColorForYourself(original);
		}

		if (entity.isInvisibleTo(player)) {
			return original;
		}

		return ArgbUtils.swapAlpha(original, getAlpha(cameraPos, entityPos));
	}

	private static int getAlpha(Vec3d cameraPos, Vec3d entityPos) {
		float hidingActivationDistance = 4.0F;
		float fullHidingDistance = 2.8F;
		float minHidingValue = 0.2F;
		float distance = calculateDistance(cameraPos, entityPos);

		if (hidingActivationDistance <= 0F) {
			return 255;
		}

		float a = hidingActivationDistance - fullHidingDistance;
		float b = distance - fullHidingDistance;

		if (a <= 0F || b <= 0F) {
			return (int) (minHidingValue * 255F);
		}

		if (b >= a) {
			return 255;
		}

		return (int) (MathHelper.clamp((minHidingValue + ((1F - minHidingValue) * (b / a))), 0F, 1F) * 255F);
	}

	private static int getColorForYourself(int original) {
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			return ArgbUtils.swapAlpha(original, (int) (255F * 0.2F));
		}
		return original;
	}

	public static float calculateDistance(Vec3d cameraPos, Vec3d entityPos) {
		float f = (float)(cameraPos.getX() - entityPos.getX());
		float g = (float)(cameraPos.getY() - entityPos.getY());
		float h = (float)(cameraPos.getZ() - entityPos.getZ());
		return MathHelper.sqrt(f * f + g * g + h * h);
	}


	public static boolean canRenderTransparencyShadow(Entity entity) {
		IgnoringConfig config = IgnoringConfig.get();
		return config.ignoreRender && config.shouldIgnorePlayer(entity);

	}
}
