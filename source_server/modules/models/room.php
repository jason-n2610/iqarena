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
                            r.owner_id = u.user_id AND r.status = 0';
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

        // tao room moi
        public static function createNewRoom($room_name, $owner_id, $max_member, $bet_score, $time_per_question)
        {
            $query = "INSERT INTO
                            rooms(
                                    room_name,
                                    owner_id,
                                    max_member,
                                    bet_score,
                                    time_per_question,
                                    number_of_members
                                )
                        VALUES(
                                '{$room_name}',
                                '{$owner_id}',
                                '{$max_member}',
                                '{$bet_score}',
                                '{$time_per_question}',
                                1
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

        // lấy về bet_score của room
        public static function getBetScore($room_id){
            $query = "  SELECT  bet_score
                        FROM    rooms
                        WHERE   room_id = '{$room_id}'";
            return @mysql_query($query) or die('getBetScore(): ' . mysql_error());
        }

        public static function changeRoomToPlaying($room_id){
            $query = "  UPDATE  rooms
                        SET     status = 1
                        WHERE   room_id = '{$room_id}'";
            return @mysql_query($query) or die('changeRoomToPlaying(): ' . mysql_error());
        }

        // tự động +1 vào number_of_members của room
        public static function increateMemberOfRoom($room_id){
            $query = "  UPDATE  rooms
                        SET     number_of_members = number_of_members + 1
                        WHERE   room_id = '{$room_id}'";
            $result = @mysql_query($query) or die ('updateMemberOfRoom '  . mysql_error());
        }

        // tự động -1 vào number_of_members của room
        public static function decreateMemberOfRoom($room_id){
            $query = "  UPDATE  rooms
                        SET     number_of_members = number_of_members - 1
                        WHERE   room_id = '{$room_id}'";
            $result = @mysql_query($query) or die ('updateMemberOfRoom '  . mysql_error());
        }
    }

?>