package ovh.fedox.flockapi.util;


import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.mineacademy.fo.remain.CompSound;

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
	 * The sound types
	 */
	public enum SoundType {
		SUCCESS,
		FAILURE,
		INFO,
		WARNING
	}

}
