package ovh.fedox.flockapi.database;

import org.mineacademy.fo.Common;
import ovh.fedox.flockapi.database.MongoDBManager;
import ovh.fedox.flockapi.database.model.ApiPlayer;
import ovh.fedox.flockapi.database.model.FlockTransaction;
import ovh.fedox.flockapi.database.repository.ApiPlayerRepository;
import ovh.fedox.flockapi.database.repository.FlockTransactionRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * FlockEconomyAPI.java - API for managing Flock currency
 * <p>
 * Created on 4/2/2025 by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */
public class FlockEconomyAPI {
	private static FlockEconomyAPI instance;
	private final ApiPlayerRepository playerRepository;
	private final FlockTransactionRepository transactionRepository;

	/**
	 * Private constructor to enforce singleton pattern
	 *
	 * @param mongoDBManager The MongoDB manager
	 */
	private FlockEconomyAPI(MongoDBManager mongoDBManager) {
		this.playerRepository = mongoDBManager.getApiPlayerRepository();
		this.transactionRepository = mongoDBManager.getFlockTransactionRepository();
	}

	/**
	 * Initialize the economy API
	 *
	 * @param mongoDBManager The MongoDB manager
	 */
	public static void initialize(MongoDBManager mongoDBManager) {
		if (instance == null) {
			instance = new FlockEconomyAPI(mongoDBManager);
		}
	}

	/**
	 * Get the economy API instance
	 *
	 * @return The economy API instance
	 * @throws IllegalStateException if the API has not been initialized
	 */
	public static FlockEconomyAPI getInstance() {
		if (instance == null) {
			throw new IllegalStateException("FlockEconomyAPI has not been initialized. Call initialize() first.");
		}
		return instance;
	}

	/**
	 * Get a player's balance
	 *
	 * @param uuid The player's UUID
	 * @return The player's balance or 0 if the player doesn't exist
	 */
	public double getBalance(UUID uuid) {
		Optional<ApiPlayer> playerOpt = playerRepository.findByUUID(uuid);
		return playerOpt.map(ApiPlayer::getFlocks).orElse(0.0);
	}

	/**
	 * Check if a player has enough flocks
	 *
	 * @param uuid   The player's UUID
	 * @param amount The amount to check
	 * @return True if the player has enough flocks
	 */
	public boolean hasEnough(UUID uuid, double amount) {
		return getBalance(uuid) >= amount;
	}

	/**
	 * Add flocks to a player
	 *
	 * @param uuid   The player's UUID
	 * @param amount The amount to add
	 * @param reason The reason for adding
	 * @param source The source of the transaction (plugin name, etc.)
	 * @return True if the operation was successful
	 */
	public boolean addFlocks(UUID uuid, double amount, String reason, String source) {
		if (amount <= 0) {
			return false;
		}

		Optional<ApiPlayer> playerOpt = playerRepository.findByUUID(uuid);
		if (playerOpt.isPresent()) {
			ApiPlayer player = playerOpt.get();
			player.setFlocks(player.getFlocks() + amount);
			playerRepository.save(player);

			// Log transaction
			FlockTransaction transaction = new FlockTransaction(
					uuid,
					FlockTransaction.TransactionType.ADD,
					amount,
					reason,
					source,
					player.getFlocks()
			);
			transactionRepository.save(transaction);
			return true;
		}
		return false;
	}

	/**
	 * Remove flocks from a player
	 *
	 * @param uuid   The player's UUID
	 * @param amount The amount to remove
	 * @param reason The reason for removing
	 * @param source The source of the transaction (plugin name, etc.)
	 * @return True if the operation was successful
	 */
	public boolean removeFlocks(UUID uuid, double amount, String reason, String source) {
		if (amount <= 0) {
			return false;
		}

		Optional<ApiPlayer> playerOpt = playerRepository.findByUUID(uuid);
		if (playerOpt.isPresent()) {
			ApiPlayer player = playerOpt.get();
			if (player.getFlocks() < amount) {
				return false;
			}

			player.setFlocks(player.getFlocks() - amount);
			playerRepository.save(player);

			// Log transaction
			FlockTransaction transaction = new FlockTransaction(
					uuid,
					FlockTransaction.TransactionType.REMOVE,
					amount,
					reason,
					source,
					player.getFlocks()
			);
			transactionRepository.save(transaction);
			return true;
		}
		return false;
	}

