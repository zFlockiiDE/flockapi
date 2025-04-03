package ovh.fedox.flockapi.database.repository.impl;


import com.mongodb.client.MongoCollection;
import org.bson.Document;
import ovh.fedox.flockapi.database.model.impl.ApiPlayer;
import ovh.fedox.flockapi.database.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * ApiPlayerRepository.java - Repository for API players
 * <p>
 * Created on 3/31/2025 by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */
public class ApiPlayerRepository extends MongoRepository<ApiPlayer> {

	/**
	 * Creates a new API Player Repository
	 *
	 * @param collection The MongoDB Collection
	 */
	public ApiPlayerRepository(MongoCollection<Document> collection) {
		super(collection, ApiPlayer.class, ApiPlayer::new);
	}

	/**
	 * Find a player by UUID
	 *
	 * @param uuid The player's UUID
	 * @return The player or an empty Optional
	 */
	public Optional<ApiPlayer> findByUUID(UUID uuid) {
		return findByField("uuid", uuid.toString());
	}

	/**
	 * Find a player by name
	 *
	 * @param name The player's name
	 * @return The player or an empty Optional
	 */
	public Optional<ApiPlayer> findByName(String name) {
		return findByField("name", name);
	}

	/**
	 * Create a new player if they don't exist
	 *
	 * @param uuid The player's UUID
	 * @param name The player's name
	 * @return The player
	 */
	public ApiPlayer createIfNotExists(UUID uuid, String name) {
		Optional<ApiPlayer> existingPlayer = findByUUID(uuid);
		if (existingPlayer.isPresent()) {
			ApiPlayer player = existingPlayer.get();
			// Update name if changed
			if (!player.getName().equals(name)) {
				player.setName(name);
				save(player);
			}
			return player;
		} else {
			ApiPlayer newPlayer = new ApiPlayer(uuid, name);
			save(newPlayer);
			return newPlayer;
		}
	}

	/**
	 * Update a player's last join time
	 *
	 * @param uuid The player's UUID
	 */
	public void updateLastJoin(UUID uuid) {
		findByUUID(uuid).ifPresent(player -> {
			player.updateLastJoin();
			save(player);
		});
	}

	/**
	 * Add play time to a player
	 *
	 * @param uuid   The player's UUID
	 * @param flocks Flocks to add
	 */
	public void addFlocks(UUID uuid, int flocks) {
		findByUUID(uuid).ifPresent(player -> {
			player.addFlocks(flocks);
			save(player);
		});
	}
}

