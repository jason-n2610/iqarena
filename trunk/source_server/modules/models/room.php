<?php

    class Room
    {
        // lay ve tat ca cac room
        public static function getAllRoom()
        {
            $query = 'SELECT
                            room_id,
                            room_name,
                            owner_id,
                            username,
                            max_member,
                            bet_score,
                            time_per_question
                        FROM
                            rooms AS r, users AS u
                        WHERE
                            r.owner_id = u.user_id';
            $result = @mysql_query($query) or die('getAllRoom(): ' . mysql_error());
            return $result;
        }
        
        public static function getMaxMemberOfRoom($room_id)
        {
            $query = "  SELECT  max_member
                        FROM    rooms
                        WHERE   room_id = '{$room_id}'";
            $result = @mysql_query($query) or die ('do not getMaxMemberOfRoom '. mysql_error());
            return $result;
        }
        
        public static function getBetScoreOfRoom($room_id){
            $query = "  SELECT  bet_score
                        FROM    rooms
                        WHERE   room_id = '{$room_ID}'";
            $result = @mysql_query($query) or die ('getBetScoreOfRoom() '. mysql_error());
            return $result;
        }

        // tao room moi
        public static function createNewRoom($room_name, $owner_id, $max_member, $bet_score, $time_per_question)
        {
            $query = "INSERT INTO
                            rooms(
                                    room_name,
                                    owner_id,
                                    max_member,
                                    bet_score,
                                    time_per_question
                                )
                        VALUES(
                                '{$room_name}',
                                '{$owner_id}',
                                '{$max_member}',
                                '{$bet_score}',
                                '{$time_per_question}'
                            )";

            $result = @mysql_query($query) or die('addRoom(): ' . mysql_error());
            return $result;
        }

        // xoa room
        public static function removeRoom($room_id)
        {
            $query = "
                        DELETE FROM rooms
                        WHERE room_id = '{$room_id}'
                        ";
            return @mysql_query($query) or die('removeRoom(): ' . mysql_error());
        }
    }

?>