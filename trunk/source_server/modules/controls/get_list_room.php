<?php
    
    $path = getcwd();
    include ($path . "/include/mysql.php");
    include ($path . "/modules/models/room.php");
    
    MySQL::connect();
    $result = Room::getAllRoom();
    if ($result == null)
    {
        // truong hop ko co room nao
        echo '{"type":"get_list_room", "value":"false", "message":"không có room nào!"}';
    } else
    {
        $length = mysql_num_rows($result);
        if ($length != 0)
        {
            $output = null;
            // co room
            while ($row = mysql_fetch_assoc($result))
            {
                $output[] = $row;
            }
            echo '{"type":"get_list_room", "value":"true", "info":';
            echo json_encode($output);
            echo '}';
        } else
        {
            echo '{"type":"get_list_room", "value":"false", "message":"không có room nào!"}';
        }
    }

?>