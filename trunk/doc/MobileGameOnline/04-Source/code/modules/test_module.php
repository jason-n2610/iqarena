<?php
class TestModule{
function test_checkExistAccount(){	//finished
$data = array(
"admin	1",		//	"INPUT	RETURN"
"lamdv8xvn	0",
"abcd_01	1",
"abcd_02	1",
"askcj	0",
"adminxz	0"
);

echo("<hr /><b>test_checkExistAccount</b><br />");
foreach($data as $line){
	$params = split("	", $line);
	//debug_message_r($params);
	//echo("x".UserManager::checkExistAccount($params[0])."x");
	if(UserManager::checkExistAccount($params[0]) == $params[1]){
		//echo("True: {$params[0]} {$params[1]}<br />");
	}else{
		echo("<font color=red>Fail: {$line}</font><br />");
	}
}
}

function test_checkExistEmail(){	//finished
$data = array(
"duongvanlam88@gmail.com	1",
"duongvanlam-xxx@gmail.com	1",
"abcd_01@gmail.com	0",
"abcd_02@gmail.com	0",
"abcd@gmail.com	0",
"abcaSfd@gmail.com	0",
"abcd?#$%2@gmail.com	0"
);

echo("<hr /><b>test_checkExistEmail</b><br />");
foreach($data as $line){
	$params = split("	", $line);

	if(UserManager::checkExistEmail($params[0]) == $params[1]){
		//do nothing
	}else{
		echo("<font color=red>Fail: {$line}</font><br />");
	}
}
}

function test_sendMail(){
}

function test_createActivationInfo(){
}

function test_changePassword(){
}

function test_checkLoggedIn(){	//finished
$data = array(	//"USERID	RETURN"
"1	1",
"2	1",
"3	1",
"4	1",
"5	1",
"6	1"
);

echo("<hr /><b>test_checkLoggedIn</b><br />");
foreach($data as $line){
	$params = split("	", $line);

	if(UserManager::checkLoggedIn($params[0]) == $params[1]){
		//do nothing
	}else{
		echo("<font color=red>Fail: {$line}</font><br />");
	}
}
}


function testAll(){
	TestModule::test_checkExistAccount();
	TestModule::test_checkExistEmail();
	TestModule::test_sendMail();
	TestModule::test_createActivationInfo();
	TestModule::test_changePassword();
	TestModule::test_checkLoggedIn();
}
}
?>