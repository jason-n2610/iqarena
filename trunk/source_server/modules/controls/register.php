<?php

    // neu 1 trong cac truong du lieu la khac null
    if ((!empty($_POST['username'])) && (!empty($_POST['password'])))   
    {
        require ('/include/mysql.php');
        require ('/modules/models/user.php');

        // connect database
        MySQL::connect();
        
        $username = $_POST['username'];
        $password = $_POST['password'];
        $username = stripcslashes($username);
        $password = stripcslashes($password);
        $username = mysql_real_escape_string($username);
        $password = mysql_real_escape_string($password);

        // lay ve user admin
        $result = User::getUserByUserName($username);
        if (mysql_num_rows($result) != 0)
        {
            while ($row = mysql_fetch_array($result))
            {
                echo 'username true';
            }
        } else
        {
            echo "ko ton tai user";
        }                

        // giai phong du lieu
        unset($result);
        unset($username);
        unset($password);

        // dong ket noi
        MySQL::close();

    }

?>