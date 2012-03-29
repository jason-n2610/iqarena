<?php

/**
 * @author hoangnh
 * @copyright 2012
 */

    // cap nhat cau tra loi cua user

    // kiem tra cac du lieu do members gui len
    if (isset($_POST['room_id']) && isset($_POST['user_id']) && isset($_POST['question_id']) && isset($_POST['question_answer']))
    {
        $path = getcwd();
        include ($path . '/include/mysql.php');
        include ($path . '/modules/models/room_members.php');

        // connect server
        MySQL::connect();

        $result = RoomMembers::answerQuestion($_POST['room_id'], $_POST['user_id'], $_POST['question_id'], $_POST['question_answer']);
        echo 'response: ' . $result;

        MySQL::close();
    }

?>