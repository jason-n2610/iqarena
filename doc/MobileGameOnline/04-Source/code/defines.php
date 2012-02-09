<?php
defined('_PPCLink') or die('Restricted access');

//	SERVER
define("FULL_HOST_NAME", "http://localhost/");
define("MF_SEPARATE", "_@^^_");	//Dau ngan cach giua cac truong trong MESSAGE
define("DBF_SEPARATE", "_@@@_");	//Dau ngan cach giua cac truong de luu trong Database
define("AF_SEPARATE", "_@##_");	//Dau ngan cach giua cac truong trong ACTION
define("ISF_SEPARATE", "_@##_");	//Dau ngan cach giua cac truong trong INIT STATE
define("SPF_SEPARATE", "_@##_");	//Dau ngan cach giua cac truong trong SEARCH PATTERN
define("VERIFY_CODE_LENGTH", 128);
define("ACTIVATION_CODE_LENGTH", 40);

define("STATE_SUCCESS", 1);
define("STATE_FAIL", 2);
define("STATE_LOGIN_FAIL", 3);

//	DATE & TIME
define("MAX_LOGIN_TIME", 3600);	// Sau 3600s neu ko co request nao` tu Client thi` Server cho User do tu dong Logout
define("NORMAL_TIME_FORMAT", "Y-m-d H:i:s");
define("NORMAL_DATE_FORMAT", "Y-m-d");
define("NORMAL_FORUM_BIRTHDAY_FORMAT", "m-d-Y");

//	DATABASE
define("MAX_USER", 9999999999);
define("GAME_DB_PREFIX", "game_");
define("FORUM_DB_PREFIX", "vb_");
define("DEFAUL_ACTIVE_GROUP_ID", 3);	//Active group is la` 3
define("DEFAUL_USER_GROUP_ID", 2);
define("DEFAUL_SHOW_BIRTHDAY", 0);
define("DEFAUL_USER_TITLE", "Junior Member");
define("DEFAULT_REPUTATION_LEVEL_ID", 5);
define("FORUM_URL", "http://abc.deltago.com/vbb");	//ko co dau '/' o cuoi cung

//	MAIL
define("TP_ACTIVE", 0);
define("TP_CHANGE_PASSWORD", 1);
define("ADMIN_EMAIL", "PPCLink<abc@gmail.com>");

//MESSAGE TYPE
define("REGISTER", 1);
define("LOGIN", 2);
define("FORGOT_PASSWORD", 3);
define("VIEW_USER_INFO", 4);
define("UPDATE_USER_INFO", 5);
define("LOGOUT", 6);
define("NEW_GAME", 7);
define("GET_LIST_GAME_STATUS", 8);
define("GET_LIST_GAME", 9);
define("ACTION", 10);
define("GET_CURRENT_CHESS_BOARD_STATE", 11);
define("GET_LASTEST_ACTIONS", 12);
define("ADD_FRIEND", 13);
define("DELETE_FRIEND", 14);
define("GET_FRIEND_LIST", 15);
define("GET_NEW_INFO_BITS", 16);

//	FRIEND LIST SORT BY
define("SORT_BY_NAME", 0);
define("SORT_BY_DATE_ADD", 1);
define("SORT_BY_MOST_PLAY_WITH", 2);

//	ACTIONS
define("ACT_MOVE", 1);
define("ACT_DRAW", 2);
define("ACT_RETURN_DRAW", 3);
define("ACT_TAKE_BACK", 4);
define("ACT_RETURN_TAKE_BACK", 5);
define("ACT_RESIGN", 6);
define("ACT_ABORT", 7);
define("ACT_END_GAME", 8);

define("ACCEPT", 1);
define("DECLINE", 0);

//	STATES
define("STATE_PLAYING", 0);
define("STATE_WAITING", 1);
define("STATE_INVITING", 2);
define("STATE_INVITED", 3);
define("STATE_ABORTED", 4);
define("STATE_ENDED", 5);
define("STATE_WIN", 6);
define("STATE_LOST", 7);
define("STATE_DRAW", 8);


define("ACTIVATION_TYPE", 0);
define("CHANGE_PASSWORD_TYPE", 1);

define("COLOR_WHITE", "WHITE");
define("COLOR_BLACK", "BLACK");


//Các bít cờ
define("BIT_NEW_ACTION", 0);
define("BIT_NEW_PLAYING_GAME", 1);
define("BIT_NEW_INVITED_GAME", 2);
?>