<?php
defined('_PPCLink') or die('Restricted access');

class ChineseChess{
var $state_id = null;
var $number_of_actions = null;
var $actions_arr = null;

//	FUNCTIONS
function createInitState(){
	$pieces_arr[] = new Piece(CHINESE_CHESS_PIECE_CHARIOT, X_A, Y_1, COLOR_WHITE);
	$pieces_arr[] = new Piece(CHINESE_CHESS_PIECE_HORSE, X_B, Y_1, COLOR_WHITE);
	$pieces_arr[] = new Piece(CHINESE_CHESS_PIECE_ELEPHANT, X_C, Y_1, COLOR_WHITE);
	$pieces_arr[] = new Piece(CHINESE_CHESS_PIECE_ADVISOR, X_D, Y_1, COLOR_WHITE);
	$pieces_arr[] = new Piece(CHINESE_CHESS_PIECE_GENERAL, X_E, Y_1, COLOR_WHITE);
	$pieces_arr[] = new Piece(CHINESE_CHESS_PIECE_ADVISOR, X_F, Y_1, COLOR_WHITE);
	$pieces_arr[] = new Piece(CHINESE_CHESS_PIECE_ELEPHANT, X_G, Y_1, COLOR_WHITE);
	$pieces_arr[] = new Piece(CHINESE_CHESS_PIECE_HORSE, X_H, Y_1, COLOR_WHITE);
	$pieces_arr[] = new Piece(CHINESE_CHESS_PIECE_CHARIOT, X_I, Y_1, COLOR_WHITE);
	
	foreach($pieces_arr as $piece){
		$rt_arr[] = $piece->getStrPiece();
	}
	return implode(DBF_SEPARATE, $rt_arr);
}
function verifyMovement(){	return true;	//not finish
}
function getNextTurnUsername(){
	global $inMsg;
	
	$ccb = new ChineseChessBoard();
	$ccb->init();
	$ccb->runAllActions();
	
	$opponentID = UserManager::getOpponentID($inMsg->params["game_id"], UserManager::getUserID($ccb->moveActions_arr[count($ccb->moveActions_arr) - 1]["username"]));
	return UserManager::getUsername($opponentID);
}
/*
 *@user
 *@inMsg
 *@outMsg
 */
function move(){	//finished
	global $user, $inMsg, $outMsg;
	
	if($this->state_id == STATE_PLAYING){
		if(!($this->number_of_actions) || (($this->number_of_actions) && (($this->actions_arr[$this->number_of_actions - 1]["type_of_action"] == ACT_MOVE) || ($this->actions_arr[$this->number_of_actions - 1]["type_of_action"] == ACT_RETURN_DRAW) || ($this->actions_arr[$this->number_of_actions - 1]["type_of_action"] == ACT_RETURN_TAKE_BACK)))){
			$this->writeActionToDatabase();
		}else{
			$outMsg->updateState(STATE_FAIL, "This action can't excutable at moment! Please try again!");
		}
	}else{
		$outMsg->updateState(STATE_FAIL, "Database Error! Please try again later.");
	}
}
function draw(){	//finished
	global $user, $inMsg, $outMsg;
	
	if($this->state_id == STATE_PLAYING){
		if(!($this->number_of_actions) || (($this->number_of_actions) && ($this->actions_arr[$this->number_of_actions - 1]["type_of_action"] == ACT_MOVE))){
			$this->writeActionToDatabase();
		}else{
			$outMsg->updateState(STATE_FAIL, "This action can't excutable at moment! Please try again!");
		}
	}else{
		$outMsg->updateState(STATE_FAIL, "Database Error! Please try again later.");
	}
}
function returnDraw(){	//finished
	global $user, $inMsg, $outMsg;
	
	if($this->state_id == STATE_PLAYING){
		if($this->actions_arr[$this->number_of_actions - 1]["type_of_action"] == ACT_DRAW){
			$this->writeActionToDatabase();
		}else{
			$outMsg->updateState(STATE_FAIL, "This action can't excutable at moment! Please try again!");
		}
	}else{
		$outMsg->updateState(STATE_FAIL, "Database Error! Please try again later.");
	}
}
function takeBack(){	//finished
	global $user, $inMsg, $outMsg;
	
	if($this->state_id == STATE_PLAYING){
		if($this->actions_arr[$this->number_of_actions - 1]["type_of_action"] == ACT_MOVE){
			$this->writeActionToDatabase();
		}else{
			$outMsg->updateState(STATE_FAIL, "This action can't excutable at moment! Please try again!");
		}
	}else{
		$outMsg->updateState(STATE_FAIL, "Database Error! Please try again later.");
	}
}
function returnTakeBack(){	//finished
	global $user, $inMsg, $outMsg;
	
	if($this->state_id == STATE_PLAYING){
		if($this->actions_arr[$this->number_of_actions - 1]["type_of_action"] == ACT_TAKE_BACK){
			$this->writeActionToDatabase();
		}else{
			$outMsg->updateState(STATE_FAIL, "This action can't excutable at moment! Please try again!");
		}
	}else{
		$outMsg->updateState(STATE_FAIL, "Database Error! Please try again later.");
	}
}
function abort(){	//finished
	global $user, $inMsg, $outMsg;
	
	if($this->state_id == STATE_PLAYING){
		if(!($this->number_of_actions) || (($this->number_of_actions) && (($this->actions_arr[$this->number_of_actions - 1]["type_of_action"] == ACT_MOVE) || ($this->actions_arr[$this->number_of_actions - 1]["type_of_action"] == ACT_RETURN_DRAW) || ($this->actions_arr[$this->number_of_actions - 1]["type_of_action"] == ACT_RETURN_TAKE_BACK)))){
			//not write to actions line. Update state only
			$this->writeActionToDatabase();
			
			$query = "SELECT * FROM ".GAME_DB_PREFIX."game WHERE game_id='{$inMsg->params["game_id"]}'";
			$result = mysql_query($query, $GLOBALS["link"]);
			checkQuerySuccess($result);
			$row = mysql_fetch_array($result, MYSQL_ASSOC);
			
			$yourid = $user->userid;
			$friendid = ($yourid == $row["userid1"])?$row["userid2"]:$row["userid1"];
			
			$query_arr = array(
					"state_id" => STATE_ABORTED,
					"userid_abort" => $yourid,
					"end_at" => date(NORMAL_TIME_FORMAT),
					"note" => "Player {$user->username} has aborted!"
					);
			$query = makeUpdateQuery(GAME_DB_PREFIX."game", $query_arr, "game_id='{$inMsg->params["game_id"]}'");
			$result = mysql_query($query, $GLOBALS["link"]);
			checkQuerySuccess($result);
			
		}else{
			$outMsg->updateState(STATE_FAIL, "This action can't excutable at moment! Please try again!");
		}
	}else{
		$outMsg->updateState(STATE_FAIL, "Database Error! Please try again later.");
	}
}
function resign(){	//finished
	global $user, $inMsg, $outMsg;
	
	if($this->state_id == STATE_PLAYING){
		if(!($this->number_of_actions) || (($this->number_of_actions) && (($this->actions_arr[$this->number_of_actions - 1]["type_of_action"] == ACT_MOVE) || ($this->actions_arr[$this->number_of_actions - 1]["type_of_action"] == ACT_RETURN_DRAW) || ($this->actions_arr[$this->number_of_actions - 1]["type_of_action"] == ACT_RETURN_TAKE_BACK)))){
			//not write to actions line. Update state only
			$this->writeActionToDatabase();
			
			$query = "SELECT * FROM ".GAME_DB_PREFIX."game WHERE game_id='{$inMsg->params["game_id"]}'";
			$result = mysql_query($query, $GLOBALS["link"]);
			checkQuerySuccess($result);
			$row = mysql_fetch_array($result, MYSQL_ASSOC);
			
			$yourid = $user->userid;
			$friendid = ($yourid == $row["userid1"])?$row["userid2"]:$row["userid1"];
			
			$query_arr = array(
					"state_id" => STATE_RESIGN,
					"userid_win" => $friendid,
					"end_at" => date(NORMAL_TIME_FORMAT),
					"note" => "Player {$user->username} has resigned!"
					);
			$query = makeUpdateQuery(GAME_DB_PREFIX."game", $query_arr, "game_id='{$inMsg->params["game_id"]}'");
			$result = mysql_query($query, $GLOBALS["link"]);
			checkQuerySuccess($result);
			
		}else{
			$outMsg->updateState(STATE_FAIL, "This action can't excutable at moment! Please try again!");
		}
	}else{
		$outMsg->updateState(STATE_FAIL, "Database Error! Please try again later.");
	}
}
function endGame(){	//finished, ben win send action nay` len server
	global $user, $inMsg, $outMsg;
	
	if($this->state_id == STATE_PLAYING){
		if(!($this->number_of_actions) || (($this->number_of_actions) && (($this->actions_arr[$this->number_of_actions - 1]["type_of_action"] == ACT_MOVE) || ($this->actions_arr[$this->number_of_actions - 1]["type_of_action"] == ACT_RETURN_DRAW) || ($this->actions_arr[$this->number_of_actions - 1]["type_of_action"] == ACT_RETURN_TAKE_BACK)))){
			//not write to actions line. Update state only
			$this->writeActionToDatabase();
			
			$query_arr = array(
					"state_id" => STATE_ENDED,
					"userid_win" => $yourid,
					"end_at" => date(NORMAL_TIME_FORMAT)
					);
			$query = makeUpdateQuery(GAME_DB_PREFIX."game", $query_arr, "game_id='{$inMsg->params["game_id"]}'");
			$result = mysql_query($query, $GLOBALS["link"]);
			checkQuerySuccess($result);
			
		}else{
			$outMsg->updateState(STATE_FAIL, "This action can't excutable at moment! Please try again!");
		}
	}else{
		$outMsg->updateState(STATE_FAIL, "Database Error! Please try again later.");
	}
}
function getLastestGameActions(){	//finished
	global $user, $inMsg, $outMsg;
	
	for($i=($inMsg->params["n"] - 1); $i<count($this->actions_arr); $i++){
		$outMsg->params["action"][] = Action::makeStrAction($this->actions_arr[$i]);
	}
	
	$outMsg->params["next_turn_username"] = (ChineseChess::getNextTurnUsername());
	if($outMsg->state == STATE_SUCCESS){
		deleteANewInfoBit($user->userid, BIT_NEW_ACTION);
	}
}
function getCurrentChessBoardState(){	//finished
	global $user, $inMsg, $outMsg;
	
	$ccb = new ChineseChessBoard();
	$ccb->init();
	$ccb->runAllActions();
	
	for($i=1; $i<=CHINESE_CHESS_MAX_COLUMN; $i++){
		for($j=1; $j<=CHINESE_CHESS_MAX_ROW; $j++){
			if(isset($ccb->square[$i][$j])){
				$outMsg->params["position"][] = $ccb->square[$i][$j]->type .ISF_SEPARATE. $ccb->square[$i][$j]->x .ISF_SEPARATE. $ccb->square[$i][$j]->y .ISF_SEPARATE. $ccb->square[$i][$j]->color;
			}
		}
	}
}
//	CONTROLLERS

/*
 *@user
 *@inMsg
 *@outMsg
 */
function actionController(){	//finished
	global $user, $inMsg, $outMsg;
	
	switch($inMsg->params["action"]["type_of_action"]){
		case ACT_MOVE:{
			if($this->verifyMovement()){	//moveable
				$this->move();
			}else{
				$outMsg->updateState(STATE_FAIL, "Invalid movement!");
			}
			
			break;
		}
		case ACT_DRAW:{
			$this->draw();
			break;
		}
		case ACT_RETURN_DRAW:{
			$this->returnDraw();
			break;
		}
		case ACT_TAKE_BACK:{
			$this->takeBack();
			break;
		}
		case ACT_RETURN_TAKE_BACK:{
			$this->returnTakeBack();
			break;
		}
		case ACT_RESIGN:{
			$this->resign();
			break;
		}
		case ACT_ABORT:{
			$this->abort();
			break;
		}
		case ACT_END_GAME:{
			$this->endGame();
			break;
		}
		default:{
			$outMsg->updateState(STATE_FAIL, "Invalid Action!");
		}
	}
	
	if($outMsg->state == STATE_SUCCESS){
		addANewInfoBit(UserManager::getOpponentID($inMsg->params["game_id"], $userid), BIT_NEW_ACTION);
	}
}

//------------------
function writeActionToDatabase(){
	global $user, $inMsg, $outMsg;
	
	$all_actions = "";
	for($i=0; $i<count($this->actions_arr); $i++){
		if($i==0){
			$all_actions .= Action::makeStrAction($this->actions_arr[$i]);
		}else{
			$all_actions .= DBF_SEPARATE. Action::makeStrAction($this->actions_arr[$i]);
		}
	}
	if($all_actions){
		$all_actions .= DBF_SEPARATE. Action::makeStrAction($inMsg->params["action"]);
	}else{
		$all_actions .= Action::makeStrAction($inMsg->params["action"]);
	}
	
	$query_arr = array(
			"number_of_actions" => ++ $this->number_of_actions,
			"actions" => $all_actions
			);
	$query = makeUpdateQuery(GAME_DB_PREFIX."game", $query_arr, "game_id='{$inMsg->params["game_id"]}'");
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
}

/*
 *@user
 *@inMsg
 *@outMsg
 */
function ChineseChess(){
	global $user, $inMsg, $outMsg;
	
	$query = "SELECT * FROM ".GAME_DB_PREFIX."game WHERE game_id='{$inMsg->params["game_id"]}'";
	debug_message($query);
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
	if(mysql_num_rows($result)==1){
		$row = mysql_fetch_array($result, MYSQL_ASSOC);
		$this->number_of_actions = $row["number_of_actions"];
		$this->state_id = $row["state_id"];
		
		$tmp = split(DBF_SEPARATE, $row["actions"]);
		if(is_array($tmp)){
			foreach($tmp as $act){
				$this->actions_arr[] = Action::parseAction($act);
			}
		}
	}else{
		$outMsg->updateState(STATE_FAIL, "Database Error! Please try again later.");
	}
}

function __destruct(){
	/*debug_message("<pre>{$this->number_of_actions}");
	debug_message_r($this->actions_arr);*/
}
}
?>
<?php
class ChineseChessBoard{
var $square = array();
var $moveActions_arr = array();

function runAllActions(){	//finished
	global $gameInstance;
	
	if(is_array($gameInstance->actions_arr)){
		foreach($gameInstance->actions_arr as $action){
			$this->runAction($action);
		}
	}
}
function runAction($action){	//finished
	switch($action["type_of_action"]){	//finished
		case ACT_MOVE:{
			$this->moveActions_arr[] = $action;
			if(isset($this->square[$action["x_source"]][$action["y_source"]])){
				$this->square[$action["x_target"]][$action["y_target"]] = $this->square[$action["x_source"]][$action["y_source"]];
				$this->square[$action["x_target"]][$action["y_target"]]->x = $action["x_target"];
				$this->square[$action["x_target"]][$action["y_target"]]->y = $action["y_target"];
				unset($this->square[$action["x_source"]][$action["y_source"]]);
			}
			break;
		}
		case ACT_RETURN_TAKE_BACK:{	//finished
			if($action["return"] == ACCEPT){
				//revert last action
				$lastMoveAction = $this->moveActions_arr[count($this->moveActions_arr) - 1];
				unset($this->moveActions_arr[count($this->moveActions_arr) - 1]);
				
				if(isset($this->square[$action["x_target"]][$action["y_target"]])){
					$this->square[$action["x_source"]][$action["y_source"]] = $this->square[$action["x_target"]][$action["y_target"]];
					$this->square[$action["x_source"]][$action["y_source"]]->x = $action["x_source"];
					$this->square[$action["x_source"]][$action["y_source"]]->y = $action["y_source"];
					unset($this->square[$action["x_target"]][$action["y_target"]]);
				}
			}
			break;
		}
		default:{
			break;
		}
	}
}


function init(){	//finished
	global $user, $inMsg, $outMsg, $gameInstance;
	
	//read from database
	$query = "SELECT * FROM ".GAME_DB_PREFIX."game WHERE game_id='{$inMsg->params["game_id"]}'";
	$result = mysql_query($query, $GLOBALS["link"]);
	$row = mysql_fetch_array($result, MYSQL_ASSOC);
	
	$this->actions_arr = $gameInstance->actions_arr;
	
	//read init state to SQUARE[x][y]
	$tmp_arr = split(DBF_SEPARATE, $row["init_state"]);
	if(is_array($tmp_arr)){
		foreach($tmp_arr as $tmp){
			$tmp = split(ISF_SEPARATE, $tmp);
			$type = $tmp[0];
			$x = $tmp[1];
			$y = $tmp[2];
			$color = $tmp[3];
			
			$this->square[$x][$y] = new Piece($type, $x, $y, $color);
		}
	}
}

function __destruct(){
	debug_message("<br /><pre>");
	debug_message_r($this->square);
}
}

class Piece{
var $x, $y;
var $color;
var $type;

function Piece($type, $x, $y, $color){
	$this->type = $type;
	$this->x = $x;
	$this->y = $y;
	$this->color = $color;
}

function getStrPiece(){	//return STRING:	type:x:y:color
	return ($this->type .AF_SEPARATE. $this->x .AF_SEPARATE. $this->y .AF_SEPARATE. $this->color);
}
}
?>