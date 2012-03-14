<?php
    if (isset($_POST['room_id']))
    {

        require ($path.'/include/mysql.php');
        require ($path.'/modules/models/room_members.php');

        // remove special character
        $_POST['room_id'] = stripcslashes($_POST['room_id']);
        $_POST['room_id'] = mysql_real_escape_string($_POST['room_id']);

        MySQL::connect();
        $result = RoomMembers::getMembersInRoom($_POST['room_id']);
        if($result)
    }
?>