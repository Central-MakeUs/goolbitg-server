DROP TABLE IF EXISTS spending_types;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS user_surveys;
DROP TABLE IF EXISTS user_stats;
DROP TABLE IF EXISTS challenges;
DROP TABLE IF EXISTS challenge_records;
DROP TABLE IF EXISTS challenge_stats;
DROP TABLE IF EXISTS challenge_user_stats;
DROP TABLE IF EXISTS buyornots;
DROP TABLE IF EXISTS buyornot_votes;
DROP TABLE IF EXISTS challenge_groups;
DROP TABLE IF EXISTS notices;
DROP TABLE IF EXISTS challenge_group_stats;
DROP TABLE IF EXISTS challenge_group_records;
DROP TABLE IF EXISTS challenge_group_user_stats;

CREATE TABLE spending_types (
  id BIGINT,
  title VARCHAR(100) NOT NULL UNIQUE,
  image_url VARCHAR(100) NOT NULL,
  goal INT,
  PRIMARY KEY (id)
);

CREATE TABLE users (
  id VARCHAR(50),
  apple_id VARCHAR(50) UNIQUE,
  kakao_id VARCHAR(50) UNIQUE,
  nickname VARCHAR(10) UNIQUE,
  register_date DATE NOT NULL,
  birthday DATE,
  gender VARCHAR(6) CHECK (gender IN ('MALE', 'FEMALE')),
  spending_type_id BIGINT,
  allow_push_notification BOOLEAN,
  agreement1 BOOLEAN,
  agreement2 BOOLEAN,
  agreement3 BOOLEAN,
  agreement4 BOOLEAN,
  PRIMARY KEY (id),
  FOREIGN KEY (spending_type_id) REFERENCES spending_types(id) ON DELETE SET NULL
);

CREATE TABLE user_surveys (
  user_id VARCHAR(50),
  check_1 BOOLEAN,
  check_2 BOOLEAN,
  check_3 BOOLEAN,
  check_4 BOOLEAN,
  check_5 BOOLEAN,
  check_6 BOOLEAN,
  avg_income_per_month INT,
  avg_spending_per_month INT,
  prime_use_day VARCHAR(10),
  prime_use_time TIME,
  PRIMARY KEY (user_id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE user_stats (
  user_id VARCHAR(50),
  challenge_count INT DEFAULT 0,
  post_count INT DEFAULT 0,
  achivement_guage INT DEFAULT 0,
  continue_count INT DEFAULT 0,
  PRIMARY KEY (user_id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE daily_records (
  user_id VARCHAR(50),
  date DATE NOT NULL,
  saving INT DEFAULT 0,
  status VARCHAR(7) DEFAULT 'WAIT' CHECK (status IN ('WAIT', 'SUCCESS', 'FAIL')),
  PRIMARY KEY (user_id, date),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE challenges (
  id BIGINT AUTO_INCREMENT,
  title VARCHAR(50) NOT NULL,
  image_url VARCHAR(50) NOT NULL,
  reward INT NOT NULL,
  participant_count INT DEFAULT 0,
  avg_achive_ratio FLOAT DEFAULT 0.0,
  max_achive_days INT DEFAULT 0,
  PRIMARY KEY (id)
);

CREATE TABLE challenge_records (
  challenge_id BIGINT,
  user_id VARCHAR(50),
  date DATE NOT NULL,
  status VARCHAR(7) DEFAULT 'WAIT' CHECK (status IN ('WAIT', 'SUCCESS', 'FAIL')),
  location INT,
  PRIMARY KEY (challenge_id, user_id, date),
  FOREIGN KEY (challenge_id) REFERENCES challenges(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE challenge_stats (
  challenge_id BIGINT,
  user_id VARCHAR(50),
  continue_count INT DEFAULT 0,
  current_continue_count INT DEFAULT 0,
  total_count INT DEFAULT 0,
  enroll_count INT DEFAULT 0,
  PRIMARY KEY (challenge_id, user_id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (challenge_id) REFERENCES challenges(id) ON DELETE CASCADE
);

CREATE TABLE buyornots (
  id BIGINT AUTO_INCREMENT,
  writer_id VARCHAR(50) NOT NULL,
  product_name VARCHAR(50) NOT NULL,
  product_price INT NOT NULL,
  product_image_url VARCHAR(100) NOT NULL,
  good_reason VARCHAR(50) NOT NULL,
  bad_reason VARCHAR(50) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP AS CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (writer_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE buyornot_votes (
  post_id BIGINT NOT NULL,
  voter_id VARCHAR(50) NOT NULL,
  vote VARCHAR(4) DEFAULT 'GOOD' CHECK (vote IN ('GOOD', 'BAD')),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP AS CURRENT_TIMESTAMP,
  PRIMARY KEY (post_id, voter_id),
  FOREIGN KEY (post_id) REFERENCES buyornots(id) ON DELETE CASCADE,
  FOREIGN KEY (voter_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE challenge_groups (
  id BIGINT AUTO_INCREMENT,
  owner_id VARCHAR(50) NOT NULL,
  title VARCHAR(50) NOT NULL,
  hashtags VARCHAR(100) DEFAULT '',
  image_url VARCHAR(50) NOT NULL,
  reward INT NOT NULL,
  max_size INT NOT NULL,
  people_count INT DEFAULT 1,
  participant_count INT DEFAULT 0,
  avg_achive_ratio FLOAT DEFAULT 0.0,
  max_achive_days INT DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP AS CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE notices (
  id BIGINT AUTO_INCREMENT,
  receiver_id VARCHAR(50) NOT NULL,
  message VARCHAR(100) NOT NULL,
  published_at DATETIME NOT NULL,
  type VARCHAR(9) DEFAULT 'CHALLENGE' CHECK (type IN ('CHALLENGE', 'VOTE', 'CHAT')),
  read TINYINT DEFAULT 0,
  PRIMARY KEY (id),
  FOREIGN KEY (receiver_id) REFERENCES users(id)
);

CREATE TABLE challenge_group_records (
  group_id BIGINT,
  user_id VARCHAR(50),
  date DATE NOT NULL,
  status VARCHAR(7) DEFAULT 'WAIT' CHECK (status IN ('WAIT', 'SUCCESS', 'FAIL')),
  PRIMARY KEY (group_id, user_id, date),
  FOREIGN KEY (group_id) REFERENCES challenge_groups(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE challenge_group_stats (
  group_id BIGINT,
  user_id VARCHAR(50),
  continue_count INT DEFAULT 0,
  total_count INT DEFAULT 0,
  enroll_count INT DEFAULT 0,
  PRIMARY KEY (group_id, user_id),
  FOREIGN KEY (group_id) REFERENCES challenge_groups(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

