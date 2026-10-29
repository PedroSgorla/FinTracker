package com.fintrack.repository;

import com.fintrack.exceptions.DataAccessException;
import com.fintrack.model.Identifiable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<T extends Identifiable<Long>> implements Repository<T, Long> {

    private final Map<Long, T> storage = new LinkedHashMap<>();
    private long nextId = 1;

    @Override
    public T save(T entity) {
        if (entity.getId() == null) {
            entity.setId(nextId++);
        } else if (!storage.containsKey(entity.getId())) {
            throw new DataAccessException("No entity with id " + entity.getId());
        }
        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<T> findAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public boolean deleteById(Long id) {
        return storage.remove(id) != null;
    }
}