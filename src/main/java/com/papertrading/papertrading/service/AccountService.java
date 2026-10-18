package com.papertrading.papertrading.service;

import com.papertrading.papertrading.entity.Account;
import com.papertrading.papertrading.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    private static final BigDecimal INITIAL_BALANCE =
            new BigDecimal("100000.00");

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Account createAccount(Long userId){
        if(userId == null || userId <= 0){
            throw new IllegalArgumentException("User ID must be positive");
        }

        if(accountRepository.findByUserId(userId).isPresent()){
            throw new IllegalArgumentException("Account already exists for the user");
        }

        Account acc = new Account();
        acc.setUserId(userId);
        acc.setBalance(INITIAL_BALANCE);

        return accountRepository.save(acc);
    }
}
