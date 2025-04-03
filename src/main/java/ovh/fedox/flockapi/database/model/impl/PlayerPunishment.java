package ovh.fedox.flockapi.database.model.impl;


import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import ovh.fedox.flockapi.constants.PunishType;
import ovh.fedox.flockapi.database.model.GameEntity;

import java.util.Date;

/**
 * PlayerPunishment.java - Model for player punishments
 * <p>
 * Created on 4/2/2025 at 3:29 PM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

@Getter
@Setter
public class PlayerPunishment implements GameEntity {

	private String uuid;
	private String reason;
	private Date at;
	private Date until;
	private String punisher;
	private PunishType type;

	public PlayerPunishment() {
	}

	public PlayerPunishment(String uuid, String reason, Date at, Date until, String punisher, PunishType type) {
		this.uuid = uuid;
		this.reason = reason;
		this.at = at;
		this.until = until;
		this.punisher = punisher;
		this.type = type;
	}

	/**
	 * Converts the object to a Document
	 *
	 * @return the document
	 */
	@Override
	public Document toDocument() {
		Document doc = new Document();

		doc.put("uuid", uuid);
		doc.put("reason", reason);
		doc.put("at", at);
		doc.put("until", until);
		doc.put("punisher", punisher);
		doc.put("type", type);

		return doc;
	}

	/**
	 * Converts the object from a Document
	 *
	 * @param document the document
	 */
	@Override
	public void fromDocument(Document document) {
		this.uuid = document.getString("uuid");
		this.reason = document.getString("reason");
		this.at = document.getDate("at");
		this.until = document.getDate("until");
		this.punisher = document.getString("punisher");
		this.type = PunishType.valueOf(document.getString("type"));
	}

	@Override
	public String getId() {
		return uuid;
	}

}
