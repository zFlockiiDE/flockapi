package ovh.fedox.flockapi.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import org.mineacademy.fo.Common;
import ovh.fedox.flockapi.database.repository.impl.ApiPlayerRepository;
import ovh.fedox.flockapi.database.repository.impl.FlockTransactionRepository;
import ovh.fedox.flockapi.database.repository.impl.PlayerPunishmentRepository;
import ovh.fedox.flockapi.database.service.flockeconomy.FlockEconomyAPI;
import ovh.fedox.flockapi.database.service.punishment.PunishmentService;

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
	@Getter
	private FlockTransactionRepository flockTransactionRepository;
	@Getter
	private PlayerPunishmentRepository playerPunishmentRepository;

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

			// Initialize repositories
			MongoCollection<Document> apiPlayerCollection = database.getCollection("api_players");
			this.apiPlayerRepository = new ApiPlayerRepository(apiPlayerCollection);
			repositories.put("apiPlayer", apiPlayerRepository);

			MongoCollection<Document> flockTransactionCollection = database.getCollection("flock_transactions");
			this.flockTransactionRepository = new FlockTransactionRepository(flockTransactionCollection);
			repositories.put("flockTransaction", flockTransactionRepository);

			MongoCollection<Document> playerPunishmentCollection = database.getCollection("player_punishments");
			this.playerPunishmentRepository = new PlayerPunishmentRepository(playerPunishmentCollection);
			repositories.put("playerPunishment", playerPunishmentRepository);

			// Initialize the economy API
			FlockEconomyAPI.initialize(this);

			// Initialize the punishment api
			PunishmentService.initialize(this);

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

