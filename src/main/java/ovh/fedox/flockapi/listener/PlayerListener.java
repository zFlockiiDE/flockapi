package ovh.fedox.flockapi.listener;

import com.earth2me.essentials.libs.kyori.adventure.text.Component;
import io.papermc.paper.advancement.AdvancementDisplay;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;
import ovh.fedox.flockapi.FlockAPI;
import ovh.fedox.flockapi.constants.Groups;
import ovh.fedox.flockapi.database.RedisManager;
import ovh.fedox.flockapi.database.service.punishment.PunishmentService;
import ovh.fedox.flockapi.settings.Settings;
import ovh.fedox.flockapi.util.ColorUtil;

import java.util.UUID;

/**
 * PlayerListener.java - Listener for player-related events
 * <p>
 * Created on 3/31/2025 at 4:18 AM by Fedox. Copyright © 2025 Fedox. All rights reserved.
 */

@AutoRegister
public final class PlayerListener implements Listener {

	/**
	 * Initialize the teams for the scoreboard
	 */
	public static void initializeTeams() {
		try {
			Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

			// Create default team if it doesn't exist
			Team defaultTeam = scoreboard.getTeam("z_default");
			if (defaultTeam == null) {
				defaultTeam = scoreboard.registerNewTeam("z_default");
			}

			for (Groups group : Groups.values()) {
				String teamName = group.getPriority() + "_" + group.getIdentifier();
				Team team = scoreboard.getTeam(teamName);
				if (team == null) {
					team = scoreboard.registerNewTeam(teamName);
				}

				String formatted = ColorUtil.format("<gradient=" + group.getGradientStart() + "," + group.getGradientEnd() + "><bold>" + group.getName().toUpperCase() + "</bold></gradient>");
				team.setPrefix(formatted + " §8§l➛ §r§f");
			}
		} catch (Exception e) {
			FlockAPI.getInstance().getLogger().severe("Error initializing teams: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Handle player advancement events
	 *
	 * @param event The advancement event
	 */
	@EventHandler
	public void onPlayerAchievement(PlayerAdvancementDoneEvent event) {
		event.message(null);

		if (Settings.PlayerListener.ACHIEVEMENTS) {
			String key = event.getAdvancement().getKey().getKey();

			if (key.startsWith("recipes/")) {
				return;
			}

			if (key.equals("root")) {
				return;
			}

			AdvancementDisplay display = event.getAdvancement().getDisplay();

			String translatedTitle;
			if (display != null) {
				try {
					translatedTitle = Component.translatable(
							"advancements." + key.replace("/", ".") + ".title").toString();
				} catch (NoClassDefFoundError e) {
					translatedTitle = key.substring(key.lastIndexOf('/') + 1).replace('_', ' ');
					translatedTitle = translatedTitle.substring(0, 1).toUpperCase() + translatedTitle.substring(1);
				}
			} else {
				translatedTitle = key.substring(key.lastIndexOf('/') + 1).replace('_', ' ');
				translatedTitle = translatedTitle.substring(0, 1).toUpperCase() + translatedTitle.substring(1);
			}

			if (translatedTitle.equalsIgnoreCase("Root")) {
				return;
			}

			Bukkit.broadcastMessage(Common.colorize("&8&l[&a⚔&8&l] &f" + event.getPlayer().getName() +
					" &7folgendes Achievement erspielt: &a" + translatedTitle));

		}

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event) {
		String joinedAddress = event.getRealAddress().getHostAddress();
		String bungeeCordAddress = Settings.PROXY_IP;

		System.out.println("joinedAddress: " + joinedAddress);
		System.out.println("bungeeCordAddress: " + bungeeCordAddress);

		if (!joinedAddress.equals(bungeeCordAddress)) {
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Common.colorize("deine mudda stinkt nach fisch"));
		}
	}

	/**
	 * Handle player join events
	 *
	 * @param event The player join event
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);

		final Player player = event.getPlayer();
		final UUID playerUniqueId = player.getUniqueId();

		if (PunishmentService.getInstance().checkBan(player)) {
			player.setGameMode(GameMode.SPECTATOR);
			return;
		}

		if (Settings.PlayerListener.JOIN_MESSAGE) {
			Bukkit.broadcastMessage(Common.colorize("&8&l[&a+&8&l] &7" + player.getName()));
		}

		if (Settings.General_Settings.COUNT_PLAYERS) {
			String serverName = FlockAPI.getProperties().get("server-name");
			int playerCount = Bukkit.getOnlinePlayers().size();

			RedisManager.getJedis().hset("players", serverName, String.valueOf(playerCount));
		}


		FlockAPI.getMongoManager().getApiPlayerRepository().createIfNotExists(playerUniqueId, player.getName());
		FlockAPI.getMongoManager().getApiPlayerRepository().updateLastJoin(playerUniqueId);

		String groupName = FlockAPI.getLuckPerms().getUserManager().getUser(playerUniqueId).getPrimaryGroup();

		addPlayerToTeam(player, groupName);
	}

	/**
	 * Handle player quit events
	 *
	 * @param event The player quit event
	 */
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);

		final Player player = event.getPlayer();

		if (PunishmentService.getInstance().checkBan(player)) {
			return;
		}

		if (Settings.PlayerListener.QUIT_MESSAGE) {
			Bukkit.broadcastMessage(Common.colorize("&8&l[&c-&8&l] &7" + player.getName()));
		}

		if (Settings.General_Settings.COUNT_PLAYERS) {
			String serverName = FlockAPI.getProperties().get("server-name");
			int playerCount = Bukkit.getOnlinePlayers().size() - 1;

			RedisManager.getJedis().hset("players", serverName, String.valueOf(playerCount));
		}
	}

