package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Optional;

public interface MPAStorage {
    Optional<MPA> getMPA(int id);
    List<MPA> findAllMPA();
    Optional<MPA> getMPAForFilm(long id);
}
