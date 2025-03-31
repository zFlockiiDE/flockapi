package ovh.fedox.flockapi.constants;


import lombok.Getter;

/**
 * Groups.java -
 * <p>
 * Created on 3/31/2025 at 4:46 AM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

@Getter
public enum Groups {

	CREATOR("creator", "creator", "#389c2d", "#38de26", 1),
	ENTWICKER("entwickler", "dev", "#35adb8", "#24dced", 2),
	BUILDER("builder", "builder", "#a3a3a3", "#d9d9d9", 3),
	SUPPORTER("supporter", "sup", "#857d19", "#b0a613", 4),
	COOL("cool", "cool", "#80248c", "#bf22d4", 5);

	private final String identifier;
	private final String name;
	private final String gradientStart;
	private final String gradientEnd;
	private final int priority;

	Groups(String identifier, String name, String gradientStart, String gradientEnd, int priority) {
		this.identifier = identifier;
		this.name = name;
		this.gradientStart = gradientStart;
		this.gradientEnd = gradientEnd;
		this.priority = priority;
	}

	public static Groups getGroupByName(String name) {
		for (Groups group : Groups.values()) {
			if (group.getIdentifier().equalsIgnoreCase(name)) {
				return group;
			}
		}
		return null;
	}

}
