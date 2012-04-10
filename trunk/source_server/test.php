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
    include ($path . '/modules/models/room_members.php');


     // lay ra mang cac cau hoi
        MySQL::connect();
        User::updateScoreForLosersAfterGame(5, 396, 56);
        $dbScore = User::getScore(5);
        while($row = mysql_fetch_assoc($dbScore)){
            echo $row['score_level'];
        }
        MySQL::close();
?>