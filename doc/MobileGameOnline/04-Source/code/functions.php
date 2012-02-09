<?php
defined('_PPCLink') or die('Restricted access');

function makeInsertQuery($table, $arr){	//Tao Insert Query
	$tmp1 = "";
	$tmp2 = "";
	$keys = array_keys($arr);
	debug_message_r($keys);
	
	for($i=0; $i<count($arr); $i++){
		if($i==0){	//Lan dau tien
			$tmp1 .= "{$keys[$i]}";	//Key
			$tmp2 .= "'{$arr[$keys[$i]]}'";
		}else{
			$tmp1 .= ", {$keys[$i]}";	//Key
			$tmp2 .= ", '{$arr[$keys[$i]]}'";
		}
	}
	$query = "INSERT INTO {$table}({$tmp1}) VALUES ({$tmp2})";
	
	debug_message($query);
	return $query;
}

function makeUpdateQuery($table, $arr, $condition){	//Tao Update Query
	$tmp = "";
	$keys = array_keys($arr);
	debug_message_r($keys);
	
	for($i=0; $i<count($arr); $i++){
		if($i==0){	//Lan dau tien
			$tmp .= "{$keys[$i]}='{$arr[$keys[$i]]}'";
		}else{
			$tmp .= ", {$keys[$i]}='{$arr[$keys[$i]]}'";
		}
	}
	$query = "UPDATE {$table} SET {$tmp} WHERE {$condition}";
	debug_message($query);
	
	return $query;
}

function checkQuerySuccess($result){
	global $outMsg;
	
	if(!$result){
		$outMsg->updateState(STATE_FAIL, "Can't query to Database");
		return false;
	}
	return true;
}

function debug_message($str){
	global $_GET;
	if(isset($_GET["debug"]) && $_GET["debug"]=="false"){
	}else{
		echo($str);
	}
}

function debug_message_r($object){
	global $_GET;
	if(isset($_GET["debug"]) && $_GET["debug"]=="false"){
	}else{
		print_r($object);
	}
}
function send_mail($email, $subject, $message){
	// To send HTML mail, the Content-type header must be set
	$headers  = "MIME-Version: 1.0" . "\r\n";
	$headers .= "Content-type: text/html; charset=iso-8859-1" . "\r\n";

	// Additional headers
	//$headers .= "To: {$email}" . "\r\n";
	$headers .= "From: ". ADMIN_EMAIL . "\r\n";
	//$headers .= "Cc: birthdayarchive@example.com" . "\r\n";
	//$headers .= "Bcc: birthdaycheck@example.com" . "\r\n";

	// Mail it
	mail($email, $subject, $message, $headers);
}


function randomChar(){
	$s = rand(1,5);
	if($s==1){
		return rand(0, 9);
	}else{
		return chr(rand(97, 122));
	}
}

function randomString($n){
	$rt = "";
	for($i=0; $i<$n; $i++){
		$rt .= randomChar();
	}
	return $rt;
}

function addANewInfoBit($userid, $type_of_new_info){		//Thêm các bits báo hiệu NEW
	global $user;
	
	if($userid == $user->userid){
		$user->new_info_bits = ($user->new_info_bits | (1 << $type_of_new_info));	//Phép OR
		$query_arr = array("new_info_bits" => $user->new_info_bits);
	}else{
		$opponent = new User($userid);
		$opponent->new_info_bits = ($opponent->new_info_bits | (1 << $type_of_new_info));	//Phép OR
		$query_arr = array("new_info_bits" => $opponent->new_info_bits);
	}
	
	$query = makeUpdateQuery(FORUM_DB_PREFIX."user", $query_arr, "userid='{$userid}'");
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
}
function deleteANewInfoBit($userid, $type_of_new_info){	//Xóa bit báo hiệu NEW về 0
	global $user;
	
	if($userid == $user->userid){
		$user->new_info_bits = ($user->new_info_bits & (~(1 << $type_of_new_info)));	//Phép AND
		$query_arr = array("new_info_bits" => $user->new_info_bits);
	}else{
		$opponent = new User($userid);
		$opponent->new_info_bits = ($opponent->new_info_bits & (~(1 << $type_of_new_info)));	//Phép AND
		$query_arr = array("new_info_bits" => $opponent->new_info_bits);
	}
	
	$query = makeUpdateQuery(FORUM_DB_PREFIX."user", $query_arr, "userid='{$userid}'");
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
}
?>