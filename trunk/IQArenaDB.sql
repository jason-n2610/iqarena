SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

CREATE DATABASE if not exists iqarena
DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE iqarena;

-- check if any table exists
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS room_members;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS question_types;
DROP TABLE IF EXISTS question_fields;
DROP TABLE IF EXISTS room_questions;
DROP TABLE IF EXISTS markets;
DROP TABLE IF EXISTS user_items;
DROP TABLE IF EXISTS pictures;
DROP TABLE IF EXISTS grafts;
DROP TABLE IF EXISTS user_grafts;
DROP TABLE IF EXISTS levels;
DROP TABLE IF EXISTS member_answers;
DROP TABLE IF EXISTS structure_of_questions;
DROP TABLE IF EXISTS question_reviews;
DROP TABLE IF EXISTS awards;
DROP TABLE IF EXISTS categories;

CREATE TABLE categories(
	category_id int unsigned not null auto_increment,
	category_name nvarchar(100) not null,
	number_of_questions int not null,
	date_create datetime,
	describle_category nvarchar(200),
	PRIMARY KEY (category_id)
);

CREATE TABLE users (
    user_id int unsigned not null auto_increment,
    username varchar(30) not null,
    password varchar(32) not null,
    email varchar(100) not null,
    score_level float not null,
    registed_date datetime,
    power_user tinyint not null,
    money float,
	status tinyint not null,
    PRIMARY KEY (user_id)
);

CREATE TABLE rooms (
    room_id int unsigned not null auto_increment,
    room_name varchar(50) not null,
    owner_id int unsigned not null,
    max_member int,
	number_of_members int not null,
    bet_score float,
	time_per_question tinyint unsigned,
	status tinyint unsigned not null,	-- status: 0-waiting, 1-playing
    PRIMARY KEY (room_id)
);

CREATE TABLE room_members (
    room_member_id int unsigned not null auto_increment,
    user_id int unsigned not null,
    room_id int unsigned not null,
	member_type tinyint unsigned not null,	-- chu phong hay ko?
    question_id int unsigned,
    graft_id int unsigned,
    score float not null,
    combo tinyint,
	last_answer tinyint unsigned,
	status tinyint unsigned not null,
    PRIMARY KEY (room_member_id)
);

CREATE TABLE questions (
    question_id int unsigned not null auto_increment,
    question_type_id tinyint unsigned not null,
	question_field_id tinyint unsigned not null,
    question_name nvarchar(800) not null,
    answer_a nvarchar(200) not null,
    answer_b nvarchar(200) not null,
    answer_c nvarchar(200) not null,
    answer_d nvarchar(200) not null,
    answer tinyint not null,
    describle_answer nvarchar(500),
    PRIMARY KEY (question_id)
);

CREATE TABLE question_types (
    question_type_id tinyint unsigned not null auto_increment,
    question_type_value tinyint unsigned not null,
	question_type_name nvarchar(50),
    score float,
    PRIMARY KEY (question_type_id)
);

CREATE TABLE question_fields (
    question_field_id tinyint unsigned not null auto_increment,
    field_name nvarchar(100) not null,
    description nvarchar(500),
    PRIMARY KEY (question_field_id)
);

CREATE TABLE room_questions (
    room_question_id int unsigned not null auto_increment,
    room_id int unsigned not null,
    question_id int unsigned not null,
    PRIMARY KEY (room_question_id)
);

CREATE TABLE markets (
    item_id int unsigned not null auto_increment,
    item_name nvarchar(100) not null,
    describle nvarchar(500),
    src nvarchar(500),
    money float,
    PRIMARY KEY (item_id)
);

CREATE TABLE user_items (
    user_item_id int unsigned not null auto_increment,
    user_id int unsigned not null,
    item_id int unsigned not null,
    total tinyint not null,
    PRIMARY KEY (user_item_id)
);

CREATE TABLE pictures (
    picture_id int unsigned not null auto_increment,
    src nvarchar(500) not null,
    money float not null,
    PRIMARY KEY (picture_id)
);

CREATE TABLE grafts (
    graft_id int unsigned not null auto_increment,
    picture_id int unsigned not null,
    src nvarchar(500) not null,
    graft_type smallint not null,
    describle nvarchar(500),
    PRIMARY KEY (graft_id)
);

CREATE TABLE user_grafts (
    user_graft_id int unsigned not null auto_increment,
    user_id int unsigned not null,
    graft_id int unsigned not null,
    total tinyint not null,
    PRIMARY KEY (user_graft_id)
);

CREATE TABLE member_answers (
    member_answer_id int unsigned not null auto_increment,
    user_id int unsigned not null,
    question_id int unsigned not null,
    graft_id int unsigned not null,
    score float not null,
    combo tinyint,
    PRIMARY KEY (member_answer_id)
);

CREATE TABLE levels (
    level_id int unsigned not null auto_increment,
    level_name nvarchar(50) not null,
    score_min float not null,
    score_max float not null,
    PRIMARY KEY (level_id)
);

CREATE TABLE structure_of_questions (
    structure_of_question_id int unsigned not null auto_increment,
    question_type_id tinyint unsigned not null,
    number_of_questions tinyint not null,
    PRIMARY KEY (structure_of_question_id)
);

CREATE TABLE question_reviews (
    question_review_id int unsigned not null auto_increment,
    user_id int unsigned not null,
    question_name nvarchar(200) not null,
    question_type_id tinyint unsigned not null,
    answer_a nvarchar(200) not null,
    answer_b nvarchar(200) not null,
    answer_c nvarchar(200) not null,
    answer_d nvarchar(200) not null,
    answer tinyint not null,
    describle_answer nvarchar(500),
    PRIMARY KEY (question_review_id)
);

CREATE TABLE awards (
	award_id int unsigned not null auto_increment,
	user_name varchar(50) not null,
	score float not null,
	date_record datetime,
	PRIMARY KEY (award_id)
);