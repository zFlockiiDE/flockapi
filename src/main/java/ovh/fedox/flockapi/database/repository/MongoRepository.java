package ovh.fedox.flockapi.database.repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import lombok.Getter;
import org.bson.Document;
import org.bson.conversions.Bson;
import ovh.fedox.flockapi.database.model.GameEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Generic MongoDB Repository
 *
 * @param <T> The entity type
 */
public class MongoRepository<T extends GameEntity> {
	/**
	 * -- GETTER --
	 * Returns the MongoDB collection
	 *
	 * @return The MongoDB collection
	 */
	@Getter
	private final MongoCollection<Document> collection;
	private final Class<T> entityClass;
	private final Supplier<T> entityFactory;

	/**
	 * Creates a new MongoDB Repository
	 *
	 * @param collection    The MongoDB Collection
	 * @param entityClass   The class of the entity
	 * @param entityFactory A factory method to create new entities
	 */
	public MongoRepository(MongoCollection<Document> collection, Class<T> entityClass, Supplier<T> entityFactory) {
		this.collection = collection;
		this.entityClass = entityClass;
		this.entityFactory = entityFactory;
	}

	/**
	 * Saves an entity to the database
	 *
	 * @param entity The entity to save
	 */
	public void save(T entity) {
		Document document = entity.toDocument();
		document.put("_id", entity.getId());
		collection.replaceOne(
				Filters.eq("_id", entity.getId()),
				document,
				new ReplaceOptions().upsert(true)
		);
	}

	/**
	 * Finds an entity by its ID
	 *
	 * @param id The ID of the entity
	 * @return The found entity or an empty Optional
	 */
	public Optional<T> findById(String id) {
		Document document = collection.find(Filters.eq("_id", id)).first();
		if (document == null) {
			return Optional.empty();
		}

		try {
			T entity = entityFactory.get();
			entity.fromDocument(document);
			return Optional.of(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	/**
	 * Finds an entity by a specific field
	 *
	 * @param fieldName The name of the field
	 * @param value     The value of the field
	 * @return The found entity or an empty Optional
	 */
	public Optional<T> findByField(String fieldName, Object value) {
		Document document = collection.find(Filters.eq(fieldName, value)).first();
		if (document == null) {
			return Optional.empty();
		}

		try {
			T entity = entityFactory.get();
			entity.fromDocument(document);
			return Optional.of(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	/**
	 * Finds entities by a specific filter
	 *
	 * @param filter The filter to apply
	 * @return A list of found entities
	 */
	public List<T> findByFilter(Bson filter) {
		List<T> results = new ArrayList<>();
		FindIterable<Document> documents = collection.find(filter);

		for (Document document : documents) {
			try {
				T entity = entityFactory.get();
				entity.fromDocument(document);
				results.add(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return results;
	}

	/**
	 * Finds all entities
	 *
	 * @return A list of all entities
	 */
	public List<T> findAll() {
		List<T> results = new ArrayList<>();
		FindIterable<Document> documents = collection.find();

		for (Document document : documents) {
			try {
				T entity = entityFactory.get();
				entity.fromDocument(document);
				results.add(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return results;
	}

	/**
	 * Deletes an entity
	 *
	 * @param entity The entity to delete
	 * @return true if the entity was deleted, otherwise false
	 */
	public boolean delete(T entity) {
		DeleteResult result = collection.deleteOne(Filters.eq("_id", entity.getId()));
		return result.getDeletedCount() > 0;
	}

	/**
	 * Deletes an entity by its ID
	 *
	 * @param id The ID of the entity
	 * @return true if the entity was deleted, otherwise false
	 */
	public boolean deleteById(String id) {
		DeleteResult result = collection.deleteOne(Filters.eq("_id", id));
		return result.getDeletedCount() > 0;
	}

	/**
	 * Executes an action for each entity in the collection
	 *
	 * @param action The action to execute
	 */
	public void forEach(Consumer<T> action) {
		for (Document document : collection.find()) {
			try {
				T entity = entityFactory.get();
				entity.fromDocument(document);
				action.accept(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Gives the number of entities in the collection
	 *
	 * @return The number of entities
	 */
	public long count() {
		return collection.countDocuments();
	}
}

