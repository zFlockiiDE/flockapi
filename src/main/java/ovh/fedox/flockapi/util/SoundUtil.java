package ovh.fedox.flockapi.util;


import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.CompSound;
import ovh.fedox.flockapi.FlockAPI;

/**
 * SoundUtil.java - Sound utils for playing sounds
 * <p>
 * Created on 3/12/2025 at 1:37 PM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

@UtilityClass
public class SoundUtil {

	/**
	 * Play a custom action sound for the player
	 *
	 * @param player The player to play the sound for
	 * @param type   The type of sound to play {@link SoundType}
	 */
	public static void playSound(Player player, SoundType type) {
		switch (type) {
			case SUCCESS:
				CompSound.ENTITY_PLAYER_LEVELUP.play(player, 0.4f, 0.4f);
				break;
			case FAILURE:
				CompSound.ENTITY_IRON_GOLEM_HURT.play(player, 0.4f, 0.4f);
				break;
			case INFO:
				CompSound.BLOCK_NOTE_BLOCK_BELL.play(player, 0.4f, 0.4f);
				break;
			case WARNING:
				CompSound.BLOCK_NOTE_BLOCK_BASS.play(player, 0.4f, 0.4f);
				break;
		}
	}

	/**
	 * Play the start sound for the player
	 *
	 * @param player The player to play the sound for
	 */
	public static void playStartSound(Player player) {
		Location loc = player.getLocation();
		Plugin plugin = SimplePlugin.getPlugin(FlockAPI.class);

		player.playSound(loc, Sound.BLOCK_NOTE_BLOCK_HARP, 1.0F, 1.0F);
		player.getServer().getScheduler().runTaskLater(plugin, () -> {
			player.playSound(loc, Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.2F);
		}, 2L);
		player.getServer().getScheduler().runTaskLater(plugin, () -> {
			player.playSound(loc, Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0F, 1.4F);
		}, 4L);
		player.getServer().getScheduler().runTaskLater(plugin, () -> {
			player.playSound(loc, Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1.0F, 1.6F);
		}, 6L);
		player.getServer().getScheduler().runTaskLater(plugin, () -> {
			player.playSound(loc, Sound.BLOCK_NOTE_BLOCK_BELL, 1.0F, 1.6F);
		}, 8L);
	}

	/**
	 * The sound types
	 */
	public enum SoundType {
		SUCCESS,
		FAILURE,
		INFO,
		WARNING
	}

}
