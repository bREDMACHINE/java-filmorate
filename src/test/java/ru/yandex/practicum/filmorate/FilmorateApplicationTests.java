package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
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
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:scriptForTests.sql")
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final GenreDbStorage genreStorage;
    private final MPADbStorage mpaStorage;

    private void addUserOne() {
        User userOneAddInTable = new User();
        userOneAddInTable.setName("testName");
        userOneAddInTable.setEmail("test@test.ru");
        userOneAddInTable.setLogin("testLogin");
        userOneAddInTable.setBirthday(LocalDate.of(1985, 5, 18));
        userStorage.addUser(userOneAddInTable);
    }

    private void addUserTwo() {
        User userTwoAddInTable = new User();
        userTwoAddInTable.setName("testNameTwo");
        userTwoAddInTable.setEmail("testTwo@test.ru");
        userTwoAddInTable.setLogin("testLoginTwo");
        userTwoAddInTable.setBirthday(LocalDate.of(1990, 1, 22));
        userStorage.addUser(userTwoAddInTable);
    }

    private void addUserThree() {
        User userThreeAddInTable = new User();
        userThreeAddInTable.setName("testNameThree");
        userThreeAddInTable.setEmail("testThree@test.ru");
        userThreeAddInTable.setLogin("testLoginThree");
        userThreeAddInTable.setBirthday(LocalDate.of(1990, 1, 11));
        userStorage.addUser(userThreeAddInTable);
    }

    private void addFilmOne() {
        Film filmOneAddInTable = new Film();
        filmOneAddInTable.setName("testNameFilm");
        filmOneAddInTable.setDescription("testDescription");
        filmOneAddInTable.setReleaseDate(LocalDate.of(1990, 1, 1));
        filmOneAddInTable.setMpa(new MPA(1));
        filmOneAddInTable.setDuration(120);
        filmOneAddInTable.setRate(0);
        filmStorage.addFilm(filmOneAddInTable);
    }

    private void addFilmTwo() {
        Film filmTwoAddInTable = new Film();
        filmTwoAddInTable.setName("testNameFilmTwo");
        filmTwoAddInTable.setDescription("testDescriptionTwo");
        filmTwoAddInTable.setReleaseDate(LocalDate.of(2000, 1, 1));
        filmTwoAddInTable.setMpa(new MPA(3));
        filmTwoAddInTable.setDuration(120);
        filmTwoAddInTable.setRate(0);
        filmStorage.addFilm(filmTwoAddInTable);
    }

    @Test
    void testAddUserStanFunc() {
        addUserOne();

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
    void testUpdateUserStanFunc() {
        addUserOne();
        User userOneUpdateInTable = userStorage.getUser(1);
        userOneUpdateInTable.setLogin("testName");
        userStorage.updateUser(userOneUpdateInTable);

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
    public void testGetUserStanFunc() {
        addUserOne();
        addUserTwo();

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
    public void testFindAllUsersStanFunc() {
        addUserOne();
        addUserTwo();

        List<User> listTwo = userStorage.findAllUsers();
        assertEquals(listTwo.size(), 2);
    }

    @Test
    public void testAddToFriendsStanFunc() {
        addUserOne();
        addUserTwo();
        addUserThree();
        userStorage.addToFriends(1, 2);
        userStorage.addToFriends(1, 3);

        assertEquals(userStorage.getListFriends(1).size(), 2);
    }

    @Test
    public void testRemoveFromFriendsStanFunc() {
        addUserOne();
        addUserTwo();
        addUserThree();
        userStorage.addToFriends(1, 2);
        userStorage.addToFriends(1, 3);
        userStorage.removeFromFriends(1,3);

        assertEquals(userStorage.getListFriends(1).size(), 1);
    }

    @Test
    public void testGetListGeneralFriendsStanFunc() {
        addUserOne();
        addUserTwo();
        addUserThree();
        userStorage.addToFriends(1, 2);
        userStorage.addToFriends(1, 3);
        userStorage.addToFriends(3,1);
        userStorage.addToFriends(2,1);

        assertEquals(userStorage.getListGeneralFriends(2, 3).size(), 1);
    }

    @Test
    public void testGetListFriendsStanFunc() {
        addUserOne();
        addUserTwo();
        addUserThree();
        userStorage.addToFriends(1, 2);
        userStorage.addToFriends(1, 3);
        userStorage.addToFriends(3,1);
        userStorage.addToFriends(2,1);

        assertEquals(userStorage.getListFriends(2).size(), 1);
    }

    @Test
    public void testAddFilmStanFunc() {
        addFilmOne();

        Film film = filmStorage.getFilm(1);
        assertThat(Optional.of(film))
                .isPresent()
                .hasValueSatisfying(f ->
                        assertThat(f)
                                .hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("name", "testNameFilm")
                                .hasFieldOrPropertyWithValue("description", "testDescription")
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1990, 1, 1))
                );
    }

    @Test
    public void testUpdateFilmStanFunc() {
        addFilmOne();
        Film filmOneUpdateInTable = filmStorage.getFilm(1);
        filmOneUpdateInTable.setName("testUpdateName");
        filmOneUpdateInTable.setMpa(new MPA(1));
        filmStorage.updateFilm(filmOneUpdateInTable);

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
    public void testGetFilmStanFunc() {
        addFilmOne();
        addFilmTwo();

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
    public void testFindAllFilmsStanFunc() {
        addFilmOne();
        addFilmTwo();

        List<Film> listTwo = filmStorage.findAllFilms();
        assertEquals(listTwo.size(), 2);
    }

    @Test
    public void testAddLikeStanFunc() {
        addUserOne();
        addUserTwo();
        addUserThree();
        addFilmOne();
        addFilmTwo();
        filmStorage.addLike(1, 1);
        filmStorage.addLike(1, 2);
        filmStorage.addLike(1, 3);
        filmStorage.addLike(2, 1);
        filmStorage.addLike(2, 2);

        List<Film> topFilm = filmStorage.getTopFilms(1);
        assertEquals(topFilm, List.of(filmStorage.getFilm(1)));
    }

    @Test
    public void testRemoveLikeStanFunc() {
        addUserOne();
        addUserTwo();
        addUserThree();
        addFilmOne();
        addFilmTwo();
        filmStorage.addLike(1, 1);
        filmStorage.addLike(1, 2);
        filmStorage.addLike(1, 3);
        filmStorage.addLike(2, 1);
        filmStorage.addLike(2, 2);
        filmStorage.removeLike(1,3);
        filmStorage.removeLike(1,2);

        List<Film> topFilm = filmStorage.getTopFilms(1);
        assertEquals(topFilm, List.of(filmStorage.getFilm(2)));
    }

    @Test
    public void testGetTopFilmsStanFunc() {
        addUserOne();
        addUserTwo();
        addUserThree();
        addFilmOne();
        addFilmTwo();
        filmStorage.addLike(2, 1);

        List<Film> topFilm = filmStorage.getTopFilms(1);
        assertEquals(topFilm, List.of(filmStorage.getFilm(2)));
    }

    @Test
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
    public void testFindAllGenreStanFunc() {

        List<Genre> listTwo = genreStorage.findAllGenre();
        assertEquals(listTwo.size(), 6);
    }

    @Test
    public void testAddGenresFromFilmStanFunc() {
        addFilmOne();
        addFilmTwo();
        List<Genre> fromFilm = List.of(new Genre(1), new Genre(3));
        genreStorage.addGenresFromFilm(fromFilm, 1);

        List<Genre> forFilm = genreStorage.getGenresForFilm(1);
        assertEquals(forFilm.size(), 2);
    }

    @Test
    public void testGetGenresForFilmStanFunc() {
        addFilmOne();
        addFilmTwo();
        List<Genre> fromFilm = List.of(new Genre(2), new Genre(5), new Genre(6));
        genreStorage.addGenresFromFilm(fromFilm, 2);

        List<Genre> forFilm = genreStorage.getGenresForFilm(2);
        assertEquals(forFilm.size(), 3);
    }

    @Test
    public void testRemoveGenresForFilmStanFunc() {
        addFilmOne();
        addFilmTwo();
        List<Genre> fromFilm = List.of(new Genre(2), new Genre(5), new Genre(6));
        genreStorage.addGenresFromFilm(fromFilm, 2);
        genreStorage.removeGenresForFilm(2);

        List<Genre> forFilm = genreStorage.getGenresForFilm(2);
        assertEquals(forFilm.size(), 0);
    }

    @Test
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
    public void testFindAllMPAStanFunc() {
        List<MPA> listTwo = mpaStorage.findAllMPA();
        assertEquals(listTwo.size(), 5);
    }

    @Test
    public void testGetMPAForFilmStanFunc() {
        addFilmOne();
        addFilmTwo();

        MPA mpa = mpaStorage.getMPAForFilm(2);
        assertEquals(mpa.getName(), "PG-13");
    }
}