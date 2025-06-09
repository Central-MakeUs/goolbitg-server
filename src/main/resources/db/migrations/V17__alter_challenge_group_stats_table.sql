ALTER TABLE challenge_group_stats
DROP COLUMN continue_count,
DROP COLUMN enroll_count,
ADD COLUMN saving INTEGER;
