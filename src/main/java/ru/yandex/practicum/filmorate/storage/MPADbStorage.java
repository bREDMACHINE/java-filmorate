package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MPADbStorage implements MPAStorage{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MPADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<MPA> getMPA(int id) {
        String sqlQuery = "select * from RATINGS where MPA_ID = ?";
        final List<MPA> ratings = jdbcTemplate.query(sqlQuery, MPADbStorage::makeMPA, id);
        if (ratings.size() != 1) {
            return Optional.empty();
        }
        return Optional.ofNullable(ratings.get(0));
    }

    @Override
    public List<MPA> findAllMPA() {
        String sqlQuery = "select * from RATINGS";
        return jdbcTemplate.query(sqlQuery, MPADbStorage::makeMPA);
    }

    @Override
    public Optional<MPA> getMPAForFilm(long id) {
        String sqlQuery = "select * from RATINGS join FILMS F on RATINGS.MPA_ID = F.MPA_ID where F.FILM_ID = ?";
        final List<MPA> ratings = jdbcTemplate.query(sqlQuery, MPADbStorage::makeMPA, id);
        if (ratings.size() != 1) {
            return Optional.empty();
        }
        return Optional.ofNullable(ratings.get(0));
    }

    static MPA makeMPA(ResultSet rs, int rowNum) throws SQLException {
        return new MPA(rs.getInt("MPA_ID"),
                rs.getString("MPA_NAME"));
    }
}
