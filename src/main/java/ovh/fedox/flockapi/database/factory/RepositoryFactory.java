package ovh.fedox.flockapi.database.factory;


import ovh.fedox.flockapi.database.MongoDBAPI;
import ovh.fedox.flockapi.database.model.GameEntity;
import ovh.fedox.flockapi.database.repository.MongoRepository;

import java.util.function.Supplier;

/**
 * RepositoryFactory.java - Factory for creating repositories
 * <p>
 * Created on 3/31/2025 by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */
public class RepositoryFactory {

	/**
	 * Create a new repository
	 *
	 * @param name The repository name
	 * @param collectionName The collection name
	 * @param entityClass The entity class
	 * @param entityFactory The entity factory
	 * @param <T> The entity type
	 * @return The created repository
	 */
	public static <T extends GameEntity> MongoRepository<T> createRepository(
			String name,
			String collectionName,
			Class<T> entityClass,
			Supplier<T> entityFactory) {

		return MongoDBAPI.getInstance().createRepository(
				name,
				collectionName,
				entityClass,
				entityFactory
		);
	}

	/**
	 * Get a repository by name
	 *
	 * @param name The repository name
	 * @param <T> The repository type
	 * @return The repository
	 */
	public static <T> T getRepository(String name) {
		return MongoDBAPI.getInstance().getRepository(name);
	}
}

