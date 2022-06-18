package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);
    Film updateFilm(Film film);
    Film getFilm(long id);
    List<Film> findAllFilms();
    boolean isValidation(Film film);
    boolean isContain(long id);
}
