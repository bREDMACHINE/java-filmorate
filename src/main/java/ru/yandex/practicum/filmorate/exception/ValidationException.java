package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends IllegalArgumentException {
    public ValidationException(final String message) {
        super(message);
    }
}
