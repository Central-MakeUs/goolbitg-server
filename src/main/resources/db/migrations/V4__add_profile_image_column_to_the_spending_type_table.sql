ALTER TABLE `spending_types` ADD COLUMN `profile_url` VARCHAR(100) NOT NULL;

UPDATE `spending_types`
SET `profile_url` = 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/profile/01.png'
WHERE `id` = 1;
UPDATE `spending_types`
SET `profile_url` = 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/profile/02.png'
WHERE `id` = 2;
UPDATE `spending_types`
SET `profile_url` = 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/profile/03.png'
WHERE `id` = 3;
UPDATE `spending_types`
SET `profile_url` = 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/profile/04.png'
WHERE `id` = 4;
UPDATE `spending_types`
SET `profile_url` = 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/profile/05.png'
WHERE `id` = 5;
