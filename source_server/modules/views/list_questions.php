<html>
    <head>
        <meta http-equiv="content-type" content="text/html"  charset="utf-8"/>
        <title>Danh sách câu hỏi</title>
    </head>
    <body>
        <?php
        /**
         * @author hoangnh
         * @copyright 2012
         * @company ppclink
         */

        /*
         *  Phan danh cho admin xem danh sach cau hoi
         */

            $path = "D:\\xampp\\htdocs\\iqarena\\source_server";
            include ($path . '\\include\\mysql.php');
            include ($path . '\\modules\\models/questions.php');

            echo '  <table border="1" width="90%" cellspacing="3" cellpadding="3" align="center">
                        <tr>
                            <td align="left" width="5%"> <b>No.</b> </td>
                            <td align="left" width="20%"> <b>Question</b> </td>
                            <td align="left" width="10%"> <b>Answer A</b> </td>
                            <td align="left" width="10%"> <b>Answer B</b> </td>
                            <td align="left" width="10%"> <b>Answer C</b> </td>
                            <td align="left" width="10%"> <b>Answer D</b> </td>
                            <td align="left" width="5%"> <b>Answer</b> </td>
                            <td align="left" width="10%"> <b>Type</b> </td>
                            <td align="left" width="10%"> <b>Edit</b> </td>
                            <td align="left" width="10%"> <b>Delete</b> </td>
                        </tr>
                 ';
            MySQL::connect();
            $tblQuestion = Question::getAllQuestions();
            $count = 1;
            while($row = mysql_fetch_assoc($tblQuestion)){
                echo "  <tr>
                            <td align=\"left\" > " . $count . "</td>" .
                            "<td align=\"left\" > {$row['question_name']}" .
                            "<td align=\"left\" > {$row['answer_a']}" .
                            "<td align=\"left\" > {$row['answer_b']}" .
                            "<td align=\"left\" > {$row['answer_c']}" .
                            "<td align=\"left\" > {$row['answer_d']}" .
                            "<td align=\"left\" > {$row['answer']}" .
                            "<td align=\"left\" > {$row['question_type_name']}" .
                            "<td align=\"left\" > <a href=\"edit_question.php?question_id={$row['question_id']}\">edit</a></td>".
                            "<td align=\"left\" > <a href=\"edit_question.php?question_id={$row['question_id']}\">delete</a></td>".
                        "</tr>\n
                     ";
                $count++;
            }
            echo "</table>";
            MySQL::close();
        ?>
    </body>
</html>