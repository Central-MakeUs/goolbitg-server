CREATE TABLE `registration_tokens` (
  `registration_token` VARCHAR(100) NOT NULL,
  `user_id` VARCHAR(50) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`registration_token`)
);

