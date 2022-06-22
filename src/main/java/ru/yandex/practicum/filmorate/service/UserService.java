package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage inMemoryUserStorage;

    @Autowired
    public UserService(UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addToFriends(long id, long friendId) {
        getUser(id).getFriends().add(getUser(friendId).getId());
        getUser(friendId).getFriends().add(getUser(id).getId());
    }

    public void removeFromFriends(long id, long friendId) {
        getUser(id).getFriends().remove(getUser(friendId).getId());
        getUser(friendId).getFriends().remove(getUser(id).getId());
    }

    public List<User> getListGeneralFriends(long id, long otherId) {
        return getUser(id).getFriends().stream()
                .filter(x -> getUser(otherId).getFriends().contains(x))
                .map(this::getUser)
                .collect(Collectors.toList());
    }

    public List<User> getListFriends(long id) {
        return getUser(id).getFriends().stream()
                .map(this::getUser)
                .collect(Collectors.toList());
    }

    public User addUser(User user) {
        if (isValidation(user)) {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            return inMemoryUserStorage.addUser(user);
        }
        return null;
    }

    public User updateUser(User user) {
        if (isValidation(getUser(user.getId()))) {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            return inMemoryUserStorage.updateUser(user);
        }
        return null;
    }

    public User getUser(long id) {
        return inMemoryUserStorage.getUser(id).orElseThrow(() -> new NotFoundException("указанный ID не существует"));
    }

    public List<User> findAllUsers() {
        return inMemoryUserStorage.findAllUsers();
    }

    private boolean isValidation(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("дата рождения не может быть в будущем");
        }
        return true;
    }
}
