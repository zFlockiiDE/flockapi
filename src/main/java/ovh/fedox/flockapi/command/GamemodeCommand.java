package ovh.fedox.flockapi.command;


import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;
import ovh.fedox.flockapi.util.SoundUtil;

/**
 * GamemodeCommand.java - The command to change the gamemode of a player
 * <p>
 * Created on 3/31/2025 at 8:02 AM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

@AutoRegister
public final class GamemodeCommand extends SimpleCommand {

	public GamemodeCommand() {
		super("gamemode|gm");

		setMinArguments(1);
		setUsage("<gamemode> [player]");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final String gamemode = args[0];
		Player target = args.length > 1 ? findPlayer(args[1]) : getPlayer();

		if (target == null)
			target = getPlayer();

		GameMode mode = findGamemode(gamemode);

		if (mode == null) {
			tellError("Der Gamemode '" + gamemode + "' existiert nicht.");
			SoundUtil.playSound(getPlayer(), SoundUtil.SoundType.FAILURE);
			return;
		}

		target.setGameMode(mode);
		tellSuccess("Der Gamemode von '" + target.getName() + "' wurde auf '" + mode.name() + "' gesetzt.");
		SoundUtil.playSound(getPlayer(), SoundUtil.SoundType.SUCCESS);
	}

	private GameMode findGamemode(String input) {
		switch (input) {
			case "0":
			case "s":
			case "survival":
				return GameMode.SURVIVAL;
			case "1":
			case "c":
			case "creative":
				return GameMode.CREATIVE;
			case "2":
			case "a":
			case "adventure":
				return GameMode.ADVENTURE;
			case "3":
			case "sp":
			case "spectator":
				return GameMode.SPECTATOR;
			default:
				return null;
		}
	}
}
