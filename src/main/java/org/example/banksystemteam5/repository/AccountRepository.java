package org.example.banksystemteam5.repository;

import org.example.banksystemteam5.model.Account;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class AccountRepository {
    private final Map<Long, Account> accountMap =
            new ConcurrentHashMap<>();
    private final Map<Long, Set<Long>> accountIdsByUserId = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);
    private final UserRepository userRepository;

    public AccountRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(accountMap.get(id));
    }

    public List<Account> findAllByUserId(Long userId) {
        return accountIdsByUserId.getOrDefault(userId, Set.of()).stream()
                .map(accountMap::get)
                .toList();
    }

    public synchronized void updateBalances(Long fromAccountId, BigDecimal fromBalance,
                                            Long toAccountId, BigDecimal toBalance) {
        Account from = findById(fromAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Sender account does not exist"));
        Account to = findById(toAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Receiver account does not exist"));

        from.setBalance(fromBalance);
        to.setBalance(toBalance);
    }

    public Account saveAccount(Account account, Long userId) {
        if (account.getId() != null) {
            throw new IllegalArgumentException("This method only creates new accounts");
        }
        if (userId == null || userRepository.findById(userId).isEmpty()) {
            throw new IllegalArgumentException("Account owner does not exist");
        }

        account.setId(idSequence.incrementAndGet());
        accountMap.put(account.getId(), account);
        accountIdsByUserId.computeIfAbsent(userId, id -> ConcurrentHashMap.newKeySet())
                .add(account.getId());
        return account;
    }

}
