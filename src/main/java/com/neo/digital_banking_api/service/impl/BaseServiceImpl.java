package com.neo.digital_banking_api.service.impl;

import com.neo.digital_banking_api.service.BaseService;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Base service implementation
 * Extended by all entity-specific service implementations
 */
public abstract class BaseServiceImpl<T, ID> implements BaseService<T, ID> {

    protected JpaRepository<T, ID> repository;

    @Override
    public T create(T entity) {
        return repository.save(entity);
    }

    @Override
    public T findById(ID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void update(T entity) {
        repository.save(entity);
    }

    @Override
    public void delete(ID id) {
        repository.deleteById(id);
    }
}

