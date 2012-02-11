<?php
// class user, define method access user table
class user
{
    public $result;
    
    public function getAllUser()
    {
        $query = 'SELECT user_id, username, passwd, score, registed_date, power_user FROM users';
        $this->result = @mysql_query($query) or die(mysql_error());
        return $this->result;
    }

    public function getUserById($user)
    {

    }
    
    public function getUserByUserName($username)
    {
        $query = 'SELECT user_id FROM users WHERE username=$username';
        $result = @mysql_query($query) or die(mysql_error());
        return $result;
    }

    public function addUser($username, $passwd, $score, $register_date, $power_user)
    {
        $query = 'INSERT INTO users(username, passwd, score, register_date, power_user)
                    VALUES ($username, $passwd, $score, $register_date, $power_user)';
        $result = @mysql_query($query) or die(mysql_error());
        return $result;
    }
    
    public function freeResult()
    {
        echo "free";
        mysql_free_result($this->result) or die(mysql_error());
    }
}
?>