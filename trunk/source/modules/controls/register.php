<?php
// neu 1 trong cac truong du lieu la khac null
//if ((!empty($_POST['username'])) && (!empty($_POST['passwd'])) && (!empty($_POST['email'])))
//{
require ('/include/mysql.php');
require ('/modules/models/user.php');

// tao doi tuong mysql va user
$objUser = new user();
$objMySql = new mysql();

// connect database
$objMySql->connect();

// lay ve thong tin tat ca user
$result = $objUser->getAllUser();
while ($row = mysql_fetch_array($result)) {
    echo $row['username'] . " " . $row['passwd'] . " " . $row['registed_date'];
    echo "<br />";
}

// giai phong ket qua
$objUser->freeResult();

// dong ket noi
$objMySql->close();

// }


?>