	/**
	 * Set a player's flocks
	 *
	 * @param uuid   The player's UUID
	 * @param amount The amount to set
	 * @param reason The reason for setting
	 * @param source The source of the transaction (plugin name, etc.)
	 * @return True if the operation was successful
	 */
	public boolean setFlocks(UUID uuid, double amount, String reason, String source) {
		if (amount < 0) {
			return false;
		}

		Optional<ApiPlayer> playerOpt = playerRepository.findByUUID(uuid);
		if (playerOpt.isPresent()) {
			ApiPlayer player = playerOpt.get();
			player.setFlocks(amount);
			playerRepository.save(player);

			// Log transaction
			FlockTransaction transaction = new FlockTransaction(
					uuid,
					FlockTransaction.TransactionType.SET,
					amount,
					reason,
					source,
					player.getFlocks()
			);
			transactionRepository.save(transaction);
			return true;
		}
		return false;
	}

	/**
	 * Transfer flocks from one player to another
	 *
	 * @param fromUuid The sender's UUID
	 * @param toUuid   The receiver's UUID
	 * @param amount   The amount to transfer
	 * @param reason   The reason for the transfer
	 * @param source   The source of the transaction (plugin name, etc.)
	 * @return True if the operation was successful
	 */
	public boolean transferFlocks(UUID fromUuid, UUID toUuid, double amount, String reason, String source) {
		if (amount <= 0 || fromUuid.equals(toUuid)) {
			return false;
		}

		Optional<ApiPlayer> fromPlayerOpt = playerRepository.findByUUID(fromUuid);
		Optional<ApiPlayer> toPlayerOpt = playerRepository.findByUUID(toUuid);

		if (fromPlayerOpt.isPresent() && toPlayerOpt.isPresent()) {
			ApiPlayer fromPlayer = fromPlayerOpt.get();
			ApiPlayer toPlayer = toPlayerOpt.get();

			if (fromPlayer.getFlocks() < amount) {
				return false;
			}

			fromPlayer.setFlocks(fromPlayer.getFlocks() - amount);
			toPlayer.setFlocks(toPlayer.getFlocks() + amount);

			playerRepository.save(fromPlayer);
			playerRepository.save(toPlayer);

			FlockTransaction fromTransaction = new FlockTransaction(
					fromUuid,
					toUuid,
					amount,
					reason,
					source,
					fromPlayer.getFlocks()
			);
			transactionRepository.save(fromTransaction);

			return true;
		}
		return false;
	}

	/**
	 * Get a player's transaction history
	 *
	 * @param uuid The player's UUID
	 * @return List of transactions
	 */
	public List<FlockTransaction> getTransactionHistory(UUID uuid) {
		return transactionRepository.findByPlayerUuid(uuid);
	}

	/**
	 * Get a player's transaction history with pagination
	 *
	 * @param uuid   The player's UUID
	 * @param limit  Maximum number of transactions to return
	 * @param offset Number of transactions to skip
	 * @return List of transactions
	 */
	public List<FlockTransaction> getTransactionHistory(UUID uuid, int limit, int offset) {
		return transactionRepository.findByPlayerUuid(uuid, limit, offset);
	}

	/**
	 * Get a player's transaction history within a date range
	 *
	 * @param uuid  The player's UUID
	 * @param start Start date
	 * @param end   End date
	 * @return List of transactions
	 */
	public List<FlockTransaction> getTransactionHistory(UUID uuid, Date start, Date end) {
		return transactionRepository.findByPlayerUuidAndDateRange(uuid, start, end);
	}

	/**
	 * Format flocks amount to a readable string
	 *
	 * @param amount The amount to format
	 * @return Formatted string
	 */
	public String formatFlocks(double amount) {
		return String.format("%.2f Flocks", amount);
	}
}
