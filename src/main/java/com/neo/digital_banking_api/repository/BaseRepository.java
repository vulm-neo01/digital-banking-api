package com.neo.digital_banking_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Base repository interface
 * Extended by all entity-specific repositories
 */
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {

}
