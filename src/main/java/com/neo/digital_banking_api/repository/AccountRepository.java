package com.neo.digital_banking_api.repository;

import com.neo.digital_banking_api.entity.Account;
import java.util.List;

public interface AccountRepository extends BaseRepository<Account, Long> {
    // Custom query methods
    List<Account> findByUserId(Long userId);
    List<Account> findByUserIdAndIsDeletedFalse(Long userId);
}
