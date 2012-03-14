<?php
    // user join room
    if (isset($_POST['room_id']) && isset($_POST['user_id']))
    {
        require ($path.'/include/mysql.php');
        require ($path.'/modules/models/room_members.php');

        // remove special character

        $_POST['room_id'] = stripcslashes($_POST['room_id']);
        $_POST['room_id'] = mysql_real_escape_string($_POST['room_id']);

        $_POST['user_id'] = stripcslashes($_POST['user_id']);
        $_POST['user_id'] = mysql_real_escape_string($_POST['user_id']);

        MySQL::connect();
        $result = RoomMembers::joinRoom($_POST['room_id'], $_POST['user_id']);
        if ($result)
        {
            echo '{"type":"join_room", "value":"true", "message"="tham gia thanh cong"}';
        }
        else
        {
             echo '{"type":"join_room", "value":"false", "message":"không tham gia duoc phong"}';
        }

        unset($result);

        // dong ket noi
        MySQL::close();

    }
?>