<?php
defined('_PPCLink') or die('Restricted access');

class InMessageManager{
	var $userid = null;
	var $params = array();
	var $others = array();
	

	function InMessageManager(){
		/*global $_POST, $outMsg;
		$this->params["verify_code"] = $_POST["verify_code"];
		$this->params["type_of_message"] = $_POST["type_of_message"];
		//........
		
		$query = "SELECT * FROM ".GAME_DB_PREFIX."login WHERE verify_code='{$this->params["verify_code"]}'";
		$result = mysql_query($query, $GLOBALS["link"]);
		checkQuerySuccess($result);
		if(mysql_num_rows($result)){
			$row = mysql_fetch_array($result, MYSQL_ASSOC);
			$this->userid = $row["userid"];
			
			//..........
		}else if($this->params["verify_code"] == ""){
			//nhung controller ko can` login
			//$this->userid = 5;
		}else{
			$outMsg->updateState(STATE_LOGIN_FAIL, "Invalid verify code! Please login again.");
		}*/
	}
	
	function inMessageController(){
		global $_POST, $outMsg, $user, $gameInstance;
		
		$this->setValueFor("type_of_message");
		switch($this->params["type_of_message"]){
			///////////////////////////////////////////////////////////////////////////
			case REGISTER:{
				$this->setValueFor("username");
				$this->setValueFor("email");
				$this->setValueFor("password");
				$this->setValueFor("birthday");
				$this->setValueFor("icq");
				$this->setValueFor("aim");
				$this->setValueFor("yahoo");
				$this->setValueFor("msn");
				$this->setValueFor("skype");
				
				UserManager::registerController();
				
				break;
			}
			case LOGIN:{
				$this->setValueFor("username");
				$this->setValueFor("password");
				
				UserManager::loginController();
				break;
			}
			case FORGOT_PASSWORD:{
				$this->setValueFor("username");
				
				UserManager::forgotPasswordController();
				break;
			}
			case VIEW_USER_INFO:{
				$this->setValueFor("verify_code");
				$this->setValueFor("company_verify_code");
				$this->setValueFor("username");
				
				$this->updateUserId();
				$this->isMyCompanyInMessage();
				
				$user = new User($this->userid);
				UserManager::viewInfoController();
				break;
			}
			case UPDATE_USER_INFO:{
				$this->setValueFor("verify_code");
				$this->setValueFor("company_verify_code");
				$this->setValueFor("old_password");
				$this->setValueFor("new_password");
				$this->setValueFor("email");
				$this->setValueFor("birthday");
				$this->setValueFor("icq");
				$this->setValueFor("aim");
				$this->setValueFor("yahoo");
				$this->setValueFor("msn");
				$this->setValueFor("skype");
				
				$this->updateUserId();
				$this->isMyCompanyInMessage();
				
				$user = new User($this->userid);
				UserManager::updateInfoController();
				break;
			}
			case LOGOUT:{
				$this->setValueFor("verify_code");
				
				$this->updateUserId();
				$this->isMyCompanyInMessage();
				
				$user = new User($this->userid);
				UserManager::logoutController();
				break;
			}
			///////////////////////////////////////////////////////////////////////////
			case NEW_GAME:{
				$this->setValueFor("verify_code");
				$this->setValueFor("type_of_game");
				$this->params["search_pattern"] = Search::parseSearch($_POST["search_pattern"]);
				$this->updateUserId();
				$this->isMyCompanyInMessage();
				
				$user = new User($this->userid);
				GameManager::newGameController();
				break;
			}
			case GET_NEW_INFO_BITS:{
				$this->setValueFor("verify_code");
				$this->setValueFor("company_verify_code");
				
				$this->updateUserId();
				$this->isMyCompanyInMessage();
				
				$user = new User($this->userid);
				GameManager::getNewInfoBitsController();
				break;
			}
			case GET_LIST_GAME_STATUS:{
				$this->setValueFor("verify_code");
				$this->setValueFor("company_verify_code");
				$this->setValueFor("type_of_game");
				
				$this->updateUserId();
				$this->isMyCompanyInMessage();
				
				$user = new User($this->userid);
				GameManager::getListGameStatusController();
				break;
			}
			case GET_LIST_GAME:{
				$this->setValueFor("verify_code");
				$this->setValueFor("type_of_game");
				$this->setValueFor("state_of_game");
				$this->setValueFor("max_numbers");
				$this->setValueFor("n");
				
				$this->updateUserId();
				$this->isMyCompanyInMessage();
				
				$user = new User($this->userid);
				GameManager::getListGameController();
				break;
			}
			case ACTION:{
				$this->setValueFor("verify_code");
				$this->setValueFor("game_id");
				
				//$this->setValueFor("action");
				
				$this->updateUserId();
				$this->isMyCompanyInMessage();
				
				$this->params["action"] = Action::parseAction(UserManager::getUsername($this->userid) .AF_SEPARATE. $_POST["type_of_action"] .AF_SEPARATE. $_POST["action"]);
				
				$user = new User($this->userid);
				$gameInstance = new ChineseChess();
				GameManager::genericActionController();
				break;
			}
			case GET_CURRENT_CHESS_BOARD_STATE:{
				$this->setValueFor("verify_code");
				$this->setValueFor("game_id");
				
				$this->updateUserId();
				$this->isMyCompanyInMessage();
				
				$user = new User($this->userid);
				$gameInstance = new ChineseChess();
				GameManager::gameInfoController();
				break;
			}
			case GET_LASTEST_ACTIONS:{
				$this->setValueFor("verify_code");
				$this->setValueFor("game_id");
				$this->setValueFor("n");
				
				$this->updateUserId();
				$this->isMyCompanyInMessage();
				
				$user = new User($this->userid);
				$gameInstance = new ChineseChess();
				GameManager::gameInfoController();
				break;
			}
			///////////////////////////////////////////////////////////////////////////
			case ADD_FRIEND:{
				$this->setValueFor("verify_code");
				$this->setValueFor("company_verify_code");
				$this->setValueFor("username");
				
				$this->updateUserId();
				$this->isMyCompanyInMessage();
				
				$user = new User($this->userid);
				FriendManager::addFriendController();
				break;
			}
			case DELETE_FRIEND:{
				$this->setValueFor("verify_code");
				$this->setValueFor("company_verify_code");
				$this->setValueFor("username");
				
				$this->updateUserId();
				$this->isMyCompanyInMessage();
				
				$user = new User($this->userid);
				FriendManager::deleteFriendController();
				break;
			}
			case GET_FRIEND_LIST:{
				$this->setValueFor("verify_code");
				$this->setValueFor("company_verify_code");
				$this->setValueFor("max_numbers");
				$this->setValueFor("n");
				$this->setValueFor("sort_by");
				
				$this->updateUserId();
				$this->isMyCompanyInMessage();
				
				$user = new User($this->userid);
				FriendManager::getFriendListController();
				break;
			}
			default:{
				$outMsg->updateState(STATE_FAIL, "Invalid Message!");
				break;
			}
		}
	}

