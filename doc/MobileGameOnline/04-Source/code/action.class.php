<?php
defined('_PPCLink') or die('Restricted access');

class Action{

function makeStrAction($action){
	switch($action["type_of_action"]){
		case ACT_MOVE:{
			$rt = ($action["username"] .AF_SEPARATE. $action["type_of_action"] .AF_SEPARATE. $action["x_source"] .AF_SEPARATE. $action["y_source"] .AF_SEPARATE. $action["x_target"] .AF_SEPARATE. $action["y_target"]);
			break;
		}
		case ACT_DRAW:
		case ACT_TAKE_BACK:
		case ACT_RESIGN:
		case ACT_ABORT:{
			$rt = ($action["username"] .AF_SEPARATE. $action["type_of_action"] .AF_SEPARATE. $action["message"]);
			break;
		}
		case ACT_RETURN_DRAW:
		case ACT_RETURN_TAKE_BACK:{
			$rt = ($action["username"] .AF_SEPARATE. $action["type_of_action"] .AF_SEPARATE. $action["message"] .AF_SEPARATE. $action["return"]);
			break;
		}
		case ACT_END_GAME:{
			$rt = ($action["username"] .AF_SEPARATE. $action["type_of_action"]);
			break;
		}
		default:{
			break;
		}
	}
	return $rt;
}

function parseAction($strAction){	//return ARRAY
	$tmp = split(AF_SEPARATE, $strAction);
	$rt["username"] = $tmp[0];
	$rt["type_of_action"] = $tmp[1];
	
	switch($tmp[1]){
		case ACT_MOVE:{
			$rt["x_source"] = $tmp[2];
			$rt["y_source"] = $tmp[3];
			$rt["x_target"] = $tmp[4];
			$rt["y_target"] = $tmp[5];
			break;
		}
		case ACT_DRAW:{
			$rt["message"] = $tmp[2];
			break;
		}
		case ACT_RETURN_DRAW:{
			$rt["message"] = $tmp[2];
			$rt["return"] = $tmp[3];
			break;
		}
		case ACT_TAKE_BACK:{
			$rt["message"] = $tmp[2];
			break;
		}
		case ACT_RETURN_TAKE_BACK:{
			$rt["message"] = $tmp[2];
			$rt["return"] = $tmp[3];
			break;
		}
		case ACT_RESIGN:{
			$rt["message"] = $tmp[2];
			break;
		}
		case ACT_ABORT:{
			$rt["message"] = $tmp[2];
			break;
		}
		case ACT_END_GAME:{
			break;
		}
		default:{
			break;
		}
	}
	return $rt;
}
}
?>