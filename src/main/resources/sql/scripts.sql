CREATE TABLE `authorities` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `name` varchar(50) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user_table` (`id`)
);

INSERT INTO `authorities` (`user_id`, `name`)
VALUES (1, 'VIEWACCOUNT');

INSERT INTO `authorities` (`user_id`, `name`)
VALUES (1, 'VIEWCARDS');

INSERT INTO `authorities` (`user_id`, `name`)
VALUES (1, 'VIEWLOANS');

INSERT INTO `authorities` (`user_id`, `name`)
VALUES (1, 'VIEWBALANCE');
