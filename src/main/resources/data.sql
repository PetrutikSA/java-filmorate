DELETE FROM rating;
DELETE FROM genres;

INSERT INTO rating (rating_id, name)
VALUES (1, 'G'), (2, 'PG'), (3, 'PG-13'), (4, 'R'), (5, 'NC-17');

INSERT INTO genres (genre_id, name)
VALUES (1, 'Комедия'), (2, 'Драма'), (3, 'Мультфильм'), (4, 'Триллер'), (5, 'Документальный'), (6, 'Боевик');
--
--INSERT INTO users (email, login, name, birthday)
--VALUES ('Adelbert_Lebsack40@yahoo.com', 'kbxjCjdIKV', 'Dorothy Hyatt', '1976-03-21'),
--    ('Bernadine.Ortiz@hotmail.com', '7tFjQa7wpD', 'Doris McDermott DDS', '1970-12-19'),
--    ('Valerie_Runolfsdottir89@yahoo.com', '4TMlMXaTmr', 'Miss Samuel Purdy', '1987-04-14'),
--    ('Jena_Cummerata@gmail.com', '9PN4mGQNjV', 'Doris McDermott DDS', '1972-06-21');
--
--INSERT INTO approved_friends (user1_id, user2_id)
--VALUES (1, 2);
--
--INSERT INTO requested_friends (user_id, friend_id)
--VALUES (1, 3),
--       (2, 3),
--       (2, 4);
--
--INSERT INTO films (name, description, release_date, duration, rating_id)
--VALUES ('WVpj46QoOT9P8Nv', 'cqu3io2rnhkFcQLazR3Vc7XKKiVQQbYRRMC6sjFPh0ax1qo3kX', '2003-05-11', '79', 1),
--    ('hiqhgiegjr', 'efwiuhfoiuhwaofihaifhf', '2021-05-16', '120', 3),
--    ('efweiufhihfeiuhfi', 'ifuewifwiuhfiuhewfihwief', '1980-09-11', '180', 3),
--    ('efwefwefwoiefjow', 'wfefgwifyuweufwiuhfieuhw', '2020-09-09', '20', 5);
--
--INSERT INTO films_likes (film_id, user_id)
--VALUES (1, 3), (1, 1), (2, 1);
--
--INSERT INTO films_genres (film_id, genre_id)
--VALUES (1, 3), (2, 1), (3, 2), (3, 3), (4, 1);