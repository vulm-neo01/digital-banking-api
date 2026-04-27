package com.neo.digital_banking_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Base repository interface
 * Extended by all entity-specific repositories
 */
@Repository
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {

}

