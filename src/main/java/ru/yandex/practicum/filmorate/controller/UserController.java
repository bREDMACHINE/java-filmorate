package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private final UserStorage inMemoryUserStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return inMemoryUserStorage.findAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable long id) {
        return inMemoryUserStorage.getUser(id);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriendsOfUser(@PathVariable long id) {
        return userService.getListFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getGeneralFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getListGeneralFriends(id, otherId);
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return inMemoryUserStorage.addUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        return inMemoryUserStorage.updateUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable long id, @PathVariable long friendId) {
        userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable long id, @PathVariable long friendId) {
        userService.removeFromFriends(id, friendId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNegativeValidation(final ValidationException e) {
        return Map.of("Validation Error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleRuntimeException(final RuntimeException e) {
        return Map.of("Runtime Error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNullPointerException(final NullPointerException e) {
        return Map.of("NullPointer Error", e.getMessage());
    }
}
