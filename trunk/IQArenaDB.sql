
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

CREATE TABLE users(
	user_id int unsigned not null auto_increment,
	username varchar(30) not null,
	passwd varchar(32) not null,
	score float,
	registed_date datetime,
	power_user tinyint not null,
	PRIMARY KEY (user_id)
);

CREATE TABLE rooms(
	room_id int unsigned not null auto_increment,
	room_name varchar(50) not null,
	max_mem int,
	min_mem int,
	status tinyint,
	PRIMARY KEY (room_id)
);

CREATE TABLE room_members(
	room_member_id int unsigned not null auto_increment,
	user_id int unsigned not null,
	room_id int unsigned not null,
	user_type tinyint,
	PRIMARY KEY (room_member_id)
);

CREATE TABLE questions(
	question_id int unsigned not null auto_increment,
	question_type_id tinyint unsigned not null,
	question_name nvarchar(200) not null,
	answer_a nvarchar(200) not null,
	answer_b nvarchar(200) not null,
	answer_c nvarchar(200) not null,
	answer_d nvarchar(200) not null,
	answer tinyint not null,
	PRIMARY KEY (question_id)
);

CREATE TABLE question_types(
	question_type_id tinyint unsigned not null auto_increment,
	question_field_id tinyint unsigned not null,
	question_type tinyint unsigned not null,
	score float not null,
	PRIMARY KEY (question_type_id)
);

CREATE TABLE question_fields(
	question_field_id tinyint unsigned not null auto_increment,
	field_name nvarchar(100) not null,
	description nvarchar(500),
	PRIMARY KEY (question_field_id)
);

CREATE TABLE room_questions(
	room_question_id int unsigned not null auto_increment,
	room_id int unsigned not null,
	question_id int unsigned not null,
	PRIMARY KEY (room_question_id)
);

INSERT INTO users(username, passwd, score, registed_date, power_user)
	VALUES ('admin', '0945fc9611f55fd0e183fb8b044f1afe', 0, '2012-1-2 10:00:00' , 1),
		('user', '0945fc9611f55fd0e183fb8b044f1afe', 100, '2012-1-2 10:00:00', 0);

