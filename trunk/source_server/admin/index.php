<?php

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
