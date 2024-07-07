CREATE TABLE IF NOT EXISTS users (
    user_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(256) NOT NULL UNIQUE,
    login VARCHAR(256) NOT NULL,
    name VARCHAR(256),
    birthday DATE
);

CREATE TABLE IF NOT EXISTS requested_friends (
    requested_friends_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INT REFERENCES users(user_id) ON DELETE CASCADE,
    friend_id INT REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS approved_friends (
    approved_friends_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user1_id INT REFERENCES users(user_id) ON DELETE CASCADE,
    user2_id INT REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS rating (
    rating_id INT PRIMARY KEY,
    name VARCHAR(256) NOT NULL
);

CREATE TABLE IF NOT EXISTS genres (
    genres_id INT PRIMARY KEY,
    name VARCHAR(256) NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    film_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(256) NOT NULL,
    description VARCHAR(200),
    release_date DATE,
    duration INT,
    rating_id INT REFERENCES rating(rating_id) ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS films_genres (
    films_genres INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    film_id INT REFERENCES films(film_id) ON DELETE CASCADE,
    genre_id INT REFERENCES genres(genres_id) ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS films_likes (
    films_likes INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    film_id INT REFERENCES films(film_id) ON DELETE CASCADE,
    user_id INT REFERENCES users(user_id) ON DELETE CASCADE
);
