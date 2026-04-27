package com.neo.digital_banking_api.service;

import com.neo.digital_banking_api.entity.Account;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    // Account CRUD operations
    List<Account> findAll();
    Optional<Account> findById(Long id);
    Account save(Account account);
    void deleteById(Long id);

    // Business operations
    Account createAccount(Long userId, BigDecimal initialBalance);
    Account deposit(Long accountId, BigDecimal amount);
    Account withdraw(Long accountId, BigDecimal amount);
    Account transfer(Long fromAccountId, Long toAccountId, BigDecimal amount);

    // Query operations
    List<Account> findByUserId(Long userId);
    Optional<Account> findByUserIdAndId(Long userId, Long accountId);
}
