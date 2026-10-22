package org.example.banksystemteam5.service;

import jakarta.validation.Validator;
import org.example.banksystemteam5.dto.AccountResponse;
import org.example.banksystemteam5.dto.CreateAccountRequest;
import org.example.banksystemteam5.model.Account;
import org.example.banksystemteam5.model.Role;
import org.example.banksystemteam5.model.User;
import org.example.banksystemteam5.repository.AccountRepository;
import org.example.banksystemteam5.repository.UserRepository;
import org.example.banksystemteam5.validation.AccountValidation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final Validator validator;
    private final AccountValidation accountValidation;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository,
                          Validator validator, AccountValidation accountValidation) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.validator = validator;
        this.accountValidation = accountValidation;
    }

    public AccountResponse createAccount(CreateAccountRequest createAccountRequest) {
        if (createAccountRequest == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account request is required");
        }
        var violations = validator.validate(createAccountRequest);
        if (!violations.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid account request");
        }

        User user = new User();
        user.setUsername(createAccountRequest.getUsername());
        user.setRole(Role.USER);

        Account account = new Account();
        account.setOwnerName(createAccountRequest.getOwnerName());
        account.setBalance(createAccountRequest.getInitialBalance());
        account.setIban("DEMO-" + UUID.randomUUID());

        if (!accountValidation.validate(account)) {
            throw new IllegalStateException("Created account is invalid");
        }

        User savedUser = userRepository.saveUser(user);
        Account savedAccount = accountRepository.saveAccount(account, savedUser.getId());
        return toResponse(savedAccount);
    }

    public AccountResponse getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        return toResponse(account);
    }

    private AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .iban(account.getIban())
                .ownerName(account.getOwnerName())
                .balance(account.getBalance())
                .build();
    }
}
