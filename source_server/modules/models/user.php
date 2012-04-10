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
                            users';
            $result = @mysql_query($query) or die('getAllUser(): ' . mysql_error());
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
                            user_id == '{$user_id}' ";
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
                                    (SELECT bet_score * number_of_members
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
                                    ((SELECT bet_score * number_of_members
                                     FROM   rooms
                                     WHERE  room_id = '{$room_id}'
                                     LIMIT  1) / '{$members}')
                        WHERE   user_id = '{$user_id}'";
            $result = mysql_query($query) or die('updateScore(): ' . mysql_error());
            return $result;
        }
    }

?>