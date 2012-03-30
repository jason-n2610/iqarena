ALTER TABLE rooms
DROP COLUMN min_member;
ALTER TABLE rooms
DROP COLUMN status;
ALTER TABLE rooms
DROP COLUMN number_of_member;
ALTER TABLE rooms
ADD COLUMN time_per_question tinyint unsigned;
ALTER TABLE rooms
CHANGE COLUMN win_score bet_score float;
ALTER TABLE users
ADD COLUMN status tinyint not null;

