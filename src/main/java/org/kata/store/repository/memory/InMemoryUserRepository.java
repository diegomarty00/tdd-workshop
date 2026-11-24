package org.kata.store.repository.memory;

import java.util.HashMap;
import java.util.Map;

import org.kata.store.domain.User;
import org.kata.store.repository.UserRepository;

public class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public User findById(String id) {
        return users.get(id);
    }

    @Override
    public boolean exists(String id) {
        return users.containsKey(id);
    }
}
