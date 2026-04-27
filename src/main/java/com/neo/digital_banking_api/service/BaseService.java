package com.neo.digital_banking_api.service;

/**
 * Base service interface
 * Extended by all entity-specific services
 */
public interface BaseService<T, ID> {

    T create(T entity);

    T findById(ID id);

    void update(T entity);

    void delete(ID id);
}

