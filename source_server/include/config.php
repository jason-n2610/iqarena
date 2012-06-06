<?php

//    DEFINE('DB_USER', 'b12_10430992');
//    DEFINE('DB_PASSWD', '123456');
//    DEFINE('DB_HOST', 'sql302.byethost12.com');
//    DEFINE('DB_NAME', 'b12_10430992_iqarena');

	// Config pages
	$path_site = "/iqarena/source_server/admin";  // du?ng d?n thu m?c website, slash d?u và cu?i dòng
	define('_BASE', $_SERVER['DOCUMENT_ROOT'].$path_site);   // du?ng d?n tuy?t d?i E:/Wamp/www/myWebsite/
	// Smarty config
	include _BASE.'/libs/smarty/Smarty.class.php';
	$smarty = new Smarty();
	$smarty->template_dir = _BASE.'/templates/';  // template folder
    $smarty->compile_dir = _BASE.'/templates_c/'; // template cache
    $smarty->cache_dir = _BASE.'/cache'; // php cache
?>