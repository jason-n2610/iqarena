SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

CREATE DATABASE if not exists iqarena
DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

USE iqarena;

-- check if any table exists
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS question_types;
DROP TABLE IF EXISTS room_members;
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

CREATE TABLE users (
    user_id int unsigned not null auto_increment,
    username varchar(30) not null,
    password varchar(32) not null,
    email varchar(100) not null,
    score_level float,
    registed_date datetime,
    power_user tinyint not null,
    money float,
    PRIMARY KEY (user_id)
);

CREATE TABLE rooms (
    room_id int unsigned not null auto_increment,
    room_name varchar(50) not null,
    owner_id int unsigned not null,
    max_member int,
    min_member int,
    status tinyint,
    win_score float,
    number_of_member int,
    PRIMARY KEY (room_id)
);

CREATE TABLE room_members (
    room_member_id int unsigned not null auto_increment,
    user_id int unsigned not null,
    room_id int unsigned not null,
	type tinyint unsigned not null,
    question_id int unsigned,
    graft_id int unsigned,
    score float,
    combo tinyint,
    PRIMARY KEY (room_member_id)
);

CREATE TABLE questions (
    question_id int unsigned not null auto_increment,
    question_type_id tinyint unsigned not null,
	question_field_id tinyint unsigned not null,
    question_name nvarchar(200) not null,
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
    score float not null,
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