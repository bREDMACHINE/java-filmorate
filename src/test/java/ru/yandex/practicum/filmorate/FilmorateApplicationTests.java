package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.MPADbStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final GenreDbStorage genreStorage;
    private final MPADbStorage mpaStorage;

    @Test
    @Order(1)
    void testAddUserStanFunc() {
        User userInTable = new User();
        userInTable.setName("testName");
        userInTable.setEmail("test@test.ru");
        userInTable.setLogin("testLogin");
        userInTable.setBirthday(LocalDate.of(1985, 5, 18));
        userStorage.addUser(userInTable);

        User user = userStorage.getUser(1);
        assertThat(Optional.of(user))
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u)
                                .hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("name", "testName")
                                .hasFieldOrPropertyWithValue("login", "testLogin")
                                .hasFieldOrPropertyWithValue("email", "test@test.ru")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1985, 5, 18))
                );
    }

    @Test
    @Order(2)
    void testUpdateUserStanFunc() {
        User userInTable = userStorage.getUser(1);
        userInTable.setLogin("testName");
        userStorage.updateUser(userInTable);

        User user = userStorage.getUser(1);
        assertThat(Optional.of(user))
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u)
                                .hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("name", "testName")
                                .hasFieldOrPropertyWithValue("login", "testName")
                                .hasFieldOrPropertyWithValue("email", "test@test.ru")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1985, 5, 18))
                );
    }

    @Test
    @Order(3)
    public void testGetUserStanFunc() {
        User userInTable = new User();
        userInTable.setName("testNameTwo");
        userInTable.setEmail("testTwo@test.ru");
        userInTable.setLogin("testLoginTwo");
        userInTable.setBirthday(LocalDate.of(1990, 1, 22));
        userStorage.addUser(userInTable);

        User user = userStorage.getUser(2);
        assertThat(Optional.of(user))
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u)
                                .hasFieldOrPropertyWithValue("id", 2L)
                                .hasFieldOrPropertyWithValue("name", "testNameTwo")
                                .hasFieldOrPropertyWithValue("login", "testLoginTwo")
                                .hasFieldOrPropertyWithValue("email", "testTwo@test.ru")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1990, 1, 22))
                );
    }

    @Test
    @Order(4)
    public void testFindAllUsersStanFunc() {
        List<User> listTwo = userStorage.findAllUsers();
        assertEquals(listTwo.size(), 2);
    }

    @Test
    @Order(5)
    public void testAddToFriendsStanFunc() {
        User userInTable = new User();
        userInTable.setName("testNameThree");
        userInTable.setEmail("testThree@test.ru");
        userInTable.setLogin("testLoginThree");
        userInTable.setBirthday(LocalDate.of(1990, 1, 11));
        userStorage.addUser(userInTable);
        userStorage.addToFriends(1, 2);
        userStorage.addToFriends(1, 3);

        assertEquals(userStorage.getListFriends(1).size(), 2);
    }

    @Test
    @Order(6)
    public void testRemoveFromFriendsStanFunc() {
        userStorage.removeFromFriends(1,3);

        assertEquals(userStorage.getListFriends(1).size(), 1);
    }

    @Test
    @Order(7)
    public void testGetListGeneralFriendsStanFunc() {
        userStorage.addToFriends(1,3);
        userStorage.addToFriends(3,1);
        userStorage.addToFriends(2,1);

        assertEquals(userStorage.getListGeneralFriends(2, 3).size(), 1);
    }

    @Test
    @Order(8)
    public void testGetListFriendsStanFunc() {

        assertEquals(userStorage.getListFriends(2).size(), 1);
    }

    @Test
    @Order(9)
    public void testAddFilmStanFunc() {
        Film filmInTable = new Film();
        filmInTable.setName("testNameFilm");
        filmInTable.setDescription("testDescription");
        filmInTable.setReleaseDate(LocalDate.of(1990, 1, 1));
        filmInTable.setMpa(new MPA(1));
        filmInTable.setDuration(120);
        filmInTable.setRate(1);
        filmStorage.addFilm(filmInTable);

        Film film = filmStorage.getFilm(1);
        assertThat(Optional.of(film))
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u)
                                .hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("name", "testNameFilm")
                                .hasFieldOrPropertyWithValue("description", "testDescription")
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1990, 1, 1))
                );
    }

    @Test
    @Order(10)
    public void testUpdateFilmStanFunc() {
        Film filmInTable = filmStorage.getFilm(1);
        filmInTable.setName("testUpdateName");
        filmInTable.setMpa(new MPA(1));
        filmStorage.updateFilm(filmInTable);

        Film film = filmStorage.getFilm(1);
        assertThat(Optional.of(film))
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u)
                                .hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("name", "testUpdateName")
                                .hasFieldOrPropertyWithValue("description", "testDescription")
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1990, 1, 1))
                );
    }

    @Test
    @Order(11)
    public void testGetFilmStanFunc() {
        Film filmInTable = new Film();
        filmInTable.setName("testNameFilmTwo");
        filmInTable.setDescription("testDescriptionTwo");
        filmInTable.setReleaseDate(LocalDate.of(2000, 1, 1));
        filmInTable.setMpa(new MPA(3));
        filmInTable.setDuration(120);
        filmInTable.setRate(1);
        filmStorage.addFilm(filmInTable);

        Film film = filmStorage.getFilm(2);
        assertThat(Optional.of(film))
                .isPresent()
                .hasValueSatisfying(f ->
                        assertThat(f)
                                .hasFieldOrPropertyWithValue("id", 2L)
                                .hasFieldOrPropertyWithValue("name", "testNameFilmTwo")
                                .hasFieldOrPropertyWithValue("description", "testDescriptionTwo")
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2000, 1, 1))
                );
    }

    @Test
    @Order(12)
    public void testFindAllFilmsStanFunc() {
        List<Film> listTwo = filmStorage.findAllFilms();
        assertEquals(listTwo.size(), 2);
    }

    @Test
    @Order(13)
    public void testAddLikeStanFunc() {
        filmStorage.addLike(1, 1);
        filmStorage.addLike(1, 2);
        filmStorage.addLike(1, 3);
        filmStorage.addLike(2, 1);
        filmStorage.addLike(2, 2);

        List<Film> topFilm = filmStorage.getTopFilms(1);
        assertEquals(topFilm, List.of(filmStorage.getFilm(1)));
    }

    @Test
    @Order(14)
    public void testRemoveLikeStanFunc() {
        filmStorage.removeLike(1,3);
        filmStorage.removeLike(1,2);

        List<Film> topFilm = filmStorage.getTopFilms(1);
        assertEquals(topFilm, List.of(filmStorage.getFilm(2)));
    }

    @Test
    @Order(15)
    public void testGetTopFilmsStanFunc() {
        filmStorage.removeLike(2,2);
        filmStorage.removeLike(2,1);

        List<Film> topFilm = filmStorage.getTopFilms(1);
        assertEquals(topFilm, List.of(filmStorage.getFilm(1)));
    }

    @Test
    @Order(16)
    public void testGetGenreStanFunc() {
        Genre genre = genreStorage.getGenre(2);
        assertThat(Optional.of(genre))
                .isPresent()
                .hasValueSatisfying(g ->
                        assertThat(g)
                                .hasFieldOrPropertyWithValue("id", 2)
                                .hasFieldOrPropertyWithValue("name", "Драма")
                );
    }

    @Test
    @Order(17)
    public void testFindAllGenreStanFunc() {

        List<Film> listTwo = filmStorage.findAllFilms();
        assertEquals(listTwo.size(), 2);
    }

    @Test
    @Order(18)
    public void testAddGenresFromFilmStanFunc() {
        List<Genre> fromFilm = List.of(new Genre(1), new Genre(3));
        genreStorage.addGenresFromFilm(fromFilm, 1);

        List<Genre> forFilm = genreStorage.getGenresForFilm(1);
        assertEquals(forFilm.size(), 2);
    }

    @Test
    @Order(19)
    public void testGetGenresForFilmStanFunc() {
        List<Genre> fromFilm = List.of(new Genre(2), new Genre(5), new Genre(6));
        genreStorage.addGenresFromFilm(fromFilm, 2);

        List<Genre> forFilm = genreStorage.getGenresForFilm(2);
        assertEquals(forFilm.size(), 3);
    }

    @Test
    @Order(20)
    public void testRemoveGenresForFilmStanFunc() {
        genreStorage.removeGenresForFilm(2);

        List<Genre> forFilm = genreStorage.getGenresForFilm(2);
        assertEquals(forFilm.size(), 0);
    }

    @Test
    @Order(21)
    public void testGetMPAStanFunc() {
        MPA rating = mpaStorage.getMPA(2);
        assertThat(Optional.of(rating))
                .isPresent()
                .hasValueSatisfying(r ->
                        assertThat(r)
                                .hasFieldOrPropertyWithValue("id", 2)
                                .hasFieldOrPropertyWithValue("name", "PG")
                );
    }

    @Test
    @Order(22)
    public void testFindAllMPAStanFunc() {
        List<MPA> listTwo = mpaStorage.findAllMPA();
        assertEquals(listTwo.size(), 5);
    }

    @Test
    @Order(23)
    public void testGetMPAForFilmStanFunc() {

        MPA mpa = mpaStorage.getMPAForFilm(2);
        assertEquals(mpa.getName(), "PG-13");
    }
}