package com.fintrack.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Generic contract for storing and retrieving entities.
 *
 * @param <T>  the type of entity handled by the repository
 * @param <ID> the type of the entity's identifier
 */
public interface Repository<T,ID>{

    /**
     * Saves an entity: inserts it if it is new, or updates it if it already exists.
     *
     * @param entity the entity to save
     * @return the saved entity, including its generated identifier
     */
    T save(T entity);

    /**
     * Finds an entity by its identifier.
     *
     * @param id the identifier to look for
     * @return the entity, or an empty {@link Optional} if none exists
     */
    Optional<T> findById(ID id);

    /**
     * Returns all stored entities.
     *
     * @return a list of all entities, possibly empty
     */
    List<T> findAll();

    /**
     * Deletes the entity with the given identifier.
     *
     * @param id the identifier of the entity to delete
     * @return {@code true} if an entity was deleted, {@code false} if none existed
     */
    boolean deleteById(ID id);

    /**
     * Saves every entity of the given collection.
     *
     * @param entities the entities to save; may contain subtypes of {@code T}
     */
    default void saveAll(Collection<? extends T> entities) {
        for (T entity : entities) {
            save(entity);
        }
    }

    /**
     * Adds all stored entities to the given collection.
     *
     * @param target the collection to fill; may hold supertypes of {@code T}
     */
    default void findAllInto(Collection<? super T> target) {
        target.addAll(findAll());
    }
}

