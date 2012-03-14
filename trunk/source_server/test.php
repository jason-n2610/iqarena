<?php
    $path = getcwd();
    include ($path."/include/mysql.php");
    include ($path."/modules/models/room_members.php");

    MySQL::connect();
    $list_user = RoomMembers::getMembersInRoom(17);
    var_dump($list_user);
    $output = null;
    while($row = mysql_fetch_assoc($list_user)){
        $output[] = $row;
    }
    echo json_encode($output);
    MySQL::close();
?>