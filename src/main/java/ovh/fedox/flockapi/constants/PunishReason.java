package ovh.fedox.flockapi.constants;


import lombok.Getter;
import org.bukkit.Material;

/**
 * PunishReason.java - Enum for different punishment reasons
 * <p>
 * Created on 4/2/2025 at 6:29 AM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

@Getter
public enum PunishReason {

	HACKING("Hacking / Cheating", "Killaura, Autoclicker, X-Ray, etc.", PunishType.BAN, 30, Material.DIAMOND_SWORD),
	SPAM("Spam", "Zeichenspam, Nachrichtenspam etc.", PunishType.MUTE, 2, Material.PAPER),
	ADVERTISEMENT("Advertisement", "Werbung für andere Server, Dienste etc.", PunishType.MUTE, 4, Material.BOOK),
	INSULT("Beleidigung / Provokation", "Beleidigungen, Hate Speech etc.", PunishType.MUTE, 6, Material.IRON_SWORD),
	RACISM("Rassismus / Nationalsozialismus", "Rassistische Äußerungen, Rassismus etc.", PunishType.BAN, 3, Material.IRON_AXE),
	PHISHING("Phishing", "Versuch, Accountdaten zu stehlen", PunishType.BAN, 7, Material.IRON_PICKAXE),
	BUG_ABUSE("Bug Abuse", "Ausnutzen von Fehlern im Spiel", PunishType.BAN, 1, Material.SPIDER_EYE),
	BAD_NAME("Bad Name", "Unangemessener Name", PunishType.BAN, 1, Material.NAME_TAG),
	BAD_SKIN("Bad Skin", "Unangemessener Skin (Böse Symbole, Nacktskins etc)", PunishType.BAN, 1, Material.LEATHER_CHESTPLATE),
	TEAMING("Teaming", "Zusammenarbeit mit anderen Spielern in PVP Modis", PunishType.BAN, 1, Material.TOTEM_OF_UNDYING),
	HAUSVERBOT("Hausverbot", "Hausverbot auf dem Server. (Nur nach Absprache nutzen!!)", PunishType.BAN, 999, Material.BARRIER);

	private final String name;
	private final String description;
	private final PunishType type;
	private final int duration;
	private final Material icon;

	PunishReason(String name, String description, PunishType type, int duration, Material icon) {
		this.name = name;
		this.description = description;
		this.type = type;
		this.duration = duration;
		this.icon = icon;
	}

}
