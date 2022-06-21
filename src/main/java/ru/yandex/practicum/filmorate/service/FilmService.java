package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage inMemoryFilmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage, UserService userService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.userService = userService;
    }

    public void addLike(long id, long userId) {
        getFilm(id).getLikes().add(userService.getUser(userId).getId());
    }

    public void removeLike(long id, long userId) {
        getFilm(id).getLikes().remove(userService.getUser(userId).getId());
    }

    public List<Film> getTopFilms(int count) {
        return findAllFilms().stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film addFilm(Film film) {
        if (isValidation(film)) {
            return inMemoryFilmStorage.addFilm(film);
        }
        return null;
    }

    public Film updateFilm(Film film) {
        if (isValidation(getFilm(film.getId()))) {
            return inMemoryFilmStorage.updateFilm(film);
        }
        return null;
    }

    public Film getFilm(long id) {
        return Optional.ofNullable(inMemoryFilmStorage.getFilm(id))
                .orElseThrow(() -> new NotFoundException("указанный ID не существует"));
    }

    public List<Film> findAllFilms() {
        return inMemoryFilmStorage.findAllFilms();
    }

    public boolean isValidation(Film film) {
        if (film.getName().isEmpty()) {
            throw new ValidationException("название не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("максимальная длина описания — 200 символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("дата релиза — не может быть раньше 28 декабря 1895 года");
        }
        else if (film.getDuration() < 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
        return true;
    }
}
