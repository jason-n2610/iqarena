<?php
    /*
     * Lay ve noi dung file 'room_id'.txt de kiem tra xem co members moi ko?
     */

    if (isset($_POST['room_id']))
    {
        $filename = $_POST['room_id'].'.txt';
        if (file_exists($filename))
        {
            $result = file_get_contents($filename);
            echo $result;
        }
        else
        {
            echo "false";
        }
    }
?>