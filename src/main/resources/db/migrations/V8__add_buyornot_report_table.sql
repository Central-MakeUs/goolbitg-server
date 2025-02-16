CREATE TABLE `buyornot_reports` (
  `post_id` BIGINT NOT NULL,
  `reporter_id` VARCHAR(50) NOT NULL,
  `reason` VARCHAR(50) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`post_id`, `reporter_id`)
);

