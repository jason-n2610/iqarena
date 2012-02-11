<?php

    // class user, define method access user table
    class user
    {
        public $result;

        public function getAllUser()
        {
            $query = 'SELECT user_id, username, passwd, score, registed_date, power_user FROM users';
            $this->result = @mysql_query($query) or die('getAllUser(): ' . mysql_error());
            return $this->result;
        }

        public function getUserById($user)
        {

        }

        public function getUserByUserName($username)
        {
            $query = "SELECT user_id FROM users WHERE username='{$username}'";
            $result = @mysql_query($query) or die('getUserByUserName(): ' . mysql_error());
            return $result;
        }

        public function addUser($username, $passwd, $score, $register_date, $power_user)
        {
            $query = 'INSERT INTO users(username, passwd, score, register_date, power_user)
                    VALUES ($username, $passwd, $score, $register_date, $power_user)';
            $result = @mysql_query($query) or die('addUser(): ' . mysql_error());
            return $result;
        }

        public function freeResult()
        {
            unset($this->result);
        }

        public function getNumResult()
        {
            return mysql_num_rows($this->result);
        }
    }

?>