<?php
    require ($path."/include/mysql.php"); 
    require ($path."/modules/models/room.php"); 
    
    MySQL::connect();
    $result = Room::getAllRoom();
    if ($result == null)
    {
        // truong hop ko co room nao
        echo '{"type":"get_list_room", "value":"false", "message":"không có room nào!"}';
    }
    else
    {
        // co room
        while($row = mysql_fetch_assoc($result))
        {
            $output[] = $row;
        }
        echo '{"type":"get_list_room", "value":"true", "info":';
        echo json_encode($output);
        echo '}';
    }
?>