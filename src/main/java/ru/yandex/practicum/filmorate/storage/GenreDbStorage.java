package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreDbStorage implements GenreStorage{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Genre> getGenre(int id) {
        String sqlQuery = "select * from GENRES where GENRE_ID = ?";
        final List <Genre> genres = jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre, id);
        if (genres.size() != 1) {
            return Optional.empty();
        }
        return Optional.ofNullable(genres.get(0));
    }

    @Override
    public List<Genre> findAllGenre() {
        String sqlQuery = "select * from GENRES";
        return jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre);
    }

    @Override
    public void addGenresForFilm(List<Genre> genres, long id) {
        for (Genre genre : genres) {
            String sqlQuery = "insert into GENRE_FILMS(FILM_ID, GENRE_ID) values (?, ?)";
            jdbcTemplate.update(sqlQuery, id, genre.getId());
        }
    }

    @Override
    public List<Genre> getGenresForFilm(long id) {
        String sqlQuery = "select * from GENRES where GENRE_ID in (select GENRE_ID from GENRE_FILMS where FILM_ID = ?)";
        return jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre, id);
    }

    @Override
    public void removeGenresForFilm(long id) {
        String sqlQuery = "delete from GENRE_FILMS where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME"));
    }
}
