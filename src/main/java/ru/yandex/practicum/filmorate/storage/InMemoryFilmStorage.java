package ru.yandex.practicum.filmorate.storage;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long id = 1;

    public Film addFilm(Film film) {
        film.setId(id);
        films.put(id, film);
        id++;
        return film;
    }

    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    public Optional<Film> getFilm(long id) {
        return Optional.ofNullable(films.get(id));
    }

    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }
}
