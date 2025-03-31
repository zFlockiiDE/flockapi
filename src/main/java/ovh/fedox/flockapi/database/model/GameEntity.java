package ovh.fedox.flockapi.database.model;


import org.bson.Document;

/**
 * GameEntity.java - Base interface for every mongodb model
 * <p>
 * Created on 3/30/2025 at 2:32 PM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

public interface GameEntity {

	/**
	 * Convert this entity to a document
	 *
	 * @return the document
	 */
	Document toDocument();

	/**
	 * Load this entity from a document
	 *
	 * @param document the document
	 */
	void fromDocument(Document document);

	/**
	 * The unique id of this entity
	 *
	 * @return the id
	 */
	String getId();

}
