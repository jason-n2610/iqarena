<?php
    $path = getcwd();

 session_start();
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
    else if ($_POST['message'] == "create_new_room")
    {
        require ($path.'/modules/controls/create_new_room.php');
    }
    else if ($_POST['message'] == "remove_room")
    {
        require ($path.'/modules/controls/remove_room.php');
    }
    else if ($_POST['message'] == "join_room")
    {
        require ($path.'/modules/controls/join_room.php');
    }
    else if ($_POST['message'] == "exit_room")
    {
        require ($path.'/modules/controls/exit_room.php');
    }
    else if ($_POST['message'] == "get_members_in_room")
    {
        require ($path.'/modules/controls/get_members_in_room.php');
    }
    else if ($_POST['message'] == "check_members_in_room")
    {
        require ($path.'/modules/controls/check_members_in_room.php');
    }
    else if ($_POST['message'] == "exit_room")
    {
        require ($path.'/modules/controls/exit_room.php');
    }
?>