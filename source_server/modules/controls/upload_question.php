<?php
/**
 * @author hoangnh
 * @copyright 2012
 */
    $path = getcwd();
    include ($path . '/include/mysql.php');
    include ($path . '/modules/models/questions.php');

    $question_name = $_POST['question_name'];
    $question_type_id = $_POST['question_type_id'];
    $answer_a = $_POST['answer_a'];
    $answer_b = $_POST['answer_b'];
    $answer_c = $_POST['answer_c'];
    $answer_d = $_POST['answer_d'];
    $answer = $_POST['answer'];
    $describle_answer = $_POST['describle_answer'];

    $question_name = stripcslashes($question_name);
    $question_name = mysql_real_escape_string($question_name);
    $question_type_id = stripcslashes($question_type_id);
    $question_type_id = mysql_real_escape_string($question_type_id);
    $answer_a = stripcslashes($answer_a);
    $answer_a = mysql_real_escape_string($answer_a);
    $answer_b = stripcslashes($answer_b);
    $answer_b = mysql_real_escape_string($answer_b);
    $answer_c = stripcslashes($answer_c);
    $answer_c = mysql_real_escape_string($answer_c);
    $answer_d = stripcslashes($answer_d);
    $answer_d = mysql_real_escape_string($answer_d);
    $answer = stripcslashes($answer);
    $answer = mysql_real_escape_string($answer);
    $describle_answer = stripcslashes($describle_answer);
    $describle_answer = mysql_real_escape_string($describle_answer);

    MySQL::connect();

    $result = Question::insertReviewQuestion($question_name, $question_type_id, $answer_a, $answer_b, $answer_c, $answer_d, $answer, $describle_answer);
    if ($result)
    {
        echo '{"type":"upload_question", "value":"true", "message":"Thank you very much!"}';
    }
    else
    {
        echo '{"type":"upload_question", "value":"false", "message":"'.mysql_error().'"}';
    }

    MySQL::close();
?>