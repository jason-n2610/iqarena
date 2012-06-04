<?php
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
            $message = "Yor must enter full information";
        }
        else{
            MySQL::connect();
            $result = User::checkAdminLogin($username, $password);
            if (mysql_num_rows($result) != 0){

                if (isset($_POST['rememberme'])) {
            /* Set cookie to last 1 year */
            setcookie('username', $_POST['username'], time()+60*60*24*365, '/account', 'http://localhost/iqarena/source_server/admin/index.php');
            setcookie('password', ($_POST['password']), time()+60*60*24*365, '/account', 'http://localhost/iqarena/source_server/admin/index.php');

        } else {
            /* Cookie expires when browser closes */
            setcookie('username', $_POST['username'], false, '/account', 'http://localhost/iqarena/source_server/admin/index.php');
            setcookie('password', ($_POST['password']), false, '/account', 'http://localhost/iqarena/source_server/admin/index.php');
        }

                header('Location: controls/main.php');
            }
            else{
                $message = "incorrect username or password. Please try again";
            }
        }
    }
    $smarty->assign('message', $message);
	$smarty->display('login.html');
?>