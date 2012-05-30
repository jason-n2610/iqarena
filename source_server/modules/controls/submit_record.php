<?php

/**
 * @author hoangnh
 * @copyright 2012
 */

/**
 * them record vao award
 */
if (isset($_POST['user_name']) && isset($_POST['score'])){
    $path = getcwd();
    include ($path . '/include/mysql.php');
    include ($path . '/modules/models/award.php');

    MySQL::connect();
    $user_name = $_POST['user_name'];
    $score = $_POST['score'];
    $username = stripcslashes($user_name);
    $password = stripcslashes($score);
    $username = mysql_real_escape_string($user_name);
    $password = mysql_real_escape_string($score);

    $result = Award::insertAward($user_name, $score);
    if ($result){
        echo '{"type":"submit_record", "value":"true", "message":"success!", "award_id":"'.mysql_insert_id(). '"}';
    }
    else{
        echo '{"type":"submit_record", "value":"false", "message":"failed!"}';
    }
}
?>