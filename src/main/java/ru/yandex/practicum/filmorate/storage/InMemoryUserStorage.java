package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 1;

    public User addUser(User user) {
        user.setId(id);
        users.put(id, user);
        id++;
        return user;
    }

    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public Optional<User> getUser(long id) {
        return Optional.ofNullable(users.get(id));
    }

    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }
}
