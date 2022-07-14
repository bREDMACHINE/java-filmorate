package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    Optional<Genre> getGenre(int id);
    List<Genre> findAllGenre();
    void addGenresForFilm(List<Genre> genres, long id);
    List<Genre> getGenresForFilm(long id);
    void removeGenresForFilm(long id);
}
