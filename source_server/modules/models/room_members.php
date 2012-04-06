<?php

    class RoomMembers
    {

        // tra ve info cua members trong room
        public static function getMembersInRoom($room_id)
        {
            $query = "  SELECT
                            room_member_id,
                            u.user_id,
                            username,
                            member_type
                        FROM room_members AS rm, users AS u
                        WHERE rm.user_id = u.user_id AND rm.room_id = '{$room_id}'";
            $result = @mysql_query($query) or die('getMembersInRoom: ' . mysql_error());
            return $result;
        }

        // tra ve answer cua members trong room
        public static function getMembersAnswer($room_id)
        {
            $query = "  SELECT  u.user_id, room_member_id, username, last_answer, score, graft_id, combo, member_type
                        FROM    room_members AS rm, users AS u
                        WHERE   rm.user_id = u.user_id AND rm.room_id = '{$room_id}'";
            $result = @mysql_query($query) or die ('getMembersAnswer(): ' . mysql_error());
            return $result;
        }

        // members join room
        public static function joinRoom($room_id, $user_id)
        {
            $query = "  INSERT INTO room_members(room_id, user_id)
                        VALUE('{$room_id}' , '{$user_id}')";
            $result = @mysql_query($query) or die('joinRoom: ' . mysql_error());
            return $result;
        }

        // them chu phong vao room
        public static function ownerRoom($room_id, $user_id)
        {
            $query = "  INSERT INTO room_members(room_id, user_id, member_type)
                        VALUE('{$room_id}' , '{$user_id}', 1)";
            $result = @mysql_query($query) or die('joinRoom: ' . mysql_error());
            return $result;
        }

        public static function removeMembersInRoom($room_id)
        {
            $query = "  DELETE FROM room_members
                        WHERE room_id = '{$room_id}'";
            $result = @mysql_query($query) or die('removeMembersInRoom: ' . mysql_error());
            return $result;
        }

        // remove member in room
        public static function removeMemberInRoom($member_id)
        {
            $query = "  DELETE FROM room_members
                        WHERE room_member_id = '{$member_id}'";
            $result = @mysql_query($query) or die('removeMemberInRoom: ' . mysql_error());
            return $result;
        }

        // answer question
        public static function answerQuestion($member_id, $question_id, $last_answer)
        {
            $query = "  UPDATE  room_members
                        SET     question_id='{$question_id}', last_answer='{$last_answer}'
                        WHERE   room_member_id = '{$member_id}'";
            $result = @mysql_query($query) or die('answerQuestion() ' . mysql_error());
            return $result;
        }

        // update score
        public static function updateScore($member_id, $score)
        {
            $query = "  UPDATE  room_members
                        SET     score = score + '{$score}'
                        WHERE   room_member_id = '{$member_id}'";
            $result = @mysql_query($query) or die('updateScore() ' . mysql_error());
            return $result;
        }

        // get question_id cua member
        public static function getQuestionIdOfMember($member_id)
        {
            $query = "  SELECT  question_id
                        FROM    room_members
                        WHERE   room_member_id = '{$member_id}'";
            $result = @mysql_query($query) or die('getQuestionIdOfMember() ' . mysql_error());
            return $result;
        }
    }

?>