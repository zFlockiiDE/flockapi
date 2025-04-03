package ovh.fedox.flockapi.database.repository.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;
import ovh.fedox.flockapi.database.model.impl.FlockTransaction;
import ovh.fedox.flockapi.database.repository.MongoRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * FlockTransactionRepository.java - Repository for Flock currency transactions
 * <p>
 * Created on 4/2/2025 by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */
public class FlockTransactionRepository extends MongoRepository<FlockTransaction> {

	/**
	 * Creates a new Flock Transaction Repository
	 *
	 * @param collection The MongoDB Collection
	 */
	public FlockTransactionRepository(MongoCollection<Document> collection) {
		super(collection, FlockTransaction.class, FlockTransaction::new);
	}

	/**
	 * Find transactions by player UUID
	 *
	 * @param uuid The player's UUID
	 * @return List of transactions
	 */
	public List<FlockTransaction> findByPlayerUuid(UUID uuid) {
		return findByFilter(Filters.eq("playerUuid", uuid.toString()));
	}

	/**
	 * Find transactions by player UUID with pagination
	 *
	 * @param uuid   The player's UUID
	 * @param limit  Maximum number of transactions to return
	 * @param offset Number of transactions to skip
	 * @return List of transactions
	 */
	public List<FlockTransaction> findByPlayerUuid(UUID uuid, int limit, int offset) {
		Bson filter = Filters.eq("playerUuid", uuid.toString());
		return getCollection()
				.find(filter)
				.sort(Sorts.descending("timestamp"))
				.skip(offset)
				.limit(limit)
				.map(doc -> {
					FlockTransaction transaction = new FlockTransaction();
					transaction.fromDocument(doc);
					return transaction;
				})
				.into(new java.util.ArrayList<>());
	}

	/**
	 * Find transactions by type
	 *
	 * @param type The transaction type
	 * @return List of transactions
	 */
	public List<FlockTransaction> findByType(FlockTransaction.TransactionType type) {
		return findByFilter(Filters.eq("type", type.name()));
	}

	/**
	 * Find transactions by date range
	 *
	 * @param start Start date
	 * @param end   End date
	 * @return List of transactions
	 */
	public List<FlockTransaction> findByDateRange(Date start, Date end) {
		Bson filter = Filters.and(
				Filters.gte("timestamp", start),
				Filters.lte("timestamp", end)
		);
		return findByFilter(filter);
	}

	/**
	 * Find transactions by player UUID and date range
	 *
	 * @param uuid  The player's UUID
	 * @param start Start date
	 * @param end   End date
	 * @return List of transactions
	 */
	public List<FlockTransaction> findByPlayerUuidAndDateRange(UUID uuid, Date start, Date end) {
		Bson filter = Filters.and(
				Filters.eq("playerUuid", uuid.toString()),
				Filters.gte("timestamp", start),
				Filters.lte("timestamp", end)
		);
		return findByFilter(filter);
	}

	/**
	 * Find transactions by source
	 *
	 * @param source The source of the transaction
	 * @return List of transactions
	 */
	public List<FlockTransaction> findBySource(String source) {
		return findByFilter(Filters.eq("source", source));
	}
}
