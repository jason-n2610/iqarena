<?php
    $path = getcwd();
    $filename= $path . '/check_change_room.txt' ;
    $fstring = "";
    if (file_exists($filename))
    {
        $fstring = file_get_contents($filename);
    }
    echo $fstring ;
?>