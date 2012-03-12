<?php
/**
 * tao 1 room moi thi chi can room_name, owner_id, max_member, win_score
 */
    // neu 1 trong cac truong du lieu la khac null
    if ((isset($_POST['room_name'])) && (isset($_POST['owner_id'])) && (isset($_POST['max_member']) && (isset($_POST['win_score']))))
    {
        require ($path.'/include/mysql.php');
        require ($path.'/modules/models/room.php');

        // connect database
        MySQL::connect();

        $_POST['room_name'] = stripcslashes($_POST['room_name']);
        $_POST['room_name'] = mysql_real_escape_string($_POST['room_name']);

        $_POST['owner_id'] = stripcslashes($_POST['owner_id']);
        $_POST['owner_id'] = mysql_real_escape_string($_POST['owner_id']);

        $_POST['max_member'] = stripcslashes($_POST['max_member']);
        $_POST['max_member'] = mysql_real_escape_string($_POST['max_member']);

        $_POST['win_score'] = stripcslashes($_POST['win_score']);
        $_POST['win_score'] = mysql_real_escape_string($_POST['win_score']);

        $result = Room::createNewRoom($_POST['room_name'], $_POST['owner_id'], $_POST['max_member'], 2, 0, $_POST['win_score'], 1);
        if ($result)
        {
            echo '{"type":"create_new_room", "value":"true", "message":"tạo room thành công"}';

            $filename= $path.'/count.txt' ;
            $fd = fopen ($filename , "r+") or die ("Can't open $filename") ;
            $fstring = fread ($fd , filesize ($filename)) ;
            $fstring = $fstring + 1;
            $fout= fwrite ($fd , $fstring) ;
            fclose($fd) ;
        }
        else
        {
            echo '{"type":"create_new_room", "value":"false", "message":"không tạo được room"}';
        }

        unset($result);

        // dong ket noi
        MySQL::close();

    }

?>