	/**
	 * Handle player chat events
	 *
	 * @param event The player chat event
	 */
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {

		final Player player = event.getPlayer();

		if (PunishmentService.getInstance().checkMute(player)) {
			event.setCancelled(true);
			return;
		}

		if (Settings.PlayerListener.CHAT_FORMAT) {
			String groupName = FlockAPI.getLuckPerms().getUserManager().getUser(player.getUniqueId()).getPrimaryGroup();
			Groups group = Groups.getGroupByName(groupName);

			String formattedMessage = ColorUtil.format("<color=#e6e6e6>" + event.getMessage() + "</color>");

			if (group != null) {
				String formatted = ColorUtil.format("<gradient=" + group.getGradientStart() + "," + group.getGradientEnd() + "><bold>" + group.getIdentifier().toUpperCase() + "</bold></gradient>");

				event.setCancelled(true);

				Bukkit.broadcastMessage(formatted + " §r§f" + player.getName() + " §8§l➛ §r" + formattedMessage);
			} else {
				event.setCancelled(true);
				Bukkit.broadcastMessage(player.getName() + " §8§l➛ §r" + formattedMessage);
			}
		}

	}

	/**
	 * Add a player to a team based on their group
	 *
	 * @param player    The player to add
	 * @param groupName The group name to add the player to
	 */
	/**
	 * Add a player to a team based on their group
	 *
	 * @param player    The player to add
	 * @param groupName The group name to add the player to
	 */
	private void addPlayerToTeam(Player player, String groupName) {
		try {
			Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
			String playerName = player.getName();

			Team currentTeam = null;
			for (Team team : scoreboard.getTeams()) {
				if (team.hasEntry(playerName)) {
					currentTeam = team;
					break;
				}
			}

			Team targetTeam = null;
			if (groupName.equals("default")) {
				targetTeam = scoreboard.getTeam("z_default");
			} else {
				Groups group = Groups.getGroupByName(groupName);
				if (group != null) {
					String teamName = group.getPriority() + "_" + group.getIdentifier();
					targetTeam = scoreboard.getTeam(teamName);
				}
			}

			if (currentTeam != null && (!currentTeam.equals(targetTeam))) {
				currentTeam.removeEntry(playerName);
			}

			if (targetTeam != null && (currentTeam == null || !currentTeam.equals(targetTeam))) {
				targetTeam.addEntry(playerName);

				if (groupName.equals("default")) {
					player.setPlayerListName(playerName);
				} else {
					Groups group = Groups.getGroupByName(groupName);
					if (group != null) {
						String formatted = ColorUtil.format("<gradient=" + group.getGradientStart() + "," + group.getGradientEnd() + "><bold>" + group.getName().toUpperCase() + "</bold></gradient>");
						player.setPlayerListName(formatted + " §8§l➛ §r§f" + playerName);
					}
				}
			}
		} catch (Exception e) {
			FlockAPI.getInstance().getLogger().warning("Error managing team for player " + player.getName() + ": " + e.getMessage());
			e.printStackTrace();
		}
	}

}
