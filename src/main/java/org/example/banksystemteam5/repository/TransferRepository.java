package org.example.banksystemteam5.repository;

import org.example.banksystemteam5.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TransferRepository {

    private final List<Transaction> transactions = new ArrayList<>();
    private final AtomicLong idSequence = new AtomicLong(0);

    public synchronized List<Transaction> saveTransfer(Transaction outgoing, Transaction incoming) {
        outgoing.setId(idSequence.incrementAndGet());
        incoming.setId(idSequence.incrementAndGet());
        List<Transaction> saved = List.of(outgoing, incoming);
        transactions.addAll(saved);
        return saved;
    }

    public synchronized List<Transaction> findAllByAccountId(Long accountId) {
        return transactions.stream()
                .filter(transaction -> accountId.equals(transaction.getAccount().getId()))
                .toList();
    }
}
