<?php

    // class user, define method access user table
    class User
    {

        public static function getAllUser()
        {
            $query = 'SELECT 
                            user_id, 
                            username, 
                            password,
                            email, 
                            score_level, 
                            registed_date, 
                            power_user,
                            money 
                        FROM 
                            users';
            $result = @mysql_query($query) or die('getAllUser(): ' . mysql_error());
            return $result;
        }

        public static function getUserById($user)
        {

        }

        public static function getUserByUserName($username)
        {
            $query = "  SELECT 
                            user_id 
                        FROM 
                            users 
                        WHERE 
                            username = '{$username}' ";
            $result = @mysql_query($query) or die('getUserByUserName(): ' . mysql_error());
            return $result;
        }
        
        public static function checkUserLogin($username, $password)
        {
            $query = "  SELECT
                            user_id
                        FROM
                            users
                        WHERE 
                            username = '{$username}' && password = '{$password}' ";              
            $result = @mysql_query($query) or die('checkUserLogin: ' . mysql_error());
            return $result;
        }

        public static function addUser($username, $password,$email, $score_level, $register_date, $power_user, $money)
        {
            $query = "  INSERT INTO 
                            users(
                                username, 
                                password,
                                email, 
                                score_level, 
                                registed_date, 
                                power_user,
                                money )
                        VALUES 
                            (
                                '{$username}', 
                                '{$password}',
                                '{$email}', 
                                '{$score_level}', 
                                '{$register_date}', 
                                '{$power_user}',
                                '{$money}' )";
            $result = @mysql_query($query) or die('addUser(): ' . mysql_error());
            return $result;
        }
    }

?>