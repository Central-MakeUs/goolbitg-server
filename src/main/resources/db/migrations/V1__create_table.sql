CREATE TABLE `challenges` (
  `id` BIGINT AUTO_INCREMENT,
  `title` VARCHAR(50) NOT NULL,
  `image_url_large` VARCHAR(100) NOT NULL,
  `image_url_small` VARCHAR(100) NOT NULL,
  `reward` INT NOT NULL,
  `participant_count` INT DEFAULT 0,
  `max_achieve_days` INT DEFAULT 0,
  `total_records` INT DEFAULT 0,
  `achieved_records` INT DEFAULT 0,
  PRIMARY KEY (`id`)
);

CREATE TABLE `spending_types` (
  `id` VARCHAR(10),
  `title` VARCHAR(50) NOT NULL UNIQUE,
  `image_url` VARCHAR(100) NOT NULL,
  `goal` INT,
  PRIMARY KEY (`id`)
);

CREATE TABLE `users` (
  `id` VARCHAR(50),
  `apple_id` VARCHAR(50) UNIQUE,
  `kakao_id` VARCHAR(50) UNIQUE,
  `nickname` VARCHAR(10) UNIQUE,
  `register_date` DATE NOT NULL,
  `birthday` DATE,
  `gender` ENUM('MALE', 'FEMALE'),
  `spending_type_id` VARCHAR(10),
  `allow_push_notification` BOOLEAN,
  `agreement1` BOOLEAN,
  `agreement2` BOOLEAN,
  `agreement3` BOOLEAN,
  `agreement4` BOOLEAN,
  PRIMARY KEY (`id`)
);

CREATE TABLE `user_stats` (
  `user_id` VARCHAR(50),
  `challenge_count` INT DEFAULT 0,
  `post_count` INT DEFAULT 0,
  `achievement_guage` INT DEFAULT 0,
  `continue_count` INT DEFAULT 0,
  PRIMARY KEY (`user_id`)
);

CREATE TABLE `daily_records` (
  `user_id` VARCHAR(50),
  `date` DATE NOT NULL,
  `saving` INT DEFAULT 0,
  `total_challenges` INT DEFAULT 0,
  `achieved_challenges` INT DEFAULT 0,
  PRIMARY KEY (`user_id`, `date`)
);

CREATE TABLE `challenge_records` (
  `challenge_id` BIGINT,
  `user_id` VARCHAR(50),
  `date` DATE NOT NULL,
  `status` ENUM('WAIT', 'SUCCESS', 'FAIL') DEFAULT 'WAIT',
  `location` INT,
  PRIMARY KEY (`challenge_id`, `user_id`, `date`)
);

CREATE TABLE `unregister_histories` (
  `user_id` VARCHAR(50),
  `unregister_date` DATE NOT NULL,
  `reason` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`user_id`)
);

CREATE TABLE `challenge_stats` (
  `challenge_id` BIGINT,
  `user_id` VARCHAR(50),
  `continue_count` INT DEFAULT 0,
  `current_continue_count` INT DEFAULT 0,
  `total_count` INT DEFAULT 0,
  `enroll_count` INT DEFAULT 0,
  PRIMARY KEY (`challenge_id`, `user_id`)
);

CREATE TABLE `notices` (
  `id` BIGINT AUTO_INCREMENT,
  `receiver_id` VARCHAR(50) NOT NULL,
  `message` VARCHAR(100) NOT NULL,
  `published_at` DATETIME NOT NULL,
  `type` ENUM('CHALLENGE', 'VOTE', 'CHAT') DEFAULT 'CHALLENGE',
  `read` BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (`id`)
);

CREATE TABLE `challenge_groups` (
  `id` BIGINT AUTO_INCREMENT,
  `owner_id` VARCHAR(50) NOT NULL,
  `title` VARCHAR(50) NOT NULL,
  `hashtags` VARCHAR(100) DEFAULT "",
  `max_size` INT NOT NULL,
  `reward` INT NOT NULL,
  `people_count` INT DEFAULT 1,
  `participant_count` INT DEFAULT 0,
  `is_hidden` BOOLEAN NOT NULL,
  `password` VARCHAR(4) NOT NULL,
  `avg_achieve_ratio` FLOAT(5, 2) DEFAULT 0.0,
  `max_achieve_days` INT DEFAULT 0,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

CREATE TABLE `challenge_group_records` (
  `group_id` BIGINT,
  `user_id` VARCHAR(50),
  `date` DATE NOT NULL,
  `status` ENUM('WAIT', 'SUCCESS', 'FAIL') DEFAULT 'WAIT',
  PRIMARY KEY (`group_id`, `user_id`, `date`)
);

CREATE TABLE `challenge_group_stats` (
  `group_id` BIGINT,
  `user_id` VARCHAR(50),
  `continue_count` INT DEFAULT 0,
  `total_count` INT DEFAULT 0,
  `enroll_count` INT DEFAULT 0,
  PRIMARY KEY (`group_id`, `user_id`)
);

CREATE TABLE `buyornots` (
  `id` BIGINT AUTO_INCREMENT,
  `writer_id` VARCHAR(50) NOT NULL,
  `product_name` VARCHAR(50) NOT NULL,
  `product_price` VARCHAR(50) NOT NULL,
  `product_image_url` VARCHAR(100) NOT NULL,
  `good_reason` VARCHAR(50) NOT NULL,
  `bad_reason` VARCHAR(50) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

CREATE TABLE `buyornot_votes` (
  `post_id` BIGINT NOT NULL,
  `voter_id` VARCHAR(50) NOT NULL,
  `vote` ENUM('GOOD', 'BAD') DEFAULT 'good',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`post_id`, `voter_id`)
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
  `prime_use_day` VARCHAR(10),
  `prime_use_time` TIME,
  PRIMARY KEY (`user_id`)
);

