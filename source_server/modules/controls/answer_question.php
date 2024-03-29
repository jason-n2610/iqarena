<?php

    /**
     * @author hoangnh
     * @copyright 2012
     */

    // cap nhat cau tra loi cua user

    // kiem tra cac du lieu do members gui len
    if (isset($_POST['member_id']) && isset($_POST['room_id']) && isset($_POST['question_id']) && isset($_POST['question_answer']))
    {
        $path = getcwd();
        include ($path . '/include/mysql.php');
        include ($path . '/modules/models/room_members.php');
        include ($path . '/modules/models/questions.php');


        $pathGetAnswer = $path . '/' . $_POST['room_id'] . '_get_list_answer.txt';
        if (file_exists($pathGetAnswer)){
            unlink($pathGetAnswer);
        }

        $pathFileReady = $path.'/'.$_POST['room_id'].'_ready.txt';
        if (file_exists($pathFileReady)){
            unlink($pathFileReady);
        }
        // connect server
        MySQL::connect();

        if (isset($_POST['help']) && $_POST['help'] == "release"){
            // lay ve cau tra loi dung va cap nhat cho nguoi choi
            $result = Question::getAnswerQuestion($_POST['question_id']);
            while($row = mysql_fetch_assoc($result)){
                $_POST['question_answer'] = $row['answer'];
            }

            // cap nhat diem cho member
            RoomMembers::updateScoreHelpRealse($_POST['member_id']);
        }
        else{
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
                if (file_exists($strPathQuestionFile)){
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
                        case 1:
                            $score = 10;
                            break;
                        case 2:
                            $score = 10;
                            break;
                        case 3:
                            $score = 10;
                            break;
                        case 4:
                            $score = 10;
                            break;
                        case 5:
                            $score = 20;
                            break;
                        case 6:
                            $score = 50;
                            break;
                        case 7:
                            $score = 100;
                            break;
                        case 8:
                            $score = 200;
                            break;
                        case 9:
                            $score = 500;
                            break;
                        case 10:
                            $score = 1000;
                            break;
                        case 11:
                            $score = 2000;
                            break;
                        case 12:
                            $score = 3000;
                            break;
                        case 13:
                            $score = 5000;
                            break;
                        case 14:
                            $score = 8000;
                            break;
                        case 15:
                            $score = 10000;
                            break;
                    }

                    if (isset($_POST['help']) && $_POST['help'] == "helpx2"){
                        $score = $score * 2;
                    }

                    RoomMembers::updateScore($_POST['member_id'], $score);
                }
            }
        }

        $result = RoomMembers::answerQuestion($_POST['member_id'], $_POST['question_id'],
            $_POST['question_answer']);

        // cap nhat status cho member la 0
        RoomMembers::updateStatusForMember($_POST['member_id'], 0);


        // kiem tra neu user la nguoi cuoi cung tra loi cau hoi thi phat tin hieu cho cac members
        // khac de members nay lay danh sach tra loi cua nhung nguoi choi khac
        if (mysql_num_rows(RoomMembers::getMemberIdsInRoom($_POST['room_id'])) ==
            mysql_num_rows(RoomMembers::getMemberIdsInRoomByQues($_POST['room_id'], $_POST['question_id']))){
                // tao ra file 'room_id'_get_list_answer.txt de thong bao cho cac member khac lay ve list answer

                $pathGetAnswer = $path . '/' . $_POST['room_id'] . '_get_list_answer.txt';
                $fGetAnswer = fopen($pathGetAnswer, "w") or die("Can't open " . $pathGetAnswer);
                fclose($fGetAnswer);
            }

        MySQL::close();
    }

?>