package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private long id;
    private String name;
    private String description;
    private String releaseDate;
    private int duration;
    private Set<Long> likes = new HashSet<>();
}
