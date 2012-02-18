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
        
        $password = md5($password);

        // lay ve user admin
        $result = User::checkUserLogin($username, $password);
        if (mysql_num_rows($result) != 0)
        {             
            // user da ton tai
            echo 'login thanh cong';   
        } 
        else
        {
            // user chua ton tai, them user
            echo 'user chua ton tai';
        }                

        // giai phong du lieu
        unset($result);
        unset($username);
        unset($password);

        // dong ket noi
        MySQL::close();

    }

?>