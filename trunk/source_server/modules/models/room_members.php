<?php
    class RoomMembers
    {
        public static function getMembersInRoom($room_id)
        {
            $query = "  SELECT
                            u.user_id,
                            username,
                            email,
                            score_level,
                            registed_date,
                            power_user,
                            money
                        FROM room_members AS rm, users AS u
                        WHERE rm.user_id = u.user_id AND rm.room_id = '{$room_id}'";
            $result = @mysql_query($query) or die ('getMembersInRoom: '.mysql_error());
            return $result;
        }

        public static function joinRoom($room_id, $user_id)
        {
            $query = "  INSERT INTO room_members(room_id, user_id)
                        VALUE('{$room_id}' , '{$user_id}')";
            $result = @mysql_query($query) or die ('joinRoom: '.mysql_error());
            return $result;
        }

        public static function removeMembersInRoom($room_id)
        {
            $query = "  DELETE FROM room_members
                        WHERE room_id = '{$room_id}'";
            $result = @mysql_query($query) or die ('removeMembersInRoom: '.mysql_error());
            return $result;
        }

        // remove member in room
        public static function removeMemberInRoom($member_id, $room_id)
        {
            $query = "  DELETE FROM room_members
                        WHERE room_id = '{$room_id}' AND user_id = '{$member_id}'";
            $result = @mysql_query($query) or die ('removeMemberInRoom: '.mysql_error());
            return $result;
        }
    }
?>