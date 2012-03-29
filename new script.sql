ALTER TABLE question_types
DROP COLUMN question_field_id;
ALTER TABLE question_types
ADD COLUMN question_type_name NVARCHAR(50);
ALTER TABLE question_types
CHANGE COLUMN question_type question_type_value tinyint unsigned;
ALTER TABLE questions
ADD COLUMN question_field_id tinyint unsigned not null;
ALTER TABLE room_members
ADD COLUMN last_answer tinyint unsigned;
