CREATE TABLE `sequences` (
  `name` VARCHAR(50) NOT NULL,
  `curval` BIGINT UNSIGNED DEFAULT 0,
  PRIMARY KEY (`name`)
);

INSERT INTO `sequences` ( `name` )
VALUES ( 'id_seq' );
