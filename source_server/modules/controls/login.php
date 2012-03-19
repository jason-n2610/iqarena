<?php                  
    // neu 1 trong cac truong du lieu la khac null   
    if ((isset($_POST['username'])) && (isset($_POST['password'])))
    {
        require ($path."/include/mysql.php"); 
        require ($path."/modules/models/user.php"); 
        // connect database
        MySQL::connect();

        $username = $_POST['username'];
        $username = stripcslashes($username);
        $username = mysql_real_escape_string($username);

        $password = $_POST['password'];
        $password = stripcslashes($password);
        $password = mysql_real_escape_string($password);

        $checkResult = false;
        // lay ve user admin
        $result = User::checkUserLogin($username, $password);
        if (mysql_num_rows($result) != 0)
        {
            // user ton tai, login thanh cong
            $checkResult = true;
            while ($row=mysql_fetch_assoc($result)) 
                $output[]=$row;
            echo '{"type":"login", "value":"true", "message":"login success!", "info":';
            echo json_encode($output);
            echo '}';
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
                echo '{"type":"login", "value":"false", "message":"sai password"}';
            } else
            {                   
                echo '{"type":"login", "value":"false", "message":"user chưa tồn tại"}';
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