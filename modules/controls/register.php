<?php
    // neu 1 trong cac truong du lieu la khac null
    if ((!empty($_POST['username'])) && (!empty($_POST['passwd'])) && (!empty($_POST['email'])))
    {
        require_once('../../../include/mysql.php');        
    }
?>