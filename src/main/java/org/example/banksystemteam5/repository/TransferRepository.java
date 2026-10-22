package org.example.banksystemteam5.repository;

import org.example.banksystemteam5.model.Transaction;
import org.example.banksystemteam5.model.Account;
import org.example.banksystemteam5.model.TransactionType;
import org.example.banksystemteam5.validation.TransactionValidation;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TransferRepository {

    private final List<Transaction> transactions = new ArrayList<>();
    private final AtomicLong idSequence = new AtomicLong(0);
    private final TransactionValidation transactionValidation;

    public TransferRepository(TransactionValidation transactionValidation) {
        this.transactionValidation = transactionValidation;
    }

    public synchronized List<Transaction> saveTransfer(Transaction outgoing, Transaction incoming) {
        if (!transactionValidation.validate(outgoing) || !transactionValidation.validate(incoming)) {
            throw new IllegalArgumentException("Invalid transfer transactions");
        }
        if (outgoing.getId() != null || incoming.getId() != null
                || outgoing.getType() != TransactionType.TRANSFER_OUT
                || incoming.getType() != TransactionType.TRANSFER_IN
                || outgoing.getAccount().getId().equals(incoming.getAccount().getId())
                || outgoing.getAmount().compareTo(incoming.getAmount()) != 0
                || !outgoing.getCreatedAt().equals(incoming.getCreatedAt())) {
            throw new IllegalArgumentException("Transfer requires two matching new transactions");
        }
        Transaction storedOutgoing = copyOf(outgoing);
        Transaction storedIncoming = copyOf(incoming);
        storedOutgoing.setId(idSequence.incrementAndGet());
        storedIncoming.setId(idSequence.incrementAndGet());
        List<Transaction> saved = List.of(storedOutgoing, storedIncoming);
        transactions.addAll(saved);
        return saved.stream().map(this::copyOf).toList();
    }

    public synchronized List<Transaction> findAllByAccountId(Long accountId) {
        return transactions.stream()
                .filter(transaction -> accountId.equals(transaction.getAccount().getId()))
                .map(this::copyOf)
                .toList();
    }

    private Transaction copyOf(Transaction transaction) {
        Account account = transaction.getAccount();
        Account snapshot = new Account(account.getId(), account.getIban(), account.getOwnerName(), account.getBalance());
        return new Transaction(transaction.getId(), snapshot, transaction.getType(), transaction.getAmount(),
                transaction.getCreatedAt(), transaction.getNote());
    }
}
