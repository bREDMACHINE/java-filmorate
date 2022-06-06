package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
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

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        if (user.getId() < 1) {
            throw new ValidationException("ID не может быть меньше единицы");
        } else if (isValidation(user)) {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            return user;
        }
        return null;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
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
}
