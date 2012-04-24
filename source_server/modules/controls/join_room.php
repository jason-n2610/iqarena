<?php

// user join room
if (isset($_POST['room_id']) && isset($_POST['user_id'])) {
    $path = getcwd();
    include ($path . '/include/mysql.php');
    include ($path . '/modules/models/room.php');
    include ($path . '/modules/models/room_members.php');
    include ($path . '/modules/models/user.php');

    // remove special character

    $_POST['room_id'] = stripcslashes($_POST['room_id']);
    $_POST['room_id'] = mysql_real_escape_string($_POST['room_id']);

    $_POST['user_id'] = stripcslashes($_POST['user_id']);
    $_POST['user_id'] = mysql_real_escape_string($_POST['user_id']);

    MySQL::connect();

    // kiểm tra user có đủ điểm để tham gia room không
    // điều kiến user->score -> bet_score của room
    $scoreUser; // diem cua user
    $scoreBetRoom;  // diem cuoc cua room
    Room::increateMemberOfRoom($_POST['room_id']);
    $tblUser = User::getScore($_POST['user_id']);
    $tblRoom = Room::getBetScore($_POST['room_id']);
    echo $_POST['room_id'];
    while($row = mysql_fetch_array($tblUser, MYSQL_NUM)){
        $scoreUser = $row[0];
    }
    while($row = mysql_fetch_array($tblRoom, MYSQL_NUM)){
        $scoreBetRoom = $row[0];
    }
    if ($scoreUser < $scoreBetRoom){
        // truong hop user khong du diem tham gia room
        echo '{"type":"join_room", "value":"false", "message":"Bạn không đủ điểm tham gia phòng chơi"}';
    }
    else{
        // kiem tra xem room da day chua
        $tbRoomMembers = RoomMembers::getMembersInRoom($_POST['room_id']);
        $roomCount = mysql_num_rows($tbRoomMembers);

        // lay ve max_member cua room
        $maxMembers = Room::getMaxMemberOfRoom($_POST['room_id']);

        // bien kiem tra xem room full chua
        $isFull = false;
        $max = 0;
        while ($row = mysql_fetch_array($maxMembers, MYSQL_NUM)) {
            $max = $row[0];
        }

        if ($roomCount == (int)$max)
        {
            $isFull = true;
        }
        // neu room da full
        if ($isFull) {
            echo '{"type":"join_room", "value":"false", "message":"phòng chơi đã đầy"}';
        }

        // user tham gia dc room
        else {
            $result = RoomMembers::joinRoom($_POST['room_id'], $_POST['user_id']);
            if ($result) {
                $memberID = mysql_insert_id();
                echo '{"type":"join_room", "value":"true", "message"="tham gia thanh cong", "member_id":"' . $memberID . '"}';
                $filename = $path . '/' . $_POST['room_id'] . '.txt';
                $fstring = "";
                if (file_exists($filename)) {
                    $fstring = file_get_contents($filename);
                }

                // thay doi noi dung file 'roomid'.txt
                settype($fstring, "integer");
                $fstring = $fstring + 1;
                $fd = fopen($filename, "w") or die("Can't open" . $filename);
                $fout = fwrite($fd, $fstring);
                fclose($fd);
            } else {
                echo '{"type":"join_room", "value":"false", "message":"không tham gia duoc phong"}';
            }
        }
    }

    unset($result);

    // dong ket noi
    MySQL::close();

}

?>