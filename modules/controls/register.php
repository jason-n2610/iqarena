<?php
    // neu 1 trong cac truong du lieu la khac null
    //if ((!empty($_POST['username'])) && (!empty($_POST['passwd'])) && (!empty($_POST['email'])))
    //{
        require_once('/include/mysql.php');     
        require_once('/modules/models/user.php');
        echo 'tao doi tuong mysql';
        $mysql = new mysql();
        echo 'khoi tao connect';
        $mysql->connect();
        echo 'ket thuc connect';
        $a = new user();
   // }
?>