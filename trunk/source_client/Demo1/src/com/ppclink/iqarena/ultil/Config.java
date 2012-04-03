package com.ppclink.iqarena.ultil;

import android.os.Environment;

public class Config {
	public static final String FILE_CONFIG_NAME = "config.dat";
	public static final String PATH_CONFIG = Environment.getDataDirectory()
			+ "/data/com.ppclink.iqarena";
	public static final String REQUEST_ANSWER_QUESTION = "answer_question";
	public static final String REQUEST_CHECK_CHANGE_ROOM = "check_change_room";
	public static final String REQUEST_CHECK_MEMBERS_IN_ROOM = "check_members_in_room";
	public static final String REQUEST_CREATE_NEW_ROOM = "create_new_room";
	public static final String REQUEST_EXIT_ROOM = "exit_room";

	public static final String REQUEST_GET_LIST_ROOM = "get_list_room";

	public static final String REQUEST_GET_MEMBERS_ANSWER = "get_members_answer";
	public static final String REQUEST_GET_MEMBERS_IN_ROOM = "get_members_in_room";
	public static final String REQUEST_GET_QUESTION = "get_question";
	public static final String REQUEST_JOIN_ROOM = "join_room";
	// public static final String REQUEST_SERVER_ADDR =
	// "http://hoangnh29.byethost12.com";
//	public static final String REQUEST_SERVER_ADDR = "http://192.168.1.108/iqarena/source_server/index.php";
	public static final String REQUEST_LOGIN = "login";
	public static final String REQUEST_PLAY_GAME = "play_game";
	public static final String REQUEST_REGISTER = "register";
	public static final String REQUEST_REMOVE_ROOM = "remove_room";
	public static final String REQUEST_SERVER_ADDR = "http://10.0.2.2/iqarena/source_server/index.php";
}