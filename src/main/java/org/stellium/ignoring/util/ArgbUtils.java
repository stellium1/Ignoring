package org.stellium.ignoring.util;

/*
 * This file is part of Transparent-Entities(https://github.com/LopyMine/Transparent-Entities)
 * Copyright (C) LopyMine(https://github.com/LopyMine)
 *
 * Modified by stellium1 in Ignoring(https://github.com/stellium1/Ignoring).
 * Licensed under the GNU Lesser General Public License v3.0
 */
public class ArgbUtils {

	public static int applyAlpha(int argb, int alpha) {
		return (argb & 0x00FFFFFF) | (alpha << 24);
	}


	public static int swapAlpha(int argb, int alpha) {
		return getArgb(Math.min(getAlpha(argb), alpha), getRed(argb), getGreen(argb), getBlue(argb));
	}

	public static int getAlpha(int argb) {
		return argb >>> 24;
	}

	public static int getRed(int argb) {
		return argb >> 16 & 255;
	}

	public static int getGreen(int argb) {
		return argb >> 8 & 255;
	}

	public static int getBlue(int argb) {
		return argb & 255;
	}

	public static int getArgb(int alpha, int red, int green, int blue) {
		return alpha << 24 | red << 16 | green << 8 | blue;
	}

}