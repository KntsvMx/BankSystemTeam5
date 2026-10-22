package org.example.banksystemteam5.repository;

import org.example.banksystemteam5.exception.DuplicateUsernameException;
import org.example.banksystemteam5.model.User;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {

    private final Map<Long, User> userMap = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userMap.get(id));
    }

    public Optional<User> findByUsername(String username) {
        return userMap.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    public synchronized User saveUser(User user) {
        if (user.getId() != null) {
            throw new IllegalArgumentException("This method only creates new users");
        }
        if (findByUsername(user.getUsername()).isPresent()) {
            throw new DuplicateUsernameException();
        }

        user.setId(idSequence.incrementAndGet());
        userMap.put(user.getId(), user);
        return user;
    }
}
