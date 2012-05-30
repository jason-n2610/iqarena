<?php

    // class award, define method access award table
    class Award
    {

        public static function getTopAward()
        {
            $query = '  SELECT  award_id, user_name, score, date_record
                        FROM    awards
                        ORDER BY score DESC
                        LIMIT   20';
            $result = @mysql_query($query) or die('getTopAward(): ' . mysql_error());
            return $result;
        }

        public static function insertAward($user_name, $score)
        {
            $query = "  INSERT INTO awards(user_name, score, date_record)
                        VALUES      ('{$user_name}', '{$score}', NOW() ) ";
            $result = mysql_query($query) or die('insertAward(): ' . mysql_error());
            return $result;
        }
    }

?>