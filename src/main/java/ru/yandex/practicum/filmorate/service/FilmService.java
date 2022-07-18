package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage inMemoryFilmStorage;
    private final UserService userService;
    private final MPAService mpaService;
    private final GenreService genreService;

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage, UserService userService, MPAService mpaService, GenreService genreService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.userService = userService;
        this.mpaService = mpaService;
        this.genreService = genreService;
    }

    public void addLike(long id, long userId) {
        inMemoryFilmStorage.addLike(getFilm(id).getId(), userService.getUser(userId).getId());
    }

    public void removeLike(long id, long userId) {
        inMemoryFilmStorage.removeLike(getFilm(id).getId(), userService.getUser(userId).getId());
    }

    public List<Film> getTopFilms(int count) {
        List<Film> films = inMemoryFilmStorage.getTopFilms(count);
        for (Film film : films) {
            film.setGenres(genreService.getGenresForFilm(film.getId()));
            film.setMpa(mpaService.getMPAForFilm(film.getId()));
        }
        return films;
    }

    public Film addFilm(Film film) {
        if (isValid(film)) {
            Film addedFilm = inMemoryFilmStorage.addFilm(film);
            if (film.getGenres().size() != 0) {
                genreService.addGenresFromFilm(film.getGenres(), film.getId());
            }
            addedFilm.setGenres(genreService.getGenresForFilm(addedFilm.getId()));
            addedFilm.setMpa(mpaService.getMPAForFilm(addedFilm.getId()));
            return addedFilm;
        }
        return null;
    }

    public Film updateFilm(Film film) {
        if (isValid(getFilm(film.getId()))) {
            genreService.removeGenresForFilm(film.getId());
            Film updatedFilm = inMemoryFilmStorage.updateFilm(film);
            if (film.getGenres().size() != 0) {
                genreService.addGenresFromFilm(film.getGenres(), film.getId());
            }

            updatedFilm.setGenres(genreService.getGenresForFilm(updatedFilm.getId()));
            updatedFilm.setMpa(mpaService.getMPAForFilm(updatedFilm.getId()));
            return updatedFilm;
        }
        return null;
    }

    public Film getFilm(long id) {
        Film queriedFilm = inMemoryFilmStorage.getFilm(id);
        queriedFilm.setGenres(genreService.getGenresForFilm(queriedFilm.getId()));
        queriedFilm.setMpa(mpaService.getMPAForFilm(queriedFilm.getId()));
        return queriedFilm;
    }

    public List<Film> findAllFilms() {
        return inMemoryFilmStorage.findAllFilms();
    }

    private boolean isValid(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("дата релиза — не может быть раньше 28 декабря 1895 года");
        }
        return true;
    }
}
