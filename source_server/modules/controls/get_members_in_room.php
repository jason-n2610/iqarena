<?php
if (isset($_POST['room_id'])) {

    require ($path . '/include/mysql.php');
    require ($path . '/modules/models/room_members.php');

    // remove special character
    $_POST['room_id'] = stripcslashes($_POST['room_id']);
    $_POST['room_id'] = mysql_real_escape_string($_POST['room_id']);

    MySQL::connect();
    $result = RoomMembers::getMembersInRoom($_POST['room_id']);
    if ($result == null) {
        // truong hop ko co room nao
        echo '{"type":"get_members_in_room", "value":"false", "message":"không có member nào!"}';
    } else {
        $length = mysql_num_rows($result);
        if ($length != 0) {
            $output = null;
            // co room
            while ($row = mysql_fetch_assoc($result)) {
                $output[] = $row;
            }
            echo '{"type":"get_members_in_room", "value":"true", "message":"success", "members":';
            echo json_encode($output);
            echo '}';
        } else {
            echo '{"type":"get_members_in_room", "value":"false", "message":"không có member nào!"}';
        }
    }
}
?>