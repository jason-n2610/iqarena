<?php

    class Room
    {
        public static $checkChangeRoom = false;
        
        public static function getAllRoom()
        {
            $query = 'SELECT 
                            room_id, 
                            room_name, 
                            owner_id,
                            max_member, 
                            min_member, 
                            status,
                            win_score, 
                            number_of_member
                        FROM 
                            rooms';
            $result = @mysql_query($query) or die('getAllRoom(): ' . mysql_error());
            return $result;
        }
        
        public static function addRoom($room_name, $owner_id, $max_member, $min_member, $status, $win_score, $number_of_member)
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
            if (isset($result))
            {
                self::$checkChangeRoom = !self::$checkChangeRoom;
            }
            return $result;
        }
    }

?>