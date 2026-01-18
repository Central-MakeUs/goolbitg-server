ALTER TABLE challenges
ADD COLUMN category VARCHAR(20);

ALTER TABLE challenge_groups
ADD COLUMN category VARCHAR(20);

update challenges
set category = 'FOOD'
where id in (1, 3, 4, 5, 6);

update challenges
set category = 'TRAFFIC'
where id in (2);

update challenges
set category = 'SHOPING'
where id in (7);

update challenges
set category = 'LIVING'
where id in (8);

update challenges
set category = 'ETC'
where id in (9, 10);
