<?php
// class user, define method access user table
class user
{
    public function getAllUser()
    {

    }

    public function getUserById(int $user)
    {

    }
    
    public function addUser($username, $passwd, $score, $register_date, $power_user)
    {
        $query = 'INSERT INTO users(username, passwd, score, register_date, power_user)
                    VALUES ($username, $passwd, $score, $register_date, $power_user)';
        $result = @mysql_query($query);
        return $result;   
    }
}
?>