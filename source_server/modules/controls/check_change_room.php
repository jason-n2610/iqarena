<?php    
    //require ($path."/modules/models/room.php");
//    echo var_dump(Room::$checkChangeRoom); 
//    echo Room::$checkChangeRoom;
    $path = getcwd();  
    $filename= $path.'/count.txt' ;
    $fd = fopen ($filename , "r") or die ("Can't open $filename") ;
    $fstring = fread ($fd , filesize ($filename)) ;
    echo "$fstring" ;
    fclose($fd) ;  
?>