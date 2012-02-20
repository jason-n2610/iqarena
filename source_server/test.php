<?php

    if ($_POST['message'] == "login")
    {
        require ('/modules/controls/login.php'); 
    }
    else if ($_POST['message'] == "register")
    {
        require ('/modules/controls/register.php');
    }

?>