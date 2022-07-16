package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User addUser(User user);
    User updateUser(User user);
    User getUser(long id);
    List<User> findAllUsers();
    void addToFriends(long id, long friendId);
    void removeFromFriends(long id, long friendId);
    List<User> getListGeneralFriends(long id, long otherId);
    List<User> getListFriends(long id);
}
