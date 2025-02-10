INSERT INTO spending_types (
    id, title, image_url, goal
) VALUES ( 1, '거지굴비', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/spending_type/01.png', 20000 ),
        ( 2, '우당탕 굴비', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/spending_type/02.png', 50000 ),
        ( 3, '룰루굴비', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/spending_type/03.png', 100000 ),
        ( 4, '배부른굴비', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/spending_type/04.png', 200000 ),
        ( 5, '이로운 굴비', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/spending_type/05.png', null );

INSERT INTO users (
    id, apple_id, kakao_id, nickname,
    register_date, birthday, gender, spending_type_id,
    allow_push_notification, agreement1, agreement2, agreement3, agreement4
) VALUES ( 'id0001', 'ap0001', 'ka0001', '굴비왕',
    '2025-01-15', '1999-03-01', 'MALE', 5, 1, 1, 1, 1, 1),
        ( 'id0005', 'ap0005', 'ka0005', '굴비대신',
    '2025-01-21', '1999-04-28', 'FEMALE', 5, 1, 1, 1, 1, 1),
        ( 'id0006', 'ap0006', 'ka0006', '굴비신하',
    '2025-01-21', '1999-04-28', 'FEMALE', 4, 1, 1, 1, 1, 1),
        ( 'id0007', 'ap0007', 'ka0007', '굴비상인',
    '2025-01-21', '1999-04-28', 'FEMALE', 3, 1, 1, 1, 1, 1),
        ( 'id0008', 'ap0008', 'ka0008', '굴비농민',
    '2025-01-21', '1999-04-28', 'FEMALE', 2, 1, 1, 1, 1, 1),
        ( 'id0009', 'ap0009', 'ka0009', '굴비노예',
    '2025-01-21', '1999-04-28', 'FEMALE', 1, 1, 1, 1, 1, 1);

INSERT INTO user_surveys (
    user_id, check_1, check_2, check_3, check_4, check_5, check_6,
    avg_income_per_month, avg_spending_per_month,
    prime_use_day, prime_use_time
) VALUES ( 'id0001', 0, 0, 0, 0, 0, 0,
    3600000, 30000, 'FRIDAY', '20:00:00' );

INSERT INTO user_stats (
    user_id, challenge_count, post_count, achievement_guage, continue_count
) VALUES ( 'id0001', 2, 1, 2000, 2 );

INSERT INTO challenges (
    id, title, image_url_large, image_url_small, reward, participant_count, max_achieve_days, total_records, achieved_records
) VALUES ( 1, '야식 안시켜먹기', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/challenge_large/challenge01.png', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/challenge_small/challenge01.png', 15000, 1, 3, 3, 3 ),
        ( 2, '택시 안타고 대중교통 이용하기', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/challenge_large/challenge02.png', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/challenge_small/challenge02.png', 7000, 0, 0, 0, 0 ),
        ( 3, '아침마다 커피 안마시기', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/challenge_large/challenge03.png', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/challenge_small/challenge03.png', 2000, 0, 0, 0, 0 ),
        ( 4, '식비 만원 이하로 소비하기', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/challenge_large/challenge04.png', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/challenge_small/challenge04.png', 10000, 0, 0, 0, 0 ),
        ( 5, '배달어플 사용 안하고 요리해서 먹기', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/challenge_large/challenge05.png', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/challenge_small/challenge05.png', 15000, 0, 0, 0, 0 ),
        ( 6, '길거리 음식 안사먹기', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/challenge_large/challenge06.png', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/challenge_small/challenge06.png', 3000, 0, 0, 0, 0 ),
        ( 7, '온라인 쇼핑 줄이기', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/challenge_large/challenge07.png', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/challenge_small/challenge07.png', 30000, 0, 0, 0, 0 ),
        ( 8, '편의점에서 소액 소비 안하기', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/challenge_large/challenge08.png', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/challenge_small/challenge08.png', 5000, 0, 0, 0, 0 ),
        ( 9, '가계부 작성하기', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/challenge_large/challenge09.png', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/challenge_small/challenge09.png', 10000, 0, 0, 0, 0 ),
        ( 10, '무지출하기', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/challenge_large/challenge10.png', 'https://goolbitg-bucket.s3.ap-northeast-2.amazonaws.com/challenge_small/challenge10.png', 10000, 0, 0, 0, 0 );

INSERT INTO challenge_stats (
    challenge_id, user_id, continue_count, current_continue_count, total_count, enroll_count
) VALUES ( 1, 'id0005', 0, 0, 0, 0 ),
        ( 2, 'id0005', 0, 0, 0, 0 ),
        ( 3, 'id0005', 0, 0, 0, 0 ),
        ( 4, 'id0005', 0, 0, 0, 0 ),
        ( 5, 'id0005', 0, 0, 0, 10 ),
        ( 6, 'id0005', 0, 0, 0, 0 ),
        ( 7, 'id0005', 0, 0, 0, 9 ),
        ( 8, 'id0005', 0, 0, 0, 0 ),
        ( 9, 'id0005', 0, 0, 0, 0 ),
        ( 10, 'id0005', 0, 0, 0, 8 ),
        ( 1, 'id0006', 0, 0, 0, 9 ),
        ( 2, 'id0006', 0, 0, 0, 10 ),
        ( 3, 'id0006', 0, 0, 0, 0 ),
        ( 4, 'id0006', 0, 0, 0, 0 ),
        ( 5, 'id0006', 0, 0, 0, 0 ),
        ( 6, 'id0006', 0, 0, 0, 0 ),
        ( 7, 'id0006', 0, 0, 0, 0 ),
        ( 8, 'id0006', 0, 0, 0, 0 ),
        ( 9, 'id0006', 0, 0, 0, 0 ),
        ( 10, 'id0006', 0, 0, 0, 8 ),
        ( 1, 'id0007', 0, 0, 0, 0 ),
        ( 2, 'id0007', 0, 0, 0, 0 ),
        ( 3, 'id0007', 0, 0, 0, 0 ),
        ( 4, 'id0007', 0, 0, 0, 0 ),
        ( 5, 'id0007', 0, 0, 0, 10 ),
        ( 6, 'id0007', 0, 0, 0, 0 ),
        ( 7, 'id0007', 0, 0, 0, 9 ),
        ( 8, 'id0007', 0, 0, 0, 0 ),
        ( 9, 'id0007', 0, 0, 0, 8 ),
        ( 10, 'id0007', 0, 0, 0, 0 ),
        ( 1, 'id0008', 0, 0, 0, 9 ),
        ( 2, 'id0008', 0, 0, 0, 0 ),
        ( 3, 'id0008', 0, 0, 0, 10 ),
        ( 4, 'id0008', 0, 0, 0, 8 ),
        ( 5, 'id0008', 0, 0, 0, 0 ),
        ( 6, 'id0008', 0, 0, 0, 0 ),
        ( 7, 'id0008', 0, 0, 0, 0 ),
        ( 8, 'id0008', 0, 0, 0, 0 ),
        ( 9, 'id0008', 0, 0, 0, 0 ),
        ( 10, 'id0008', 0, 0, 0, 0 ),
        ( 1, 'id0009', 0, 0, 0, 0 ),
        ( 2, 'id0009', 0, 0, 0, 8 ),
        ( 3, 'id0009', 0, 0, 0, 0 ),
        ( 4, 'id0009', 0, 0, 0, 0 ),
        ( 5, 'id0009', 0, 0, 0, 0 ),
        ( 6, 'id0009', 0, 0, 0, 9 ),
        ( 7, 'id0009', 0, 0, 0, 0 ),
        ( 8, 'id0009', 0, 0, 0, 10 ),
        ( 9, 'id0009', 0, 0, 0, 0 ),
        ( 10, 'id0009', 0, 0, 0, 0 );

