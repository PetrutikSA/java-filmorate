INSERT INTO users (email, login, name, birthday)
VALUES ('Adelbert_Lebsack40@yahoo.com', 'kbxjCjdIKV', 'Dorothy Hyatt', '1976-03-21'),
    ('Bernadine.Ortiz@hotmail.com', '7tFjQa7wpD', 'Doris McDermott DDS', '1970-12-19'),
    ('Valerie_Runolfsdottir89@yahoo.com', '4TMlMXaTmr', 'Miss Samuel Purdy', '1987-04-14'),
    ('Jena_Cummerata@gmail.com', '9PN4mGQNjV', 'Doris McDermott DDS', '1972-06-21');

INSERT INTO accepted_friends (user1_id, user2_id)
VALUES (1, 2);

INSERT INTO requested_friends (user_id, friend_id)
VALUES (1, 3),
       (2, 3),
       (2, 4);