<?php
class mysql
{
    public $connection = '';

    public function connect()
    {
        global $config;
        # tạo connect tới localhost
        $this->connection = @mysql_connect('localhost', 'root', 'admin') or
            die('Could not connect to mysql: ' . mysql_error());
        # lấy dữ liệu từ database
        mysql_select_db('iqarena', $this->connection) or die('Could not select database: ' . mysql_error());
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