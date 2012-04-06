<?php

    /**
     * @author hoangnh
     * @copyright 2012
     */

    // cap nhat cau tra loi cua user

    // kiem tra cac du lieu do members gui len
    if (isset($_POST['member_id']) && isset($_POST['room_id']) && isset($_POST['question_id']) &&
        isset($_POST['question_answer']))
    {
        $path = getcwd();
        include ($path . '/include/mysql.php');
        include ($path . '/modules/models/room_members.php');
        include ($path . '/modules/models/questions.php');

        // connect server
        MySQL::connect();

        $result = RoomMembers::answerQuestion($_POST['member_id'], $_POST['question_id'],
            $_POST['question_answer']);

        echo 'response: ' . $result;


        // kiem tra cau tra loi cua nguoi choi
        $trueAnswer = Question::getAnswerQuestion($_POST['question_id']);
        $isTrue = false;

        // lay ve cau tra loi dung
        $strTrueAnswer = 'null';
        if (mysql_num_rows($trueAnswer) != 0)
        {
            while ($row = mysql_fetch_array($trueAnswer, MYSQL_NUM))
            {
                $strTrueAnswer = $row[0];
                break;
            }
        }

        // kiem tra xem member tra loi dung ko?
        if ($_POST['question_answer'] == $strTrueAnswer)
        {
            $isTrue = true;
        }

        // neu dung thi cap nhat diem cho user
        if ($isTrue)
        {
            // xem cau hoi la cau thu may
            // lay ra danh sach cac cau hoi
            $strPathQuestionFile = $path . '/' . $_POST['room_id'] . '_question.txt';
            // lay ve mang ids cau hoi
            $fQuestion = file_get_contents($strPathQuestionFile);
            $arrayQuestionIds = explode(",", $fQuestion);
            $len = count($arrayQuestionIds);
            $strCurrentQuestion; // cau hoi hien tai
            for ($i = 0; $i < $len; $i++)
            {
                if ($arrayQuestionIds[$i] == $_POST['question_id'])
                {
                    $strCurrentQuestion = $i;
                    break;
                }
            }

            // kiem tra xem cau hoi cua thu may
            // neu tu cau 5, nguoi choi nhan duoc 5 diem
            // cau thu 6: 8 diem
            // cau thu 7: 12 diem
            // cau thu 8: 20 diem
            // cau thu 9: 30 diem
            // cau thu 10: 50 diem
            // cau thu 11: 80 diem
            // cau thu 12: 120 diem
            // cau thu 13: 180 diem
            // cau thu 14: 250 diem
            // cau thu 15: 350 diem     --> end
            $score = 0;
            switch ($strCurrentQuestion + 1)
            {
                case 5:
                    $score = 5;
                    break;
                case 6:
                    $score = 8;
                    break;
                case 7:
                    $score = 12;
                    break;
                case 8:
                    $score = 20;
                    break;
                case 9:
                    $score = 30;
                    break;
                case 10:
                    $score = 50;
                    break;
                case 11:
                    $score = 80;
                    break;
                case 12:
                    $score = 120;
                    break;
                case 13:
                    $score = 180;
                    break;
                case 14:
                    $score = 250;
                    break;
                case 15:
                    $score = 350;
                    break;
            }

            RoomMembers::updateScore($_POST['member_id'], $score);
        }

        MySQL::close();
    }

?>