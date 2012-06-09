<?php

    class Category
    {
        // lay ve tat ca cac room
        public static function getAllCategory()
        {
            $query = 'SELECT
                            category_id,
                            category_name,
                            date_create,
                            describle_category
                        FROM
                            categories';
            $result = @mysql_query($query) or die('getAllCategory(): ' . mysql_error());
            return $result;
        }
    }

?>