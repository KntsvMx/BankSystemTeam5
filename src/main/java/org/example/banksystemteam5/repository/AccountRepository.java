package org.example.banksystemteam5.repository;

import org.example.banksystemteam5.model.Account;
import org.example.banksystemteam5.validation.AccountValidation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

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
    private final AccountValidation accountValidation;

    public AccountRepository(UserRepository userRepository, AccountValidation accountValidation) {
        this.userRepository = userRepository;
        this.accountValidation = accountValidation;
    }

    public synchronized Optional<Account> findById(Long id) {
        return Optional.ofNullable(accountMap.get(id)).map(this::copyOf);
    }

    public synchronized List<Account> findAllByUserId(Long userId) {
        return accountIdsByUserId.getOrDefault(userId, Set.of()).stream()
                .map(accountMap::get)
                .map(this::copyOf)
                .toList();
    }

    public synchronized void transferBalance(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        if (fromAccountId == null || toAccountId == null || fromAccountId.equals(toAccountId)
                || amount == null || amount.signum() <= 0 || amount.scale() > 2
                || amount.precision() - amount.scale() > 15) {
            throw new IllegalArgumentException("Invalid balance transfer");
        }
        Account from = accountMap.get(fromAccountId);
        Account to = accountMap.get(toAccountId);
        if (from == null || to == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        if (from.getBalance().compareTo(amount) < 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient funds");
        }

        BigDecimal fromBalance = from.getBalance().subtract(amount);
        BigDecimal toBalance = to.getBalance().add(amount);
        from.setBalance(fromBalance);
        to.setBalance(toBalance);
    }

    public synchronized Account saveAccount(Account account, Long userId) {
        if (account == null || !accountValidation.validate(account)) {
            throw new IllegalArgumentException("Invalid account");
        }
        Account stored = copyOf(account);
        if (stored.getId() != null) {
            throw new IllegalArgumentException("This method only creates new accounts");
        }
        if (userId == null || userRepository.findById(userId).isEmpty()) {
            throw new IllegalArgumentException("Account owner does not exist");
        }

        stored.setId(idSequence.incrementAndGet());
        accountMap.put(stored.getId(), stored);
        accountIdsByUserId.computeIfAbsent(userId, id -> ConcurrentHashMap.newKeySet())
                .add(stored.getId());
        return copyOf(stored);
    }

    private Account copyOf(Account account) {
        return new Account(account.getId(), account.getIban(), account.getOwnerName(), account.getBalance());
    }
}
