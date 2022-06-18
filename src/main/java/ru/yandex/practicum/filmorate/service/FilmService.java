package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage inMemoryFilmStorage;
    private final UserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage, UserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addLike(long id, long userId) {
        if (inMemoryFilmStorage.isContain(id) && inMemoryUserStorage.isContain(userId)) {
            inMemoryFilmStorage.getFilm(id).getLikes().add(userId);
        }
    }

    public void removeLike(long id, long userId) {
        if (inMemoryFilmStorage.isContain(id) && inMemoryUserStorage.isContain(userId)) {
            inMemoryFilmStorage.getFilm(id).getLikes().remove(userId);
        }
    }

    public List<Film> getTopFilms(String countSting) {
        int count = 10;
        if (isNumeric(countSting)) {
            count = Integer.parseInt(countSting);
        }
        return inMemoryFilmStorage.findAllFilms().stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public static boolean isNumeric(String str) {
        if (str != null) {
            for (char c : str.toCharArray()) {
                if (!Character.isDigit(c)) return false;
            }
            return true;
        }
        return false;
    }
}
