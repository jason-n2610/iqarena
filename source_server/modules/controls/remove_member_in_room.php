<?php

/**
 * @author hoangnh
 * @copyright 2012
 * @company ppclink
 */

    // khi chu phong remove 1 member trong room
    if (isset($_POST['member_id']) && isset($_POST['room_id']))
    {
        $path = getcwd();
        include ($path . '/include/mysql.php');
        include ($path . '/modules/models/room_members.php');

        MySQL::connect();
        // xoa member khoi room
        $result = RoomMembers::removeMemberInRoom($_POST['member_id']);
        if ($result)
        {
            // thay doi noi dung file check change room de thong bao cho cac user khac biet
            $filename = $path . '/' . $_POST['room_id'] . '.txt';
            $f_contentFile = "";
            if (file_exists($filename))
            {
                $f_contentFile = file_get_contents($filename);
            }

            // thay doi noi dung file 'roomid'.txt
            settype($f_contentFile, "integer");
            $f_contentFile = $f_contentFile + 1;
            $fd = fopen($filename, "w") or die("Can't open" . $filename);
            $fout = fwrite($fd, $f_contentFile);
            fclose($fd);
        }
        else
        {
            echo mysql_error();
        }
        MySQL::close();
    }

?>