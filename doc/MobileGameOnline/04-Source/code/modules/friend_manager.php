<?php
defined('_PPCLink') or die('Restricted access');

class FriendManager{

//	FUNCTIONS
/**
 *@user
 *@inMsg
 *@outMsg
 */
function addFriend(){
	global $user, $inMsg, $outMsg;
	
	//find friend's userid
	$query = "SELECT * FROM ".FORUM_DB_PREFIX."user WHERE username='{$inMsg->params["username"]}'";
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
	$row = mysql_fetch_array($result, MYSQL_ASSOC);
	
	//kiem tra xem friend nay` da co trong Friend List chua
	$query = "SELECT * FROM ".GAME_DB_PREFIX."friends_list WHERE userid='{$user->userid}' and friend_id='{$row["userid"]}'";
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
	if(mysql_num_rows($result)){	//da ton tai friend nay` --> ko them dc
		$outMsg->updateState(STATE_FAIL, "This user already is your friend!");
		return false;
	}else if($user->userid != $row["userid"]){	//chua ton tai friend --> them dc
		$curr_t = date(NORMAL_TIME_FORMAT);
		$query_arr = array(
					"userid" => $user->userid,
					"friend_id" => $row["userid"],
					"add_at" => $curr_t
					);
		$query = makeInsertQuery(GAME_DB_PREFIX."friends_list", $query_arr);
		$result = mysql_query($query, $GLOBALS["link"]);
		checkQuerySuccess($result);
		
		$outMsg->updateState(STATE_SUCCESS, "You added user '{$inMsg->params["username"]}' into your friends list!");
		return true;
	}else{	//friend trung` voi user dang dang nhap
		$outMsg->updateState(STATE_FAIL, "You can't add yourself as a friend!");
		return false;
	}
}

/**
 *@user
 *@inMsg
 *@outMsg
 */
function deleteFriend(){
	global $user, $inMsg, $outMsg;
	
	//find friend's userid
	$query = "SELECT * FROM ".FORUM_DB_PREFIX."user WHERE username='{$inMsg->params["username"]}'";
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
	$row = mysql_fetch_array($result, MYSQL_ASSOC);
	
	//kiem tra xem friend nay` da co trong Friend List chua
	$query = "SELECT * FROM ".GAME_DB_PREFIX."friends_list WHERE userid='{$user->userid}' and friend_id='{$row["userid"]}'";
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
	if(mysql_num_rows($result)){	//2 nguoi da la friend --> xoa dc
		$query = "DELETE FROM ".GAME_DB_PREFIX."friends_list WHERE userid='{$user->userid}' and friend_id='{$row["userid"]}'";
		$result = mysql_query($query, $GLOBALS["link"]);
		checkQuerySuccess($result);
		
		$outMsg->updateState(STATE_SUCCESS, "You deleted '{$inMsg->params["username"]}' from your friends list!");
		return true;
	}else{	//chua ton tai lien ket friend --> ko xoa dc
		$outMsg->updateState(STATE_FAIL, "This user isn't your friend!");
		return false;
	}
}

/**
 *@user
 *@inMsg
 *@outMsg
 */
function getFriendList(){
	global $user, $inMsg, $outMsg;
	
	switch($inMsg->params["sort_by"]){
		case SORT_BY_NAME:{
			$query = "SELECT ".GAME_DB_PREFIX."friends_list.*, username FROM ".GAME_DB_PREFIX."friends_list, ".FORUM_DB_PREFIX."user WHERE (".GAME_DB_PREFIX."friends_list.friend_id=".FORUM_DB_PREFIX."user.userid) and (".GAME_DB_PREFIX."friends_list.userid='{$user->userid}') ORDER BY username";
			break;
		}
		case SORT_BY_DATE_ADD:{
			$query = "SELECT ".GAME_DB_PREFIX."friends_list.*, username FROM ".GAME_DB_PREFIX."friends_list, ".FORUM_DB_PREFIX."user WHERE (".GAME_DB_PREFIX."friends_list.friend_id=".FORUM_DB_PREFIX."user.userid) and (".GAME_DB_PREFIX."friends_list.userid='{$user->userid}') ORDER BY add_at";
			break;
		}
		case SORT_BY_MOST_PLAY_WITH:{	//not yet
			return;
			break;
		}
		default:{
			return;
			break;
		}
	}
	
	debug_message($query);
	//get All Friend
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
	while($row = mysql_fetch_array($result, MYSQL_ASSOC)){
		$tmp_arr[] = $row["username"];
	}
	
	for($i = ($inMsg->params["n"] - 1); $i < ($inMsg->params["n"] + $inMsg->params["max_numbers"] - 1); $i++){
		if($tmp_arr[$i]){
			$outMsg->params["friend"][] = $tmp_arr[$i];
		}
	}
}


//	CONTROLLERS
/**
 *@user
 *@inMsg
 *@outMsg
 */
function addFriendController(){
	global $user, $inMsg, $outMsg;
	
	if(UserManager::checkLoggedIn($user->userid)){
		if(UserManager::checkExistAccount($inMsg->params["username"])){
			FriendManager::addFriend();
		}else{
			$outMsg->updateState(STATE_FAIL, "Not found username '{$inMsg->params["username"]}'!");
		}
	}else{
		$outMsg->updateState(STATE_FAIL, "You are not logged in. Please login!");
	}
}

/**
 *@user
 *@inMsg
 *@outMsg
 */
function deleteFriendController(){
	global $user, $inMsg, $outMsg;
	
	if(UserManager::checkLoggedIn($user->userid)){
		if(UserManager::checkExistAccount($inMsg->params["username"])){
			FriendManager::deleteFriend();
		}else{
			$outMsg->updateState(STATE_FAIL, "Not found username '{$inMsg->params["username"]}'!");
		}
	}else{
		$outMsg->updateState(STATE_FAIL, "You are not logged in. Please login!");
	}
}

/**
 *@user
 *@inMsg
 *@outMsg
 */
function getFriendListController(){
	global $user, $inMsg, $outMsg;
	
	if(UserManager::checkLoggedIn($user->userid)){
		FriendManager::getFriendList();
	}else{
		$outMsg->updateState(STATE_FAIL, "You are not logged in. Please login!");
	}
}
}
?>