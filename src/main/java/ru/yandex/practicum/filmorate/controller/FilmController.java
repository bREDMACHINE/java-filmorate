package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) {
        if (isValidation(film)) {
            film.setId(id);
            films.put(id, film);
            id++;
            return film;
        }
        return null;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        if (film.getId() < 1) {
            throw new ValidationException("ID не может быть меньше единицы");
        } else if (isValidation(film)) {
            films.put(film.getId(), film);
            return film;
        }
        return null;
    }

    @GetMapping("/films")
    public List<Film> findAll() {
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
}
