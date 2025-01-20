USE goolbitg;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE spending_types;
TRUNCATE TABLE users;
TRUNCATE TABLE user_surveys;
TRUNCATE TABLE user_stats;
TRUNCATE TABLE challenges;
TRUNCATE TABLE challenge_records;
TRUNCATE TABLE challenge_stats;
TRUNCATE TABLE challenge_user_stats;
TRUNCATE TABLE buyornots;
TRUNCATE TABLE buyornot_votes;
TRUNCATE TABLE challenge_groups;
TRUNCATE TABLE notices;
TRUNCATE TABLE challenge_group_stats;
TRUNCATE TABLE challenge_group_records;
TRUNCATE TABLE challenge_group_user_stats;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO spending_types (
    title, image_url
) VALUES ( "자린고비 굴비", "exmaple_url" );

INSERT INTO users (
    id, apple_id, kakao_id, nickname,
    register_date, birthday, gender, spending_type_id
) VALUES ( "id0001", "ap0001", "ka0001", "굴비왕",
    "2025-01-15", "1999-03-01", "male", 1 );

INSERT INTO user_surveys (
    user_id, check_1, check_2, check_3, check_4, check_5, check_6,
    avg_income_per_month, avg_spending_per_month, spending_habit_score,
    prime_use_day, prime_use_time
) VALUES ( "id0001", FALSE, TRUE, TRUE, FALSE, TRUE, TRUE,
    3600000, 2000000, 60, "friday", "20:00:00" );

INSERT INTO user_stats (
    user_id, challenge_count, post_count, achivement_guage
) VALUES ( "id0001", 14, 4, 37.2 );

INSERT INTO challenges (
    title, image_url
) VALUES ( "커피 안마시기", "exmaple_url" );

INSERT INTO challenge_records (
    challenge_id, user_id, date, status
) VALUES ( 1, "id0001", "2025-01-15", "success" );

INSERT INTO challenge_stats (
    challenge_id, participant_count, avg_achive_ratio, max_achive_days
) VALUES ( 1, 23, 50, 63 );

INSERT INTO challenge_user_stats (
    challenge_id, user_id, continue_count, total_count, enroll_count
) VALUES ( 1, "id0001", 12, 15, 6 );

INSERT INTO buyornots (
    writer_id, product_name, product_price, product_image_url,
    good_reason, bad_reason
) VALUES ( "id0001", "Tecket 후드티", 97000, "exmaple_url",
    "후드티 안산지 벌써 2년 다됨", "집에 후드티만 10장 있긴함" );

INSERT INTO buyornot_votes (
    post_id, voter_id, vote
) VALUES ( 1, "id0001", "good" );

INSERT INTO challenge_groups (
    owner_id, title, hashtags, image_url, max_size, people_count
) VALUES ( "id0001", "배달음식 안시켜먹기",  "배달,음식", "exmaple_url", 6, 1 );

INSERT INTO notices (
    receiver_id, message, published_at, type, `read`
) VALUES ( "id0001", "오늘 아직 챌린지를 달성하지 못했어요.",
    "2025-01-07 14:26:32", "challenge", FALSE );

INSERT INTO challenge_group_stats (
    group_id, participant_count, avg_achive_ratio, max_achive_days
) VALUES ( 1, 12, 23.50, 9 );

INSERT INTO challenge_group_records (
    group_id, user_id, date, status
) VALUES ( 1, "id0001", "2025-01-14", "fail" );
