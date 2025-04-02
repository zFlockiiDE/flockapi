package ovh.fedox.flockapi.command;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.remain.Remain;
import ovh.fedox.flockapi.FlockAPI;
import ovh.fedox.flockapi.util.SoundUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

/**
 * StopCommand.java - Stops the server
 * <p>
 * Created on 4/2/2025 at 3:55 AM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

@AutoRegister
public final class StopCommand extends SimpleCommand {

	public StopCommand() {
		super("shutdown");

		setUsage("[seconds]");
		setDescription("Fahre den Server herunter");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		int seconds = args.length == 1 ? Integer.parseInt(args[0]) : 0;

		if (seconds > 0) {
			for (Player player : Remain.getOnlinePlayers()) {
				SoundUtil.playSound(player, SoundUtil.SoundType.INFO);
				Messenger.warn(player, "Der Server wird in &6&l" + seconds + " Sekunden &6heruntergefahren.");
			}

			Common.runLater(20 * seconds, () -> {
				for (Player player : Remain.getOnlinePlayers()) {
					try {
						ByteArrayOutputStream b = new ByteArrayOutputStream();
						DataOutputStream out = new DataOutputStream(b);

						out.writeUTF("Connect");
						out.writeUTF("lobby");

						player.sendPluginMessage(FlockAPI.getInstance(), "BungeeCord", b.toByteArray());
					} catch (Exception e) {
						player.kickPlayer("Der Server wird heruntergefahren.");
					}
				}
			});

			Common.runLaterAsync(20 * seconds + 20, Bukkit::shutdown);
		} else {
			Common.runLater(() -> {
				for (Player player : Remain.getOnlinePlayers()) {
					try {
						ByteArrayOutputStream b = new ByteArrayOutputStream();
						DataOutputStream out = new DataOutputStream(b);

						out.writeUTF("Connect");
						out.writeUTF("lobby");

						player.sendPluginMessage(FlockAPI.getInstance(), "BungeeCord", b.toByteArray());
					} catch (Exception e) {
						player.kickPlayer("Der Server wird heruntergefahren.");
					}
				}
			});

			Common.runLaterAsync(20, Bukkit::shutdown);
		}

	}
}
