<?php

    // neu 1 trong cac truong du lieu la khac null
    //if ((!empty($_POST['username'])) && (!empty($_POST['passwd'])) && (!empty($_POST['email'])))
    //{
    require ('/include/mysql.php');
    require ('/modules/models/user.php');

    // connect database
    MySQL::connect();

    $result = User::getAllUser();
    // lay ve thong tin tat ca user
    while ($row = mysql_fetch_array($result))
    {
        echo $row['username'] . " " . $row['passwd'] . " " . $row['registed_date'];
        echo "<br />";
    }
    
    // lay ve user admin
    $result = User::getUserByUserName('admin1');
    if (mysql_num_rows($result) != 0)
    {
        while ($row = mysql_fetch_array($result))
        {
            echo "get pass user: admin " . $row['user_id'];
        }
    } else
    {
        echo "ko ton tai user";
    }
    
    // giai phong du lieu    
    unset($result);

    // dong ket noi
    MySQL::close();

    // }


?>