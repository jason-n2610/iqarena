<?php
    // xoa room
    if (isset($_POST['room_id']))
    {
    require ($path.'/include/mysql.php');
        require ($path.'/modules/models/room.php');

        // connect database
        MySQL::connect();

        $_POST['room_id'] = stripcslashes($_POST['room_id']);
        $_POST['room_id'] = mysql_real_escape_string($_POST['room_id']);

        $result = Room::removeRoom($_POST['room_id']);
        if ($result)
        {
            echo '{"type":"remove_room", "value":"true", "message":"xóa room thành công"}';

            $filename= $path.'/check_change_room.txt' ;
            $fstring = "";
            if (file_exists($filename))
            {
                $fstring = file_get_contents($filename);
            }
            if ($fstring == '0')
            {
                $fstring = '1';
            }
            else
            {
                $fstring = '0';
            }
            $fd = fopen ($filename , "w") or die ("Can't open $filename") ;
            $fout= fwrite ($fd , $fstring) ;
            fclose($fd) ;
        }
        else
        {
            echo '{"type":"remove_room", "value":"false", "message":"không xóa được room"}';
        }

        unset($result);

        // dong ket noi
        MySQL::close();

    }
?>