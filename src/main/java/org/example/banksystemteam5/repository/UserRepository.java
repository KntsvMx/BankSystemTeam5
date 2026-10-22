package org.example.banksystemteam5.repository;

import org.example.banksystemteam5.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {

    private final Map<Long, User> userMap = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userMap.get(id)).map(this::copyOf);
    }

    public Optional<User> findByUsername(String username) {
        return userMap.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .map(this::copyOf);
    }

    public synchronized User saveUser(User user) {
        if (user == null || user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        User stored = copyOf(user);
        if (stored.getId() != null) {
            throw new IllegalArgumentException("This method only creates new users");
        }
        if (findByUsername(stored.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken");
        }

        stored.setId(idSequence.incrementAndGet());
        userMap.put(stored.getId(), stored);
        return copyOf(stored);
    }

    private User copyOf(User user) {
        return new User(user.getId(), user.getUsername(), user.getPasswordHash(), user.getRole());
    }
}
