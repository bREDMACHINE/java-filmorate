package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenre(int id) {
        String sqlQuery = "SELECT * FROM genres WHERE genre_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, GenreDbStorage::makeGenre, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("указанный ID не существует");
        }
    }

    @Override
    public List<Genre> findAllGenre() {
        String sqlQuery = "SELECT * FROM genres";
        return jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre);
    }

    @Override
    public void addGenresFromFilm(List<Genre> genres, long id) {
        String sqlQuery = "INSERT INTO genre_films(film_id, genre_id) VALUES (?, ?)";
        for (Genre genre : genres) {
            jdbcTemplate.update(sqlQuery, id, genre.getId());
        }
    }

    @Override
    public List<Genre> getGenresForFilm(long id) {
        String sqlQuery = "SELECT * FROM genres WHERE genre_id IN (SELECT genre_id FROM genre_films WHERE film_id = ?)";
        return jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre, id);
    }

    @Override
    public void removeGenresForFilm(long id) {
        String sqlQuery = "DELETE FROM genre_films WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    private static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME"));
    }
}
