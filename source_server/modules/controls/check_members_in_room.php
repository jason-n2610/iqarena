<?php
    /*
     * Lay ve noi dung file 'room_id'.txt de kiem tra xem co members moi ko?
     */

    if (isset($_POST['room_id']))
    {
        $path = getcwd();
        $filename = $path . '/' . $_POST['room_id'] . '.txt';

        // kiem tra xem file ton tai hay ko
        // neu file ton tai tuc la room van con
        // neu file ko ton tai, tuc room da bi xoa, thong bao lai cho user
        if (file_exists($filename))
        {
            $result = file_get_contents($filename);
            echo $result;
        }
        else
        {
            echo 'exit';
        }

        // kiem tra xem file 'room_id'_play.txt co tai tai ko, neu ton tai tuc da co tin hieu bat dau choi tu chu phong
        $filePlay = $path . '/' . $_POST['room_id'] . '_play.txt';
        if (file_exists($filePlay))
        {
            echo 'play';
        }
    }
?>