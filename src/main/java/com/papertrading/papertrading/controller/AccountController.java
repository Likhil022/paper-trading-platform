package com.papertrading.papertrading.controller;

import com.papertrading.papertrading.entity.Account;
import com.papertrading.papertrading.service.AccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping("/createAccount")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestParam Long userId){
        return accountService.createAccount(userId);
    }
}
