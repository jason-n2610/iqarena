<?php

/**
 * @author hoangnh
 * @copyright 2012
 * @company ppclink
 */

if (isset($_POST['member_id']) && isset($_POST['room_id'])){

    $path = getcwd();
    include ($path . '/include/mysql.php');
    include ($path . '/modules/models/room_members.php');

    MySQL::connect();

    // cap nhat trang thai ready cho member
    RoomMembers::updateStatusForMember($_POST['member_id'], 1);

    // kiem tra xem da du member ready chua
    // neu du tao file 'room_id'_ready thong bao cho cac members khac biet
    if (mysql_num_rows(RoomMembers::getMemberIdsInRoom($_POST['room_id'])) ==
        mysql_num_rows(RoomMembers::getMemberIdsInRoomByStatus($_POST['room_id']))){
            $pathFileReady = $path.'/'.$_POST['room_id'].'_ready.txt';
            $fReady = fopen($pathFileReady, "w") or die("Can't open " . $pathFileReady);
            fclose($fReady);
        }

    MySQL::close();
}

?>