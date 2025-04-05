package ovh.fedox.flockapi.util;


import lombok.experimental.UtilityClass;

/**
 * GuiUtil.java - Utilities for every gui
 * <p>
 * Created on 3/25/2025 at 1:08 PM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

@UtilityClass
public class GuiUtil {

	public int[] getCornerSlots(int rows) {
		switch (rows) {
			case 1 -> {
				return new int[]{0, 8};
			}
			case 2 -> {
				return new int[]{0, 8, 9, 17};
			}
			case 3 -> {
				return new int[]{0, 1, 7, 8, 9, 17, 18, 19, 25, 26};
			}
			case 4 -> {
				return new int[]{0, 1, 7, 8, 9, 17, 18, 26, 27, 28, 34, 35};
			}
			case 5 -> {
				return new int[]{0, 1, 7, 8, 9, 17, 27, 35, 36, 37, 37, 43, 44};
			}
			case 6 -> {
				return new int[]{0, 1, 7, 8, 9, 17, 36, 44, 45, 46, 52, 53};
			}
		}
		return new int[]{};
	}

}
