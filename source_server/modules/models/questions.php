<?php

    class Question
    {

        // lay ra 1 question dua vao type va field
        public static function getQuestionByTypeAndField($question_type_id, $question_field_id)
        {
            $query = "  SELECT  question_id, question_name, answer_a, answer_b, answer_c, answer_d
                        FROM    questions
                        WHERE   question_type_id='{$question_type_id}'
                                AND question_field_id='{$question_field_id}'
                        ORDER   BY RAND()
                        LIMIT   1";
            $result = @mysql_query($query) or die('getQuestionByTypeAndField() ' .
                mysql_error());
            return $result;
        }

        // lay ra tat ca question dua vao type va field
        public static function getQuestionsByTypeAndField($question_type_id, $question_field_id)
        {
            $query = "  SELECT  question_id, question_name, answer_a, answer_b, answer_c, answer_d
                        FROM    questions
                        WHERE   question_type_id='{$question_type_id}'
                                AND question_field_id='{$question_field_id}'";
            $result = @mysql_query($query) or die('getQuestionByTypeAndField() ' .
                mysql_error());
            return $result;
        }

        // lay ra mot cau hoi ngau nhien tu csdl
        public static function getQuestion()
        {
            $query = "  SELECT  question_id, question_name, answer_a, answer_b, answer_c, answer_d
                        FROM    questions
                        ORDER   BY RAND()
                        LIMIT   1";
            $result = @mysql_query($query) or die('getQuestionByTypeAndField() ' .
                mysql_error());
            return $result;
        }

        // lay ve answer cua mot cau hoi dua vao question_id
        public static function getAnswerQuestion($question_id)
        {
            $query = "  SELECT answer, describle_answer
                        FROM questions
                        WHERE question_id = '{$question_id}'";
            $result = @mysql_query($query) or die('getAnswerQuestion() ' . mysql_error());
            return $result;
        }
    }

?>