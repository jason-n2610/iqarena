<?php

    // neu 1 trong cac truong du lieu la khac null
    //if ((!empty($_POST['username'])) && (!empty($_POST['passwd'])) && (!empty($_POST['email'])))
    //{
    require ('/include/mysql.php');
    require ('/modules/models/user.php');

    // tao doi tuong mysql va user
    $objUser = new user();
    $objMySql = new mysql();

    // connect database
    $objMySql->connect();

    $result = $objUser->getAllUser();
    // lay ve thong tin tat ca user
    while ($row = mysql_fetch_array($result))
    {
        echo $row['username'] . " " . $row['passwd'] . " " . $row['registed_date'];
        echo "<br />";
    }

    $objUser->freeResult();

    // lay ve user admin
    $result = $objUser->getUserByUserName('admin2');
    if (mysql_num_rows($result) != 0)
    {
        echo '!= 0 <br/>';
        while ($row = mysql_fetch_array($result))
        {
            echo "get pass user: admin " . $row['user_id'];
        }
    } else
    {
        echo '== 0 <br/>';
        echo "ko ton tai user";
    }

    // dong ket noi
    $objMySql->close();

    // }


?>