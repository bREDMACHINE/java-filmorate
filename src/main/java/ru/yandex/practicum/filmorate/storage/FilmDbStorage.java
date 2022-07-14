package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "insert into FILMS(FILM_NAME, DESCRIPTION, MPA_ID, RELEASEDATE, DURATION, RATE) values (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
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
        String sqlQuery = "update FILMS SET FILM_NAME = ?, DESCRIPTION = ?, MPA_ID = ?, RELEASEDATE = ?, DURATION = ?, RATE = ? " +
                "WHERE FILM_ID = ?";
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
    public Optional<Film> getFilm(long id) {
        String sqlQuery = "select * from FILMS where FILM_ID = ?";
        final List <Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, id);
        if (films.size() != 1) {
            return Optional.empty();
        }
        return Optional.ofNullable(films.get(0));
    }

    @Override
    public List<Film> findAllFilms() {
        String sqlQuery = "select * from FILMS";
        return jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm);
    }

    @Override
    public void addLike(long id, long userId) {
        String sqlQuery = "insert into USER_FILMS(FILM_ID, USER_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    @Override
    public void removeLike(long id, long userId) {
        String sqlQuery = "delete from USER_FILMS where FILM_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    @Override
    public List<Film> getTopFilms(int count) {
        String sqlQuery = "select FILMS.* from FILMS left join USER_FILMS UF on FILMS.FILM_ID = UF.FILM_ID group by FILMS.FILM_ID  ORDER BY count(UF.USER_ID) DESC LIMIT ?";
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
