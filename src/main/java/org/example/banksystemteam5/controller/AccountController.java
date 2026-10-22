package org.example.banksystemteam5.controller;

import org.example.banksystemteam5.service.AccountService;
import org.example.banksystemteam5.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class AccountController {


    private AccountService accountService;
    private TransferService transferService;

    @Autowired
    public AccountController(AccountService accountService, TransferService transferService) {
        this.accountService = accountService;
        this.transferService = transferService;
    }

    @RequestMapping("POST /account")
    public AccountService createAccount() {
        return accountService;
    }

    @RequestMapping("GET /account/{id}")
    public AccountService getAccountById(@PathVariable("id") Long id) {
        return accountService;
    }

    @RequestMapping("GET /account/{id}/transactions")
    public TransferService getTransactions(@PathVariable("id") Long id) {
        return transferService;
    }

    @RequestMapping("POST /transfer")
    public TransferService createTransaction() {
        return transferService;
    }
}
