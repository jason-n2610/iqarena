<?php
	session_start();
 // include file config
    $path = "/iqarena/source_server/";
    $uri = $_SERVER['DOCUMENT_ROOT'];
	require $uri.$path.'include/config.php';
    require $uri.$path.'include/mysql.php';
    require $uri.$path.'modules/models/user.php';

    $message;

    if (isset($_GET['sub'])){
        if ($_GET['sub'] == 'logout'){
            session_destroy();
            header ('Location: login.php');
        }
    }

    if (isset($_POST['btnSubmit'])){
        $old_password = $_POST['old_password'];
        $old_password = stripcslashes($old_password);
        $old_password = mysql_real_escape_string($old_password);
        $password = $_POST['password'];
        $password = stripcslashes($password);
        $password = mysql_real_escape_string($password);
        $re_password = $_POST['re_password'];
        $re_password = stripcslashes($re_password);
        $re_password = mysql_real_escape_string($re_password);


        if ($old_password == '' || $password == '' || $re_password == '') {
            $message = "You must enter full information";
        }
        else if($password != $re_password){
            $message = "Two password do not the same";
        }
        else{
            MySQL::connect();
            $result = User::checkUserLogin($_SESSION['username'], md5($old_password));
            if (mysql_num_rows($result) != 0){
                User::changePassword($_SESSION['username'], md5($password));
                $message = "Change successfully!";
            }
            else{
                $message = "Password do not match";
            }
        }
    }
    $smarty->assign('message', $message);
	$smarty->display('account.html');
?>