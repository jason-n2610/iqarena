
<!--
 @author hoangnh
 @copyright 2012
 @company ppclink
 -->

<html>
    <head>
        <meta http-equiv="content-type" content="text/html"  charset="utf-8"/>
        <title>Chỉnh sửa câu hỏi</title>
    </head>
    <body>
        <h1 align="center"> Chỉnh sửa câu hỏi</h1>
        <?php
            if (isset($_GET['question_id'])){
                $path = "D:\\xampp\\htdocs\\iqarena\\source_server";
                include ($path . '\\include\\mysql.php');
                include ($path . '\\modules\\models/questions.php');
                MySQL::connect();
                $tblQuestion = Question::getQuestionById($_GET['question_id']);
                $questionName;
                $answerA;
                $answerB;
                $answerC;
                $answerD;
                $answer;
                while($row = mysql_fetch_assoc($tblQuestion)){
                    $questionName = $row['question_name'];
                    $answerA = $row['answer_a'];
                    $answerB = $row['answer_b'];
                    $answerC = $row['answer_c'];
                    $answerD = $row['answer_d'];
                    $answer = $row['answer'];
                }
                MySQL::close();
            }
        ?>
        <table border="1" width="50%" cellspacing="3" cellpadding="3" align="center">
            <tr>
                <td align="left" width="40%">Question</td>
                <td><input type="text" name="question_name" value="<?php echo $questionName ?>"/></td>
            </tr>
            <tr>
                <td>Answer A</td>
                <td><?php echo $answerA ?></td>
            </tr>
            <tr>
                <td>Answer B</td>
                <td><?php echo $answerB ?></td>
            </tr>
            <tr>
                <td>Answer C</td>
                <td><?php echo $answerC ?></td>
            </tr>
            <tr>
                <td>Answer D</td>
                <td><?php echo $answerD ?></td>
            </tr>
            <tr>
                <td>Answer</td>
                <td><?php echo $answer ?></td>
            </tr>
        </table>
    </body>
</html>