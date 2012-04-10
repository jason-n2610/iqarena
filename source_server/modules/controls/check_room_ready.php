<?php

/**
 * @author hoangnh
 * @copyright 2012
 * @company ppclink
 */

if (isset($_POST['room_id'])){
    $path = getcwd();
    $pathFileReady = $path.'/'.$_POST['room_id'].'_ready.txt';
    if (file_exists($pathFileReady)){
        echo 'ready';
    }
    else{
        echo '0';
    }
}

?>