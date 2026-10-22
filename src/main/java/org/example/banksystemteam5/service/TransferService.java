package org.example.banksystemteam5.service;

import jakarta.validation.Validator;
import org.example.banksystemteam5.dto.TransactionResponse;
import org.example.banksystemteam5.dto.TransferRequest;
import org.example.banksystemteam5.model.Account;
import org.example.banksystemteam5.model.Transaction;
import org.example.banksystemteam5.model.TransactionType;
import org.example.banksystemteam5.repository.AccountRepository;
import org.example.banksystemteam5.repository.TransferRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private final Validator validator;

    public TransferService(AccountRepository accountRepository, TransferRepository transferRepository,
                           Validator validator) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.validator = validator;
    }

    public List<TransactionResponse> transfer(TransferRequest request) {
        if (request == null || !validator.validate(request).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transfer request");
        }
        if (request.getFromAccountId().equals(request.getToAccountId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Accounts must be different");
        }

        synchronized (accountRepository) {
            Account from = findAccount(request.getFromAccountId());
            Account to = findAccount(request.getToAccountId());
            BigDecimal amount = request.getAmount();
            if (from.getBalance().compareTo(amount) < 0) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient funds");
            }

            BigDecimal fromBalance = from.getBalance().subtract(amount);
            BigDecimal toBalance = to.getBalance().add(amount);
            LocalDateTime createdAt = LocalDateTime.now();
            Transaction outgoing = createTransaction(from, TransactionType.TRANSFER_OUT, request, createdAt);
            Transaction incoming = createTransaction(to, TransactionType.TRANSFER_IN, request, createdAt);

            List<TransactionResponse> response = transferRepository.saveTransfer(outgoing, incoming).stream()
                    .map(this::toResponse)
                    .toList();
            accountRepository.updateBalances(from.getId(), fromBalance, to.getId(), toBalance);
            return response;
        }
    }

    public List<TransactionResponse> getTransactions(Long accountId) {
        synchronized (accountRepository) {
            findAccount(accountId);
            return transferRepository.findAllByAccountId(accountId).stream()
                    .map(this::toResponse)
                    .toList();
        }
    }

    private Account findAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }

    private Transaction createTransaction(Account account, TransactionType type, TransferRequest request,
                                          LocalDateTime createdAt) {
        return Transaction.builder()
                .account(account)
                .type(type)
                .amount(request.getAmount())
                .createdAt(createdAt)
                .note(request.getNote())
                .build();
    }

    private TransactionResponse toResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .accountId(transaction.getAccount().getId())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .createdAt(transaction.getCreatedAt())
                .note(transaction.getNote())
                .build();
    }
}
