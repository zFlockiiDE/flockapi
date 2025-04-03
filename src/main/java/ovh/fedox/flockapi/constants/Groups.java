package ovh.fedox.flockapi.constants;


import lombok.Getter;

/**
 * Groups.java - Enum for different player groups
 * <p>
 * Created on 3/31/2025 at 4:46 AM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

@Getter
public enum Groups {

	LE_CHEF("le_chef", "Le chef", "#389c2d", "#38de26", 1),
	DEVELOPER("developer", "dev", "#35adb8", "#24dced", 2),
	MODERATOR("moderator", "mod", "#bd2626", "#de1616", 3),
	BUILDER("builder", "builder", "#a3a3a3", "#d9d9d9", 4),
	CREATOR("creator", "creator", "#961da1", "#ce21de", 5),
	COOL("cool", "cool", "#244c8c", "#2e6fd9", 6);

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

	/**
	 * Get a group by its name
	 *
	 * @param name the name of the group
	 * @return the group if found, null otherwise
	 */
	public static Groups getGroupByName(String name) {
		for (Groups group : Groups.values()) {
			if (group.getIdentifier().equalsIgnoreCase(name)) {
				return group;
			}
		}
		return null;
	}

}
