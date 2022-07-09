# java-filmorate  
Template repository for Filmorate project.  

## Диаграмма базы данных  
![](Diagrammaya.png)
## Описание базы данных  

### films  
Содержит данные о фильмах.  
Таблица включает следующие поля:  
* первичный ключ __film_id__ - идентификатор фильма;  
* name - название фильма;  
* description - описание фильма;  
* внешний ключ mpa_id (отсылает к таблице ratings) - рейтинг ассоциации кинокомпаний  
* releasedate - дата выхода;  
* duration - продолжительность фильма.  

### genres  
Содержит данные о жанрах.  
Таблица включает следующие поля:
* первичный ключ __genre_id__ - идентификатор жанра;
* name - название жанра, например:
  - Комедия,
  - Драма,
  - Мультфильм,
  - Триллер,
  - Документальный,
  - Боевик.  

### genre_films  
Содержит данные о жанрах.  
Таблица включает следующие поля:
* внешний ключ (часть составного ключа) genre_id (отсылает к таблице genres) - идентификатор жанра;
* внешний ключ (часть составного ключа) film_id (отсылает к таблице films) - идентификатор фильма.  

### ratings  
* первичный ключ __mpa_id__ - идентификатор рейтинга;
* name - название рейтинга, например:
  - G — у фильма нет возрастных ограничений,
  - PG — детям рекомендуется смотреть фильм с родителями,
  - PG-13 — детям до 13 лет просмотр не желателен,
  - R — лицам до 17 лет просматривать фильм можно только в присутствии взрослого,
  - NC-17 — лицам до 18 лет просмотр запрещён.  

### users  
Содержит данные о пользователях.  
Таблица включает следующие поля:
* первичный ключ __user_id__ - идентификатор пользователя;  
* email - почта пользователя;  
* login - логин пользователя;  
* name - имя пользователя;  
* birthday - день рождения пользователя.  

### user_films  
Содержит данные о лайках.  
Таблица включает следующие поля:
* внешний ключ (часть составного ключа) user_id (отсылает к таблице users) - идентификатор пользователя, поставившего лайк;
* внешний ключ (часть составного ключа) film_id (отсылает к таблице films) - идентификатор фильма, которому поставлен лайк.  

### friendship  
Содержит данные о статусе дружбы.  
Таблица включает следующие поля:
* первичный ключ __id_friend__ - идентификатор пользователя;  
* внешний ключ user_id (отсылает к таблице users) - идентификаторы друзей пользователя
* confirmation - статус свзяи "дружба", может иметь значения:  
  - неподтверждённая — когда один пользователь отправил запрос на добавление другого пользователя в друзья,  
  - подтверждённая — когда второй пользователь согласился на добавление.  

## Примеры запросов для основных операций  

### Получение всех пользователей:  

```
SELECT *  
FROM users
```  

### Получение всех фильмов:  

```
SELECT *
FROM films  
```  

### Получение топ 10 наиболее популярных фильмов:  

```
SELECT f.*
FROM films AS f 
WHERE f.film_id IN (SELECT uf.film_id
    FROM user_films AS uf
    GROUP BY uf.film_id
    ORDER BY COUNT(uf.user_id) DESC
    LIMIT 10)    
GROUP BY f.film_id  
ORDER BY COUNT(uf.user_id) DESC;
```
