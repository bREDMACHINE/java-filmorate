package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {
	User user = new User();
	UserStorage inMemoryUserStorage = new InMemoryUserStorage();
	Film film = new Film();
	FilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();

	@BeforeEach
	void install() {
		film.setName("correctFilmName");
		film.setDescription("correctFilmDescription");
		film.setDuration(100);
		film.setReleaseDate("1985-05-18");
		assertTrue(inMemoryFilmStorage.isValidation(film));
		user.setName("correctUserName");
		user.setEmail("correctUserEmail@mail.ru");
		user.setLogin("correctUserLogin");
		user.setBirthday("1985-05-18");
		assertTrue(inMemoryUserStorage.isValidation(user));
	}

	@Test
	void emptyName() {
		film.setName("");
		final ValidationException exception = assertThrows(
				ValidationException.class,
				() -> inMemoryFilmStorage.isValidation(film));
		assertEquals("название не может быть пустым", exception.getMessage());
	}

	@Test
	void longDescription() {
		film.setDescription("incorrectDescription ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
		final ValidationException exception = assertThrows(
				ValidationException.class,
				() -> inMemoryFilmStorage.isValidation(film));
		assertEquals("максимальная длина описания — 200 символов", exception.getMessage());
	}

	@Test
	void beforeBirthdayCinema() {
		film.setReleaseDate("1800-05-18");
		final ValidationException exception = assertThrows(
				ValidationException.class,
				() -> inMemoryFilmStorage.isValidation(film));
		assertEquals("дата релиза — не может быть раньше 28 декабря 1895 года", exception.getMessage());
	}

	@Test
	void negativeDuration() {
		film.setDuration(-1);
		final ValidationException exception = assertThrows(
				ValidationException.class,
				() -> inMemoryFilmStorage.isValidation(film));
		assertEquals("продолжительность фильма должна быть положительной", exception.getMessage());
	}

	@Test
	void emptyEmail() {
		user.setEmail("");
		final ValidationException exception = assertThrows(
				ValidationException.class,
				() -> inMemoryUserStorage.isValidation(user));
		assertEquals("электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
	}

	@Test
	void withoutDogEmail() {
		user.setEmail("incorrectUserEmailmail.ru");
		final ValidationException exception = assertThrows(
				ValidationException.class,
				() -> inMemoryUserStorage.isValidation(user));
		assertEquals("электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
	}

	@Test
	void emptyLogin() {
		user.setLogin("");
		final ValidationException exception = assertThrows(
				ValidationException.class,
				() -> inMemoryUserStorage.isValidation(user));
		assertEquals("логин не может быть пустым и содержать пробелы", exception.getMessage());
	}

	@Test
	void withoutSpaceLogin() {
		user.setLogin("incorrect login");
		final ValidationException exception = assertThrows(
				ValidationException.class,
				() -> inMemoryUserStorage.isValidation(user));
		assertEquals("логин не может быть пустым и содержать пробелы", exception.getMessage());
	}

	@Test
	void futureBirthday() {
		user.setBirthday("2023-05-18");
		final ValidationException exception = assertThrows(
				ValidationException.class,
				() -> inMemoryUserStorage.isValidation(user));
		assertEquals("дата рождения не может быть в будущем", exception.getMessage());
	}
}
