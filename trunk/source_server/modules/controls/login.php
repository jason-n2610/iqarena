<?php

    // neu 1 trong cac truong du lieu la khac null
    if ((!empty($_POST['username'])) && (!empty($_POST['password'])))
    {
        require ('/include/mysql.php');
        require ('/modules/models/user.php');

        // connect database
        MySQL::connect();

        $username = $_POST['username'];
        $username = stripcslashes($username);
        $username = mysql_real_escape_string($username);

        $password = $_POST['password'];
        $password = stripcslashes($password);
        $password = mysql_real_escape_string($password);

        $checkResult = false;
        echo 'server';
        // lay ve user admin
        $result = User::checkUserLogin($username, $password);
        if (mysql_num_rows($result) != 0)
        {
            // user ton tai, login thanh cong
            $checkResult = true;
            while ($row=mysql_fetch_assoc($result)) 
                $output[]=$row;
            echo '{ "login":{"value":"true""message":"login success!""info":';
            echo json_encode($output);
            echo '}}';
        } 
        else
        {
            // user chua ton tai, login that bai
            $checkResult = false;
        }

        if (!$checkResult)
        {
            $result = User::getUserByUserName($username);
            if (mysql_num_rows($result) != 0)
            {
                echo '{ "login":{"value":"false""message":"sai password"}}';
            } else
            {
                echo '{ "login":{"value":"false""message":"user chua ton tai"}}';
            }
        } 

        // giai phong du lieu
        unset($result);
        unset($username);
        unset($password);

        // dong ket noi
        MySQL::close();

    }

?>