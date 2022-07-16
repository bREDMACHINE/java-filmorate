package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    Genre getGenre(int id);
    List<Genre> findAllGenre();
    void addGenresFromFilm(List<Genre> genres, long id);
    List<Genre> getGenresForFilm(long id);
    void removeGenresForFilm(long id);
}
