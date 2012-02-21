<?php                   
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