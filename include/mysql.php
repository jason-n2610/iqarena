<?php
class mysql
{
    public $connection = '';

    public function connect()
    {
        global $config;
        # tạo connect tới localhost
        $this->connection = @mysql_connect($config['dbhost'], $config['dbuser'], $config['dbpass']) or
            die('Could not connect to mysql: ' . mysql_er());
        # lấy dữ liệu từ database
        mysql_select_db($con['dbname'], $this->connection) or die('Could not select database: ' .
            mysql_er());
    }

    public function query($sql)
    {
        $query = @mysql_query($sql, $this->connection);
        return $query;
    }

    public function fetch_array($query, $order = MYSQL_ASSOC)
    {
        $row = @mysql_fetch_array($query, $order);
        return $row;
    }

    public function num_rows($query)
    {
        $number = @mysql_num_rows($query);
        return $number;
    }
}
?>