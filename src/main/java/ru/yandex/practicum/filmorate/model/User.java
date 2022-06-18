package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private String birthday;
    private Set<Long> friends = new HashSet<>();
}
