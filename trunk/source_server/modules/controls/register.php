<?php
    $path = getcwd();
    // neu 1 trong cac truong du lieu la khac null
    if ((!empty($_POST['username'])) && (!empty($_POST['password'])) && (!empty($_POST['email'])))   
    {
        require ($path.'/include/mysql.php');
        require ($path.'/modules/models/user.php');

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
            echo 'đã tồn tại user này';   
        } 
        else
        {
            // user chua ton tai, them user
            echo User::addUser($username, $password, $email, 0, 0, 0);
            echo 'thêm thành công';
        }                

        // giai phong du lieu
        unset($result);
        unset($username);
        unset($password);

        // dong ket noi
        MySQL::close();

    }

?>