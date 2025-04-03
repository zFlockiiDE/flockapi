package ovh.fedox.flockapi.database.service.punishment;


import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.remain.Remain;
import ovh.fedox.flockapi.FlockAPI;
import ovh.fedox.flockapi.constants.PunishReason;
import ovh.fedox.flockapi.constants.PunishType;
import ovh.fedox.flockapi.database.MongoDBManager;
import ovh.fedox.flockapi.database.model.impl.PlayerPunishment;
import ovh.fedox.flockapi.database.repository.impl.ApiPlayerRepository;
import ovh.fedox.flockapi.database.repository.impl.PlayerPunishmentRepository;
import ovh.fedox.flockapi.util.SoundUtil;

import java.text.DateFormat;
import java.util.*;

import static ovh.fedox.flockapi.FlockAPI.BUNGEECORD_CHANNEL;

/**
 * PunishmentService.java - The service class for the punishment system.
 * <p>
 * Created on 4/3/2025 at 1:36 PM by Fedox. Copyright © 2025 Fedox. All rights reserved.
 */

public class PunishmentService {

	public static List<String> banTemplate = List.of(
			"§c§l✖ §8§m                    §c§l[ §4§lBANN §c§l]§8§m                    §r§c§l ✖",
			"",
			"§4§l⚠ §c§lDu wurdest vom Netzwerk ausgeschlossen! §4§l⚠",
			"",
			"§8» §7Grund: §c%reason%",
			"§8» §7Gebannt von: §c%banner%",
			"§8» §7Ablauf: §c%until%",
			"",
			"§7Du kannst einen Entbannungsantrag auf unserem Discord stellen:",
			"§c§ndiscord.zflockii.de",
			"",
			"§8§m                                                                §r"
	);

	private static PunishmentService instance;
	private final ApiPlayerRepository playerRepository;
	private final PlayerPunishmentRepository punishmentRepository;

	/**
	 * Private constructor to enforce singleton pattern
	 *
	 * @param mongoDBManager The MongoDB manager
	 */
	private PunishmentService(MongoDBManager mongoDBManager) {
		this.playerRepository = mongoDBManager.getApiPlayerRepository();
		this.punishmentRepository = mongoDBManager.getPlayerPunishmentRepository();
	}

	/**
	 * Initialize the punishment API
	 *
	 * @param mongoDBManager The MongoDB manager
	 */
	public static void initialize(MongoDBManager mongoDBManager) {
		if (instance == null) {
			instance = new PunishmentService(mongoDBManager);
		}
	}

	/**
	 * Get the punishment API instance
	 *
	 * @return The punishment API instance
	 * @throws IllegalStateException if the API has not been initialized
	 */
	public static PunishmentService getInstance() {
		if (instance == null) {
			throw new IllegalStateException("PunishmentService has not been initialized. Call initialize() first.");
		}
		return instance;
	}

	/**
	 * Punish a player with a specific reason
	 *
	 * @param player The player to punish
	 * @param banner The player who is punishing
	 * @param reason The reason for the punishment
	 */
	public void punishPlayer(OfflinePlayer player, Player banner, PunishReason reason) {
		PlayerPunishment punishment = new PlayerPunishment();

		Date now = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DAY_OF_MONTH, reason.getDuration());
		Date until = calendar.getTime();

		punishment.setAt(now);
		punishment.setPunisher(banner.getUniqueId().toString());
		punishment.setReason(reason.getName());
		punishment.setType(reason.getType());
		punishment.setUntil(until);
		punishment.setUuid(player.getUniqueId().toString());

		punishmentRepository.save(punishment);

		String formattedDate = DateFormat.getDateInstance().format(until);

