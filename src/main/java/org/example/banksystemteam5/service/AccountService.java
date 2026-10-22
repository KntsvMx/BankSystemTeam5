package org.example.banksystemteam5.service;

import org.example.banksystemteam5.dto.AccountResponse;
import org.example.banksystemteam5.dto.CreateAccountRequest;
import org.example.banksystemteam5.model.Account;
import org.example.banksystemteam5.model.Role;
import org.example.banksystemteam5.model.User;
import org.example.banksystemteam5.repository.AccountRepository;
import org.example.banksystemteam5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository repository;
    private final UserRepository userRepository;

    @Autowired
    public AccountService(AccountRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public AccountResponse createAccount(CreateAccountRequest createAccountRequest) {
        User user = new User();
        user.setUsername(createAccountRequest.getUsername());
        user.setRole(Role.USER);

        Account account = new Account();
        account.setOwnerName(createAccountRequest.getOwnerName());
        account.setBalance(createAccountRequest.getInitialBalance());
        account.setIban("DEMO-" + UUID.randomUUID());

        User savedUser = userRepository.saveUser(user);
        Account savedAccount = repository.saveAccount(account, savedUser.getId());

        return AccountResponse.builder()
                .id(savedAccount.getId())
                .iban(savedAccount.getIban())
                .balance(savedAccount.getBalance())
                .ownerName(savedAccount.getOwnerName())
                .build();
    }

    public AccountResponse getAccountById(Long id) {
        synchronized (repository) {
            Account account = repository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
            return AccountResponse.builder()
                    .id(account.getId())
                    .iban(account.getIban())
                    .ownerName(account.getOwnerName())
                    .balance(account.getBalance())
                    .build();
        }
    }

}
