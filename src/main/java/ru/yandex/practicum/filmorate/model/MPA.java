package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MPA {
    private int id;
    private String name;

    public MPA(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public MPA() {
    }
}