	function isMyCompanyInMessage(){
		return true;
	}
	
	function getCompanyVerifyCode(){
		//return 
	}

	function setValueFor($str){	//setValue cho params
		global $_POST;
		$this->params[$str] = $_POST[$str];
	}
	
	function updateUserId(){	//Cap nhat userid cho In Message
		$query = "SELECT * FROM ".GAME_DB_PREFIX."login WHERE verify_code='{$this->params["verify_code"]}'";
		$result = mysql_query($query, $GLOBALS["link"]);
		checkQuerySuccess($result);
		if(mysql_num_rows($result)){
			$row = mysql_fetch_array($result, MYSQL_ASSOC);
			$this->userid = $row["userid"];
		}
	}

	function __destruct(){
		debug_message("<i><pre>");
		debug_message_r($this);
		debug_message("</pre></i>");
	}
}

class OutMessageManager{
	var $params = array();
	var $state = STATE_SUCCESS;
	var $message = null;
	
	function updateState($state, $message){
		switch($state){
			case STATE_SUCCESS:{
				if($this->state == STATE_SUCCESS){
					$this->message = $message;
				}
				break;
			}case STATE_FAIL:{
				if($this->state == STATE_SUCCESS){
					$this->state = STATE_FAIL;
					$this->message = $message;
				}
				break;
			}case STATE_LOGIN_FAIL:{
				if($this->state == STATE_SUCCESS){
					$this->state = STATE_LOGIN_FAIL;
					$this->message = $message;
				}
				break;
			}default:{
				break;
			}
		}
	}
	
	function OutMessageManager(){
		$this->state = STATE_SUCCESS;
		$this->message = "Fine, Let's continue...";
	}
	
