<?php

	session_start();

	if(isset($_SESSION['views']))
	{
		$_SESSION['views']=$_SESSION['views']+1;
	}
	else
	{
		$_SESSION['views']=1;
	}
	$path = "/iqarena/source_server/";
    $uri = $_SERVER['DOCUMENT_ROOT'];
	require $uri.$path.'include/config.php';
    require $uri.$path.'include/mysql.php';
    require $uri.$path.'modules/models/user.php';
	
	MySQL::connect();
	
	// ĐỊNH NGHĨA CHẠY QUA TRANG CHỦ begin
	define ( 'index', true );
	// ĐỊNH NGHĨA CHẠY QUA TRANG CHỦ end
    if (isset($_COOKIE['username']) && isset($_COOKIE['password']))
    {

        if (($_POST['username'] != 'admin') || ($_POST['password'] != "123456"))
        {
            header('Location: login.html');
        } else
        {
            echo 'Welcome back ' . $_COOKIE['username'];
        }

    } else
    {
        header('Location: controls/login.php');
    }

?>
