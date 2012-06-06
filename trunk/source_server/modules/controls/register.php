<?php

    // neu 1 trong cac truong du lieu la khac null
    if ((isset($_POST['username'])) && (isset($_POST['password'])) && (isset($_POST['email'])))
    {
        $path = getcwd();
        include ($path . '/include/mysql.php');
        include ($path . '/modules/models/user.php');

        // connect database
        MySQL::connect();

        $username = $_POST['username'];
        $password = $_POST['password'];
        $email = $_POST['email'];
        $username = stripcslashes($username);
        $password = stripcslashes($password);
        $email = stripcslashes($email);
        $username = mysql_real_escape_string($username);
        $password = mysql_real_escape_string($password);
        $email = mysql_real_escape_string($email);

        // lay ve user admin
        $result = User::getUserByUserName($username);
        if (mysql_num_rows($result) != 0)
        {
            // user da ton tai
            echo '{"type":"register", "value":"false", "message":"user đã tồn tại"}';
        } else
        {
            // user chua ton tai, them user
            $isSuccess = User::addUser($username, $password, $email, 8000, 0, 0);
            if ($isSuccess)
            {
                echo '{"type":"register", "value":"true", "message":"register success!", "info":';
                echo '[{"user_id"="' . mysql_insert_id() . '", "username"="' . $username .
                    '", "email"="' . $email . '", "score_level"="8000", "registed_date"="' . date('Y-m-d H:i:s') .
                    '", "power_user"="0", "money"="0"}]}';
            } else
            {
                echo '{"type":"register", "value":"false", "message":"Đăng kí thất bại"}';
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