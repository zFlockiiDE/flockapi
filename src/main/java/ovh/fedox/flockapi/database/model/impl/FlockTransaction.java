package ovh.fedox.flockapi.database.model.impl;

import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import ovh.fedox.flockapi.database.model.GameEntity;

import java.util.Date;
import java.util.UUID;

/**
 * FlockTransaction.java - Model for currency transactions
 * <p>
 * Created on 4/2/2025 by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */
@Getter
@Setter
public class FlockTransaction implements GameEntity {
	private String id;
	private UUID playerUuid;
	private TransactionType type;
	private double amount;
	private String reason;
	private Date timestamp;
	private String source;
	private UUID targetUuid;
	private double balanceAfter;

	public FlockTransaction() {
		this.timestamp = new Date();
	}

	/**
	 * Create a new transaction
	 *
	 * @param playerUuid   The player's UUID
	 * @param type         The transaction type
	 * @param amount       The amount
	 * @param reason       The reason for the transaction
	 * @param source       The source of the transaction (plugin name, etc.)
	 * @param balanceAfter The balance after the transaction
	 */
	public FlockTransaction(UUID playerUuid, TransactionType type, double amount, String reason, String source, double balanceAfter) {
		this.id = UUID.randomUUID().toString();
		this.playerUuid = playerUuid;
		this.type = type;
		this.amount = amount;
		this.reason = reason;
		this.timestamp = new Date();
		this.source = source;
		this.balanceAfter = balanceAfter;
	}

	/**
	 * Create a transfer transaction
	 *
	 * @param playerUuid   The player's UUID
	 * @param targetUuid   The target player's UUID
	 * @param amount       The amount
	 * @param reason       The reason for the transaction
	 * @param source       The source of the transaction (plugin name, etc.)
	 * @param balanceAfter The balance after the transaction
	 */
	public FlockTransaction(UUID playerUuid, UUID targetUuid, double amount, String reason, String source, double balanceAfter) {
		this.id = UUID.randomUUID().toString();
		this.playerUuid = playerUuid;
		this.type = TransactionType.TRANSFER;
		this.amount = amount;
		this.reason = reason;
		this.timestamp = new Date();
		this.source = source;
		this.targetUuid = targetUuid;
		this.balanceAfter = balanceAfter;
	}

	@Override
	public Document toDocument() {
		Document doc = new Document();
		doc.put("playerUuid", playerUuid.toString());
		doc.put("type", type.name());
		doc.put("amount", amount);
		doc.put("reason", reason);
		doc.put("timestamp", timestamp);
		doc.put("source", source);
		doc.put("balanceAfter", balanceAfter);

		if (targetUuid != null) {
			doc.put("targetUuid", targetUuid.toString());
		}

		return doc;
	}

	@Override
	public void fromDocument(Document document) {
		this.id = document.getString("_id");
		this.playerUuid = UUID.fromString(document.getString("playerUuid"));
		this.type = TransactionType.valueOf(document.getString("type"));
		this.amount = document.getDouble("amount");
		this.reason = document.getString("reason");
		this.timestamp = document.getDate("timestamp");
		this.source = document.getString("source");
		this.balanceAfter = document.getDouble("balanceAfter");

		String targetUuidStr = document.getString("targetUuid");
		if (targetUuidStr != null) {
			this.targetUuid = UUID.fromString(targetUuidStr);
		}
	}

	@Override
	public String getId() {
		return id;
	}

	/**
	 * Transaction types
	 */
	public enum TransactionType {
		ADD,
		REMOVE,
		TRANSFER,
		SET
	}
}
