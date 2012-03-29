<?php

/**
 * @author hoangnh
 * @copyright 2012
 */

    /*
     * Member request cau hoi
     * Server sẽ kiểm tra file 'room_id'_play_question.txt để xem số câu hỏi đã trả lời của room
     * Nếu file chưa tồn tại tức là câu hỏi thứ nhất
     */

    if (isset($_POST['room_id']))
    {
        $path = getcwd();
        // kiem tra xem co ton tai file 'room_id'_play.txt ko, neu co thi xoa di
        $pathRoom = $path . '/' . $_POST['room_id'] . '_play.txt';
        if (file_exists($pathRoom))
        {
            unlink($pathRoom);
        }
        $fRoomQuestion = $path . '/' . $_POST['room_id'] . '_question.txt';
        if (file_exists($fRoomQuestion))
        {
            include ($path . '/include/mysql.php');
            include ($path . '/modules/models/questions.php');

            // doc noi dung file
            $fContent = file_get_contents($fRoomQuestion);
            $aQuestionIds = explode(",", $fContent);

            MySQL::connect();

            $question = Question::getQuestionById($aQuestionIds[0]);
            if (mysql_num_rows($question) != 0)
            {
                $output = null;
                while ($row = mysql_fetch_assoc($question))
                    $output[] = $row;
                echo '{"type":"get_question", "value":"true", "message":"get success!", "question":';
                echo json_encode($output);
                echo '}';
            }
            else
            {
                echo '{"type":"get_question", "value":"false", "message":"get failed!"}';
            }

            MySQL::close();
        }
    }
?>