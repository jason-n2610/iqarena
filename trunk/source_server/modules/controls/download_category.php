<?php

    if (isset($_POST['category_id']))
    {

        $path = getcwd();
        include ($path . '/include/mysql.php');
        include ($path . '/modules/models/questions.php');

        // remove special character
        $_POST['category_id'] = stripcslashes($_POST['category_id']);
        $_POST['category_id'] = mysql_real_escape_string($_POST['category_id']);

        MySQL::connect();
        $result = Question::getQuestionByCategory($_POST['category_id']);
        if ($result == null)
        {
            // truong hop ko co room nao
            echo '{"type":"download_category", "value":"false", "message":"không có question nào!"}';
        } else
        {
            $length = mysql_num_rows($result);
            if ($length != 0)
            {
                $output = null;
                // co room
                while ($row = mysql_fetch_assoc($result))
                {
                    $output[] = $row;
                }
                echo '{"type":"download_category", "value":"true", "message":"success", "questions":';
                echo json_encode($output);
                echo '}';
            } else
            {
                echo '{"type":"download_category", "value":"false", "message":"không có quesiton nào!"}';
            }
        }
    }

?>