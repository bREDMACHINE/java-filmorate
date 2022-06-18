package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        if (inMemoryUserStorage.isContain(id) && inMemoryUserStorage.isContain(friendId)) {
            inMemoryUserStorage.getUser(id).getFriends().add(friendId);
            inMemoryUserStorage.getUser(friendId).getFriends().add(id);
        }
    }

    public void removeFromFriends(long id, long friendId) {
        if (inMemoryUserStorage.isContain(id) && inMemoryUserStorage.isContain(friendId)) {
            inMemoryUserStorage.getUser(id).getFriends().remove(friendId);
            inMemoryUserStorage.getUser(friendId).getFriends().remove(id);
        }
    }

    public List<User> getListGeneralFriends(long id, long otherId) {
        return inMemoryUserStorage.getUser(id).getFriends().stream()
                .filter(x -> inMemoryUserStorage.getUser(otherId).getFriends().contains(x))
                .map(inMemoryUserStorage::getUser)
                .collect(Collectors.toList());
    }

    public List<User> getListFriends(long id) {
        return inMemoryUserStorage.getUser(id).getFriends().stream()
                .map(inMemoryUserStorage::getUser)
                .collect(Collectors.toList());
    }
}
