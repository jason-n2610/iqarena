<?php
defined('_PPCLink') or die('Restricted access');

$db_host = 'localhost';
$db_user = 'root';
$db_password = '123qwe';
$db_name = 'vbb';

$link = mysql_connect($db_host, $db_user, $db_password);
if (!$link){
	die("Not connected: " . mysql_error());
}

$db_selected = mysql_select_db($db_name, $link);
if (!$db_selected) {
    die ("Can\'t use {$db_name}: " . mysql_error());
}
?>