<?php
    class MySQL
    {
        public static $connection = '';

        public static function connect()
        {
             DEFINE('DB_USER', 'root');
             DEFINE('DB_PASSWD', 'admin');
             DEFINE('DB_HOST', 'localhost');
             DEFINE('DB_NAME', 'iqarena');
            // tạo connect tới localhost
            self::$connection = @mysql_connect(DB_HOST, DB_USER, DB_PASSWD) or die('Could not connect to mysql: ' . mysql_error());
            // lấy dữ liệu từ database
            mysql_select_db(DB_NAME, self::$connection) or die('Could not select database: ' .
                mysql_error());
        }

        public static function close()
        {
            return mysql_close(self::$connection);
        }

        public static function excuteQuery($query){
            $result = @mysql_query($query) or die (mysql_error());
            return $result;
        }
    }
?>