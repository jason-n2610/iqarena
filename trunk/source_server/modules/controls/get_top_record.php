<?php

/**
 * @author hoangnh
 * @copyright 2012
 */

    /**
     * Lay ve top cac record trong mode single_player
     */
    $path = getcwd();
    include ($path . '/include/mysql.php');
    include ($path . '/modules/models/award.php');

    MySQL::connect();

    $awards = Award::getTopAward();
    if (mysql_num_rows($awards) != 0)
    {
        $output = null;
        while ($row = mysql_fetch_assoc($awards))
            $output[] = $row;
        echo '{"type":"get_top_record", "value":"true", "message":"get success!", "awards":';
        echo json_encode($output);
        echo '}';
    }
    else
    {
        echo '{"type":"get_top_record", "value":"false", "message":"get failed!"}';
    }

    MySQL::close();
?>