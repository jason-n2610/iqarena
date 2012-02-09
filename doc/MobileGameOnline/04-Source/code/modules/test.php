<?php
include_once("message_manager.php");
include_once("user_manager.php");
include_once("test_module.php");
include_once("../conf.php");

$_POST["verify_code"] = "----------17vbv4xttoqufyuzaidav4kqm0irry7ue7husjt4clyk0unnfc3mmeabo7k0f5f1dhfb1vdpyv7nh1uoyjzmqjd9ewy82pfxphqubuaghsbdbynced1yhbt4gmag7171o";


$outMsg = new OutMessageManager();	//Phai de outMsg truoc inMsg
$inMsg = new InMessageManager();
$friend = $user = new User($inMsg->userid);

TestModule::testAll();
$inMsg->params["game_id"] = 6;
$act = "1:x1:y1:x2:y2";
$inMsg->params["action"] = Action::parseAction($act);
//debug_message_r($inMsg->params["action"]);
/*//UserManager::getUserInfo(5);

//$inMsg->params["username"] = "cloruaxxxxx";
$inMsg->params["username"] = "admin";
$inMsg->params["email"] = "duongvanlam@gmail.com";
$inMsg->params["password"] = md5("123qwe");
$inMsg->params["birthday"] = "1988/10/01";
$inMsg->params["icq"] = "_icq_??";
$inMsg->params["aim"] = "_aim_??";
$inMsg->params["yahoo"] = "_yahoo_??";
$inMsg->params["msn"] = "_msn_??";
$inMsg->params["skype"] = "_skype_??";
$inMsg->params["joindate"] = "1999/10/11";
//UserManager::registerController();

UserManager::loginController();
//UserManager::viewInfoController();
//UserManager::updateInfoController();

$arr = array(
	"mot" => "__1",
	"hai" => "__2",
	"ba" => "__3",
	"bon" => "__4",
	"nam" => "__5",
	"sau" => "__6"
	);
//makeInsertQuery("abc", $arr);
//makeUpdateQuery("abc", $arr, "{condition}");*/

//testRegister();
//testLogin();
//testViewInfo();
//testUpdateInfo();
$tmp = new ChineseChess();
$tmp->move();
?>
<?php
function testRegister(){	//ok
global $inMsg, $outMsg;

$outMsg = new OutMessageManager();	//Phai de outMsg truoc inMsg
$inMsg = new InMessageManager();

$inMsg->params["username"] = "adminxyzt";
$inMsg->params["email"] = "duongvanlam@gmail.com";
$inMsg->params["password"] = md5("123qwe");
$inMsg->params["birthday"] = "1988/10/01";
$inMsg->params["icq"] = "_icq_??";
$inMsg->params["aim"] = "_aim_??";
$inMsg->params["yahoo"] = "_yahoo_??";
$inMsg->params["msn"] = "_msn_??";
$inMsg->params["skype"] = "_skype_??";

UserManager::registerController();
}

function testLogin(){	//ok
global $inMsg, $outMsg;

$outMsg = new OutMessageManager();	//Phai de outMsg truoc inMsg
$inMsg = new InMessageManager();

$inMsg->params["username"] = "muzix";
$inMsg->params["password"] = md5("123qwe");

UserManager::loginController();
}

function testViewInfo(){	//ok
global $user, $inMsg, $outMsg;

$outMsg = new OutMessageManager();	//Phai de outMsg truoc inMsg
$inMsg = new InMessageManager();
$user = new User($inMsg->userid);echo($user->userid);

$inMsg->params["username"] = "admin";
UserManager::viewInfoController();
}

function testUpdateInfo(){	//ok
global $user, $inMsg, $outMsg;

$outMsg = new OutMessageManager();	//Phai de outMsg truoc inMsg
$inMsg = new InMessageManager();
$user = new User($inMsg->userid);

$inMsg->params["old_password"] = md5("123qwe");
//$inMsg->params["new_password"] = md5("abc");
$inMsg->params["birthday"] = "1988/10/01";
$inMsg->params["icq"] = "_icq_xxx";
$inMsg->params["aim"] = "_aim_xxx";
$inMsg->params["yahoo"] = "_yahoo_xxx";
$inMsg->params["msn"] = "_msn_??";
$inMsg->params["skype"] = "_skype_xxx";

UserManager::updateInfoController();
}
?>