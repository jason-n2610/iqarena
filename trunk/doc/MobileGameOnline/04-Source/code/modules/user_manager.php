<?php
defined('_PPCLink') or die('Restricted access');

class UserManager{

//	FUNCTIONS

function getUserID($username){
	$query = "SELECT * FROM ".FORUM_DB_PREFIX."user WHERE username='{$username}'";
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
	$row = mysql_fetch_array($result, MYSQL_ASSOC);
	return $row["userid"];
}
function getUsername($userid){
	$query = "SELECT * FROM ".FORUM_DB_PREFIX."user WHERE userid='{$userid}'";
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
	$row = mysql_fetch_array($result, MYSQL_ASSOC);
	return $row["username"];
}
function getOpponentID($game_id, $your_id){
	$query = "SELECT * FROM ".GAME_DB_PREFIX."game WHERE game_id='{$game_id}'";
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
	$row = mysql_fetch_array($result, MYSQL_ASSOC);
	$opponent_id = ($your_id == $row["userid1"])?$row["userid2"]:$row["userid1"];
	
	return $opponent_id;
}
/**
 *----- IN -----
 * @username
 *
 *----- OUT -----
 * @return: true/false
 */
public function checkExistAccount($username){		//finished
	$query = "SELECT count(*) as count FROM ".FORUM_DB_PREFIX."user WHERE username='{$username}'";
	//echo($query);
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
	$row = mysql_fetch_array($result, MYSQL_ASSOC);
	
	if($row["count"]==1){
		return true;
	}else{
		return false;
	}
}


/**
 *----- IN -----
 * @username
 *
 *----- OUT -----
 * @return: true/false
 */
public function checkExistEmail($email){		//finished
	$query = "SELECT count(*) as count FROM ".FORUM_DB_PREFIX."user WHERE email='{$email}'";
	//echo($query);
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
	$row = mysql_fetch_array($result, MYSQL_ASSOC);
	
	if($row["count"]==1){
		return true;
	}else{
		return false;
	}
}


/**
 *----- IN -----
 * @email: Email dich
 * @type: TP_ACTIVE (Send Active Mail), TP_CHANGE_PASSWORD (Send Change Password Mail)
 * @inMsg
 * @outMsg
 * @return: true/false
 */
public function sendMail($email, $type){	//....................
	global $inMsg, $outMsg;
	
	switch($type){
		case TP_ACTIVE:{		//finished
			$subject = "Activation Require at PPCLink";
			$message = "
					Dear {$inMsg->params["username"]},<br />
					Thank you for registering at the Forums. Before we can activate your account one last step must be taken to complete your registration.<br />
					<br />
					Please note - you must complete this last step to become a registered member. You will only need to visit this URL once to activate your account.<br />
					<br />
					To complete your registration, please visit this URL:<br />
					".FORUM_URL."/register.php?a=act&u={$inMsg->others["userid"]}&i={$inMsg->others["activation_code"]}<br />
					<br />
					<a href=\"".FORUM_URL."/register.php?a=act&u={$inMsg->others["userid"]}&i={$inMsg->others["activation_code"]}\">America Online Users Please Visit Here to be Activated</a><br />
					<br />
					**** Does The Above URL Not Work? ****<br />
					If the above URL does not work, please use your Web browser to go to:<br />
					".FORUM_URL."/register.php?a=ver<br />
					<br />
					Please be sure not to add extra spaces. You will need to type in your username and activation number on the page that appears when you visit the URL.<br />
					<br />
					Your Username is: {$inMsg->params["username"]}<br />
					Your Activation ID is: {$inMsg->others["activation_code"]}<br />
					<br />
					If you are still having problems signing up please contact a member of our support staff at ".ADMIN_EMAIL."<br />
					<br />
					All the best,<br />
					PPCLink - Software Company<br />
					";	//Not Finished
			send_mail($email, $subject, $message);
			break;
		}
		case TP_CHANGE_PASSWORD:{
			$subject = "Change Password at PPCLink";
			$message = "
					Dear {$inMsg->params["username"]},<br />
					<br />
					You have requested to reset your password on Game Online because you have forgotten your password. If you did not request this, please ignore it. It will expire and become useless in 24 hours time.<br />
					<br />
					To reset your password, please visit the following page:<br />
					".FORUM_URL."/login.php?a=pwd&u={$inMsg->others["userid"]}&i={$inMsg->others["change_password_code"]}<br />
					<br />
					When you visit that page, your password will be reset, and the new password will be emailed to you.<br />
					<br />
					Your username is: {$inMsg->params["username"]}<br />
					<br />
					To edit your profile, go to this page:<br />
					".FORUM_URL."/profile.php?do=editprofile<br />
					<br />
					All the best,<br />
					PPCLink - Software Company<br />
					";	//Not Finished
			send_mail($email, $subject, $message);
			break;
		}
		default:{
			$outMsg->updateState(STATE_FAIL, "Can't send mail because not found type of email!");
			break;
		}
	}
	return false;
}


/**
 *----- IN -----
 * @
 * @
 *
 *----- OUT -----
 * @return - true:
 *         - false: Message
 */
public function createActivationInfo(){	//return Link of Activation request
	
	$query = "SELECT * FROM ".FORUM_DB_PREFIX."useractivation WHERE userid='{$userid}' and activationid='{$activationId}'";
	//echo($query);
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
	$row = mysql_fetch_array($result, MYSQL_ASSOC);
	
	//Active Account
	$query = "UPDATE ".FORUM_DB_PREFIX."user SET usergroupid='{$row["usergroupid"]}' WHERE userid='{$row["userid"]}'";
	if(mysql_query($query, $GLOBALS["link"])){
		return true;
	}else{
		return false;
	}
}


/**
 *----- IN -----
 * @
 * @
 *
 *----- OUT -----
 * @return - true:
 *         - false: Message
 */
public function changePassword(){
}


/**
 *----- IN -----
 * @
 * @
 *
 *----- OUT -----
 * @return - true:
 *         - false: Message
 */
public function checkLoggedIn($userid){		//finished
	$query = "SELECT * FROM ".GAME_DB_PREFIX."login WHERE userid='{$userid}' and available='1'";
	//echo($query);
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
	if(mysql_num_rows($result)){	//Not Logged Out
		$row = mysql_fetch_array($result, MYSQL_ASSOC);
		
		$curr_t = strtotime(date(NORMAL_TIME_FORMAT));
		$last_access_t = strtotime($row["last_access_at"]);
		$delta_t = $curr_t - $last_access_t;
		if($delta_t>=0 && $delta_t<=MAX_LOGIN_TIME){		//Now is Logged In
			return true;
		}else{		//Logged out boi vi` qua MAX_LOGIN_TIME ma`nguoi dung` ko request den server 1 yeu cau nao`
			UserManager::logout($userid);
			return false;
		}
	}else{	//Logged Out
		return false;
	}
}


/**
 * @user
 * @inMsg: Message tu` Client
 * @outMsg: Message se gui den client
 */
public function updateUserInfo(){		//finished
	global $user, $inMsg, $outMsg;
	
	if((UserManager::checkExistEmail($inMsg->params["email"])) && ($user->email != $inMsg->params["email"])){	//neu email nay` la` cua nguoi khac dung
		$outMsg->updateState(STATE_FAIL, "This email is exist. Please try another email!");
		return false;
	}
	
	$query_arr = array(
					"email" => $inMsg->params["email"],
					"birthday" => $inMsg->params["birthday"],
					"icq" => $inMsg->params["icq"],
					"aim" => $inMsg->params["aim"],
					"yahoo" => $inMsg->params["yahoo"],
					"msn" => $inMsg->params["msn"],
					"skype" => $inMsg->params["skype"]
					);
	
	if($inMsg->params["old_password"] != "d41d8cd98f00b204e9800998ecf8427e"){	//Change Password
		if($inMsg->params["new_password"] != "d41d8cd98f00b204e9800998ecf8427e"){
			//	Compare with Oldpassword
			$query = "SELECT password, salt FROM ".FORUM_DB_PREFIX."user WHERE userid='{$user->userid}'";
			//echo($query);
			$result = mysql_query($query, $GLOBALS["link"]);
			checkQuerySuccess($result);
			$row = mysql_fetch_array($result, MYSQL_ASSOC);
			if($row["password"]==md5($inMsg->params["old_password"] . $row["salt"])){		//Pass trong DB = md5(md5($pass) . $salt)
				$query_arr["password"] = md5($inMsg->params["new_password"] . $row["salt"]);
			}else{
				$outMsg->updateState(STATE_FAIL, "Your Old Password Invalid!");
				return false;
			}
		}else{
			$outMsg->updateState(STATE_FAIL, "Please enter your new password!");
			return false;
		}
	}
	$query = makeUpdateQuery(FORUM_DB_PREFIX."user", $query_arr, "userid='{$user->userid}'");
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
	$outMsg->updateState(STATE_SUCCESS, "Updated your account informations!");
	return true;
}

/**
 *----- IN -----
 * @user (CLASS): User dang truy cap
 * @friend (CLASS): Nguoi ma` ban muon xem thong tin
 *
 *----- OUT -----
 * @outMsg: Cap nhat out message
 */
public function getUserInfo(){		//finished
	global $user, $friend, $outMsg;
	
	if($user->userid == $friend->userid){	//Xem thong tin cua chinh minh
		$outMsg->params["email"] = $friend->email;
		$outMsg->params["birthday"] = $friend->birthday;
		$outMsg->params["icq"] = $friend->icq;
		$outMsg->params["aim"] = $friend->aim;
		$outMsg->params["yahoo"] = $friend->yahoo;
		$outMsg->params["msn"] = $friend->msn;
		$outMsg->params["skype"] = $friend->skype;
		$outMsg->params["joindate"] = $friend->joindate;
	}else{
		$outMsg->params["email"] = $friend->email;
		$outMsg->params["joindate"] = $friend->joindate;
	}
}

//OTHERS FUNCTIONS
/**
 *@userid
 *@outMsg
 */
public function logout($userid){		//finished
	global $outMsg;
	
	$query = "UPDATE ".GAME_DB_PREFIX."login SET available='0' WHERE userid='{$userid}' and available='1'";
	//echo($query);
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
	if(mysql_affected_rows($GLOBALS["link"])==1){	//Logout thanh cong
		$outMsg->updateState(STATE_SUCCESS, "Logged out!");
		return true;
	}else{	//Co loi xuat hien
		$outMsg->updateState(STATE_FAIL, "Can't logout. Please try again.");
		return false;
	}
}

//	CONTROLLER
/**
 * @inMsg: Message tu` Client
 * @outMsg: Message se gui den client
 */
public function registerController(){		//finished
	global $inMsg,$outMsg;
	
	if(UserManager::checkExistAccount($inMsg->params["username"])){	//Account ton tai, Register FAIL
		$outMsg->updateState(STATE_FAIL, "Username '{$inMsg->params["username"]}' is exist! Try another username");
	}else if(UserManager::checkExistEmail($inMsg->params["email"])){	//Email ton tai, Register FAIL
		$outMsg->updateState(STATE_FAIL, "Email '{$inMsg->params["email"]}' is exist! Try another email");
	}else if($inMsg->params["password"]=="d41d8cd98f00b204e9800998ecf8427e"){	//password is null
		$outMsg->updateState(STATE_FAIL, "Please, enter password");
	}else if(!$inMsg->params["username"] || !$inMsg->params["email"]){
		$outMsg->updateState(STATE_FAIL, "Please, fill your informations before press submit button");
	}else if(strlen($inMsg->params["username"])>90){
		$outMsg->updateState(STATE_FAIL, "Sorry, but your username too long! Please try another.");
	}else if(strlen($inMsg->params["email"])>90){
		$outMsg->updateState(STATE_FAIL, "Sorry, but your email too long!");
	}else if($inMsg->params["birthday"] && !ereg("([0-9]{1,2})-([0-9]{1,2})-([0-9]{4})", $inMsg->params["birthday"])){
		echo("{{$inMsg->params["birthday"]}}");
		$outMsg->updateState(STATE_FAIL, "Invalid date format in birthday field!");
	}else{	//Set database & Send mail
		//get Next userid
		$query = "SELECT max(userid) as max_userid FROM ".FORUM_DB_PREFIX."user";
		//echo($query);
		$result = mysql_query($query, $GLOBALS["link"]);
		checkQuerySuccess($result);
		$row = mysql_fetch_array($result, MYSQL_ASSOC);
		$next_userid = $row["max_userid"] + 1;
		
		$salt = randomString(3);
		
		$query_arr = array(
						"userid" => $next_userid,
						"username" => $inMsg->params["username"],
						"usergroupid" => DEFAUL_ACTIVE_GROUP_ID,
						"password" => md5($inMsg->params["password"] .$salt),
						"email" => $inMsg->params["email"],
						"birthday" => $inMsg->params["birthday"],
						"icq" => $inMsg->params["icq"],
						"aim" => $inMsg->params["aim"],
						"yahoo" => $inMsg->params["yahoo"],
						"msn" => $inMsg->params["msn"],
						"skype" => $inMsg->params["skype"],
						"joindate" => mktime(),
						"salt" => $salt,
						"showbirthday" => DEFAUL_SHOW_BIRTHDAY,
						"usertitle" => DEFAUL_USER_TITLE,
						"reputationlevelid" => DEFAULT_REPUTATION_LEVEL_ID
						);
		$query = makeInsertQuery(FORUM_DB_PREFIX."user", $query_arr);
		$result1 = mysql_query($query, $GLOBALS["link"]);
		
		$query_arr = array("userid" => $next_userid);
		$query = makeInsertQuery(FORUM_DB_PREFIX."usertextfield", $query_arr);
		$result2 = mysql_query($query, $GLOBALS["link"]);
		
		$query_arr = array("userid" => $next_userid);
		$query = makeInsertQuery(FORUM_DB_PREFIX."userfield", $query_arr);
		$result3 = mysql_query($query, $GLOBALS["link"]);
		
		/*	to sendmail	*/
		$inMsg->others["userid"] = $next_userid;
		$inMsg->others["activation_code"] = randomString(ACTIVATION_CODE_LENGTH);
		
		$query = "SELECT max(useractivationid) as max_useractivationid FROM ".FORUM_DB_PREFIX."useractivation";
		$result = mysql_query($query, $GLOBALS["link"]);
		checkQuerySuccess($result);
		$row = mysql_fetch_array($result, MYSQL_ASSOC);
		$next_useractivationid = $row["max_useractivationid"] + 1;
		$query_arr = array(
						"useractivationid" => $next_useractivationid,
						"userid" => $next_userid,
						"dateline" => mktime(),
						"activationid" => $inMsg->others["activation_code"],
						"type" => ACTIVATION_TYPE,
						"usergroupid" => DEFAUL_USER_GROUP_ID
						);
		$query = makeInsertQuery(FORUM_DB_PREFIX."useractivation", $query_arr);
		$result4 = mysql_query($query, $GLOBALS["link"]);
		
		if(!$result1 || !$result2 || !$result3 || !$result4){
			$outMsg->updateState(STATE_FAIL, "Can't query to Database");
		}
		
		//success
		UserManager::sendMail($inMsg->params["email"], TP_ACTIVE);
		$outMsg->updateState(STATE_SUCCESS, "Your account has been created. An email has been sent to email {$inMsg->params["email"]}. Check your email, and click on active link to active your account!");
	}
}


/**
 * @inMsg: Message tu` Client
 * @outMsg: Message se gui den client
 */
public function loginController(){		//finished
	global $inMsg, $outMsg;
	
	if($inMsg->params["username"] != ""){
		$query = "SELECT * FROM ".FORUM_DB_PREFIX."user WHERE username='{$inMsg->params["username"]}'";
		$result = mysql_query($query, $GLOBALS["link"]);
		checkQuerySuccess($result);
		if(mysql_num_rows($result)){	//Co ton tai user trong database
			$row = mysql_fetch_array($result, MYSQL_ASSOC);
			$user = new User($row["userid"]);
			
			if(md5($inMsg->params["password"] . $user->salt) == $user->password){	//Kiem tra password co dung ko
				if($user->usergroupid != DEFAUL_ACTIVE_GROUP_ID){
					$verify_code = str_repeat("-", (strlen(MAX_USER) + 1) - strlen($user->userid)) . $user->userid . randomString(VERIFY_CODE_LENGTH - (strlen(MAX_USER) + 1));
					$curr_t = date(NORMAL_TIME_FORMAT);
					
					//Kiem tra da tao du lieu cho user nay bang __LOGIN chua?
					$query = "SELECT * FROM ".GAME_DB_PREFIX."login WHERE userid='{$user->userid}'";
					//echo($query);
					$result = mysql_query($query, $GLOBALS["link"]);
					checkQuerySuccess($result);
					
					if(mysql_num_rows($result)){	//Da co trong DB, --> update
						$query_arr = array(
									"verify_code" => $verify_code,
									"last_login_at" => $curr_t,
									"last_access_at" => $curr_t,
									"available" => true
									);
						$query = makeUpdateQuery(GAME_DB_PREFIX ."login", $query_arr, "userid='{$user->userid}'");
						mysql_query($query, $GLOBALS["link"]);
						checkQuerySuccess($result);
					}else{	//Chua co trong DB, --> create
						$query_arr = array(
									"userid" => $user->userid,
									"verify_code" => $verify_code,
									"last_login_at" => $curr_t,
									"last_access_at" => $curr_t,
									"available" => true
									);
						$query = makeInsertQuery(GAME_DB_PREFIX ."login", $query_arr);
						$result = mysql_query($query, $GLOBALS["link"]);
						checkQuerySuccess($result);
					}
					$outMsg->params["verify_code"] = $verify_code;
					$outMsg->updateState(STATE_SUCCESS, "Login success!");
				}else{
					$outMsg->updateState(STATE_FAIL, "Your account not active yet, please active your account!");
				}
			}else{
				$outMsg->updateState(STATE_FAIL, "You have entered an invalid username or password!");
			}
		}else{
			$outMsg->updateState(STATE_FAIL, "You have entered an invalid username or password!");
		}
	}else{	//Ko thay account trong CSDL
		$outMsg->updateState(STATE_FAIL, "You have entered an invalid username or password!");
	}
}


/**
 * @inMsg: Message tu` Client
 * @outMsg: Message se gui den client
 */
public function forgotPasswordController(){
	global $inMsg, $outMsg;
	
	if(UserManager::checkExistAccount($inMsg->params["username"])){
		$query = "SELECT * FROM ".FORUM_DB_PREFIX."user WHERE username='{$inMsg->params["username"]}'";
		//echo($query);
		$result = mysql_query($query, $GLOBALS["link"]);
		checkQuerySuccess($result);
		$row = mysql_fetch_array($result, MYSQL_ASSOC);
		
		$inMsg->others["userid"] = $row["userid"];
		$inMsg->others["email"] = $row["email"];
		$inMsg->others["change_password_code"] = randomString(ACTIVATION_CODE_LENGTH);
		
		$query = "DELETE FROM ".FORUM_DB_PREFIX."useractivation WHERE userid='{$inMsg->others["userid"]}'";
		$result = mysql_query($query, $GLOBALS["link"]);
		checkQuerySuccess($result);
		
		$query = "SELECT max(useractivationid) as max_useractivationid FROM ".FORUM_DB_PREFIX."useractivation";
		$result = mysql_query($query, $GLOBALS["link"]);
		checkQuerySuccess($result);
		$row = mysql_fetch_array($result, MYSQL_ASSOC);
		$next_useractivationid = $row["max_useractivationid"] + 1;
		$query_arr = array(
						"useractivationid" => $next_useractivationid,
						"userid" => $inMsg->others["userid"],
						"dateline" => mktime(),
						"activationid" => $inMsg->others["change_password_code"],
						"type" => CHANGE_PASSWORD_TYPE,
						"usergroupid" => DEFAUL_USER_GROUP_ID
						);
		$query = makeInsertQuery(FORUM_DB_PREFIX."useractivation", $query_arr);
		$result = mysql_query($query, $GLOBALS["link"]);
		
		if(!$result){
			$outMsg->updateState(STATE_FAIL, "Can't query to Database");
		}
		
		//success
		UserManager::sendMail($inMsg->others["email"], TP_CHANGE_PASSWORD);
		$outMsg->updateState(STATE_SUCCESS, "An email has been sent to email {$inMsg->others["email"]}. Check your email, and click on change password link to change your account's password!");
	}else{
		$outMsg->updateState(STATE_FAIL, "Not found account '{$inMsg->params["username"]}'");
	}
}


/**
 * @user
 * @friend
 * @inMsg
 * @outMsg
 */
public function viewInfoController(){		//finished
	global $user, $friend, $inMsg, $outMsg;
	
	if(UserManager::checkLoggedIn($user->userid)){	//Neu user da login
		$query = "SELECT * FROM ".FORUM_DB_PREFIX."user WHERE username='{$inMsg->params["username"]}'";	//username cua friend
		//echo($query);
		$result = mysql_query($query, $GLOBALS["link"]);
		if(checkQuerySuccess($result) && mysql_num_rows($result)){
			$row = mysql_fetch_array($result, MYSQL_ASSOC);
			$friend = new User($row["userid"]);
			UserManager::getUserInfo();
		}else{
			$outMsg->updateState(STATE_FAIL, "Not found user '{$inMsg->params["username"]}'. Please check it again.");
		}
	}else{
		$outMsg->updateState(STATE_FAIL, "You are not logged in. Please login!");
	}
}

/**
 * @user
 * @inMsg
 * @outMsg
 */
public function updateInfoController(){		//finished
	global $user, $inMsg, $outMsg;
	
	if(UserManager::checkLoggedIn($user->userid)){	//Neu user da login
		UserManager::updateUserInfo();
	}else{
		$outMsg->updateState(STATE_FAIL, "You are not logged in. Please login.");
	}
}

/**
 * @user
 * @inMsg
 * @outMsg
 */
public function logoutController(){		//finished
	global $user, $inMsg, $outMsg;
	
	if(UserManager::checkLoggedIn($user->userid)){	//Neu user da login
		UserManager::logout($user->userid);
	}else{
		$outMsg->updateState(STATE_FAIL, "You are not logged in. Please login.");
	}
}
}
?>