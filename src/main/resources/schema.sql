CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(256) NOT NULL UNIQUE,
    login VARCHAR(256) NOT NULL,
    name VARCHAR(256),
    birthday DATE
);

CREATE TABLE IF NOT EXISTS requested_friends (
    requested_friends_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT REFERENCES users(user_id),
    friend_id BIGINT REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS accepted_friends (
    accepted_friends_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user1_id BIGINT REFERENCES users(user_id),
    user2_id BIGINT REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS rating (
    rating_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(256) NOT NULL
);

CREATE TABLE IF NOT EXISTS genres (
    genres_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(256) NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    film_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(256) NOT NULL,
    description VARCHAR(200),
    release_date DATE,
    duration INT,
    rating_id BIGINT REFERENCES rating(rating_id)
);

CREATE TABLE IF NOT EXISTS films_genres (
    films_genres BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    film_id BIGINT REFERENCES films(film_id),
    genres_id BIGINT REFERENCES genres(genres_id)
);

CREATE TABLE IF NOT EXISTS films_likes (
    films_likes BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    film_id BIGINT REFERENCES films(film_id),
    user_id BIGINT REFERENCES users(user_id)
);
