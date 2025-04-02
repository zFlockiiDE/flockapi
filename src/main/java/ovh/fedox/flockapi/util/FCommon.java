package ovh.fedox.flockapi.util;


import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import ovh.fedox.flockapi.FlockAPI;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

/**
 * Common.java -
 * <p>
 * Created on 4/2/2025 at 4:06 AM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

@UtilityClass
public class FCommon {


	public static void sendToLobby(Player player) {
		try {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(b);

			out.writeUTF("Connect");
			out.writeUTF("lobby");

			player.sendPluginMessage(FlockAPI.getInstance(), "BungeeCord", b.toByteArray());
		} catch (Exception e) {
			player.kickPlayer("Deine Verbindung zum Server wurde unterbrochen.");
		}
	}

}
