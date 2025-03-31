package ovh.fedox.flockapi.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import org.mineacademy.fo.Common;
import ovh.fedox.flockapi.database.repository.ApiPlayerRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * MongoDBManager.java - The manager for handling database connections
 * <p>
 * Created on 3/31/2025 by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */
public class MongoDBManager {
	@Getter
	private final Map<String, Object> repositories = new HashMap<>();
	private MongoClient mongoClient;
	private MongoDatabase database;
	@Getter
	private ApiPlayerRepository apiPlayerRepository;

	/**
	 * Creates a new MongoDB connection
	 *
	 * @param connectionString The MongoDB connection string
	 * @param databaseName     The name of the database
	 */
	public MongoDBManager(String connectionString, String databaseName) {
		try {
			this.mongoClient = MongoClients.create(connectionString);
			this.database = mongoClient.getDatabase(databaseName);

			MongoCollection<Document> apiPlayerCollection = database.getCollection("api_players");
			this.apiPlayerRepository = new ApiPlayerRepository(apiPlayerCollection);
			repositories.put("apiPlayer", apiPlayerRepository);

			Common.log("&aSuccess: &7Successfully connected to MongoDB!");
		} catch (Exception e) {
			Common.logFramed("Failed to connect to MongoDB: ", e.getMessage());
		}
	}

	/**
	 * Get a collection from the database
	 *
	 * @param collectionName The name of the collection
	 * @return The MongoDB collection
	 */
	public MongoCollection<Document> getCollection(String collectionName) {
		return database.getCollection(collectionName);
	}

	/**
	 * Register a new repository
	 *
	 * @param name       The name of the repository
	 * @param repository The repository instance
	 * @param <T>        The type of the repository
	 * @return The registered repository
	 */
	public <T> T registerRepository(String name, T repository) {
		repositories.put(name, repository);
		return repository;
	}

	/**
	 * Get a repository by name
	 *
	 * @param name The name of the repository
	 * @param <T>  The type of the repository
	 * @return The repository or null if not found
	 */
	@SuppressWarnings("unchecked")
	public <T> T getRepository(String name) {
		return (T) repositories.get(name);
	}

	/**
	 * Close the MongoDB connection
	 */
	public void close() {
		if (mongoClient != null) {
			mongoClient.close();
			Common.log("&aSuccess: &7MongoDB connection closed.");
		}
	}
}
