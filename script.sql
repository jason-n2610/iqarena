ALTER TABLE rooms
ADD COLUMN status tinyint unsigned not null;
ALTER TABLE users
CHANGE COLUMN score_level score_level float not null;