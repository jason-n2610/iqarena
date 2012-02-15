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
                            username = '{$username}'";
            $result = @mysql_query($query) or die('getUserByUserName(): ' . mysql_error());
            return $result;
        }

        public static function addUser($username, $password, $score, $register_date, $power_user)
        {
            $query = '  INSERT INTO 
                            users(
                                username, 
                                password, 
                                score, 
                                register_date, 
                                power_user)
                        VALUES 
                            (
                                $username, 
                                $password, 
                                $score, 
                                $register_date, 
                                $power_user)';
            $result = @mysql_query($query) or die('addUser(): ' . mysql_error());
            return $result;
        }
    }

?>