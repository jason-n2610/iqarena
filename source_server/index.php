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
    else if ($_POST['message'] == "get_list_room")
    {
        require ($path.'/modules/controls/get_list_room.php');
    }
    else if ($_POST['message'] == "check_change_room")
    {
        require ($path.'/modules/controls/check_change_room.php');
    }
?>