		if (player.isOnline()) {
			if (reason.getType() == PunishType.BAN) {
				String banMessage = String.join("\n", banTemplate)
						.replace("%reason%", reason.getName())
						.replace("%banner%", banner.getName())
						.replace("%until%", formattedDate);

				kickPlayerFromNetwork(banner, player.getName(), banMessage);
			} else {
				Remain.sendTitle(player.getPlayer(), 10, 70, 20, "&4&l✖", "&cDu wurdest für " + reason.getDuration() + " Tag(e) stummgeschaltet!");
				SoundUtil.playSound(player.getPlayer(), SoundUtil.SoundType.WARNING);
			}
		}
	}

	/**
	 * Check if a player is banned
	 *
	 * @param player The player to check
	 * @return true if the player is banned, false otherwise
	 */
	public boolean checkBan(Player player) {
		Optional<PlayerPunishment> punishment = punishmentRepository.findByUUID(player.getUniqueId());

		if (punishment.isPresent()) {
			PlayerPunishment playerPunishment = punishment.get();

			if (playerPunishment.getType() == PunishType.BAN) {
				Date now = new Date();
				if (now.after(playerPunishment.getUntil())) {
					punishmentRepository.delete(playerPunishment);
					return false;
				}

				String bannerName = "System";
				try {
					UUID bannerUUID = UUID.fromString(playerPunishment.getPunisher());
					OfflinePlayer bannerPlayer = Bukkit.getOfflinePlayer(bannerUUID);
					if (bannerPlayer.hasPlayedBefore()) {
						bannerName = bannerPlayer.getName();
					}
				} catch (Exception e) {
					Common.logFramed("Error while getting the banner name: ", e.getMessage());
				}

				String banMessage = String.join("\n", banTemplate)
						.replace("%reason%", playerPunishment.getReason())
						.replace("%banner%", bannerName)
						.replace("%until%", DateFormat.getDateInstance().format(playerPunishment.getUntil()));

				Bukkit.getScheduler().runTaskLater(FlockAPI.getInstance(), () -> {
					kickPlayerFromNetwork(player, player.getName(), banMessage);
				}, 5L);

				return true;
			}
		}

		return false;
	}

	/**
	 * Check if a player is muted
	 *
	 * @param player The player to check
	 * @return true if the player is muted, false otherwise
	 */
	public boolean checkMute(Player player) {
		Optional<PlayerPunishment> punishment = punishmentRepository.findByUUID(player.getUniqueId());

		if (punishment.isPresent()) {
			PlayerPunishment playerPunishment = punishment.get();

			if (playerPunishment.getType() == PunishType.MUTE) {
				Date now = new Date();
				if (now.after(playerPunishment.getUntil())) {
					punishmentRepository.delete(playerPunishment);
					return false;
				}

				int daysRemaining = (int) ((playerPunishment.getUntil().getTime() - now.getTime()) / (1000 * 60 * 60 * 24));

				Bukkit.getScheduler().runTaskLater(FlockAPI.getInstance(), () -> {
					Remain.sendTitle(player, 10, 70, 20, "&4&l✖ &c&nStummgeschaltet&r &4&l✖", "&7Aufgrund: " + playerPunishment.getReason());
					Messenger.error(player, "Du bist für " + daysRemaining + " Tag(e) stummgeschaltet!");
					SoundUtil.playSound(player, SoundUtil.SoundType.WARNING);
				}, 5L);

				return true;
			}
		}

		return false;
	}

	/**
	 * Kick a player from the network
	 *
	 * @param sender       The player who is kicking
	 * @param playerToKick The player to kick
	 * @param reason       The reason for the kick
	 */
	private void kickPlayerFromNetwork(Player sender, String playerToKick, String reason) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("KickPlayer");
		out.writeUTF(playerToKick);
		out.writeUTF(reason);

		if (!Bukkit.getOnlinePlayers().isEmpty()) {
			Player onlinePlayer = Bukkit.getOnlinePlayers().iterator().next();
			onlinePlayer.sendPluginMessage(FlockAPI.getInstance(), BUNGEECORD_CHANNEL, out.toByteArray());
		}
	}


}
