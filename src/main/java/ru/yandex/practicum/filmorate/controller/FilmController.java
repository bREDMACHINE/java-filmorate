package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Map;

@RestController
public class FilmController {
    private final FilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable long id) {
        return inMemoryFilmStorage.getFilm(id);
    }

    @GetMapping("/films/popular")
    public List<Film> getTopFilms(@RequestParam(required = false) String count) {
        return filmService.getTopFilms(count);
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        return inMemoryFilmStorage.findAllFilms();
    }

    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) {
        return inMemoryFilmStorage.addFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable long id, @PathVariable long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLikeFromFilm(@PathVariable long id, @PathVariable long userId) {
        filmService.removeLike(id, userId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNegativeValidation(final ValidationException e) {
        return Map.of("Validation Error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleRuntimeException(final RuntimeException e) {
        return Map.of("Runtime Error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNullPointerException(final NullPointerException e) {
        return Map.of("NullPointer Error", e.getMessage());
    }
}
