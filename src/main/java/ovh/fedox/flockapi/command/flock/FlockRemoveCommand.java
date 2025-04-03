package ovh.fedox.flockapi.command.flock;

import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import ovh.fedox.flockapi.database.service.flockeconomy.FlockEconomyAPI;
import ovh.fedox.flockapi.util.SoundUtil;

/**
 * FlockRemoveCommand.java - Command to set the flock of a player
 * <p>
 * Created on 4/3/2025 at 7:58 PM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

public class FlockRemoveCommand extends SimpleSubCommand {

	public FlockRemoveCommand(SimpleCommandGroup parent) {
		super(parent, "remove");

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

		FlockEconomyAPI.getInstance().removeFlocks(
				player.getUniqueId(),
				Double.parseDouble(flock),
				"/flock remove " + player.getName() + " " + flock,
				getSender().getName()
		);

		tellSuccess("Du hast " + flock + " Flocks von " + player.getName() + " entfernt.");
		SoundUtil.playSound(player, SoundUtil.SoundType.SUCCESS);
	}
}
