package ru.yandex.practicum.filmorate.storage;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long id = 1;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Film addFilm(Film film) {
        if (isValidation(film)) {
            film.setId(id);
            films.put(id, film);
            id++;
            return film;
        }
        return null;
    }

    public Film updateFilm(Film film) {
        isContain(film.getId());
        if (isValidation(film)) {
            films.put(film.getId(), film);
            return film;
        }
        return null;
    }

    public Film getFilm(long id) {
        isContain(id);
        return films.get(id);
    }

    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    public boolean isValidation(Film film) {
        LocalDate filmReleaseDate = LocalDate.parse(film.getReleaseDate(), formatter);
        if (film.getName().isEmpty()) {
            throw new ValidationException("название не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("максимальная длина описания — 200 символов");
        } else if (filmReleaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("дата релиза — не может быть раньше 28 декабря 1895 года");
        } else if (film.getDuration() < 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
        return true;
    }

    public boolean isContain(long id) {
        if (!films.containsKey(id)) {
            throw new NullPointerException("указанный ID не существует");
        }
        return true;
    }
}
