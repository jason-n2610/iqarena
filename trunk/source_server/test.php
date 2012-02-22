<?php
    $path = getcwd();
    require($path.'/include/mysql.php');
    require($path.'/modules/models/user.php');
    
    MySQL::connect();
    $result = User::getAllUser();
    while ($row = mysql_fetch_assoc($result))
    {
        $output[] = $row;
    }
    echo json_encode($output);
?>