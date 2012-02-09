<?php
define("_PPCLink", "_PPCLink");

if($_POST["verify_code"]){
	setcookie($_GET["id"], $_POST["verify_code"]);
}

include_once("conf.php");

debug_message("
		<html>
		<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head>
		<body>
		");
debug_message("<font color=green>");

$outMsg = new OutMessageManager();	//Phai de outMsg truoc inMsg
$inMsg = new InMessageManager();

if($_POST["birthday"]){
	if(ereg("([0-9]{4})/([0-9]{1,2})/([0-9]{1,2})", $_POST["birthday"])){
		$_POST["birthday"] = date(NORMAL_FORUM_BIRTHDAY_FORMAT, strtotime($_POST["birthday"]));
	}else{
		$outMsg->updateState(STATE_FAIL, "Invalid date format in birthday field!");
	}
}

$_POST["password"] = md5($_POST["password"]);
$_POST["old_password"] = md5($_POST["old_password"]);
$_POST["new_password"] = md5($_POST["new_password"]);

$inMsg->inMessageController();
$outMsg->outMessageController();

debug_message("
		</body>
		</html>
		");
?>