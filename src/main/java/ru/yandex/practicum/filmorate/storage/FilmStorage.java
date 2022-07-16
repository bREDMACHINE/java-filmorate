package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);
    Film updateFilm(Film film);
    Film getFilm(long id);
    List<Film> findAllFilms();
    void addLike(long id, long userId);
    void removeLike(long id, long userId);
    List<Film> getTopFilms(int count);
}
