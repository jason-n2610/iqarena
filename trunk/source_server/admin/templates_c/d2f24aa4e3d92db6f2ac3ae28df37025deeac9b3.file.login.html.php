<?php /* Smarty version Smarty-3.1.8, created on 2012-06-05 04:26:46
         compiled from "D:/xampp/htdocs/iqarena/source_server/admin/templates\login.html" */ ?>
<?php /*%%SmartyHeaderCode:87614fcc853f0d1b89-20184642%%*/if(!defined('SMARTY_DIR')) exit('no direct access allowed');
$_valid = $_smarty_tpl->decodeProperties(array (
  'file_dependency' => 
  array (
    'd2f24aa4e3d92db6f2ac3ae28df37025deeac9b3' => 
    array (
      0 => 'D:/xampp/htdocs/iqarena/source_server/admin/templates\\login.html',
      1 => 1338863202,
      2 => 'file',
    ),
  ),
  'nocache_hash' => '87614fcc853f0d1b89-20184642',
  'function' => 
  array (
  ),
  'version' => 'Smarty-3.1.8',
  'unifunc' => 'content_4fcc853f113030_05047960',
  'variables' => 
  array (
    'message' => 0,
  ),
  'has_nocache_code' => false,
),false); /*/%%SmartyHeaderCode%%*/?>
<?php if ($_valid && !is_callable('content_4fcc853f113030_05047960')) {function content_4fcc853f113030_05047960($_smarty_tpl) {?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Internet Dreams</title>
<link rel="stylesheet" href="../../templates/css/screen.css" type="text/css" media="screen" title="default" />
<!--  jquery core -->
<script src="../../templates/js/jquery/jquery-1.4.1.min.js" type="text/javascript"></script>

<!-- Custom jquery scripts -->
<script src="../../templates/js/jquery/custom_jquery.js" type="text/javascript"></script>

<!-- MUST BE THE LAST SCRIPT IN <HEAD></HEAD></HEAD> png fix -->
<script src="js/jquery/jquery.pngFix.pack.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function(){
$(document).pngFix( );
});
</script>
</head>
<body id="login-bg">

<!-- Start: login-holder -->
<div id="login-holder">

	<!-- start logo -->
	<div id="logo-login">
		<a href="index.html"><img src="../../templates/images/shared/logo.png" width="156" height="40" alt="" /></a>
	</div>
	<!-- end logo -->

	<div class="clear"></div>

	<!--  start loginbox ................................................................................. -->
	<div id="loginbox">

	<!--  start login-inner -->
	<div id="login-inner">
        <form action="login.php" method="post">

    		<table border="0" cellpadding="0" cellspacing="0">
            <tr><td colspan="2"><font color="red"><?php echo $_smarty_tpl->tpl_vars['message']->value;?>
</font></td></tr>
    		<tr>
    			<th>Username</th>
    			<td><input type="text" name="txtUsername" placeholder="username"  class="login-inp" /></td>
    		</tr>
    		<tr>
    			<th>Password</th>
    			<td><input type="password" name="txtPassword" placeholder="password"  onfocus="this.value=''" class="login-inp" /></td>
    		</tr>
    		<tr>
    			<th></th>
    			<td valign="top"><input type="checkbox" name="rememberme" class="checkbox-size" id="login-check" /><label for="login-check">Remember me</label></td>
    		</tr>
    		<tr>
    			<th></th>
    			<td><input type="submit" name="submit" class="submit-login"  /></td>
    		</tr>
    		</table>
        </form>
	</div>
 	<!--  end login-inner -->
	<div class="clear"></div>
	<a href="" class="forgot-pwd">Forgot Password?</a>
 </div>
 <!--  end loginbox -->

	<!--  start forgotbox ................................................................................... -->
	<div id="forgotbox">
		<div id="forgotbox-text">Please send us your email and we'll reset your password.</div>
		<!--  start forgot-inner -->
		<div id="forgot-inner">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<th>Email address:</th>
			<td><input type="text" value=""   class="login-inp" /></td>
		</tr>
		<tr>
			<th> </th>
			<td><input type="button" class="submit-login"  /></td>
		</tr>
		</table>
		</div>
		<!--  end forgot-inner -->
		<div class="clear"></div>
		<a href="" class="back-login">Back to login</a>
	</div>
	<!--  end forgotbox -->

</div>
<!-- End: login-holder -->
</body>
</html><?php }} ?>