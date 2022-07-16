package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Repository
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        String sqlQuery = "INSERT INTO users(email, login, user_name, birthday) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            stmt.setDate(4, Date.valueOf(birthday));
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users SET email = ?, login = ?, user_name = ?, birthday = ? WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public User getUser(long id) {
        String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, UserDbStorage::makeUser, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("указанный ID не существует");
        }
    }

    @Override
    public List<User> findAllUsers() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser);
    }

    @Override
    public void addToFriends(long id, long friendId) {
        String sqlQuery = "INSERT INTO user_friends(userone_id, usertwo_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public void removeFromFriends(long id, long friendId) {
        String sqlQuery = "DELETE FROM user_friends WHERE userone_id = ? AND usertwo_id = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public List<User> getListGeneralFriends(long id, long otherId) {
        String sqlQuery = "SELECT * FROM users WHERE user_id IN (SELECT usertwo_id FROM user_friends " +
                "WHERE userone_id = ? AND usertwo_id IN (SELECT usertwo_id FROM user_friends WHERE userone_id = ?))";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, id,otherId);
    }

    @Override
    public List<User> getListFriends(long id) {
        String sqlQuery = "SELECT * FROM users JOIN user_friends ON users.user_id = user_friends.usertwo_id " +
                "WHERE user_friends.userone_id = ?";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, id);
    }

    private static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getLong("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("USER_NAME"),
                rs.getDate("BIRTHDAY").toLocalDate());
    }
}
