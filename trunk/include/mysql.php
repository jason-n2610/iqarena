<?php
class mysql
{
    public $connection = '';

    public function connect()
    {
        global $config;
        # tạo connect tới localhost
        $this->connection = @mysql_connect($config['dbhost'], $config['dbuser'], $config['dbpass']);
        if (!$this->connection) {
            die('Không kết nối được đến host: ' . $config['dbhost']);
        }
        # lấy dữ liệu từ database
        if (!mysql_select_db($config['dbname'], $this->connection)) {
            die('Không kết nối được đến database: ' . $config['dbname']);
        }
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