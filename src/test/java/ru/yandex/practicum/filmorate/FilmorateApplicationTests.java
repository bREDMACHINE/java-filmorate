package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {
	User user = new User();
	Film film = new Film();
	UserService userService = new UserService(new InMemoryUserStorage());
	FilmService filmService = new FilmService(new InMemoryFilmStorage(), userService);

	@BeforeEach
	void install() {
		film.setName("correctFilmName");
		film.setDescription("correctFilmDescription");
		film.setDuration(100);
		film.setReleaseDate(LocalDate.of(1985, 5, 18));
		assertTrue(filmService.isValidation(film));
		user.setName("correctUserName");
		user.setEmail("correctUserEmail@mail.ru");
		user.setLogin("correctUserLogin");
		user.setBirthday(LocalDate.of(1985, 5, 18));
		assertTrue(userService.isValidation(user));
	}

	@Test
	void emptyName() {
		film.setName("");
		final ValidationException exception = assertThrows(
				ValidationException.class,
				() -> filmService.isValidation(film));
		assertEquals("название не может быть пустым", exception.getMessage());
	}

	@Test
	void longDescription() {
		film.setDescription("incorrectDescription incorrectDescription incorrectDescription incorrectDescription " +
				"incorrectDescription incorrectDescription incorrectDescription incorrectDescription " +
				"incorrectDescription incorrectDescription incorrectDescription incorrectDescription " +
				"incorrectDescription incorrectDescription incorrectDescription incorrectDescription");
		final ValidationException exception = assertThrows(
				ValidationException.class,
				() -> filmService.isValidation(film));
		assertEquals("максимальная длина описания — 200 символов", exception.getMessage());
	}

	@Test
	void beforeBirthdayCinema() {
		film.setReleaseDate(LocalDate.of(1800, 5, 18));
		final ValidationException exception = assertThrows(
				ValidationException.class,
				() -> filmService.isValidation(film));
		assertEquals("дата релиза — не может быть раньше 28 декабря 1895 года", exception.getMessage());
	}

	@Test
	void negativeDuration() {
		film.setDuration(-1);
		final ValidationException exception = assertThrows(
				ValidationException.class,
				() -> filmService.isValidation(film));
		assertEquals("продолжительность фильма должна быть положительной", exception.getMessage());
	}

	@Test
	void emptyEmail() {
		user.setEmail("");
		final ValidationException exception = assertThrows(
				ValidationException.class,
				() -> userService.isValidation(user));
		assertEquals("электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
	}

	@Test
	void withoutDogEmail() {
		user.setEmail("incorrectUserEmailmail.ru");
		final ValidationException exception = assertThrows(
				ValidationException.class,
				() -> userService.isValidation(user));
		assertEquals("электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
	}

	@Test
	void emptyLogin() {
		user.setLogin("");
		final ValidationException exception = assertThrows(
				ValidationException.class,
				() -> userService.isValidation(user));
		assertEquals("логин не может быть пустым и содержать пробелы", exception.getMessage());
	}

	@Test
	void withoutSpaceLogin() {
		user.setLogin("incorrect login");
		final ValidationException exception = assertThrows(
				ValidationException.class,
				() -> userService.isValidation(user));
		assertEquals("логин не может быть пустым и содержать пробелы", exception.getMessage());
	}

	@Test
	void futureBirthday() {
		user.setBirthday(LocalDate.of(2023, 5, 18));
		final ValidationException exception = assertThrows(
				ValidationException.class,
				() -> userService.isValidation(user));
		assertEquals("дата рождения не может быть в будущем", exception.getMessage());
	}
}
