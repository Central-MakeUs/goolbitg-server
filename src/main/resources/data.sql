INSERT INTO spending_types (
    id, title, image_url
) VALUES ( 'st01', '자린고비 굴비', 'exmaple_url' );

-- user for registration test
INSERT INTO users ( id, register_date) 
VALUES ( 'id0002', '2025-01-15' );

INSERT INTO user_surveys ( user_id ) 
VALUES ( 'id0002' );

INSERT INTO user_stats ( user_id )
VALUES ( 'id0002' );

INSERT INTO users (
    id, apple_id, kakao_id, nickname,
    register_date, birthday, gender, spending_type_id
) VALUES ( 'id0001', 'ap0001', 'ka0001', '굴비왕',
    '2025-01-15', '1999-03-01', 'MALE', 'st01' ),
        ( 'id0003', 'ap0003', 'ka0003', '굴비왕비',
    '2025-01-21', '1999-04-28', 'FEMALE', 'st01' );

INSERT INTO user_surveys (
    user_id, check_1, check_2, check_3, check_4, check_5, check_6,
    avg_income_per_month, avg_spending_per_month, spending_habit_score,
    prime_use_day, prime_use_time
) VALUES ( 'id0001', 0, 1, 1, 0, 1, 1,
    3600000, 2000000, 60, 'FRIDAY', '20:00:00' );

INSERT INTO user_stats (
    user_id, challenge_count, post_count, achivement_guage
) VALUES ( 'id0001', 14, 4, 37.2 );

INSERT INTO challenges (
    title, image_url, participant_count, avg_achive_ratio, max_achive_days
) VALUES ( '커피 안마시기', 'exmaple_url', 1, 100, 3 ),
        ( '대중교통 이용하기', 'exmaple_url', 2, 0.56, 2 ),
        ( '외식하지 않기', 'exmaple_url', 0, 0, 0 ),
        ( '집밥 먹기', 'exmaple_url', 0, 0, 0 ),
        ( '주식 차트 확인하지 않기', 'exmaple_url', 0, 0, 0 ),
        ( '아침 일찍 일어나기', 'exmaple_url', 0, 0, 0 );

INSERT INTO challenge_records (
    challenge_id, user_id, date, status, location
) VALUES ( 2, 'id0001', '2025-01-15', 'SUCCESS', 0 ),
        ( 2, 'id0001', '2025-01-16', 'SUCCESS', 1 ),
        ( 2, 'id0001', '2025-01-17', 'FAIL', 2 ),
        ( 2, 'id0001', '2025-01-21', 'SUCCESS', 0 ),
        ( 2, 'id0001', '2025-01-22', 'SUCCESS', 1 ),
        ( 2, 'id0001', '2025-01-23', 'WAIT', 2 ),
        ( 1, 'id0001', '2025-01-17', 'SUCCESS', 0 ),
        ( 1, 'id0001', '2025-01-18', 'SUCCESS', 1 ),
        ( 1, 'id0001', '2025-01-19', 'SUCCESS', 2 ),
        ( 2, 'id0003', '2025-01-22', 'SUCCESS', 0 ),
        ( 2, 'id0003', '2025-01-23', 'WAIT', 1 ),
        ( 2, 'id0003', '2025-01-24', 'WAIT', 2 );

INSERT INTO challenge_stats (
    challenge_id, user_id, continue_count, current_continue_count, total_count, enroll_count
) VALUES ( 1, 'id0001', 3, 3, 3, 1 ),
        ( 2, 'id0001', 2, 2, 4, 2 ),
        ( 2, 'id0003', 1, 1, 1, 1 );

INSERT INTO buyornots (
    writer_id, product_name, product_price, product_image_url,
    good_reason, bad_reason
) VALUES ( 'id0001', 'Tecket 후드티', 97000, 'exmaple_url',
    '후드티 안산지 벌써 2년 다됨', '집에 후드티만 10장 있긴함' );

INSERT INTO buyornot_votes (
    post_id, voter_id, vote
) VALUES ( 1, 'id0001', 'GOOD' );

INSERT INTO challenge_groups (
    owner_id, title, hashtags, image_url, max_size, people_count, participant_count, avg_achive_ratio, max_achive_days
) VALUES ( 'id0001', '배달음식 안시켜먹기',  '배달,음식', 'exmaple_url', 6, 1, 23, 50, 63 );

INSERT INTO notices (
    receiver_id, message, published_at, type, read
) VALUES ( 'id0001', '오늘 아직 챌린지를 달성하지 못했어요.',
    '2025-01-07 14:26:32', 'CHALLENGE', 0 );

INSERT INTO challenge_group_records (
    group_id, user_id, date, status
) VALUES ( 1, 'id0001', '2025-01-14', 'FAIL' );

