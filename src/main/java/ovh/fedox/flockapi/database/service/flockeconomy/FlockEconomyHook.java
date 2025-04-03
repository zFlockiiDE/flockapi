package ovh.fedox.flockapi.database.service.flockeconomy;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * FlockEconomyHook.java - Utility class for integrating with other plugins
 * <p>
 * Created on 4/2/2025 by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */
public class FlockEconomyHook {

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

	/**
	 * Example of how to use the API from another plugin
	 *
	 * @param player The player to check
	 * @return The player's balance
	 */
	public static double getPlayerBalance(OfflinePlayer player) {
		return FlockEconomyAPI.getInstance().getBalance(player.getUniqueId());
	}

	/**
	 * Example of how to add flocks to a player from another plugin
	 *
	 * @param player     The player to add flocks to
	 * @param amount     The amount to add
	 * @param reason     The reason for adding
	 * @param pluginName The name of the plugin adding the flocks
	 * @return True if successful
	 */
	public static boolean addFlocksToPlayer(OfflinePlayer player, double amount, String reason, String pluginName) {
		return FlockEconomyAPI.getInstance().addFlocks(
				player.getUniqueId(),
				amount,
				reason,
				pluginName
		);
	}

	/**
	 * Example of how to remove flocks from a player from another plugin
	 *
	 * @param player     The player to remove flocks from
	 * @param amount     The amount to remove
	 * @param reason     The reason for removing
	 * @param pluginName The name of the plugin removing the flocks
	 * @return True if successful
	 */
	public static boolean removeFlocksFromPlayer(OfflinePlayer player, double amount, String reason, String pluginName) {
		return FlockEconomyAPI.getInstance().removeFlocks(
				player.getUniqueId(),
				amount,
				reason,
				pluginName
		);
	}

	/**
	 * Example of how to transfer flocks between players from another plugin
	 *
	 * @param from       The player sending flocks
	 * @param to         The player receiving flocks
	 * @param amount     The amount to transfer
	 * @param reason     The reason for the transfer
	 * @param pluginName The name of the plugin initiating the transfer
	 * @return True if successful
	 */
	public static boolean transferFlocksBetweenPlayers(UUID from, UUID to, double amount, String reason, String pluginName) {
		return FlockEconomyAPI.getInstance().transferFlocks(
				from,
				to,
				amount,
				reason,
				pluginName
		);
	}
}

