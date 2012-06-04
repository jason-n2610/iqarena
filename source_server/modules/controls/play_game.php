<?php

    /*
    * khi chủ phòng gửi request play_game
    * Tạo ra file 'room_id_play.txt' thong bao cho cac user khac
    * Tạo ra file 'room_id'_question.txt lưu các question_id và index - tức chỉ số câu hỏi hiện tại
    */
    if (isset($_POST['room_id']))
    {
        $path = getcwd();
        include ($path . '/include/mysql.php');
        include ($path . '/modules/models/questions.php');
        include ($path . '/modules/models/room.php');

        // tạo ra file 'room_id'_play.txt thong bao cho cac member khac room da duoc bat dau choi
        $pathPlay = $path . '/' . $_POST['room_id'] . '_play.txt';
        $filePlay = fopen($pathPlay, 'w') or die('can not open ' . $pathPlay);
        fclose($filePlay);

        // lay ra mang cac cau hoi
        MySQL::connect();
        // update trang thai playing cho room
        Room::changeRoomToPlaying($_POST['room_id']);
        // xoa file 'roomid'.txt chua cac room_members
        unlink($path . '/' . $_POST['room_id'] . '.txt');


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
        for ($i=0; $i<15; $i++){
            $result = Question::getQuestionByType($i+1);
            while($row = mysql_fetch_array($result, MYSQL_NUM))
            {
                $aQuestionIds[] = $row[0];
            }
        }
        MySQL::close();

        // noi dung file, dong dau tien la index cau hoi hien tai cua room, cac dong sau la cau id cau hoi
        $contentFile = implode(",", $aQuestionIds);
        echo $contentFile;

        // tao ra file 'room_id'_question.txt
        $pathQuestion = $path . '/' . $_POST['room_id'] . '_question.txt';
        $fileQuestion = fopen($pathQuestion, 'w') or die ('can not open ' . $pathQuestion);
        fwrite($fileQuestion, $contentFile);
        fclose($fileQuestion);
    }

?>