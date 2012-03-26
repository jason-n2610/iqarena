<?php
    /*
     * khi chủ phòng gửi request play_game
     * Tạo ra file 'room_id_play.txt' thong bao cho cac user khac
     */
     if (isset($_POST['room_id']))
     {
        // tạo ra file
        $path = getcwd();
        $fileName = $path . '/' . $_POST['room_id'] . '_play.txt';
        $file = fopen($fileName, 'w') or die ('can not open ' . $fileName);
        fclose($file);
     }
?>