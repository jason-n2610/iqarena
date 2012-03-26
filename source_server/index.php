<?php
    $path = getcwd();
    if ($_POST['message'] == "login")
    {
        include ($path.'/modules/controls/login.php');
    }
    else if ($_POST['message'] == "register")
    {
        include ($path.'/modules/controls/register.php');
    }
    else if ($_POST['message'] == "get_list_room")
    {
        include ($path.'/modules/controls/get_list_room.php');
    }
    else if ($_POST['message'] == "check_change_room")
    {
        include ($path.'/modules/controls/check_change_room.php');
    }
    else if ($_POST['message'] == "create_new_room")
    {
        include ($path.'/modules/controls/create_new_room.php');
    }
    else if ($_POST['message'] == "remove_room")
    {
        include ($path.'/modules/controls/remove_room.php');
    }
    else if ($_POST['message'] == "join_room")
    {
        include ($path.'/modules/controls/join_room.php');
    }
    else if ($_POST['message'] == "exit_room")
    {
        include ($path.'/modules/controls/exit_room.php');
    }
    else if ($_POST['message'] == "get_members_in_room")
    {
        include ($path.'/modules/controls/get_members_in_room.php');
    }
    else if ($_POST['message'] == "check_members_in_room")
    {
        include ($path.'/modules/controls/check_members_in_room.php');
    }
    else if ($_POST['message'] == "exit_room")
    {
        include ($path.'/modules/controls/exit_room.php');
    }
    else if ($_POST['message'] == "play_game")
    {
        include ($path . '/modules/controls/play_game.php');
    }
    else if ($_POST['message'] == "get_question")
    {
        include ($path . '/modules/controls/get_question.php');
    }
?>