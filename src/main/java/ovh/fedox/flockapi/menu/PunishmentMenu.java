package ovh.fedox.flockapi.menu;


import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.annotation.Position;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import ovh.fedox.flockapi.constants.PunishReason;
import ovh.fedox.flockapi.database.service.punishment.PunishmentService;
import ovh.fedox.flockapi.util.SoundUtil;

import java.util.Arrays;
import java.util.List;

/**
 * PunishmentMenu.java -
 * <p>
 * Created on 4/2/2025 at 6:39 AM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

public class PunishmentMenu extends MenuPagged<PunishReason> {

	@Position(4)
	private final Button targetButton;

	public PunishmentMenu(OfflinePlayer punishPlayer) {
		super(9 * 3, List.of(11, 12, 13, 14, 15, 15), Arrays.asList(PunishReason.values()));
		setTitle("Wähle einen Grund");

		this.targetButton = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.PLAYER_HEAD)
						.skullOwner(punishPlayer.getName())
						.name("&8» &f" + punishPlayer.getName())
						.make();
			}
		};
	}


	@Override
	protected ItemStack convertToItemStack(PunishReason item) {
		return ItemCreator.of(CompMaterial.fromMaterial(item.getIcon()))
				.name("&8» &f" + item.getName())
				.lore("&7" + item.getDescription())
				.make();
	}

	@Override
	protected void onPageClick(Player player, PunishReason item, ClickType click) {
		new ConfirmMenu(player, item).displayTo(player);
	}

	@Override
	protected int getPreviousButtonPosition() {
		return 20;
	}

	@Override
	protected int getNextButtonPosition() {
		return 24;
	}

	@Override
	protected boolean canShowNextButton() {
		return true;
	}

	@Override
	protected boolean canShowPreviousButton() {
		return true;
	}

	public static class ConfirmMenu extends Menu {

		@Position(4)
		private final Button targetButton;

		@Position(11)
		private final Button confirmButton;

		@Position(13)
		private final Button infoButton;

		@Position(15)
		private final Button cancelButton;

		public ConfirmMenu(OfflinePlayer playerToBan, PunishReason reason) {
			setTitle("Bestätige die Strafe");

			this.targetButton = new Button() {
				@Override
				public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				}

				@Override
				public ItemStack getItem() {
					return ItemCreator.of(CompMaterial.PLAYER_HEAD)
							.skullOwner(playerToBan.getName())
							.name("&8» &f" + playerToBan.getName())
							.make();
				}
			};

			this.confirmButton = new Button() {
				@Override
				public void onClickedInMenu(Player player, Menu menu, ClickType click) {
					player.closeInventory();

					PunishmentService.getInstance().punishPlayer(playerToBan, player, reason);

					Messenger.success(player, "Der Spieler wurde bestraft.");
					SoundUtil.playSound(player, SoundUtil.SoundType.SUCCESS);
				}

				@Override
				public ItemStack getItem() {
					return ItemCreator.of(CompMaterial.LIME_DYE)
							.name("&aBestätigen")
							.lore("&7Klicke hier um die Strafe zu bestätigen")
							.make();
				}
			};

			this.infoButton = new Button() {
				@Override
				public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				}

				@Override
				public ItemStack getItem() {
					return ItemCreator.of(CompMaterial.BOOK)
							.name("&8» &fInformationen")
							.lore("&7Grund: &f" + reason.getName(), "&7Beschreibung: &f" + reason.getDescription())
							.make();
				}
			};

			this.cancelButton = new Button() {
				@Override
				public void onClickedInMenu(Player player, Menu menu, ClickType click) {
					player.closeInventory();

					Messenger.info(player, "Die Strafe wurde abgebrochen.");
					SoundUtil.playSound(player, SoundUtil.SoundType.INFO);
				}

				@Override
				public ItemStack getItem() {
					return ItemCreator.of(CompMaterial.RED_DYE)
							.name("&cAbbrechen")
							.lore("&7Klicke hier um die Strafe abzubrechen")
							.make();
				}
			};
		}

	}
}
