<?php

    $path = getcwd();
    include ($path . "/include/mysql.php");
    include ($path . "/modules/models/category.php");

    MySQL::connect();
    $result = Category::getAllCategory();
    if ($result == null)
    {
        // truong hop ko co room nao
        echo '{"type":"get_category", "value":"false", "message":"không có category nào!"}';
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
            echo '{"type":"get_category", "value":"true", "info":';
            echo json_encode($output);
            echo '}';
        } else
        {
            echo '{"type":"get_category", "value":"false", "message":"không có category nào!"}';
        }
    }

?>