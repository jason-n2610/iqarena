<?php
    /*
     * Khi members thoat khoi room => remove member trong bang room_members
     * Dong thoi thong bao cho cac members khac de cap nhap members trong room
     */
    if (isset($_POST['room_id']) && isset($_POST['user_id']))
    {
        require ($path.'/include/mysql.php');
        require ($path.'/modules/models/room_members.php');

        // connect database
        MySQL::connect();

        $_POST['room_id'] = stripcslashes($_POST['room_id']);
        $_POST['room_id'] = mysql_real_escape_string($_POST['room_id']);

        $_POST['user_id'] = stripcslashes($_POST['user_id']);
        $_POST['user_id'] = mysql_real_escape_string($_POST['user_id']);

        // xoa member
        $result = RoomMembers::removeMemberInRoom($_POST['user_id'], $_POST['room_id']);
        if ($result)
        {
            echo '{"type":"exit_room", "value":"true", "message":"thoát room thành công"}';

            // thong bao cho cac member khac biet user_id da thoat khoi room
            $filename= $path.'/'.$_POST['room_id'].'.txt' ;
            $f_contentFile = "";
            if (file_exists($filename))
            {
                $f_contentFile = file_get_contents($filename);
            }

            // thay doi noi dung file 'roomid'.txt
            settype($f_contentFile, "integer");
            $f_contentFile = $f_contentFile+1;
            $fd = fopen ($filename , "w") or die ("Can't open". $filename) ;
            $fout= fwrite ($fd , $f_contentFile) ;
            fclose($fd) ;
        }
        else
        {
            echo '{"type":"exit_room", "value":"false", "message":"thoát room thất bại"}';
        }
    }
?>