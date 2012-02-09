-- phpMyAdmin SQL Dump
-- version 2.10.3
-- http://www.phpmyadmin.net
-- 
-- Host: localhost
-- Generation Time: Oct 13, 2009 at 05:44 PM
-- Server version: 5.0.51
-- PHP Version: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- Database: `vbb`
-- 

-- 
-- Dumping data for table `game_friends_list`
-- 


-- 
-- Dumping data for table `game_game`
-- 

INSERT INTO `game_game` (`game_type_id`, `game_id`, `init_state`, `number_of_actions`, `actions`, `state_id`, `userid_win`, `userid_abort`, `userid1`, `userid2`, `start_at`, `end_at`, `note`) VALUES 
(0, 0, NULL, 0, NULL, 1, NULL, NULL, 2, NULL, '2009-10-13 16:31:02', NULL, NULL),
(0, 1, NULL, 0, NULL, 2, NULL, NULL, 4, NULL, '2009-10-13 16:32:14', NULL, NULL),
(0, 2, NULL, 95, '95 Actions', 3, NULL, NULL, 1, 2, '2009-10-13 16:33:29', '2009-10-14 16:33:29', 'Going on...'),
(0, 3, NULL, 96, '96 Actions', 3, NULL, NULL, 1, 3, '2009-10-13 16:34:44', '2009-10-14 16:33:29', NULL),
(0, 4, NULL, 98, '98 Actions', 4, NULL, NULL, 1, 4, '2009-10-13 16:34:44', '2009-10-14 16:33:29', NULL),
(0, 5, NULL, 100, '100 Actions', 4, NULL, NULL, 1, 5, '2009-10-13 16:34:44', '2009-10-14 16:33:29', NULL),
(2, 6, NULL, 0, NULL, 0, NULL, NULL, 2, 5, '2009-10-13 16:34:44', NULL, NULL),
(1, 7, NULL, 0, NULL, 0, NULL, NULL, 4, 5, '2009-10-13 16:34:44', NULL, NULL),
(0, 8, NULL, 0, NULL, 0, NULL, NULL, 3, 4, '2009-10-13 16:34:44', NULL, NULL);

-- 
-- Dumping data for table `game_game_type`
-- 

INSERT INTO `game_game_type` (`game_type_id`, `game_type`, `created_at`) VALUES 
(0, 'Cờ Tướng (Chinese Ch', '2009-10-13 15:58:05'),
(1, 'Cờ Vua (Normal Chess', '2009-10-13 16:10:04'),
(2, 'Cờ Caro', '2009-10-13 16:11:58'),
(3, 'Cờ Vây', '2009-10-13 16:12:16');

-- 
-- Dumping data for table `game_login`
-- 


-- 
-- Dumping data for table `game_searching`
-- 

INSERT INTO `game_searching` (`game_type_id`, `game_id`, `search_pattern`) VALUES 
(0, 0, '1||lamdv||||'),
(0, 1, '1||||duongvanlam88@gmail.com||');

-- 
-- Dumping data for table `game_state_id`
-- 

INSERT INTO `game_state_id` (`state_id`, `state`, `note`) VALUES 
(0, 'Playing', 'Đang chơi'),
(1, 'Waiting', NULL),
(2, 'Inviting', NULL),
(3, 'Aborted', NULL),
(4, 'Ended', NULL);

-- 
-- Dumping data for table `vb_user`
-- 

/*INSERT INTO `vb_user` (`userid`, `usergroupid`, `membergroupids`, `displaygroupid`, `username`, `password`, `passworddate`, `email`, `styleid`, `parentemail`, `homepage`, `icq`, `aim`, `yahoo`, `msn`, `skype`, `showvbcode`, `showbirthday`, `usertitle`, `customtitle`, `joindate`, `daysprune`, `lastvisit`, `lastactivity`, `lastpost`, `lastpostid`, `posts`, `reputation`, `reputationlevelid`, `timezoneoffset`, `pmpopup`, `avatarid`, `avatarrevision`, `profilepicrevision`, `sigpicrevision`, `options`, `birthday`, `birthday_search`, `maxposts`, `startofweek`, `ipaddress`, `referrerid`, `languageid`, `emailstamp`, `threadedmode`, `autosubscribe`, `pmtotal`, `pmunread`, `salt`, `ipoints`, `infractions`, `warnings`, `infractiongroupids`, `infractiongroupid`, `adminoptions`, `profilevisits`, `friendcount`, `friendreqcount`, `vmunreadcount`, `vmmoderatedcount`, `socgroupinvitecount`, `socgroupreqcount`, `pcunreadcount`, `pcmoderatedcount`, `gmmoderatedcount`) VALUES 
(1, 6, '', 0, 'admin', '00683181b889bc1a0c7f6078258ae1cc', '2009-09-11', 'duongvanlam88@gmail.com', 0, '', '', '', '', '', '', '', 2, 2, 'Administrator', 0, 1252663469, 0, 1252725241, 1252725241, 1252723356, 2, 1, 10, 1, '', 0, 0, 0, 0, 0, 11536471, '', '0000-00-00', -1, 1, '', 0, 0, 0, 0, -1, 0, 0, 'qQ#', 0, 0, 0, '', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(2, 2, '', 0, 'muzix', '67508c9f361956074461208a05c150cd', '2009-09-11', 'mail_cua_hoang@yahoo.com.vn', 0, '', '', '', '', '', '', '', 1, 0, 'Junior Member', 0, 1252663858, 0, 1252724398, 1252724398, 1252723493, 3, 1, 10, 5, '0', 0, 0, 0, 0, 0, 45091927, '', '0000-00-00', -1, -1, '127.0.0.1', 0, 1, 0, 0, -1, 0, 0, 'f@o', 0, 0, 0, '', 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(3, 6, '', 0, 'lamdv', '00683181b889bc1a0c7f6078258ae1cc', '2009-09-11', 'duongvanlam@gmail.com', 0, '', '', '', '', '', '', '', 0, 2, 'Junior Member', 0, 0, 0, 1252725052, 1252725052, 1252724262, 4, 2, 10, 1, '', 0, 0, 0, 0, 0, 33554447, '', '0000-00-00', -1, 1, '', 0, 0, 0, 0, -1, 0, 0, 'qQ#', 0, 0, 0, '', 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(4, 2, '', 0, 'abc', '89afcd0b75c5dc099b6294058e5bb011', '2009-09-12', 'abc@gmail.com', 0, '', '', '', '', '', '', '', 1, 0, 'Junior Member', 0, 1252724449, 0, 1252725218, 1252725218, 1252724688, 5, 1, 10, 5, '0', 0, 0, 0, 0, 0, 45091927, '', '0000-00-00', -1, -1, '127.0.0.1', 0, 1, 0, 0, -1, 0, 0, '@/R', 0, 0, 0, '', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(5, 6, '', 0, 'xyz', '00683181b889bc1a0c7f6078258ae1cc', '2009-09-12', 'duongvanlam@gmail.com', 0, '', '', '', '', '', '', '', 0, 2, 'Administrator', 0, 0, 0, 0, 1252725709, 1252725124, 6, 1, 10, 1, '', 0, 0, 0, 0, 0, 33554447, '', '0000-00-00', -1, 1, '', 0, 0, 0, 0, -1, 0, 0, 'qQ#', 0, 0, 0, '', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
*/