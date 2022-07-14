package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        String sqlQuery = "insert into USERS(EMAIL, LOGIN, USER_NAME, BIRTHDAY) values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
//            if (birthday == null) {
//                stmt.setNull(4, Types.DATE);
//            } else {
                stmt.setDate(4, Date.valueOf(birthday));
//            }
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "update USERS SET EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public Optional<User> getUser(long id) {
        String sqlQuery = "select * from USERS where USER_ID = ?";
        final List <User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, id);
        if (users.size() != 1) {
            return Optional.empty();
        }
        return Optional.ofNullable(users.get(0));
    }

    @Override
    public List<User> findAllUsers() {
        String sqlQuery = "select * from USERS";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser);
    }

    @Override
    public void addToFriends(long id, long friendId) {
        String sqlQuery = "insert into USER_FRIENDS(USERONE_ID, USERTWO_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public void removeFromFriends(long id, long friendId) {
        String sqlQuery = "delete from USER_FRIENDS where USERONE_ID = ? and USERTWO_ID = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public List<User> getListGeneralFriends(long id, long otherId) {
        String sqlQuery = "select * from USERS where USER_ID in (select USERTWO_ID from USER_FRIENDS " +
                "where USERONE_ID = ? and USERTWO_ID in (select USERTWO_ID from USER_FRIENDS where USERONE_ID = ?))";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, id,otherId);
    }

    @Override
    public List<User> getListFriends(long id) {
        String sqlQuery = "select * from USERS join USER_FRIENDS on USERS.USER_ID = USER_FRIENDS.USERTWO_ID " +
                "where USER_FRIENDS.USERONE_ID = ?";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, id);
    }

    static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getLong("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("USER_NAME"),
                rs.getDate("BIRTHDAY").toLocalDate());
    }
}
