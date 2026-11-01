
package com.papertrading.papertrading.service;

import com.papertrading.papertrading.DTO.UserRequest;
import com.papertrading.papertrading.DTO.UserResponse;
import com.papertrading.papertrading.entity.User;
import com.papertrading.papertrading.entity.Account;
import com.papertrading.papertrading.exception.ConflictException;
import com.papertrading.papertrading.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            AccountService accountService,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse registerUser(UserRequest request) {

        // 1. Check duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException(
                    "Email is already registered");
        }

        // 2. Create user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword()));

        // 3. Save user
        User savedUser = userRepository.save(user);

        // 4. Create trading account
        Account account = accountService.createAccount(savedUser.getId());

        // 5. Prepare response
        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());
        response.setAccountId(account.getId());

        return response;
    }
}
