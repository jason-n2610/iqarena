<?php

/**
 * @author hoangnh
 * @copyright 2012
 * @company ppclink
 */

    /*
     * tra ve cho user danh sach tra loi cua cac nguoi choi trong room
     */

     if (isset($_POST['room_id']) && isset($_POST['question_id']) && isset($_POST['answer']) && isset($_POST['member_id']))
     {
        $path = getcwd();
        include ($path . '/include/mysql.php');
        include ($path . '/modules/models/room_members.php');
        include ($path . '/modules/models/questions.php');

        MySQL::connect();

        // kiem tra cau tra loi cua nguoi choi
        $trueAnswer = Question::getAnswerQuestion($_POST['question_id']);
        $isTrue = false;

        // lay ve cau tra loi dung
        $strTrueAnswer = 'null';
        if (mysql_num_rows($trueAnswer) != 0)
        {
            while($row = mysql_fetch_array($trueAnswer, MYSQL_NUM))
            {
                $strTrueAnswer = $row[0];
                break;
            }
        }

        // kiem tra xem member tra loi dung ko?
        if ($_POST['answer'] == $strTrueAnswer)
        {
            $isTrue = true;
        }

        $result = RoomMembers::getMembersAnswer($_POST['room_id']);

        // lay ve danh sach cac nguoi choi va cau tra loi
        if (mysql_num_rows($result) != 0)
        {
            $output = null;
            while ($row = mysql_fetch_assoc($result))
            {
                $output[] = $row;
            }
            echo '{"type":"get_members_answer", "value":"true", "message":"success", "answer":"' .$strTrueAnswer. '", "answers":';
            echo json_encode($output);
        }

        // neu nguoi choi tra loi dung cau hoi
        if ($isTrue)
        {
            // lay ra danh sach cac cau hoi
            $strPathQuestionFile = $path . '/' . $_POST['room_id'] . '_question.txt';
            // lay ve mang ids cau hoi
            $fQuestion = file_get_contents($strPathQuestionFile);
            $arrayQuestionIds = explode(",", $fQuestion);
            $len = count($arrayQuestionIds);
            $strCurrentQuestion;    // cau hoi hien tai
            for ( $i=0; $i<$len; $i++)
            {
                if ($arrayQuestionIds[$i] == $_POST['question_id'])
                {
                    $strCurrentQuestion = $i;
                    break;
                }
            }

            // lay ra cau hoi tiep theo gui cho client
            $dbQuestion = Question::getQuestionById($arrayQuestionIds[$strCurrentQuestion+1]);
            if (mysql_num_rows($dbQuestion) != 0)
            {
                $output = null;
                while($row = mysql_fetch_assoc($dbQuestion))
                {
                    $output[] = $row;
                }
                echo ', "next_question":';
                echo json_encode($output);
            }
        }

        // nguoi choi tra loi sai, xoa khoi table room_members
        else
        {
            RoomMembers::removeMemberInRoom($_POST['member_id']);
        }
        // ket thuc choi json
        echo '}';


        MySQL::close();
     }

?>