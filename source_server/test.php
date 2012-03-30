<?php

/**
 * @author hoangnh
 * @copyright 2012
 */
    $path = getcwd();
    include ($path . '/include/mysql.php');
    include ($path . '/modules/models/user.php');
    include ($path . '/modules/models/questions.php');
    include ($path . '/modules/models/room.php');


     // lay ra mang cac cau hoi
        MySQL::connect();
        $result = Room::getMaxMemberOfRoom(8);
        while($row = mysql_fetch_array($result, MYSQL_NUM))
        {
            $display = $row[0];
        }
        echo $display;
        MySQL::close();
?>