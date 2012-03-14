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
                            min_member,
                            status,
                            win_score,
                            number_of_member
                        FROM
                            rooms AS r, users AS u
                        WHERE
                            r.owner_id = u.user_id';
            $result = @mysql_query($query) or die('getAllRoom(): ' . mysql_error());
            return $result;
        }

        // tao room moi
        public static function createNewRoom($room_name, $owner_id, $max_member, $min_member, $status, $win_score, $number_of_member)
        {
            $query = "INSERT INTO
                            rooms(
                                    room_name,
                                    owner_id,
                                    max_member,
                                    min_member,
                                    status,
                                    win_score,
                                    number_of_member
                                )
                        VALUES(
                                '{$room_name}',
                                '{$owner_id}',
                                '{$max_member}',
                                '{$min_member}',
                                '{$status}',
                                '{$win_score}',
                                '{$number_of_member}'
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
            return @mysql_query($query) or die('removeRoom(): '.mysql_error());
        }
    }
?>