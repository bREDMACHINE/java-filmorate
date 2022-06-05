package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    Film film = new Film();
    FilmController filmcontroller = new FilmController();

    @BeforeEach
    void createUser() throws ValidationException {
        film.setName("correctFilmName");
        film.setDescription("correctFilmDescription");
        film.setDuration(100);
        film.setReleaseDate("1985-05-18");
        assertTrue(filmcontroller.isValidation(film));
    }

    @Test
    void emptyName() {
        film.setName("");
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmcontroller.isValidation(film));
        assertEquals("название не может быть пустым", exception.getMessage());
    }

    @Test
    void longDescription() {
        film.setDescription("incorrectDescription ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmcontroller.isValidation(film));
        assertEquals("максимальная длина описания — 200 символов", exception.getMessage());
    }

    @Test
    void beforeBirthdayCinema() {
        film.setReleaseDate("1800-05-18");
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmcontroller.isValidation(film));
        assertEquals("дата релиза — не может быть раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void negativeDuration() {
        film.setDuration(-1);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmcontroller.isValidation(film));
        assertEquals("продолжительность фильма должна быть положительной", exception.getMessage());
    }
}