<?php

    /*
    * thao tac voi bang question_types trong csdl, type o day la cac loai cau hoi nhu kho, de , dac biet kho, ...
    */

    class QuestionTypes
    {
        // lay ve tat ca cac question_type
        public function getAllQuestionType()
        {
            $query = '  SELECT *
                        FROM question_types';
            $result = @mysql_query($query) or die('getAllQuestionType() ' . mysql_error());
            return $result;
        }

        // them moi mot question_type
        public static function addQuestionType($question_type_value, $question_type_name,
            $score)
        {
            $query = "  INSERT INTO question_types(question_type_value, question_type_name, score)
                        VALUES ('{$question_type_value}', '{$question_type_name}', '{$score}')";
            $result = @mysql_query($query) or die('addQuestionType() ' . mysql_error());
            return $result;
        }

        // remove mot type
        public static function removeQuestionType($question_type_id)
        {
            $query = "  DELETE FROM question_types
                        WHERE question_type_id = '{$question_type_id}'";
            $result = @mysql_query($query) or die('removeQuestionType() ' . mysql_error());
            return $result;
        }

        // update mot type
        public static function updateQuestionType($question_type_id, $question_type_value,
            $question_type_name, $score)
        {
            $query = "  UPDATE  question_fields
                        SET     question_type_value = '{$question_type_value}',
                                question_type_name = '{$question_type_name}, score = '{$score}'
                        WHERE   question_type_id = '{$question_type_id}'";
            $result = @mysql_query($query) or die('updateQuestionType() ' . mysql_error());
            return $result;
        }
    }

?>