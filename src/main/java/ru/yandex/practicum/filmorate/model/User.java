package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class User {
    private long id;
    @Email(message = "электронная почта не может быть пустой и должна содержать символ @")
    private String email;
    @NotBlank(message = "логин не может быть пустым и содержать пробелы")
    @NotEmpty(message = "логин не может быть пустым и содержать пробелы")
    private String login;
    private String name;
    @Past(message = "день рождения не может быть сегодня и в будущем")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    public User(long user_id, String email, String login, String user_name, LocalDate birthday) {
        this.id = user_id;
        this.email = email;
        this.login = login;
        this.name = user_name;
        this.birthday = birthday;
    }
}
