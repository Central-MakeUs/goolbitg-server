INSERT INTO user_surveys (
    user_id, check_1, check_2, check_3, check_4, check_5, check_6,
    avg_income_per_month, avg_saving_per_month,
    prime_use_day, prime_use_time
) VALUES ( 'id0005', 0, 0, 0, 0, 0, 0, 3600000, 30000, 'FRIDAY', '20:00:00' ),
        ( 'id0006', 0, 0, 0, 0, 0, 0, 3600000, 30000, 'FRIDAY', '20:00:00' ),
        ( 'id0007', 0, 0, 0, 0, 0, 0, 3600000, 30000, 'FRIDAY', '20:00:00' ),
        ( 'id0008', 0, 0, 0, 0, 0, 0, 3600000, 30000, 'FRIDAY', '20:00:00' ),
        ( 'id0009', 0, 0, 0, 0, 0, 0, 3600000, 30000, 'FRIDAY', '20:00:00' );

INSERT INTO user_stats (
    user_id, challenge_count, post_count, achievement_guage, continue_count
) VALUES ( 'id0005', 3, 0, 0, 1 ),
        ( 'id0006', 3, 0, 0, 1 ),
        ( 'id0007', 3, 0, 0, 1 ),
        ( 'id0008', 3, 0, 0, 1 ),
        ( 'id0009', 3, 0, 0, 1 );
