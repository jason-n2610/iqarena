-- phpMyAdmin SQL Dump
-- version 2.10.3
-- http://www.phpmyadmin.net
-- 
-- Host: localhost
-- Generation Time: Oct 13, 2009 at 04:50 PM
-- Server version: 5.0.51
-- PHP Version: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- Database: `vbb`
-- 

-- --------------------------------------------------------

-- 
-- Table structure for table `game_friends_list`
-- 

DROP TABLE IF EXISTS `game_friends_list`;
CREATE TABLE `game_friends_list` (
  `userid` int(10) NOT NULL,
  `friend_id` int(10) NOT NULL,
  `add_at` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `note` text,
  PRIMARY KEY  (`userid`,`friend_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `game_friends_list`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `game_game`
-- 

DROP TABLE IF EXISTS `game_game`;
CREATE TABLE `game_game` (
  `game_type_id` smallint(6) NOT NULL,
  `game_id` int(11) NOT NULL,
  `init_state` text,
  `number_of_actions` int(11) NOT NULL,
  `actions` text,
  `state_id` smallint(6) NOT NULL,
  `userid_win` int(10) default NULL,
  `userid_abort` int(10) default NULL,
  `userid1` int(10) NOT NULL,
  `userid2` int(10) default NULL,
  `start_at` timestamp NULL default NULL,
  `end_at` timestamp NULL default NULL,
  `note` text,
  PRIMARY KEY  (`game_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `game_game`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `game_game_type`
-- 

DROP TABLE IF EXISTS `game_game_type`;
CREATE TABLE `game_game_type` (
  `game_type_id` tinyint(4) NOT NULL,
  `game_type` varchar(20) NOT NULL,
  `created_at` timestamp NOT NULL default CURRENT_TIMESTAMP
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `game_game_type`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `game_login`
-- 

DROP TABLE IF EXISTS `game_login`;
CREATE TABLE `game_login` (
  `userid` int(10) NOT NULL,
  `verify_code` varchar(500) NOT NULL,
  `last_login_at` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `last_access_at` timestamp NOT NULL default '0000-00-00 00:00:00',
  `available` tinyint(1) NOT NULL,
  PRIMARY KEY  (`userid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `game_login`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `game_searching`
-- 

DROP TABLE IF EXISTS `game_searching`;
CREATE TABLE `game_searching` (
  `game_type_id` smallint(6) NOT NULL,
  `game_id` int(10) NOT NULL,
  `search_pattern` text NOT NULL,
  PRIMARY KEY  (`game_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `game_searching`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `game_state_id`
-- 

CREATE TABLE `game_state_id` (
  `state_id` smallint(6) NOT NULL,
  `state` varchar(50) NOT NULL,
  `note` text,
  PRIMARY KEY  (`state_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Dumping data for table `game_state_id`
-- 