	function getOutMessage(){
		global $inMsg;
		
		$rt_arr = array();
		switch($inMsg->params["type_of_message"]){
			///////////////////////////////////////////////////////////////////////////
			case REGISTER:{
				$rt_arr = array(
						$this->state,
						$this->message
						);
				break;
			}
			case LOGIN:{
				$rt_arr = array(
						$this->state,
						$this->message,
						$this->params["verify_code"]
						);
				break;
			}
			case FORGOT_PASSWORD:{
				$rt_arr = array(
						$this->state,
						$this->message
						);
				break;
			}
			case VIEW_USER_INFO:{
				if($inMsg->userid == UserManager::getUserID($inMsg->params["username"])){	//view info cua minh
					$rt_arr = array(
						$this->state,
						$this->message,
						$this->params["email"],
						$this->params["birthday"],
						$this->params["aim"],
						$this->params["yahoo"],
						$this->params["msn"],
						$this->params["skype"],
						$this->params["joindate"]
						);
				}else{	//view info cua other
					$rt_arr = array(
						$this->state,
						$this->message,
						$this->params["email"],
						$this->params["joindate"]
						);
				}
				break;
			}
			case UPDATE_USER_INFO:{
				$rt_arr = array(
						$this->state,
						$this->message
						);
				break;
			}
			case LOGOUT:{
				$rt_arr = array(
						$this->state,
						$this->message
						);
				break;
			}
			///////////////////////////////////////////////////////////////////////////
			case NEW_GAME:{
				$rt_arr = array(
						$this->state,
						$this->message,
						$this->params["state_of_game"],
						$this->params["username"]
						);
				break;
			}
			case GET_NEW_INFO_BITS:{
				$rt_arr = array(
						$this->state,
						$this->message,
						
						$this->params["new_info_bits"]
						);
				break;
			}
			case GET_LIST_GAME_STATUS:{
				$rt_arr = array(
						$this->state,
						$this->message,
						
						$this->params["number_of_waiting_games"],
						$this->params["number_of_inviting_games"],
						$this->params["number_of_invited_games"],
						$this->params["number_of_playing_games"],
						$this->params["number_of_ended_games"],
						
						$this->params["number_of_win_games"],
						$this->params["number_of_draw_games"],
						$this->params["number_of_lost_games"],
						$this->params["number_of_aborted_games"]
						);
				break;
			}
			case GET_LIST_GAME:{
				$rt_arr = array(
						$this->state,
						$this->message
						);
				if(is_array($this->params["game"])){
					foreach($this->params["game"] as $game){
						$rt_arr[] = $game;
					}
				}
				
				break;
			}
			case ACTION:{
				$rt_arr = array(
						$this->state,
						$this->message
						);
				break;
			}
			case GET_CURRENT_CHESS_BOARD_STATE:{
				$rt_arr = array(
						$this->state,
						$this->message
						);
				if(is_array($this->params["position"])){
					foreach($this->params["position"] as $position){
						$rt_arr[] = $position;
					}
				}
				break;
			}
			case GET_LASTEST_ACTIONS:{
				$rt_arr = array(
						$this->state,
						$this->message,
						$this->params["next_turn_username"]
						);
				if(is_array($this->params["action"])){
					foreach($this->params["action"] as $action){
						$rt_arr[] = $action;
					}
				}
				break;
			}
			///////////////////////////////////////////////////////////////////////////
			case ADD_FRIEND:{
				$rt_arr = array(
						$this->state,
						$this->message
						);
				break;
			}
			case DELETE_FRIEND:{
				$rt_arr = array(
						$this->state,
						$this->message
						);
				break;
			}
			case GET_FRIEND_LIST:{
				$rt_arr = array(
						$this->state,
						$this->message
						);
				if(is_array($this->params["friend"])){
					foreach($this->params["friend"] as $friend){
						$rt_arr[] = $friend;
					}
				}
				break;
			}
			default:{
				break;
			}
		}
		
		return implode(MF_SEPARATE, $rt_arr);
	}
	
	//function __destruct(){
	function outMessageController(){
		$str[STATE_SUCCESS] = "SUCCESS";
		$str[STATE_FAIL] = "<font color=red>FAIL</font>";
		$str[STATE_LOGIN_FAIL] = "<font color=red>LOGIN_FAIL</font>";
		
		if(isset($_GET["debug"]) && $_GET["debug"]=="false"){	//ko phai che do debug
			echo($this->getOutMessage());
		}else{	//che do DEBUG
			echo("</font><hr />State: {$str[$this->state]}<br />Message: {$this->message}<br />PARAMS<hr /><pre>");
			debug_message_r($this->params);
		}
	}
}
?>