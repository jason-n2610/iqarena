<?php

    class mysql
    {
        public $connection = '';

        public function connect()
        {
            require_once ('/config/config.php');
            // tạo connect tới localhost
            $this->connection = @mysql_connect(DB_HOST, DB_USER, DB_PASSWD) or die('Could not connect to mysql: ' .
                mysql_error());
            // lấy dữ liệu từ database
            mysql_select_db('iqarena', $this->connection) or die('Could not select database: ' .
                mysql_error());
        }

        public function close()
        {
            return mysql_close($this->connection);
        }
    }

?>