package ovh.fedox.flockapi.database.service.punishment;


import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import ovh.fedox.flockapi.database.service.flockeconomy.FlockEconomyAPI;

/**
 * PunishmentHook.java - Utility class for integrating with other plugins
 * <p>
 * Created on 4/3/2025 at 1:35 PM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

public class PunishmentHook {

	/**
	 * Register the economy API as a service for other plugins to use
	 *
	 * @param plugin The plugin instance
	 */
	public static void registerService(JavaPlugin plugin) {
		plugin.getServer().getServicesManager().register(
				FlockEconomyAPI.class,
				FlockEconomyAPI.getInstance(),
				plugin,
				ServicePriority.Normal
		);
	}


}
