package ovh.fedox.flockapi.command;


import org.bukkit.entity.Player;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;
import ovh.fedox.flockapi.FlockAPI;
import ovh.fedox.flockapi.menu.PunishmentMenu;

import java.util.List;

/**
 * PunishCommand.java - The command to punish a player
 * <p>
 * Created on 4/2/2025 at 6:41 AM by Fedox. Copyright © 2025 Fedox. All rights reserved.
 */

@AutoRegister
public final class PunishCommand extends SimpleCommand {

	public PunishCommand() {
		super("punish");

		setUsage("<player>");
		setMinArguments(1);
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();
		findOfflinePlayer(args[0], found -> {

			if (found != null) {
				if (FlockAPI.getMongoManager().getPlayerPunishmentRepository().findByUUID(found.getUniqueId()).isPresent()) {
					tellError("Dieser Spieler ist bereits bestraft.");
					tellInfo("Bitte benutze /unpunish, um die Strafe aufzuheben.");

					return;
				}
				new PunishmentMenu(found).displayTo(player);
			}
		});
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1) {
			return completeLastWordPlayerNames();
		}

		return super.tabComplete();
	}
}
