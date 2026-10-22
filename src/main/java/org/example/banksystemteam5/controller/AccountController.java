package org.example.banksystemteam5.controller;

import jakarta.validation.Valid;
import org.example.banksystemteam5.dto.AccountResponse;
import org.example.banksystemteam5.dto.CreateAccountRequest;
import org.example.banksystemteam5.dto.TransactionResponse;
import org.example.banksystemteam5.dto.TransferRequest;
import org.example.banksystemteam5.service.AccountService;
import org.example.banksystemteam5.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountController {

    private final AccountService accountService;
    private final TransferService transferService;

    public AccountController(AccountService accountService, TransferService transferService) {
        this.accountService = accountService;
        this.transferService = transferService;
    }

    @PostMapping("/account")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount(@Valid @RequestBody CreateAccountRequest request) {
        return accountService.createAccount(request);
    }

    @GetMapping("/account/{id}")
    public AccountResponse getAccountById(@PathVariable("id") Long id) {
        return accountService.getAccountById(id);
    }

    @GetMapping("/account/{id}/transactions")
    public List<TransactionResponse> getTransactions(@PathVariable("id") Long id) {
        return transferService.getTransactions(id);
    }

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public List<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
        return transferService.transfer(request);
    }
}
