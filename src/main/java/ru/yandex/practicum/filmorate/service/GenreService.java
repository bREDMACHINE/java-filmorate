package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GenreService {

    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre getGenre(int id) {
        return genreStorage.getGenre(id);
    }

    public List<Genre> findAllGenres() {
        return genreStorage.findAllGenre();
    }

    public void addGenresFromFilm(Set<Genre> genres, long id) {
        genreStorage.addGenresFromFilm(new ArrayList<>(genres), id);
    }

    public Set<Genre> getGenresForFilm(long id) {
        return new HashSet<>(genreStorage.getGenresForFilm(id));
    }

    public void removeGenresForFilm(long id) {
        genreStorage.removeGenresForFilm(id);
    }
}
