package ovh.fedox.flockapi.command;


import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;
import ovh.fedox.flockapi.util.SoundUtil;

/**
 * DyeCommand.java -
 * <p>
 * Created on 4/1/2025 at 7:42 PM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

@AutoRegister
public final class DyeCommand extends SimpleCommand {

	public DyeCommand() {
		super("dye");

		setDescription("Färbe ein Item");
		setUsage("<color>");
		setMinArguments(1);
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();
		final ItemStack playerItem = player.getItemInHand();
		final String hex = args[0];

		if (!hex.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
			tellError("Ungültiger Hex-Code. Beispiel: #FFFFFF");
			return;
		}

		if (playerItem.getType().isAir()) {
			tellError("Du musst ein Item in der Hand halten.");
			return;
		}

		if (playerItem.getType() != Material.LEATHER_CHESTPLATE && playerItem.getType() != Material.LEATHER_BOOTS && playerItem.getType() != Material.LEATHER_HELMET && playerItem.getType() != Material.LEATHER_LEGGINGS) {
			tellError("Du kannst nur Leder-Rüstungen färben.");
			return;
		}

		final ItemStack dyedItem = new ItemStack(playerItem.getType());
		final LeatherArmorMeta dyedItemMeta = (LeatherArmorMeta) dyedItem.getItemMeta();

		dyedItemMeta.setColor(Color.fromRGB(Integer.parseInt(hex.substring(1), 16)));
		dyedItem.setItemMeta(dyedItemMeta);
		player.setItemInHand(dyedItem);

		tellSuccess("Das Item wurde erfolgreich gefärbt.");
		SoundUtil.playSound(player, SoundUtil.SoundType.SUCCESS);
	}
}
