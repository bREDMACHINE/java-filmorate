package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.impl.MPADbStorage;

import java.util.List;

@Service
public class MPAService {

    private final MPADbStorage mpaDbStorage;

    @Autowired
    public MPAService(MPADbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public MPA getMPA(int id) {
        return mpaDbStorage.getMPA(id);
    }

    public List<MPA> findAllMPA() {
        return mpaDbStorage.findAllMPA();
    }

    public MPA getMPAForFilm(long id) {
        return mpaDbStorage.getMPAForFilm(id);
    }
}
