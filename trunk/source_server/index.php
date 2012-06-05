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
    else if ($_POST['message'] == "answer_question")
    {
        include ($path . '/modules/controls/answer_question.php');
    }
    else if ($_POST['message'] == "get_members_answer")
    {
        include ($path . '/modules/controls/get_members_answer.php');
    }
    else if ($_POST['message'] == "remove_member_in_room")
    {
        include ($path . '/modules/controls/remove_member_in_room.php');
    }
    else if ($_POST['message'] == "check_others_answer")
    {
        include ($path . '/modules/controls/check_others_answer.php');
    }
    else if ($_POST['message'] == "ready_for_game")
    {
        include ($path . '/modules/controls/ready_for_game.php');
    }
    else if ($_POST['message'] == "check_room_ready")
    {
        include ($path . '/modules/controls/check_room_ready.php');
    }
    else if ($_POST['message'] == "help_5050")
    {
        include ($path . '/modules/controls/help_5050.php');
    }
    else if ($_POST['message'] == "get_question_by_type"){
        include ($path . '/modules/controls/get_question_by_type.php');
    }
    else if ($_POST['message'] == "get_top_record"){
        include ($path . '/modules/controls/get_top_record.php');
    }
    else if ($_POST['message'] == "submit_record"){
        include ($path . '/modules/controls/submit_record.php');
    }
    else{
        /* Redirect to a different page in the current directory that was requested */
        $host  = $_SERVER['HTTP_HOST'];
        $uri   = rtrim(dirname($_SERVER['PHP_SELF']), '/\\');
        $extra = 'admin/index.php';
        header("Location: http://$host$uri/$extra");
    }

?>