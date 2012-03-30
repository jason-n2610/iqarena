<?php

    /**
     * tao 1 room moi thi chi can room_name, owner_id, max_member, bet_score
     */
    // neu 1 trong cac truong du lieu la khac null
    if ((isset($_POST['room_name'])) && (isset($_POST['owner_id'])) && (isset($_POST['max_member'])) &&
        (isset($_POST['bet_score'])) && (isset($_POST['time_per_question'])) )
    {
        $path = getcwd();
        include ($path . '/include/mysql.php');
        include ($path . '/modules/models/room.php');
        include ($path . '/modules/models/room_members.php');

        // connect database
        MySQL::connect();

        $_POST['room_name'] = stripcslashes($_POST['room_name']);
        $_POST['room_name'] = mysql_real_escape_string($_POST['room_name']);

        $_POST['owner_id'] = stripcslashes($_POST['owner_id']);
        $_POST['owner_id'] = mysql_real_escape_string($_POST['owner_id']);

        $_POST['max_member'] = stripcslashes($_POST['max_member']);
        $_POST['max_member'] = mysql_real_escape_string($_POST['max_member']);

        $_POST['bet_score'] = stripcslashes($_POST['bet_score']);
        $_POST['bet_score'] = mysql_real_escape_string($_POST['bet_score']);

        $_POST['time_per_question'] = stripcslashes($_POST['time_per_question']);
        $_POST['time_per_question'] = mysql_real_escape_string($_POST['time_per_question']);

        $result = Room::createNewRoom($_POST['room_name'], $_POST['owner_id'], $_POST['max_member'], $_POST['bet_score'], $_POST['time_per_question']);
        if ($result)
        {
            $roomID = mysql_insert_id();
            echo '{"type":"create_new_room", "value":"true", "message":"tạo room thành công", "room_id":' .
                $roomID . '}';

            // thay doi file check_change_room.txt -> thong bao cho cac thanh vien khac
            $filename = $path . '/check_change_room.txt';
            $fstring = "";
            if (file_exists($filename))
            {
                $fstring = file_get_contents($filename);
            }
            // neu noi dung file la 0 thi ghi 1, ko thi ghi 0
            if ($fstring == '0')
            {
                $fstring = '1';
            } else
            {
                $fstring = '0';
            }
            $fd = fopen($filename, "w") or die("Can't open" . $filename);
            $fout = fwrite($fd, $fstring);
            fclose($fd);

            // tao file 'room_id'.txt dung de kiem tra su thay doi members  trong 1 room
            $fileMember = $roomID . '.txt';
            $fMember = fopen($fileMember, "w") or die("Can't open " . $fileMember);
            fclose($fMember);

            // them vao table room_members owner
            RoomMembers::ownerRoom($roomID, $_POST['owner_id']);
        } else
        {
            echo '{"type":"create_new_room", "value":"false", "message":"không tạo được room"}';
        }

        unset($result);

        // dong ket noi
        MySQL::close();

    }

?>