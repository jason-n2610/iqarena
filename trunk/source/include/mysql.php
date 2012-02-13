<?php

    class MySQL
    {
        public static $connection = '';

        public static function connect()
        {
            require_once ('/config/config.php');
            // tạo connect tới localhost
            self::$connection = @mysql_connect(DB_HOST, DB_USER, DB_PASSWD) or die('Could not connect to mysql: ' .
                mysql_error());
            // lấy dữ liệu từ database
            mysql_select_db('iqarena', self::$connection) or die('Could not select database: ' .
                mysql_error());
        }

        public static function close()
        {
            return mysql_close(self::$connection);
        }
    }

?>