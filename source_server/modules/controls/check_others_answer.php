<?php

/**
 * @author hoangnh
 * @copyright 2012
 * @company ppclink
 */

if (isset($_POST['room_id'])){
    $path = getcwd();
    $pathGetAnswer = $path . '/' . $_POST['room_id'] . '_get_list_answer.txt';
    if (file_exists($pathGetAnswer)){
        echo "get";
    }
    else{
        echo "0";
    }
}

?>