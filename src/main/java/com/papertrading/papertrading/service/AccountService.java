package com.papertrading.papertrading.service;

import com.papertrading.papertrading.entity.Account;
import com.papertrading.papertrading.entity.User;
import com.papertrading.papertrading.exception.ConflictException;
import com.papertrading.papertrading.exception.ResourceNotFoundException;
import com.papertrading.papertrading.repository.AccountRepository;
import com.papertrading.papertrading.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    private static final BigDecimal INITIAL_BALANCE =
            new BigDecimal("100000.00");

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Account createAccount(Long userId){
        if(userId == null || userId <= 0){
            throw new IllegalArgumentException("User ID must be positive");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(accountRepository.findByUserId(userId).isPresent()){
            throw new ConflictException("Account already exists for the user");
        }

        Account acc = new Account();
        acc.setUser(user);
        acc.setBalance(INITIAL_BALANCE);

        return accountRepository.save(acc);
    }
}
