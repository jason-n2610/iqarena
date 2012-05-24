<?php

/**
 * @author hoangnh
 * @copyright 2012
 */

    /*
     * Member request cau hoi
     */

    if (isset($_POST['level']))
    {
        $path = getcwd();

        include ($path . '/include/mysql.php');
        include ($path . '/modules/models/questions.php');

        MySQL::connect();

        $question = Question::getQuestionByTypeAndField($_POST['level'], 1);
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
?>