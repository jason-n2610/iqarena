<?php
    $filename= $path.'/check_change_room.txt' ;
    $fstring = "";
    if (file_exists($filename))
    {
        $fstring = file_get_contents($filename);
    }
    if (isset($_SESSION["create_new_room"]))
    {
        echo 'session: '+$_SESSION["create_new_room"];
    }
    else
    {
        echo "sorry, session vo dung";
    }
    echo $fstring ;
?>