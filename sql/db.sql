DROP DATABASE IF EXiSTS goolbitg;
CREATE DATABASE goolbitg;

USE goolbitg;

CREATE TABLE `spending_types` (
  `id` INT AUTO_INCREMENT,
  `title` VARCHAR(50) NOT NULL UNIQUE,
  `image_url` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `users` (
  `id` VARCHAR(50),
  `apple_id` VARCHAR(50) UNIQUE,
  `kakao_id` VARCHAR(50) UNIQUE,
  `nickname` VARCHAR(10) UNIQUE,
  `register_date` DATE NOT NULL,
  `birthday` DATE,
  `gender` ENUM('male', 'female'),
  `spending_type_id` INT,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`spending_type_id`) REFERENCES `spending_types`(`id`)
);

CREATE TABLE `user_surveys` (
  `user_id` VARCHAR(50),
  `check_1` BOOLEAN,
  `check_2` BOOLEAN,
  `check_3` BOOLEAN,
  `check_4` BOOLEAN,
  `check_5` BOOLEAN,
  `check_6` BOOLEAN,
  `avg_income_per_month` INT,
  `avg_spending_per_month` INT,
  `spending_habit_score` INT,
  `prime_use_day` VARCHAR(10),
  `prime_use_time` TIME,
  PRIMARY KEY (`user_id`),
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
);

CREATE TABLE `user_stats` (
  `user_id` VARCHAR(50),
  `challenge_count` INT DEFAULT 0,
  `post_count` INT DEFAULT 0,
  `achivement_guage` FLOAT(5, 2) DEFAULT 0.00,
  PRIMARY KEY (`user_id`),
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
);

CREATE TABLE `challenges` (
  `id` INT AUTO_INCREMENT,
  `title` VARCHAR(50) NOT NULL,
  `image_url` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `challenge_records` (
  `challenge_id` INT,
  `user_id` VARCHAR(50),
  `date` DATE NOT NULL,
  `status` ENUM('wait', 'success', 'fail') DEFAULT 'wait',
  PRIMARY KEY (`challenge_id`, `user_id`),
  FOREIGN KEY (`challenge_id`) REFERENCES `challenges`(`id`),
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
);

CREATE TABLE `challenge_stats` (
  `challenge_id` INT,
  `participant_count` INT DEFAULT 0,
  `avg_achive_ratio` FLOAT(5, 2) DEFAULT 0.00,
  `max_achive_days` INT DEFAULT 0,
  PRIMARY KEY (`challenge_id`),
  FOREIGN KEY (`challenge_id`) REFERENCES `challenges`(`id`)
);

CREATE TABLE `challenge_user_stats` (
  `challenge_id` INT,
  `user_id` VARCHAR(50),
  `continue_count` INT DEFAULT 0,
  `total_count` INT DEFAULT 0,
  `enroll_count` INT DEFAULT 0,
  PRIMARY KEY (`challenge_id`, `user_id`),
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`),
  FOREIGN KEY (`challenge_id`) REFERENCES `challenges`(`id`)
);

CREATE TABLE `buyornots` (
  `id` INT AUTO_INCREMENT,
  `writer_id` VARCHAR(50) NOT NULL,
  `product_name` VARCHAR(50) NOT NULL,
  `product_price` VARCHAR(50) NOT NULL,
  `product_image_url` VARCHAR(100) NOT NULL,
  `good_reason` VARCHAR(50) NOT NULL,
  `bad_reason` VARCHAR(50) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`writer_id`) REFERENCES `users`(`id`)
);

CREATE TABLE `buyornot_votes` (
  `id` INT AUTO_INCREMENT,
  `post_id` INT NOT NULL,
  `voter_id` VARCHAR(50) NOT NULL,
  `vote` ENUM('good', 'bad') DEFAULT 'good',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`post_id`) REFERENCES `buyornots`(`id`),
  FOREIGN KEY (`voter_id`) REFERENCES `users`(`id`)
);

CREATE TABLE `challenge_groups` (
  `id` INT AUTO_INCREMENT,
  `owner_id` VARCHAR(50) NOT NULL,
  `title` VARCHAR(50) NOT NULL,
  `hashtags` VARCHAR(100) DEFAULT "",
  `image_url` VARCHAR(50) NOT NULL,
  `max_size` INT NOT NULL,
  `people_count` INT DEFAULT 1,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`owner_id`) REFERENCES `users`(`id`)
);

CREATE TABLE `notices` (
  `id` INT AUTO_INCREMENT,
  `receiver_id` VARCHAR(50) NOT NULL,
  `message` VARCHAR(100) NOT NULL,
  `published_at` DATETIME NOT NULL,
  `type` ENUM('challenge', 'vote', 'chat') DEFAULT 'challenge',
  `read` BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`receiver_id`) REFERENCES `users`(`id`)
);

CREATE TABLE `challenge_group_stats` (
  `group_id` INT,
  `participant_count` INT DEFAULT 0,
  `avg_achive_ratio` FLOAT(5, 2) DEFAULT 0.00,
  `max_achive_days` INT DEFAULT 0,
  PRIMARY KEY (`group_id`),
  FOREIGN KEY (`group_id`) REFERENCES `challenge_groups`(`id`)
);

CREATE TABLE `challenge_group_records` (
  `group_id` INT,
  `user_id` VARCHAR(50),
  `date` DATE NOT NULL,
  `status` ENUM('wait', 'success', 'fail') DEFAULT 'wait',
  PRIMARY KEY (`group_id`, `user_id`),
  FOREIGN KEY (`group_id`) REFERENCES `challenge_groups`(`id`),
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
);

CREATE TABLE `challenge_group_user_stats` (
  `group_id` INT,
  `user_id` VARCHAR(50),
  `continue_count` INT DEFAULT 0,
  `total_count` INT DEFAULT 0,
  `enroll_count` INT DEFAULT 0,
  PRIMARY KEY (`group_id`, `user_id`),
  FOREIGN KEY (`group_id`) REFERENCES `challenge_groups`(`id`),
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
);


