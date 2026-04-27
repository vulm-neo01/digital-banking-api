package com.neo.digital_banking_api.service.impl;

import com.neo.digital_banking_api.entity.Account;
import com.neo.digital_banking_api.repository.AccountRepository;
import com.neo.digital_banking_api.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    @Transactional
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Account createAccount(Long userId, BigDecimal initialBalance) {
        Account account = new Account();
        account.setUserId(userId);
        account.setBalance(initialBalance != null ? initialBalance : BigDecimal.ZERO);
        account.setCreatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        account.credit(amount);
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account withdraw(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        account.debit(amount);
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Source account not found"));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Destination account not found"));

        fromAccount.debit(amount);
        toAccount.credit(amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return fromAccount;
    }

    @Override
    public List<Account> findByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    @Override
    public Optional<Account> findByUserIdAndId(Long userId, Long accountId) {
        return accountRepository.findByUserId(userId)
                .stream()
                .filter(account -> account.getId().equals(accountId))
                .findFirst();
    }
}
