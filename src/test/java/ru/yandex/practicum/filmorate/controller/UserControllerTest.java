package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    User user = new User();
    UserController usercontroller = new UserController();

    @BeforeEach
    void createUser() throws ValidationException {
        user.setName("correctUserName");
        user.setEmail("correctUserEmail@mail.ru");
        user.setLogin("correctUserLogin");
        user.setBirthday("1985-05-18");
        assertTrue(usercontroller.isValidation(user));
    }

    @Test
    void emptyEmail() {
        user.setEmail("");
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> usercontroller.isValidation(user));
        assertEquals("электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    void withoutDogEmail() {
        user.setEmail("incorrectUserEmailmail.ru");
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> usercontroller.isValidation(user));
        assertEquals("электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    void emptyLogin() {
        user.setLogin("");
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> usercontroller.isValidation(user));
        assertEquals("логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void withoutSpaceLogin() {
        user.setLogin("incorrect login");
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> usercontroller.isValidation(user));
        assertEquals("логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void futureBirthday() {
        user.setBirthday("2023-05-18");
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> usercontroller.isValidation(user));
        assertEquals("дата рождения не может быть в будущем", exception.getMessage());
    }
}