package ovh.fedox.flockapi.database.model.impl;


import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import ovh.fedox.flockapi.database.model.GameEntity;

import java.util.Date;
import java.util.UUID;

/**
 * ApiPlayer.java - Player model for the API
 * <p>
 * Created on 3/31/2025 by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */
@Getter
@Setter
public class ApiPlayer implements GameEntity {
	private String id;
	private UUID uuid;
	private String name;
	private Date firstJoin;
	private Date lastJoin;
	private Double flocks;

	public ApiPlayer() {
	}

	public ApiPlayer(UUID uuid, String name) {
		this.id = uuid.toString();
		this.uuid = uuid;
		this.name = name;
		this.firstJoin = new Date();
		this.lastJoin = new Date();
		this.flocks = 0.0;
	}

	@Override
	public Document toDocument() {
		Document doc = new Document();
		doc.put("uuid", uuid.toString());
		doc.put("name", name);
		doc.put("firstJoin", firstJoin);
		doc.put("lastJoin", lastJoin);
		doc.put("flocks", flocks);
		return doc;
	}

	@Override
	public void fromDocument(Document document) {
		this.id = document.getString("_id");
		this.uuid = UUID.fromString(document.getString("uuid"));
		this.name = document.getString("name");
		this.firstJoin = document.getDate("firstJoin");
		this.lastJoin = document.getDate("lastJoin");
		this.flocks = document.getDouble("flocks");
	}

	@Override
	public String getId() {
		return id;
	}

	/**
	 * Update the last join time
	 */
	public void updateLastJoin() {
		this.lastJoin = new Date();
	}

	/**
	 * Add play time in minutes
	 *
	 * @param flocks Flocks to add
	 */
	public void addFlocks(int flocks) {
		this.flocks += flocks;
	}
}
