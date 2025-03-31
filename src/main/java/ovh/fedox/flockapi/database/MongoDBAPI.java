package ovh.fedox.flockapi.database;

import lombok.Getter;
import org.mineacademy.fo.Common;
import ovh.fedox.flockapi.database.model.GameEntity;
import ovh.fedox.flockapi.database.repository.MongoRepository;

import java.util.function.Supplier;

/**
 * MongoDBAPI.java - Main API class for MongoDB integration
 * <p>
 * Created on 3/31/2025 by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */
public class MongoDBAPI {
	@Getter
	private static MongoDBAPI instance;

	@Getter
	private final MongoDBManager mongoDBManager;

	/**
	 * Initialize the MongoDB API
	 *
	 * @param connectionString The MongoDB connection string
	 * @param databaseName The database name
	 */
	public MongoDBAPI(String connectionString, String databaseName) {
		instance = this;
		this.mongoDBManager = new MongoDBManager(connectionString, databaseName);
		Common.log("&aSuccess: &7MongoDB API initialized!");
	}

	/**
	 * Create a new repository for a collection
	 *
	 * @param name The repository name
	 * @param collectionName The collection name
	 * @param entityClass The entity class
	 * @param entityFactory The entity factory
	 * @param <T> The entity type
	 * @return The created repository
	 */
	public <T extends GameEntity> MongoRepository<T> createRepository(
			String name,
			String collectionName,
			Class<T> entityClass,
			Supplier<T> entityFactory) {

		MongoRepository<T> repository = new MongoRepository<>(
				mongoDBManager.getCollection(collectionName),
				entityClass,
				entityFactory
		);

		mongoDBManager.registerRepository(name, repository);
		return repository;
	}

	/**
	 * Get a repository by name
	 *
	 * @param name The repository name
	 * @param <T> The repository type
	 * @return The repository
	 */
	public <T> T getRepository(String name) {
		return mongoDBManager.getRepository(name);
	}

	/**
	 * Close the MongoDB connection
	 */
	public void close() {
		mongoDBManager.close();
	}
}

