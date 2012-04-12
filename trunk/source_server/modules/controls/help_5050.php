<?php

/**
 * @author hoangnh
 * @copyright 2012
 * @company ppclink
 */

if (isset($_POST['question_id'])){

    $path = getcwd();
    include ($path . '/include/mysql.php');
    include ($path . '/modules/models/questions.php');

    MySQL::connect();

    $dbTrueAnswer = Question::getAnswerQuestion($_POST['question_id']);
    if (mysql_num_rows($dbTrueAnswer) > 0){
        $trueAnswer = 0;
        while($row = mysql_fetch_assoc($dbTrueAnswer)){
            $trueAnswer = $row['answer'];
        }
        if ($trueAnswer != 0){
            $array = array(1, 2, 3, 4);
            $count = count($array);
            for ($index = 0; $index < $count; $index++){
                if ($array[$index] == $trueAnswer){
                    unset($array[$index]);
                    break;
                }
            }
            // sap xep lai mang
            $array = array_values($array);
            shuffle($array);
            echo '{"type":"help_5050", "value":"true", "message":"success",';
            echo '"remove1":"' . $array[0] . '", ';
            echo '"remove2":"' . $array[1] . '"';
            echo '}';
        }
        else{
            echo '{"type":"help_5050", "value":"false", "message":"do not have value"';
        }
    }
    else{
            echo '{"type":"help_5050", "value":"false", "message":"do not have value"';
    }


    MySQL::close();
}

?>