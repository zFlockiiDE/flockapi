package ovh.fedox.flockapi.command.flock;

import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import ovh.fedox.flockapi.database.service.flockeconomy.FlockEconomyAPI;
import ovh.fedox.flockapi.util.SoundUtil;

/**
 * FlockAddCommand.java - Command to add flocks to a player
 * <p>
 * Created on 4/3/2025 at 7:58 PM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

public class FlockAddCommand extends SimpleSubCommand {

	public FlockAddCommand(SimpleCommandGroup parent) {
		super(parent, "add");

		setUsage("<player> <flock>");
		setMinArguments(2);
	}

	@Override
	protected void onCommand() {
		checkConsole();

		Player player = findPlayer(args[0]);
		String flock = args[1];

		if (player == null) {
			tellError("Der Spieler " + args[0] + " wurde nicht gefunden.");
		}

		FlockEconomyAPI.getInstance().addFlocks(
				player.getUniqueId(),
				Double.parseDouble(flock),
				"/flock add " + player.getName() + " " + flock,
				getSender().getName()
		);

		tellSuccess("Du hast " + flock + " Flocks zu " + player.getName() + " hinzugefügt.");
		SoundUtil.playSound(player, SoundUtil.SoundType.SUCCESS);
	}
}
