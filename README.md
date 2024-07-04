# JAVA-FILMORATE
Бэкенд для сервиса, который будет работать с фильмами и оценками пользователей, а также возвращать топ-5 фильмов, рекомендованных к просмотру.

## БД Структура:
![Image](./src/main/resources/DBDiagram.png)

## Описание БД:
Хранение фильмов организовано пятью таблицами:

**films**  
Содержит основную информацию о фильмах.  
Таблица включает такие поля:

  * первичный ключ film_id — идентификатор фильма; 
  * name — название фильма; 
  * description — описание фильма; 
  * release_date — дата выхода фильма.
  * duration — продолжительность фильма (храниться в минутах)
  * внешний ключ rating_id (ссылается на таблицу rating) — идентификатор рейтинга.


**genre**  
Содержит список возможных жанров фильмов.  
Таблица включает такие поля:

* первичный ключ genre_id — идентификатор жанра;
* name — наименование жанра;

**films_genres**  
Содержит список всех жанров для всех фильмов.  
Таблица включает такие поля:

* первичный ключ films_genres_id - суррогатный ключ
* внешний ключ genre_id (ссылается на таблицу genre) — идентификатор жанра.
* первичный ключ genre_id — идентификатор жанра;

**rating**  
Содержит список возможных рейтингов фильмов.  
Таблица включает такие поля:

* первичный ключ rating — идентификатор рейтинга;
* name — наименование рейтинга;


**films_likes**  
Аккумулирует список идентификаторов фильмов и идентификаторов пользователей поставивших данному фильму лайк.  
Таблица включает:

* первичный ключ films_likes_id - суррогатный ключ
* внешний ключ film_id (ссылается на таблицу films) — идентификатор фильма.
* внешний ключ user_id (ссылается на таблицу users) — идентификатор пользователя.

Хранение пользователей организовано тремя таблицами:  

**users**  
Содержит основную информацию о пользователе.  
Таблица включает такие поля:

* первичный ключ user_id — идентификатор пользователя;
* email — адрес электронной почты;
* login — логин пользователя;
* name — Имя пользователя.
* birthday — дата рождения пользователя

При этом хранение перечня друзей организовано двумя таблицами - запросы и подтвержденные друзья

**requested_friends**  
Аккумулирует список пар идентификаторов пользователей в котором один отправил приглашение в друзья другому.  
Таблица включает:

* первичный ключ requested_friends_id - суррогатный ключ
* внешний ключ user_id (ссылается на таблицу users) — идентификатор пользователя который отправил запрос на дружбу.
* внешний ключ friend_id (ссылается на таблицу users) — идентификатор пользователя кому направлен запрос на дружбу.

**accepted_friends**  
Аккумулирует список пар идентификаторов пользователей которые являются друзьями.  
Таблица включает:

* первичный ключ accepted_friends_id - суррогатный ключ
* внешний ключ user1_id (ссылается на таблицу users) — идентификатор первого пользователя.
* внешний ключ user2_id (ссылается на таблицу users) — идентификатор второго пользователя.

## Примеры запросов  
Интерфейс FilmStorage предусматривает 2 метода получения данных  

----
### *getFilmById(Integer id)*
Получение фильма по id  

Основные данные для модели фильма:

    SELECT *
    FROM films
    WHERE film_id = {id}
    LEFT JOIN rating ON rating.rating_id = films.rating_id

Список пользователей лайкнувших фильм:

    SELECT user_id
    FROM films_likes
    WHERE film_id = {id}

Список жанров для фильма:

    SELECT genre.name
    FROM films_genres
    WHERE film_id = {id}
    LEFT JOIN genre ON genre.genre_id = films_genres.genre_id

### *getAllFilms()*
Получение всех фильмов должно пройти в 5 этапов  

    SELECT *
    FROM films
    LEFT JOIN rating ON rating.rating_id = films.rating_id

Получение списка фильмов у которых есть лайки

    SELECT DISTINCT film_id
    FROM films_likes

По каждому фильму из списка фильмов с лайками выгрузка списка пользователей которые его лайкнули:

    SELECT user_id
    FROM films_likes
    WHERE film_id = {current_id}

Получение списка фильмов у которых указан жанр(-ы)

    SELECT DISTINCT film_id
    FROM films_genres

По каждому фильму из списка фильмов с жанрами выгрузить все его жанры:

    SELECT genre.name
    FROM films_genres
    WHERE film_id = {current_id}
    LEFT JOIN genre ON genre.genre_id = films_genres.genre_id

----
Интерфейс UserStorage предусматривает 2 метода получения данных:

### *getUserById(Integer id)*
Получение пользователя по id

Основные данные для модели пользователя:

    SELECT *
    FROM users
    WHERE user_id = {id}

Получение списка подтвержденных друзей:

    SELECT user2_id
    FROM accepted_friends
    WHERE user1_id = {id}
    UNION
    SELECT user1_id
    FROM accepted_friends
    WHERE user2_id = {id}

Получение списка запрошенных, но не подтвержденных друзей

    SELECT friend_id
    FROM requested_friends
    WHERE user_id = {id}

### *getAllUsers()*
Получение всех пользователей

    SELECT *
    FROM users

Получение списка пользователей у которых есть подтвержденные друзья:
    
    SELECT DISTINCT user_id
    FROM (SELECT user2_id AS user_id
    FROM accepted_friends
    UNION
    SELECT user1_id AS user_id
    FROM accepted_friends)

Получение списка подтвержденных друзей для каждого найденного пользователя:

    SELECT user2_id
    FROM accepted_friends
    WHERE user1_id = {id}
    UNION
    SELECT user1_id
    FROM accepted_friends
    WHERE user2_id = {id}

Получение списка пользователей у которых есть запрошенные друзья:
    
    SELECT DISTINCT user_id
    FROM requested_friends

Получение списка запрошенных, но не подтвержденных друзей

    SELECT friend_id
    FROM requested_friends
    WHERE user_id = {id}

