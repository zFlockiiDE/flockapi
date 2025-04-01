package ovh.fedox.flockapi.listener;

import com.earth2me.essentials.libs.kyori.adventure.text.Component;
import io.papermc.paper.advancement.AdvancementDisplay;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;
import ovh.fedox.flockapi.FlockAPI;
import ovh.fedox.flockapi.constants.Groups;
import ovh.fedox.flockapi.database.RedisManager;
import ovh.fedox.flockapi.settings.Settings;
import ovh.fedox.flockapi.util.ColorUtil;

/**
 * PlayerListener.java - Listener for player-related events
 * <p>
 * Created on 3/31/2025 at 4:18 AM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

@AutoRegister
public final class PlayerListener implements Listener {

	public static void initializeTeams() {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

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
	}

	@EventHandler
	public void onPlayerAchievement(PlayerAdvancementDoneEvent event) {
		event.message(null);

		if (Settings.PlayerListener.ACHIEVEMENTS) {
			String key = event.getAdvancement().getKey().getKey();

			if (key.startsWith("recipes/")) {
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

			Bukkit.broadcastMessage(Common.colorize("&8&l[&a⚔&8&l] &f" + event.getPlayer().getName() +
					" &7folgendes Achievement erspielt: &a" + translatedTitle));

		}

	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);

		if (Settings.PlayerListener.JOIN_MESSAGE) {
			Bukkit.broadcastMessage(Common.colorize("&8&l[&a+&8&l] &7" + event.getPlayer().getName()));
		}

		if (Settings.General_Settings.COUNT_PLAYERS) {
			String serverName = FlockAPI.getProperties().get("server-name");
			int playerCount = Bukkit.getOnlinePlayers().size();

			RedisManager.getJedis().hset("players", serverName, String.valueOf(playerCount));
		}

		FlockAPI.getMongoManager().getApiPlayerRepository().createIfNotExists(event.getPlayer().getUniqueId(), event.getPlayer().getName());
		FlockAPI.getMongoManager().getApiPlayerRepository().updateLastJoin(event.getPlayer().getUniqueId());

		String groupName = FlockAPI.getLuckPerms().getUserManager().getUser(event.getPlayer().getUniqueId()).getPrimaryGroup();

		addPlayerToTeam(event.getPlayer(), groupName);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);

		if (Settings.PlayerListener.QUIT_MESSAGE) {
			Bukkit.broadcastMessage(Common.colorize("&8&l[&c-&8&l] &7" + event.getPlayer().getName()));
		}

		if (Settings.General_Settings.COUNT_PLAYERS) {
			String serverName = FlockAPI.getProperties().get("server-name");
			int playerCount = Bukkit.getOnlinePlayers().size() - 1;

			RedisManager.getJedis().hset("players", serverName, String.valueOf(playerCount));
		}
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {

		final Player player = event.getPlayer();

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

	private void addPlayerToTeam(Player player, String groupName) {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

		for (Team team : scoreboard.getTeams()) {
			if (team.hasEntry(player.getName())) {
				team.removeEntry(player.getName());
			}
		}

		if (groupName.equals("default")) {
			Team defaultTeam = scoreboard.getTeam("z_default");
			if (defaultTeam != null) {
				defaultTeam.addEntry(player.getName());
				player.setPlayerListName(player.getName());
			}
		} else {
			Groups group = Groups.getGroupByName(groupName);

			if (group != null) {
				String teamName = group.getPriority() + "_" + group.getIdentifier();
				Team team = scoreboard.getTeam(teamName);

				if (team != null) {
					team.addEntry(player.getName());

					String formatted = ColorUtil.format("<gradient=" + group.getGradientStart() + "," + group.getGradientEnd() + "><bold>" + group.getName().toUpperCase() + "</bold></gradient>");
					player.setPlayerListName(formatted + " §8§l➛ §r§f" + player.getName());
				}
			}
		}
	}

}
