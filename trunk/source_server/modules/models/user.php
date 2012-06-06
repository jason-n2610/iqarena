<?php

    // class user, define method access user table
    class User
    {

        public static function getAllUser()
        {
            $query = 'SELECT
                            user_id,
                            username,
                            email,
                            score_level,
                            registed_date,
                            power_user,
                            money
                        FROM
                            users
                        WHERE power_user = 0';
            $result = @mysql_query($query) or die('getAllUser(): ' . mysql_error());
            return $result;
        }

        public static function getAllAdmin(){
            $query = 'SELECT
                            user_id,
                            username,
                            email,
                            score_level,
                            registed_date,
                            power_user,
                            money
                        FROM
                            users
                        WHERE power_user = 1';
            $result = @mysql_query($query) or die('getAllAdmin(): ' . mysql_error());
            return $result;
        }

        public static function changePassword($username, $password){
            $query = "  UPDATE  users
                        SET     password='{$password}'
                        WHERE   username='{$username}'";
            $result = @mysql_query($query) or die('changePassword(): ' . mysql_error());
            return $result;
        }

        public static function insertUser($username, $password, $email, $power_user){
            $query = "INSERT INTO users(username, password, email, registed_date, power_user, score_level, money)
                        VALUES ('{$username}', '{$password}', '{$email}', NOW(), '{$power_user}', 8000, 0)";
            $result = @mysql_query($query) or die('insertUser(): ' . mysql_error());
            return $result;

        }

        public static function updateUser($user_id, $power_user, $score, $money){
            $query = "UPDATE users
                        SET power_user = '{$power_user}', score_level='{$score}', money='{$money}'
                        WHERE user_id = '{$user_id}'";
            $result = @mysql_query($query) or die('updateUser(): ' . mysql_error());
            return $result;
        }

        public static function deleteUser($user_id){
            $query = "  DELETE FROM users WHERE user_id = '{$user_id}'";
            $result = mysql_query($query) or die('deleteUser() ' . mysql_error());;
            return $result;
        }

        public static function getUsersByLimit($offset, $count){
            $query = "SELECT
                            user_id,
                            username,
                            email,
                            score_level,
                            registed_date,
                            power_user,
                            money
                        FROM
                            users
                        WHERE power_user = 0
                        LIMIT {$offset}, {$count}";
            $result = @mysql_query($query) or die('getUsersByLimit(): ' . mysql_error());
            return $result;
        }

        public static function getAdminsByLimit($offset, $count){
            $query = "SELECT
                            user_id,
                            username,
                            email,
                            score_level,
                            registed_date,
                            power_user,
                            money
                        FROM
                            users
                        WHERE power_user = 1
                        LIMIT {$offset}, {$count}";
            $result = @mysql_query($query) or die('getAdminsByLimit(): ' . mysql_error());
            return $result;
        }

        // lay ve info cua user dua vao userid
        public static function getUserById($user_id)
        {
            $query = "SELECT
                            *
                        FROM
                            users
                        WHERE
                            user_id = '{$user_id}' ";
            $result = @mysql_query($query) or die('getUserById():' . mysql_error());
            return $result;
        }

        // kiem tra xem username da ton tai chua
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

        // get score dung de lay diem cua user -> kiem tra join_room
        public static function getScore($user_id){

            $query = "  SELECT
                            score_level
                        FROM
                            users
                        WHERE
                            user_id = '{$user_id}' ";
            $result = @mysql_query($query) or die('getScore(): ' . mysql_error());
            return $result;
        }

        // kiem tra account da ton tai chua
        public static function checkUserLogin($username, $password)
        {
            $query = "  SELECT
                            user_id,
                            username,
                            email,
                            score_level,
                            registed_date,
                            power_user,
                            money,
                            status
                        FROM
                            users
                        WHERE
                            username = '{$username}' && password = '{$password}' ";
            $result = @mysql_query($query) or die('checkUserLogin: ' . mysql_error());
            return $result;
        }

        public static function checkAdminLogin($username, $password){
            $query = "  SELECT
                            user_id,
                            username,
                            email,
                            score_level,
                            registed_date,
                            power_user,
                            money,
                            status
                        FROM
                            users
                        WHERE
                            username = '{$username}' && password = '{$password}' && power_user = 1 ";
            $result = @mysql_query($query) or die('checkUserLogin: ' . mysql_error());
            return $result;
        }

        // them moi 1 user
        public static function addUser($username, $password, $email, $score_level, $power_user,
            $money)
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
                                 NOW(),
                                '{$power_user}',
                                '{$money}' )";
            $result = mysql_query($query) or die('addUser(): ' . mysql_error());
            return $result;
        }

        // update score for loser
        public static function updateScoreForLosersAfterGame($user_id, $member_id, $room_id){
            $query = "  UPDATE  users
                        SET     score_level =
                                    score_level +
                                    (SELECT score
                                     FROM   room_members
                                     WHERE  room_member_id = '{$member_id}') -
                                    (SELECT bet_score
                                     FROM   rooms
                                     WHERE  room_id = '{$room_id}'
                                     LIMIT  1)
                        WHERE   user_id = '{$user_id}'";
            $result = mysql_query($query) or die('updateScore(): ' . mysql_error());
            return $result;
        }

        // update score for winner
        public static function updateScoreForWinnerAfterGame($user_id, $member_id, $room_id){
            $query = "  UPDATE  users
                        SET     score_level =
                                    score_level +
                                    (SELECT score
                                     FROM   room_members
                                     WHERE  room_member_id = '{$member_id}') +
                                    (SELECT bet_score * (number_of_members-1)
                                     FROM   rooms
                                     WHERE  room_id = '{$room_id}'
                                     LIMIT  1)
                        WHERE   user_id = '{$user_id}'";
            $result = mysql_query($query) or die('updateScore(): ' . mysql_error());
            return $result;
        }

        // update score for multiple winners
        public static function updateScoreForMultiWinnersAfterGame($user_id, $member_id, $room_id, $members){
            $query = "  UPDATE  users
                        SET     score_level =
                                    score_level +
                                    (SELECT score
                                     FROM   room_members
                                     WHERE  room_member_id = '{$member_id}') +
                                    ((SELECT bet_score * (number_of_members/'{$members}'-1)
                                     FROM   rooms
                                     WHERE  room_id = '{$room_id}'
                                     LIMIT  1))
                        WHERE   user_id = '{$user_id}'";
            $result = mysql_query($query) or die('updateScore(): ' . mysql_error());
            return $result;
        }
    }

?>