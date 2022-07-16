package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "INSERT INTO films(film_name, description, mpa_id, releasedate, duration, rate) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            if (film.getMpa() != null) {
                stmt.setInt(3, film.getMpa().getId());
            }
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(5, film.getDuration());
            stmt.setInt(6, film.getRate());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET film_name = ?, description = ?, mpa_id = ?, releasedate = ?, duration = ?, rate = ? " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getMpa().getId(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getId());
        return film;
    }

    @Override
    public Film getFilm(long id) {
        String sqlQuery = "SELECT * FROM films WHERE film_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, FilmDbStorage::makeFilm, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("указанный ID не существует");
        }
    }

    @Override
    public List<Film> findAllFilms() {
        String sqlQuery = "SELECT * FROM films";
        return jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm);
    }

    @Override
    public void addLike(long id, long userId) {
        String sqlQuery = "INSERT INTO user_films(film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    @Override
    public void removeLike(long id, long userId) {
        String sqlQuery = "DELETE FROM user_films WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    @Override
    public List<Film> getTopFilms(int count) {
        String sqlQuery = "SELECT films.* FROM films LEFT JOIN user_films ON films.film_id = user_films.film_id " +
                "GROUP BY films.film_id  ORDER BY COUNT(user_films.user_id) DESC LIMIT ?";
        return jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, count);
    }

    private static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("FILM_ID");
        String name = rs.getString("FILM_NAME");
        String description = rs.getString("DESCRIPTION");
        LocalDate releaseDate = rs.getDate("RELEASEDATE").toLocalDate();
        int duration = rs.getInt("DURATION");
        int rate = rs.getInt("RATE");
        return new Film(id, name, description, releaseDate, duration, rate);
    }
}
