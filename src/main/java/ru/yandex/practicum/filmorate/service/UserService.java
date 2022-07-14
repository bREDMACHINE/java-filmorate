package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

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
        inMemoryUserStorage.addToFriends(getUser(id).getId(), getUser(friendId).getId());
    }

    public void removeFromFriends(long id, long friendId) {
        inMemoryUserStorage.removeFromFriends(getUser(id).getId(), getUser(friendId).getId());
    }

    public List<User> getListGeneralFriends(long id, long otherId) {
        return inMemoryUserStorage.getListGeneralFriends(getUser(id).getId(),getUser(otherId).getId());
    }

    public List<User> getListFriends(long id) {
        return inMemoryUserStorage.getListFriends(getUser(id).getId());
    }

    public User addUser(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return inMemoryUserStorage.addUser(user);
    }

    public User updateUser(User user) {
        if (getUser(user.getId()).getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return inMemoryUserStorage.updateUser(user);
    }

    public User getUser(long id) {
        return inMemoryUserStorage.getUser(id).orElseThrow(() -> new NotFoundException("указанный ID не существует"));
    }

    public List<User> findAllUsers() {
        return inMemoryUserStorage.findAllUsers();
    }
}
