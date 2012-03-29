<?php

/**
 * @author hoangnh
 * @copyright 2012
 */
    $path = getcwd();
    include ($path . '/include/mysql.php');
    include ($path . '/modules/models/user.php');
    include ($path . '/modules/models/questions.php');


     // lay ra mang cac cau hoi
        MySQL::connect();
        // kiem tra cau tra loi cua nguoi choi
        $trueAnswer = Question::getAnswerQuestion(6);
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
        echo $strTrueAnswer;
        MySQL::close();
?>