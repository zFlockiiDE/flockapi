package ovh.fedox.flockapi.database.repository.impl;


import com.mongodb.client.MongoCollection;
import org.bson.Document;
import ovh.fedox.flockapi.database.model.impl.PlayerPunishment;
import ovh.fedox.flockapi.database.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * PlayerPunishmentRepository.java - Repository for player punishments
 * <p>
 * Created on 4/3/2025 at 1:04 AM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

public class PlayerPunishmentRepository extends MongoRepository<PlayerPunishment> {

	/**
	 * Creates a new PlayerPunishment Repository
	 *
	 * @param collection The MongoDB Collection
	 */
	public PlayerPunishmentRepository(MongoCollection<Document> collection) {
		super(collection, PlayerPunishment.class, PlayerPunishment::new);
	}

	/**
	 * Find a player by UUID
	 *
	 * @param uuid The player's UUID
	 * @return The player or an empty Optional
	 */
	public Optional<PlayerPunishment> findByUUID(UUID uuid) {
		return findByField("uuid", uuid.toString());
	}


}
