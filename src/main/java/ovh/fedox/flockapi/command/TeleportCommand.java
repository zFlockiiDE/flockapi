package ovh.fedox.flockapi.command;


import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;
import ovh.fedox.flockapi.util.SoundUtil;

import java.util.List;

/**
 * TeleportCommand.java - Teleport to a other player or coordinate
 * <p>
 * Created on 3/31/2025 at 8:05 AM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

@AutoRegister
public final class TeleportCommand extends SimpleCommand {

	public TeleportCommand() {
		super("teleport|tp");

		setMinArguments(1);
		setUsage("<player> [player] | <x> <y> <z>");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		if (args.length == 1) {
			final Player target = findPlayer(args[0]);

			if (target == null) {
				tellError("Spieler nicht gefunden.");
				SoundUtil.playSound(getPlayer(), SoundUtil.SoundType.FAILURE);
				return;
			}

			getPlayer().teleport(target);
			tellSuccess("Du wurdest zu '" + target.getName() + "' teleportiert.");
			SoundUtil.playSound(getPlayer(), SoundUtil.SoundType.SUCCESS);
		} else if (args.length == 2) {
			final Player player = findPlayer(args[0]);
			final Player target = findPlayer(args[1]);

			if (player == null || target == null) {
				tellError("Spieler nicht gefunden.");
				SoundUtil.playSound(getPlayer(), SoundUtil.SoundType.FAILURE);
				return;
			}

			player.teleport(target);
			tellSuccess("Du hast '" + player.getName() + "' zu '" + target.getName() + "' teleportiert.");
			SoundUtil.playSound(getPlayer(), SoundUtil.SoundType.SUCCESS);
		} else if (args.length == 3) {
			try {
				final double x = Double.parseDouble(args[0]);
				final double y = Double.parseDouble(args[1]);
				final double z = Double.parseDouble(args[2]);

				getPlayer().teleport(new Location(getPlayer().getWorld(), x, y, z));
				tellSuccess("Du wurdest zu '" + x + " " + y + " " + z + "' teleportiert.");
				SoundUtil.playSound(getPlayer(), SoundUtil.SoundType.SUCCESS);
			} catch (NumberFormatException e) {
				tellError("Ungültige Koordinaten.");
				SoundUtil.playSound(getPlayer(), SoundUtil.SoundType.FAILURE);
			}
		}
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1) {
			return completeLastWordPlayerNames();
		}

		if (args.length == 2) {
			return completeLastWordPlayerNames();
		}

		if (args.length == 3) {
			return List.of("0", "0", "0");
		}

		return super.tabComplete();
	}
}
