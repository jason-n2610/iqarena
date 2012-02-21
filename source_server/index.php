<?php   
    header('content-type: text/html; charset: utf-8');                     
    $path = getcwd();         
    if ($_POST['message'] == "login")
    {
        require ($path.'/modules/controls/login.php'); 
    }
    else if ($_POST['message'] == "register")
    {
        require ($path.'/modules/controls/register.php');
    }

?>       