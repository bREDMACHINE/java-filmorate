package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 1;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public User addUser(User user) {
        if (isValidation(user)) {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            user.setId(id);
            users.put(id, user);
            id++;
            return user;
        }
        return null;
    }

    public User updateUser(User user) {
        isContain(user.getId());
        if (isValidation(user)) {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            return user;
        }
        return null;
    }

    public User getUser(long id) {
        isContain(id);
        return users.get(id);
    }

    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    public boolean isValidation(User user) {
        LocalDate userBirthday = LocalDate.parse(user.getBirthday(), formatter);
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        } else if (userBirthday.isAfter(LocalDate.now())) {
            throw new ValidationException("дата рождения не может быть в будущем");
        }
        return true;
    }

    public boolean isContain(long id) {
        if (!users.containsKey(id)) {
            throw new NullPointerException("указанный ID не существует");
        }
        return true;
    }
}
