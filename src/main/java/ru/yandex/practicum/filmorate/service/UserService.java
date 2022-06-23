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
