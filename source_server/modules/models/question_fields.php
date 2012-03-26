<?php

    /*
    * Linh vuc khoa hoc cua cau hoi, vi du nhu Vat Ly, Toan Hoc, Thien Van Hoc, ...
    */

    class QuestionFields
    {
        // lay ve tat ca cac linh vuc cau hoi
        public static function getAllQuestionFields()
        {
            $query = '  SELECT *
                        FORM question_fields';
            $result = @mysql_query($query) or die('getAllQuestionFields() ' . mysql_error());
            return $result;
        }

        // them moi mot field
        public static function addField($fieldName, $description)
        {
            $query = "  INSERT INTO question_fields(field_name, description)
                        VALUES ('{$fieldName}', '{$description}')";
            $result = @mysql_query($query) or die('addField() ' . mysql_error());
            return $result;
        }

        // remove mot field
        public static function removeField($question_field_id)
        {
            $query = "  DELETE FROM question_fields
                        WHERE question_field_id = '{$question_field_id}'";
            $result = @mysql_query($query) or die('removeField() ' . mysql_error());
            return $result;
        }

        // update mot field
        public static function updateField($question_field_id, $fieldName, $descripton)
        {
            $query = "  UPDATE question_fields
                        SET field_name = '{$fieldName}', description = '{$description}'
                        WHERE field_id = '{$question_field_id}'";
            $result = @mysql_query($query) or die('updateField() ' . mysql_error());
            return $result;
        }
    }

?>