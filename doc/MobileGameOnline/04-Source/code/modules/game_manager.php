<?php
defined('_PPCLink') or die('Restricted access');

class GameManager{

//	FUNCTIONS
function checkExistGame($game_id){	//not-finish
}
function getNextGameID(){	//not finished
	//$query = "SELECT count(*) as count FROM ".GAME_DB_PREFIX."game";
	$query = "SELECT max(game_id) as max_game_id FROM ".GAME_DB_PREFIX."game";
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
	$row = mysql_fetch_array($result, MYSQL_ASSOC);
	return ($row["max_game_id"] + 1);
}
function countGame($state){	//finished
	global $user, $inMsg, $outMsg;
	
	switch($state){
		case STATE_PLAYING:	//ok
		case STATE_ENDED:{	//ok
			$query = "SELECT count(*) as num FROM ".GAME_DB_PREFIX."game WHERE (userid1='{$user->userid}' or userid2='{$user->userid}') and state_id='{$state}' and game_type_id='{$inMsg->params["type_of_game"]}'";
			break;
		}
		case STATE_ABORTED:{	//ok
			$query = "SELECT count(*) as num FROM ".GAME_DB_PREFIX."game WHERE userid_abort='{$user->userid}' and (userid1='{$user->userid}' or userid2='{$user->userid}') and state_id='".STATE_ABORTED."'  and game_type_id='{$inMsg->params["type_of_game"]}'";
			break;
		}
		case STATE_INVITING:{	//ok
			$query = "SELECT count(*) as num FROM ".GAME_DB_PREFIX."game WHERE userid1='{$user->userid}' and state_id='".STATE_INVITING."' and game_type_id='{$inMsg->params["type_of_game"]}'";
			break;
		}
		case STATE_INVITED:{	//ok
			$query = "SELECT count(*) as num FROM ".GAME_DB_PREFIX."game WHERE userid2='{$user->userid}' and state_id='".STATE_INVITING."' and game_type_id='{$inMsg->params["type_of_game"]}'";
			debug_message($query);
			break;
		}
		case STATE_WAITING:{	//ok
			$query = "SELECT count(*) as num FROM ".GAME_DB_PREFIX."game WHERE (userid1='{$user->userid}' or userid2='{$user->userid}') and state_id='{$state}' and game_type_id='{$inMsg->params["type_of_game"]}'";
			break;
		}
		case STATE_WIN:{	//ok
			$query = "SELECT count(*) as num FROM ".GAME_DB_PREFIX."game WHERE userid_win='{$user->userid}' and (userid1='{$user->userid}' or userid2='{$user->userid}') and state_id='".STATE_ENDED."'  and game_type_id='{$inMsg->params["type_of_game"]}'";
			break;
		}
		case STATE_LOST:{	//ok
			$query = "SELECT count(*) as num FROM ".GAME_DB_PREFIX."game WHERE (userid1='{$user->userid}' or userid2='{$user->userid}') and userid_win!='{$user->userid}' and state_id='".STATE_ENDED."' and game_type_id='{$inMsg->params["type_of_game"]}'";
			break;
		}
		case STATE_DRAW:{	//ok
			$query = "SELECT count(*) as num FROM ".GAME_DB_PREFIX."game WHERE (userid_win is null) and (userid1='{$user->userid}' or userid2='{$user->userid}') and state_id='".STATE_ENDED."' and game_type_id='{$inMsg->params["type_of_game"]}'";
			break;
		}
		default:{
			exit(-1);
			break;
		}
		
	}
	
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
	$row = mysql_fetch_array($result, MYSQL_ASSOC);
	
	return $row["num"];
}
function searchOpponent(){	//finished-PATTERN-1
	global $user, $inMsg, $outMsg;
	$search = $inMsg->params["search_pattern"];
	
	switch($search["version"]){
		case 1:{	//version 1
			if(UserManager::checkExistAccount($search["username"])){
				if($user->username == $search["username"]){
					$outMsg->updateState(STATE_FAIL, "You can't play with yourself.");
				}else{
					//tim xem da co game dang inviting giua 2 nguoi choi nay chua
					$query = "SELECT * FROM ".GAME_DB_PREFIX."game WHERE userid1='{$user->userid}' and userid2='". UserManager::getUserID($search["username"]) ."' and state_id='".STATE_INVITING."' and game_type_id='{$inMsg->params["type_of_game"]}'";
					$result = mysql_query($query, $GLOBALS["link"]);
					checkQuerySuccess($result);
					if(mysql_num_rows($result)){	//ban nay` da moi user kia roi
						$outMsg->updateState(STATE_FAIL, "You has invited user '{$search["username"]}' before. Please waiting for opponent's acceptment.");
					}else{
						//tim xem opponent da co yeu cau new-game chua? Neu chua co thi createGame, neu da co thi joinGame
						$query = "SELECT * FROM ".GAME_DB_PREFIX."game WHERE userid1='". UserManager::getUserID($search["username"]) ."' and userid2='{$user->userid}' and state_id='".STATE_INVITING."' and game_type_id='{$inMsg->params["type_of_game"]}'";
						$result = mysql_query($query, $GLOBALS["link"]);
						checkQuerySuccess($result);
						if(mysql_num_rows($result)){	//found --> joinGame
							$row = mysql_fetch_array($result, MYSQL_ASSOC);
							GameManager::joinGame($row["game_id"]);
						}else{	//not found --> createGame
							GameManager::createGame(STATE_INVITING);
						}
					}
				}
			}else{
				$outMsg->updateState(STATE_FAIL, "Not found user '{$search["username"]}'. Please, check it again.");
			}
			break;
		}
		case 2:{	//version 2
			break;
		}
		default:{
			break;
		}
	}
}
function createGame($state){	//finished-INVITING
	global $user, $inMsg, $outMsg;
	$search = $inMsg->params["search_pattern"];
	
	switch($state){
		case STATE_INVITING:{
			$query_arr = array(
					"game_type_id" => $inMsg->params["type_of_game"],
					"game_id" => GameManager::getNextGameID(),
					"state_id" => STATE_INVITING,
					"number_of_actions" => 0,
					"init_state" => ChineseChess::createInitState(),
					"actions" => "",
					"userid1" => $user->userid,
					"userid2" => UserManager::getUserID($search["username"]),
					"note" => $search["message"]
					);
			$query = makeInsertQuery(GAME_DB_PREFIX."game", $query_arr);
			$result = mysql_query($query, $GLOBALS["link"]);
			checkQuerySuccess($result);
			addANewInfoBit(UserManager::getUserID($search["username"]), BIT_NEW_INVITED_GAME);
			
			$outMsg->updateState(STATE_SUCCESS, "Waiting for opponent...");
			$outMsg->params["state_of_game"] = "WAITING";	//not-finish
			break;
		}
		default:{
		}
	}
}
function joinGame($game_id){	//finished
	global $user, $inMsg, $outMsg;
	$search = $inMsg->params["search_pattern"];
	
	$query_arr = array(
			"state_id" => STATE_PLAYING,
			"number_of_actions" => 0,
			"actions" => "",
			"init_state" => ChineseChess::createInitState(),
			"start_at" => date(NORMAL_TIME_FORMAT)
			);
	$query = makeUpdateQuery(GAME_DB_PREFIX."game", $query_arr, "game_id='{$game_id}'");
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
	deleteANewInfoBit($user->userid, BIT_NEW_INVITED_GAME);
	addANewInfoBit(UserManager::getOpponentID($game_id, $user->userid), BIT_NEW_PLAYING_GAME);
	
	
	$outMsg->updateState(STATE_SUCCESS, "Game begin!");
	$outMsg->params["state_of_game"] = "PLAYING...";		//not-finish
	$outMsg->params["username"] = UserManager::getUsername(UserManager::getOpponentID($game_id, $user->userid));
}
function getNewInfoBits(){	//not-finish
	global $user, $inMsg, $outMsg;
	
	$outMsg->params["new_info_bits"] = $user->new_info_bits;
}
function getListGameStatus(){	//finished
	global $user, $inMsg, $outMsg;
	
	$outMsg->params["number_of_playing_games"] = GameManager::countGame(STATE_PLAYING);
	$outMsg->params["number_of_waiting_games"] = GameManager::countGame(STATE_WAITING);
	$outMsg->params["number_of_inviting_games"] = GameManager::countGame(STATE_INVITING);
	$outMsg->params["number_of_invited_games"] = GameManager::countGame(STATE_INVITED);
	$outMsg->params["number_of_aborted_games"] = GameManager::countGame(STATE_ABORTED);
	$outMsg->params["number_of_ended_games"] = GameManager::countGame(STATE_ENDED);
	
	$outMsg->params["number_of_win_games"] = GameManager::countGame(STATE_WIN);
	$outMsg->params["number_of_draw_games"] = GameManager::countGame(STATE_DRAW);
	$outMsg->params["number_of_lost_games"] = GameManager::countGame(STATE_LOST);
	
}
function getListGame(){	//finished
	global $user, $inMsg, $outMsg;
	
	switch($inMsg->params["state_of_game"]){
		case STATE_PLAYING:{
			$query = "SELECT * FROM ".GAME_DB_PREFIX."game WHERE (userid1='{$user->userid}' or userid2='{$user->userid}') and state_id='{$inMsg->params["state_of_game"]}' and game_type_id='{$inMsg->params["type_of_game"]}'";
			deleteANewInfoBit($user->userid, BIT_NEW_PLAYING_GAME);
			break;
		}
		case STATE_WAITING:{
			$query = "SELECT * FROM ".GAME_DB_PREFIX."game WHERE (userid1='{$user->userid}' or userid2='{$user->userid}') and state_id='{$inMsg->params["state_of_game"]}' and game_type_id='{$inMsg->params["type_of_game"]}'";
			break;
		}
		case STATE_INVITING:{
			$query = "SELECT * FROM ".GAME_DB_PREFIX."game WHERE userid1='{$user->userid}' and state_id='{$inMsg->params["state_of_game"]}' and game_type_id='{$inMsg->params["type_of_game"]}'";
			break;
		}
		case STATE_INVITED:{
			$query = "SELECT * FROM ".GAME_DB_PREFIX."game WHERE userid2='{$user->userid}' and state_id='". STATE_INVITING ."' and game_type_id='{$inMsg->params["type_of_game"]}'";
			deleteANewInfoBit($user->userid, BIT_NEW_INVITED_GAME);
			break;
		}
		case STATE_WIN:{
			$query = "SELECT * FROM ".GAME_DB_PREFIX."game WHERE userid_win='{$user->userid}' and (userid1='{$user->userid}' or userid2='{$user->userid}') and state_id='".STATE_ENDED."' and game_type_id='{$inMsg->params["type_of_game"]}'";
			break;
		}
		case STATE_LOST:{
			$query = "SELECT * FROM ".GAME_DB_PREFIX."game WHERE userid_win!='{$user->userid}' and (userid1='{$user->userid}' or userid2='{$user->userid}') and state_id='".STATE_ENDED."' and game_type_id='{$inMsg->params["type_of_game"]}'";
			break;
		}
		case STATE_DRAW:{
			$query = "SELECT * FROM ".GAME_DB_PREFIX."game WHERE (userid_win is null) and (userid1='{$user->userid}' or userid2='{$user->userid}') and state_id='".STATE_ENDED."' and game_type_id='{$inMsg->params["type_of_game"]}'";
			break;
		}
		case STATE_ABORTED:{
			$query = "SELECT * FROM ".GAME_DB_PREFIX."game WHERE userid_abort='{$user->userid}' and (userid1='{$user->userid}' or userid2='{$user->userid}') and state_id='".STATE_ABORTED."' and game_type_id='{$inMsg->params["type_of_game"]}'";
			break;
		}
		default:{
			$outMsg->updateState(STATE_FAIL, "An error has occurred: Invalid state of game'");
			exit(-1);
			break;
		}
	}
	debug_message($query);
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
	while($row = mysql_fetch_array($result, MYSQL_ASSOC)){
		$opponent_id = ($user->userid == $row["userid1"])?$row["userid2"]:$row["userid1"];
		$start_at = $end_at = "";
		if($row["start_at"]){
			$start_at = date(NORMAL_DATE_FORMAT, strtotime($row["start_at"]));
		}
		if($row["end_at"]){
			$end_at = date(NORMAL_DATE_FORMAT, strtotime($row["end_at"]));
		}
		
		$str = $row["game_id"] .AF_SEPARATE. 
				UserManager::getUsername($opponent_id) .AF_SEPARATE. 
				$start_at .AF_SEPARATE. 
				$row["number_of_actions"] .AF_SEPARATE. 
				$end_at .AF_SEPARATE. 
				$row["note"];
		
		$tmp_arr[] = $str;
	}
	
	for($i = ($inMsg->params["n"] - 1); $i < ($inMsg->params["n"] + $inMsg->params["max_numbers"] - 1); $i++){
		if(isset($tmp_arr[$i])){
			$outMsg->params["game"][] = $tmp_arr[$i];
		}
	}
	debug_message("END-GET-LIST-GAME");
}

//	CONTROLLERS
function newGameController(){
	global $user, $inMsg, $outMsg;
	
	if(UserManager::checkLoggedIn($user->userid)){	//Neu user da login
		//search opponent
		GameManager::searchOpponent();
	}else{
		$outMsg->updateState(STATE_FAIL, "You are not logged in. Please login!");
	}

}
function getNewInfoBitsController(){
	global $user, $inMsg, $outMsg;
	
	if(UserManager::checkLoggedIn($user->userid)){	//Neu user da login
		GameManager::getNewInfoBits();
	}else{
		$outMsg->updateState(STATE_FAIL, "You are not logged in. Please login!");
	}
}
function getListGameStatusController(){	//finished
	global $user, $inMsg, $outMsg, $gameInstance;
	
	if(UserManager::checkLoggedIn($user->userid)){	//Neu user da login
		GameManager::getListGameStatus();
	}else{
		$outMsg->updateState(STATE_FAIL, "You are not logged in. Please login!");
	}
}
function getListGameController(){	//finished
	global $user, $inMsg, $outMsg, $gameInstance;
	
	if(UserManager::checkLoggedIn($user->userid)){	//Neu user da login
		GameManager::getListGame();
	}else{
		$outMsg->updateState(STATE_FAIL, "You are not logged in. Please login!");
	}
}
function genericActionController(){	//finished
	global $user, $inMsg, $outMsg, $gameInstance;
	
	if(UserManager::checkLoggedIn($user->userid)){	//Neu user da login
		$gameInstance->actionController();
	}else{
		$outMsg->updateState(STATE_FAIL, "You are not logged in. Please login!");
	}
}
function gameInfoController(){	//finished
	global $user, $inMsg, $outMsg, $gameInstance;
	
	switch($inMsg->params["type_of_message"]){
		case GET_CURRENT_CHESS_BOARD_STATE:{
			if(UserManager::checkLoggedIn($user->userid)){	//Neu user da login
				$gameInstance->getCurrentChessBoardState();
			}else{
				$outMsg->updateState(STATE_FAIL, "You are not logged in. Please login!");
			}
			break;
		}
		case GET_LASTEST_ACTIONS:{
			if(UserManager::checkLoggedIn($user->userid)){	//Neu user da login
				$gameInstance->getLastestGameActions();
			}else{
				$outMsg->updateState(STATE_FAIL, "You are not logged in. Please login!");
			}
			break;
		}
		default:{
			break;
		}
	}
}


}
?>