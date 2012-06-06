<?php
	session_start();
 // include file config
    $path = "/iqarena/source_server/";
    $uri = $_SERVER['DOCUMENT_ROOT'];
	require $uri.$path.'include/config.php';
    require $uri.$path.'include/mysql.php';
    require $uri.$path.'modules/models/user.php';

    $message;

    if (isset($_POST['submit'])){
        $username = $_POST['txtUsername'];
        $username = stripcslashes($username);
        $username = mysql_real_escape_string($username);

        $password = $_POST['txtPassword'];
        $password = stripcslashes($password);
        $password = mysql_real_escape_string($password);
        if ($username == '' || $password == '') {
            $message = "You must enter full information";
        }
        else{
            MySQL::connect();
            $result = User::checkAdminLogin($username, md5($password));
            if (mysql_num_rows($result) != 0){
				$_SESSION['username'] = $username;
                header('Location: questions.php');
            }
            else{
                $message = "incorrect username or password. Please try again";
            }
        }
    }
    $smarty->assign('message', $message);
	$smarty->display('login.html');
?>