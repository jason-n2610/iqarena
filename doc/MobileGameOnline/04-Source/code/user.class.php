<?php
defined('_PPCLink') or die('Restricted access');

class User{
	var $userid = null;
	var $username = null;
	var $password = null;
	var $new_info_bits = null;
	var $email = null;
	var $birthday = null;
	var $icq = null;
	var $aim = null;
	var $yahoo = null;
	var $msn = null;
	var $skype = null;
	var $joindate = null;
	var $salt = null;
	var $usergroupid = null;
	
	function display(){
		debug_message("{$this->userid}, {$this->username}, {$this->password},{$this->email}, {$this->birthday}, {$this->icq}, {$this->aim}, {$this->yahoo}, {$this->msn}, {$this->skype}, {$this->joindate}, {$this->salt}");
	}
	
	function User($userid){
		global $outMsg;
		
		$query = "SELECT * FROM ".FORUM_DB_PREFIX."user WHERE userid='{$userid}'";
		//echo($query);
		$result = mysql_query($query, $GLOBALS["link"]);
		checkQuerySuccess($result);
		if(mysql_num_rows($result)==1){	//Tim` thay User
			$row = mysql_fetch_array($result, MYSQL_ASSOC);
			$this->userid = $userid;
			$this->username = $row["username"];
			$this->password = $row["password"];
			$this->new_info_bits = $row["new_info_bits"];
			$this->email = $row["email"];
			$this->birthday = date(NORMAL_DATE_FORMAT , strtotime($row["birthday"]));
			$this->icq = $row["icq"];
			$this->aim = $row["aim"];
			$this->yahoo = $row["yahoo"];
			$this->msn = $row["msn"];
			$this->skype = $row["skype"];
			$this->joindate = date(NORMAL_DATE_FORMAT, $row["joindate"]);
			$this->salt = $row["salt"];
			$this->usergroupid = $row["usergroupid"];
		}else{
			$outMsg->updateState(STATE_FAIL, "Not found user in DB");
		}
	}
}
?>