<?php

/**
 * @author hoangnh
 * @copyright 2012
 */
    header("Content-type: text/html; charset=utf-8");
    $path = getcwd();
    include ($path . '/include/mysql.php');
    include ($path . '/modules/models/user.php');
    include ($path . '/modules/models/questions.php');


     // lay ra mang cac cau hoi
        MySQL::connect();
        mysql_query("set names utf8;");
        Question::addQuestion(1, 1, "hôm nay là thứ mấy nhỉ?", "thứ hai", "thứ 3", "thứ 4",
                                "thứ 4", 2, "trường hợp đặc biệt");
        $result = Question::getAllQuestions();

        while($row = mysql_fetch_array($result, MYSQL_NUM))
        {
            var_dump($row);
            echo '<br/>';
        }
        echo 'lấy dấu ở ngoài xem được ko?';
        MySQL::close();
?>