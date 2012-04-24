<?php

    class Question
    {

        public static function getAllQuestions()
        {
            mysql_query("set names utf8;");
            $query = "  SELECT  q.question_id, q.question_type_id, q.question_name, q.answer_a,
                                q.answer_b, q.answer_c, q.answer_d, q.answer,
                                q.describle_answer,
                                q.question_field_id, qt.question_type_name
                        FROM    questions AS q, question_types AS qt
                        WHERE   q.question_type_id = qt.question_type_id";
            $result = @mysql_query($query) or die('getAllQuestions() ' .
                mysql_error());
            return $result;
        }

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
            mysql_query("set names utf8;");
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
            $query = "  SELECT  answer
                        FROM    questions
                        WHERE   question_id = '{$question_id}'";
            $result = @mysql_query($query) or die('getAnswerQuestion() ' . mysql_error());
            return $result;
        }

        // lay ve question  dua vao question_id
        public static function getQuestionById($question_id)
        {
            mysql_query("set names utf8;");
            $query = "  SELECT  question_id, question_name, answer_a, answer_b, answer_c, answer_d, answer
                        FROM    questions
                        WHERE   question_id = '{$question_id}'";
            $result = @mysql_query($query) or die('getQuestionById() ' . mysql_error());
            return $result;
        }

        // lay ve cac question_id cua 1 type
        public static function getQuestionsIdsByType($question_type_id)
        {
            $query = "  SELECT  question_id
                        FROM    questions
                        WHERE   question_type_id = '{$question_type_id}'";
            $result = @mysql_query($query) or die('getQuestionsByType() ' . mysql_error());
            return $result;
        }

        // lay ve cac question_id
        public static function getQuestionIds()
        {
            $query = "  SELECT  question_id
                        FROM    questions
                        ORDER By RAND()
                        LIMIT   15 ";
            $result = @mysql_query($query) or die('getQuestionIds() ' . mysql_error());
            return $result;
        }

        // them 1 cau hoi
        public static function addQuestion($type_id, $field_id, $question, $a, $b, $c, $d, $answer, $des)
        {
            $query = "  INSERT INTO questions(question_type_id, question_field_id, question_name,
                                    answer_a, answer_b, answer_c, answer_d, answer, describle_answer
                        VALUES  ('{$type_id}', '{$field_id}', '{$question}', '{$a}', '{$b}', '{$c}',
                                    '{$d}', '{$answer}', '{$des}'";
            $result = mysql_query($query);
            return $result;
        }
    }

?>