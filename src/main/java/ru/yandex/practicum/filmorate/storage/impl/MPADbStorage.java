package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MPADbStorage implements MPAStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MPADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MPA getMPA(int id) {
        String sqlQuery = "SELECT * FROM ratings WHERE mpa_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, MPADbStorage::makeMPA, id);
        } catch (DataAccessException e) {
            throw new EntityNotFoundException("указанный ID не существует");
        }
    }

    @Override
    public List<MPA> findAllMPA() {
        String sqlQuery = "SELECT * FROM ratings";
        return jdbcTemplate.query(sqlQuery, MPADbStorage::makeMPA);
    }

    @Override
    public MPA getMPAForFilm(long id) {
        String sqlQuery = "SELECT * FROM ratings JOIN films ON ratings.mpa_id = films.mpa_id WHERE films.film_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, MPADbStorage::makeMPA, id);
        } catch (DataAccessException e) {
            throw new EntityNotFoundException("указанный ID не существует");
        }
    }

    private static MPA makeMPA(ResultSet rs, int rowNum) throws SQLException {
        return new MPA(rs.getInt("MPA_ID"),
                rs.getString("MPA_NAME"));
    }
}
