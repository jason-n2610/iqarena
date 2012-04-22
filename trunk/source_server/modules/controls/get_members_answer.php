<?php

/**
 * @author hoangnh
 * @copyright 2012
 * @company ppclink
 */

/*
* tra ve cho user danh sach tra loi cua cac nguoi choi trong room
*/

if (isset($_POST['room_id']) && isset($_POST['question_id']) && isset($_POST['answer']) && isset($_POST['user_id']) && isset($_POST['member_id'])) {
    $path = getcwd();
    include ($path . '/include/mysql.php');
    include ($path . '/modules/models/room_members.php');
    include ($path . '/modules/models/questions.php');
    include ($path . '/modules/models/user.php');
    include ($path . '/modules/models/room.php');

    MySQL::connect();

    $strPathQuestionFile = $path . '/' . $_POST['room_id'] . '_question.txt';

    // kiem tra cau tra loi cua nguoi choi
    $trueAnswer = Question::getAnswerQuestion($_POST['question_id']);
    $isTrue = false;

    // lay ve cau tra loi dung
    $strTrueAnswer = 'null';
    if (mysql_num_rows($trueAnswer) != 0) {
        while ($row = mysql_fetch_array($trueAnswer, MYSQL_NUM)) {
            $strTrueAnswer = $row[0];
            break;
        }
    }

    // kiem tra xem member tra loi dung ko?
    if ($_POST['answer'] == $strTrueAnswer) {
        $isTrue = true;
    }

    $result = RoomMembers::getMembersAnswer($_POST['room_id']);

    // dem xem co bao nhieu nguoi choi tra loi dung
    $countTrueMember = 0;
    $countMember = mysql_num_rows($result);

    // lay ve danh sach cac nguoi choi va cau tra loi
    if ($countMember != 0) {
        $output = null;
        while ($row = mysql_fetch_assoc($result)) {
            $output[] = $row;
            if ($row['last_answer'] == $strTrueAnswer) {
                $countTrueMember++;
            }
        }
        echo '{"type":"get_members_answer", "value":"true", "message":"success", "answer":"' .
            $strTrueAnswer . '", "answers":';
        echo json_encode($output);
    }
    // neu nguoi choi tra loi dung cau hoi
    if ($isTrue) {
        if ($countTrueMember == 1){
            // nguoi choi la nguoi chien thang
            // cong diem
            User::updateScoreForWinnerAfterGame($_POST['user_id'], $_POST['member_id'], $_POST['room_id']);

            // xoa room
            //sleep(1);
            Room::removeRoom($_POST['room_id']);
            // xoa member
            RoomMembers::removeMemberInRoom($_POST['member_id']);

            // xoa file cau hoi
            if (file_exists($strPathQuestionFile)){
                unlink($strPathQuestionFile);
            }

            // xoa file get list
            $pathGetAnswer = $path . '/' . $_POST['room_id'] . '_get_list_answer.txt';
            if (file_exists($pathGetAnswer)){
                unlink($pathGetAnswer);
            }

            // cập nhật điểm mới cho user
            $updateScore;
            $dbScore = User::getScore($_POST['user_id']);
            while($row = mysql_fetch_assoc($dbScore)){
                $updateScore = $row['score_level'];
            }
            echo ', "score":"' . $updateScore .  '"';
        }
        else{
            // truong hop co nhieu nguoi choi khac tra loi dung
            // hien thi tiep cau hoi
            // lay ve mang ids cau hoi
            $fQuestion = file_get_contents($strPathQuestionFile);
            $arrayQuestionIds = explode(",", $fQuestion);
            $len = count($arrayQuestionIds);
            $strCurrentQuestion; // cau hoi hien tai
            for ($i = 0; $i < $len; $i++) {
                if ($arrayQuestionIds[$i] == $_POST['question_id']) {
                    $strCurrentQuestion = $i;
                    break;
                }
            }

            // lay ra cau hoi tiep theo gui cho client
            $dbQuestion = Question::getQuestionById($arrayQuestionIds[$strCurrentQuestion + 1]);
            if (mysql_num_rows($dbQuestion) != 0) {
                $output = null;
                while ($row = mysql_fetch_assoc($dbQuestion)) {
                    $output[] = $row;
                }
                echo ', "next_question":';
                echo json_encode($output);
            }
        }
    }

    // nguoi choi tra loi sai
    else {
        if ($countTrueMember == 0){
            // truong hop ko co nguoi choi nao tra loi dung
            // cong diem cho nhung user chien thang cuoi cung
            User::updateScoreForMultiWinnersAfterGame($_POST['user_id'], $_POST['member_id'], $_POST['room_id'], $countMember);
            //sleep(1);
            // xoa file cau hoi
            if (file_exists($strPathQuestionFile)){
                unlink($strPathQuestionFile);
            }
            //sleep(1);
            // xoa room
            Room::removeRoom($_POST['room_id']);
            // xoa member
            RoomMembers::removeMemberInRoom($_POST['member_id']);

            // xoa file get list
            $pathGetAnswer = $path . '/' . $_POST['room_id'] . '_get_list_answer.txt';
            if (file_exists($pathGetAnswer)){
                unlink($pathGetAnswer);
            }
        }
        else{
            // truong hop nguoi choi tra loi sai va cuoc choi van tiep tuc
            // cong diem dat dc cua nguoi choi
            // diem cong = diem tra loi cau hoi + (-diem dat coc)
            User::updateScoreForLosersAfterGame($_POST['user_id'], $_POST['member_id'], $_POST['room_id']);
            // xoa nguoi choi
            //sleep(1);
            RoomMembers::removeMemberInRoom($_POST['member_id']);
        }

        // cập nhật điểm mới cho user
        $updateScore;
        $dbScore = User::getScore($_POST['user_id']);
        while($row = mysql_fetch_assoc($dbScore)){
            $updateScore = $row['score_level'];
        }
        echo ', "score":"' . $updateScore .  '"';
    }
// ket thuc chuoi json
echo '}';


    MySQL::close();
}

?>