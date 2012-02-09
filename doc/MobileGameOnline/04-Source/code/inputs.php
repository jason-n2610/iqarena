<?php
include("defines.php");
include("connect_to_database.inc.php");
/*$_POST["type_of_message"] = REGISTER;
$_POST["verify_code"] = "----------2dsczenoiavsakjg8bx0nvervjyb5mzvh8z793srg5z2wklgq5xlgsdonsvjd2upwctzlzlirrpigyat9n4kmdpy6og1fnd8xua64dq4o2tw0zctszl8fa";

$_POST["username"] = "lamdv8xvnabc";
$_POST["email"] = "abcd_01@gmail.com";
$_POST["password"] = md5("123qwe");
$_POST["birthday"] = "1988/10/01";
$_POST["icq"] = "_icq_??";
$_POST["aim"] = "_aim_??";
$_POST["yahoo"] = "_yahoo_??";
$_POST["msn"] = "_msn_??";
$_POST["skype"] = "_skype_??";*/

if(isset($_GET["debug"]) && $_GET["debug"]=="false"){
	define("DEBUG", "debug=false");
	$id = $_GET["id"];
}else if(isset($_GET["debug"]) && $_GET["debug"]=="true"){
	define("DEBUG", "debug=true");
	$id = $_GET["id"];
}else{
	$id = randomNumberString(5);
	die("
		<html>
		<head>
			<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />
			<script type=\"text/javascript\">
				<!--
				function redirect_to(url){
					window.location = url;
				}
				//-->
			</script>
		</head>
		<body>
			<center>
				<br /><br /><br />
				<input type=\"button\" onclick=\"redirect_to('inputs.php?id={$id}&debug=true');\" value=\"Debug Mode\"> <input type=\"button\" onclick=\"redirect_to('inputs.php?id={$id}&debug=false');\" value=\"Normal Mode\"><br /><br />
				Debug Mode: Chế độ chạy hiển thị tòan bộ thông tin để dễ dàng debug<br />
				Normal Mode: Chế độ chạy bt
			</center>
		</body>
		</html>
		");
}

$str_tr = "";
switch($_GET["type"]){
	case "":
	case "REGISTER":{
		$title = "REGISTER";
		$str_tr .= "<input type=hidden name=\"type_of_message\" value=\"".REGISTER."\">";
		$str_tr .= makeLine("username");
		$str_tr .= makeLine("email");
		$str_tr .= makeLine("password");
		$str_tr .= makeLineWithNote("birthday", "(yyyy/mm/dd)");
		$str_tr .= makeLine("icq");
		$str_tr .= makeLine("aim");
		$str_tr .= makeLine("yahoo");
		$str_tr .= makeLine("msn");
		$str_tr .= makeLine("skype");
		
		$guide = "Chỉ username & email chưa tồn tại thì mới có khả năng đăng kí được<br />
				Các username đã tồn tại: <font color=green>". getFieldDataFromDB("username", FORUM_DB_PREFIX."user") ."</font><br />
				Các email đã tồn tại: <font color=green>". getFieldDataFromDB("email", FORUM_DB_PREFIX."user") ."</font><br />
				";
		break;
	}
	case "LOGIN":{
		$title = "LOGIN";
		$str_tr .= "<input type=hidden name=\"type_of_message\" value=\"".LOGIN."\">";
		$str_tr .= makeLine("username");
		$str_tr .= makeLine("password");
		
		$guide = "Chương trình sẽ trả về cho bạn <b>'verify_code'</b> khi bạn login thành công. Và bạn dùng <b>'verify_code'</b>. này để làm mã đăng nhập trong các trang khác. Mã này cho biết bạn là ai, và có đang login hay không?<br />
				Các username đã tồn tại: <font color=green>". getFieldDataFromDB("username", FORUM_DB_PREFIX."user") ."</font><br />";
		break;
	}
	case "FORGOT_PASSWORD":{
		$title = "FORGOT PASSWORD";
		$str_tr .= "<input type=hidden name=\"type_of_message\" value=\"".FORGOT_PASSWORD."\">";
		$str_tr .= makeLine("username");
		
		$guide = "Các username đã tồn tại: <font color=green>". getFieldDataFromDB("username", FORUM_DB_PREFIX."user") ."</font><br />
				Chương trình sẽ gửi 1 email có chứa link thay đổi password cho bạn ngay khi bạn submit lên server. (Nhớ kiểm tra cả phần SPAM của email nhé)";
		break;
	}
	case "VIEW_USER_INFO":{
		$title = "VIEW USER's INFO";
		$str_tr .= "<input type=hidden name=\"type_of_message\" value=\"".VIEW_USER_INFO."\">";
		$str_tr .= makeLine("verify_code");
		$str_tr .= makeLine("username");
		
		$guide = "Bạn phải Login thì mới sử dụng được chức năng này. Copy trường 'verify_code' được server trả về khi login thành công vào trường 'verify_code' bên trên<br />
				Các username đã tồn tại: <font color=green>". getFieldDataFromDB("username", FORUM_DB_PREFIX."user") ."</font><br />
				Bạn nhập username của người mà bạn muốn xem thông tin:<br />
				- Nếu là chính bạn: sẽ trả về 8 trường<br />
				- Nếu là người khác: sẽ trả về 2 trường: 'username' và 'join date'
				
				";
		break;
	}
	case "UPDATE_USER_INFO":{
		$title = "UPDATE USER's INFO";
		$str_tr .= "<input type=hidden name=\"type_of_message\" value=\"".UPDATE_USER_INFO."\">";
		$str_tr .= makeLine("verify_code");
		$str_tr .= makeLine("email");
		$str_tr .= makeLine("old_password");
		$str_tr .= makeLine("new_password");
		$str_tr .= makeLine("birthday");
		$str_tr .= makeLine("icq");
		$str_tr .= makeLine("aim");
		$str_tr .= makeLine("yahoo");
		$str_tr .= makeLine("msn");
		$str_tr .= makeLine("skype");
		
		$guide = "Bạn phải Login thì mới sử dụng được chức năng này. Copy trường 'verify_code' được server trả về khi login thành công vào trường 'verify_code' bên trên<br />
				Nếu không muốn thay đổi password thì không điền vào 2 trường <b>'old_password'</b> và trường <b>'new_password'</b><br />
				";
		break;
	}
	case "LOGOUT":{
		$title = "LOGOUT";
		$str_tr .= "<input type=hidden name=\"type_of_message\" value=\"".LOGOUT."\">";
		$str_tr .= makeLine("verify_code");
		
		$guide = "Bạn phải Login thì mới sử dụng được chức năng này. Copy trường 'verify_code' được server trả về khi login thành công vào trường 'verify_code' bên trên<br />";
		break;
	}
	case "ADD_FRIEND":{
		$title = "ADD FRIEND";
		$str_tr .= "<input type=hidden name=\"type_of_message\" value=\"".ADD_FRIEND."\">";
		$str_tr .= makeLine("verify_code");
		$str_tr .= makeLine("username");
		
		$guide = "Bạn phải Login thì mới sử dụng được chức năng này. Copy trường 'verify_code' được server trả về khi login thành công vào trường 'verify_code' bên trên<br />
				Khi add/delete 1 người bạn thì bạn nên vào phần get_friends_list để xem kết quả có đứng không?<br />
				Các username đã tồn tại: <font color=green>". getFieldDataFromDB("username", FORUM_DB_PREFIX."user") ."</font><br />
				";
		break;
	}
	case "DELETE_FRIEND":{
		$title = "DELETE FRIEND";
		$str_tr .= "<input type=hidden name=\"type_of_message\" value=\"".DELETE_FRIEND."\">";
		$str_tr .= makeLine("verify_code");
		$str_tr .= makeLine("username");
		
		$guide = "Bạn phải Login thì mới sử dụng được chức năng này. Copy trường 'verify_code' được server trả về khi login thành công vào trường 'verify_code' bên trên<br />
				Khi add/delete 1 người bạn thì bạn nên vào phần get_friends_list để xem kết quả có đứng không?<br />
				Các username đã tồn tại: <font color=green>". getFieldDataFromDB("username", FORUM_DB_PREFIX."user") ."</font><br />
				";
		break;
	}
	case "GET_FRIEND_LIST":{
		$title = "GET FRIEND LIST";
		$str_tr .= "<input type=hidden name=\"type_of_message\" value=\"".GET_FRIEND_LIST."\">";
		$str_tr .= makeLine("verify_code");
		$str_tr .= makeLine("n");
		$str_tr .= makeLine("max_numbers");
		$str_tr .= "<tr><td>sort_by</td><td>
							<select name=\"sort_by\">
								<option value=\"".SORT_BY_NAME."\">SORT_BY_NAME
								<option value=\"".SORT_BY_DATE_ADD."\">SORT_BY_DATE_ADD
							</select>
						</td></tr>";
		
		$guide = "Bạn phải Login thì mới sử dụng được chức năng này. Copy trường 'verify_code' được server trả về khi login thành công vào trường 'verify_code' bên trên<br />
				Các username đã tồn tại: <font color=green>". getFieldDataFromDB("username", FORUM_DB_PREFIX."user") ."</font><br />
				Trường 'n': trong danh sách các friends xắp xếp theo thứ tự tăng dần, chương trình sẽ lấy bắt dầu từ friend thứ 'n' (>=n)
				Trường 'max_numbers': Số lượng friend lớn nhất muốn lấy. Tức là sẽ lấy các friend từ 'n' đến ('n' + 'max_number' - 1)
				";
		break;
	}
	case "NEW_GAME":{
		$title = "NEW GAME";
		$str_tr .= "<input type=hidden name=\"type_of_message\" value=\"".NEW_GAME."\">";
		$str_tr .= makeLine("verify_code");
		$str_tr .= makeLine("type_of_game");
		$str_tr .= makeLine("search_pattern");
		
		$guide = "Bạn phải Login thì mới sử dụng được chức năng này. Copy trường 'verify_code' được server trả về khi login thành công vào trường 'verify_code' bên trên<br />
				Trường 'type_of_game': loại của game bạn muốn tạo. Thông thường là 1 (Cờ tướng)<br />
				Trường 'search_pattern': Nội dung search, bao gồm các trường 1-(pattern nào, thông thường = 1, các pattern khác chưa được cài đặt), 2-(usernamer của đối phương), 3-(lời mời cảu bạn). Các trường này cách nhau bởi dấu ':'<br />
				VD: <b>'1:admin:Bạn chơi với mình nhé?'</b> (mời 'admin' chơi game)<br />
				Các username đã tồn tại: <font color=green>". getFieldDataFromDB("username", FORUM_DB_PREFIX."user") ."</font><br />
				";
		break;
	}
	case "GET_NEW_INFO_BITS":{
		$title = "GET_NEW_INFO_BITS";
		$str_tr .= "<input type=hidden name=\"type_of_message\" value=\"".GET_NEW_INFO_BITS."\">";
		$str_tr .= makeLine("verify_code");
		
		break;
	}
	case "GET_LIST_GAME_STATUS":{
		$title = "GET LIST GAME STATUS";
		$str_tr .= "<input type=hidden name=\"type_of_message\" value=\"".GET_LIST_GAME_STATUS."\">";
		$str_tr .= makeLine("verify_code");
		$str_tr .= makeLine("type_of_game");
		
		$guide = "Bạn phải Login thì mới sử dụng được chức năng này. Copy trường 'verify_code' được server trả về khi login thành công vào trường 'verify_code' bên trên<br />
				Kết quả trả về là các <b>số lượng</b> các game mà bạn đã chơi, đang chơi, đang chờ,...<br />
				Trường <b>'type_of_game'</b>: loại của game bạn muốn lấy danh sách các game. Thông thường là 1 (Cờ tướng)<br />
				";
		break;
	}
	case "GET_LIST_GAME":{
		$title = "GET_LIST_GAME";
		$str_tr .= "<input type=hidden name=\"type_of_message\" value=\"".GET_LIST_GAME."\">";
		$str_tr .= makeLine("verify_code");
		$str_tr .= makeLine("type_of_game");
		$str_tr .= "<tr><td>state_of_game</td><td>
							<select name=\"state_of_game\">
								<option value=\"".STATE_PLAYING."\">STATE_PLAYING
								<option value=\"".STATE_WAITING."\">STATE_WAITING
								<option value=\"".STATE_INVITING."\">STATE_INVITING
								<option value=\"".STATE_INVITED."\">STATE_INVITED
								<option value=\"".STATE_ABORTED."\">STATE_ABORTED
								<option value=\"".STATE_WIN."\">STATE_WIN
								<option value=\"".STATE_LOST."\">STATE_LOST
								<option value=\"".STATE_DRAW."\">STATE_DRAW
							</select>
						</td></tr>";
		$str_tr .= makeLine("max_numbers");
		$str_tr .= makeLine("n");
		
		$guide = "Bạn phải Login thì mới sử dụng được chức năng này. Copy trường 'verify_code' được server trả về khi login thành công vào trường 'verify_code' bên trên<br />
				Kết quả trả về là danh sách các game mà bạn đã chơi, đang chơi, đang chờ,... tùy theo loại mà bạn muốn chọn<br />
				Trường <b>'type_of_game'</b>: loại của game bạn muốn lấy danh sách các game. Thông thường là 1 (Cờ tướng)<br />
				Trường <b>'n'</b>: Sắp xếp các game theo chiều tăng dần. Lấy từ game thứ 'n'<br />
				Trường <b>'max_numbers'</b>: Số game tối đa sẽ lấy<br />
				";
		break;
	}
	case "ACTION":{
		$title = "ACTION";
		$str_tr .= "<input type=hidden name=\"type_of_message\" value=\"".ACTION."\">";
		$str_tr .= makeLine("verify_code");
		$str_tr .= makeLine("game_id");
		$str_tr .= "<tr><td>type_of_action</td><td>
							<select name=\"type_of_action\">
								<option value=\"".ACT_MOVE."\">MOVE
								<option value=\"".ACT_ABORT."\">ABORT
								<option value=\"".ACT_RESIGN."\">RESIGN
								<option value=\"".ACT_DRAW."\">RAW
								<option value=\"".ACT_RETURN_DRAW."\">RETURN_DRAW
								<option value=\"".ACT_TAKE_BACK."\">TAKE_BACK
								<option value=\"".ACT_RETURN_TAKE_BACK."\">RETURN_TAKE_BACK
								<option value=\"".ACT_END_GAME."\">END_GAME
							</select>
						</td></tr>";
		$str_tr .= makeLine("action");
		
		$guide = "Bạn phải Login thì mới sử dụng được chức năng này. Copy trường 'verify_code' được server trả về khi login thành công vào trường 'verify_code' bên trên<br />
				Kết quả trả về là trạng thái action của bạn có được thực hiện đc ko?<br />
				Trường <b>'game_id'</b>: Số ID của game (Lấy từ trang list_game)<br />
				Trường <b>'action'</b> (các trường nhỏ cách nhau bởi dấu ':'):
				<table border=1>
					<tr><th>Trường 'type_of_action'</th><th colspan=4>Trường 'action'</th></tr>
					<tr><td>MOVE</td><td>X_source</td><td>Y_source</td><td>X_target</td><td>Y_target</td></tr>
					<tr><td>DRAW</td><td>Message</td><td></td><td></td><td></td></tr>
					<tr><td>RETURN_DRAW</td><td>Message</td><td>return(Accept/Decline)</td><td></td><td></td></tr>
					<tr><td>RESIGN</td><td>Message</td><td></td><td></td><td></td></tr>
					<tr><td>ABORT</td><td>Message</td><td></td><td></td><td></td></tr>
					<tr><td>TAKE_BACK</td><td>Message</td><td></td><td></td><td></td></tr>
					<tr><td>RETURN_TAKE_BACK</td><td>Message</td><td>return(Accept/Decline)</td><td></td><td></td></tr>
					<tr><td>END_GAME</td><td></td><td></td><td></td><td></td></tr>
				</table>
				VD: nếu 'type_of_action' là MOVE thì điền vào trường 'action' là <b>'1:1:4:5'</b>. (Với ý nghĩa là di chuyển quân từ ô(1,1) đến ô(4,5))
				";
		break;
	}
	case "GET_CURRENT_CHESS_BOARD_STATE":{
		$title = "GET CURRENT CHESS BOARD STATE";
		$str_tr .= "<input type=hidden name=\"type_of_message\" value=\"".GET_CURRENT_CHESS_BOARD_STATE."\">";
		$str_tr .= makeLine("verify_code");
		$str_tr .= makeLine("game_id");
		
		$guide = "Bạn phải Login thì mới sử dụng được chức năng này. Copy trường 'verify_code' được server trả về khi login thành công vào trường 'verify_code' bên trên<br />
				Kết quả trả về là vị trí các quân cờ trên bàn cờ<br />
				";
		break;
	}
	case "GET_LASTEST_ACTIONS":{
		$title = "GET LASTEST ACTIONS";
		$str_tr .= "<input type=hidden name=\"type_of_message\" value=\"".GET_LASTEST_ACTIONS."\">";
		$str_tr .= makeLine("verify_code");
		$str_tr .= makeLine("game_id");
		$str_tr .= makeLine("n");
		
		$guide = "Bạn phải Login thì mới sử dụng được chức năng này. Copy trường 'verify_code' được server trả về khi login thành công vào trường 'verify_code' bên trên<br />
				Kết quả trả về là danh sách các actions<br />
				Trường <b>'game_id'</b>: Số ID của game<br />
				Trường <b>'n'</b>: Lấy các action từ action thứ 'n' trở đi (>=n)
				";
		break;
	}
	default:{
		break;
	}
}
?>
<html>
<head><meta http-equiv="Content-Type" content="text/html; charset=utf-8" /></head>
<body>
<a href="inputs.php?id=<?php echo($id);?>&type=REGISTER&<?php echo(DEBUG); ?>">Register</a>, 
<a href="inputs.php?id=<?php echo($id);?>&type=LOGIN&<?php echo(DEBUG); ?>">Login</a>, 
<a href="inputs.php?id=<?php echo($id);?>&type=FORGOT_PASSWORD&<?php echo(DEBUG); ?>">Forgot Password</a>, 
<a href="inputs.php?id=<?php echo($id);?>&type=VIEW_USER_INFO&<?php echo(DEBUG); ?>">View User's Info</a>, 
<a href="inputs.php?id=<?php echo($id);?>&type=UPDATE_USER_INFO&<?php echo(DEBUG); ?>">Update User's Info</a>, 
<a href="inputs.php?id=<?php echo($id);?>&type=LOGOUT&<?php echo(DEBUG); ?>">Logout</a>
<br />
<a href="inputs.php?id=<?php echo($id);?>&type=ADD_FRIEND&<?php echo(DEBUG); ?>">Add Friend</a>, 
<a href="inputs.php?id=<?php echo($id);?>&type=DELETE_FRIEND&<?php echo(DEBUG); ?>">Delete Friend</a>, 
<a href="inputs.php?id=<?php echo($id);?>&type=GET_FRIEND_LIST&<?php echo(DEBUG); ?>">Get Friends List</a>
<br />
<a href="inputs.php?id=<?php echo($id);?>&type=NEW_GAME&<?php echo(DEBUG); ?>">New Game</a>,
<a href="inputs.php?id=<?php echo($id);?>&type=GET_NEW_INFO_BITS&<?php echo(DEBUG); ?>">Get New Info Bits</a>,
<a href="inputs.php?id=<?php echo($id);?>&type=GET_LIST_GAME_STATUS&<?php echo(DEBUG); ?>">Get List Game Status</a>,
<a href="inputs.php?id=<?php echo($id);?>&type=GET_LIST_GAME&<?php echo(DEBUG); ?>">Get List Game</a>,
<a href="inputs.php?id=<?php echo($id);?>&type=ACTION&<?php echo(DEBUG); ?>">Action</a>,
<a href="inputs.php?id=<?php echo($id);?>&type=GET_CURRENT_CHESS_BOARD_STATE&<?php echo(DEBUG); ?>">Get Current Chess Board State</a>,
<a href="inputs.php?id=<?php echo($id);?>&type=GET_LASTEST_ACTIONS&<?php echo(DEBUG); ?>">Get Lastest Actions</a>
<center>
<h1><?php echo($title);?></h1>

<form action="home.php?id=<?php echo($id);?>&<?php echo(DEBUG); ?>" method="post"><table height="200px"><tr><td valign="top">
<table>
	<?php echo($str_tr);?>
	<tr><td></td><td><input type="submit"></td></tr>
</table></td></tr></table>
</form>
</center>
<hr />
<?php echo($guide); ?>
</body>
</html>
<?php
function makeLine($str){
	if($str == "verify_code"){
		return "<tr><td>{$str}</td><td><input type=text name=\"{$str}\" value=\"".$_COOKIE[$GLOBALS["id"]]."\"></td><td></td></tr>";
	}else if($str == "max_numbers"){
		return "<tr><td>{$str}</td><td><input type=text name=\"{$str}\" value=\"10\"></td><td></td></tr>";
	}else if($str == "type_of_game"){
		return "<tr><td>{$str}</td><td><input type=text name=\"{$str}\" value=\"1\"></td><td></td></tr>";
	}
	
	return "<tr><td>{$str}</td><td><input type=text name=\"{$str}\"></td><td></td></tr>";
}
function makeLineWithNote($str, $note){
	return "<tr><td>{$str}</td><td><input type=text name=\"{$str}\"></td><td>{$note}</td></tr>";
}
?>
<?php
function getFieldDataFromDB($fieldName, $table){	//get all, return STRING
	$query = "SELECT DISTINCT {$fieldName} FROM {$table}";
	$result = mysql_query($query, $GLOBALS["link"]);
	
	while($row = mysql_fetch_array($result, MYSQL_ASSOC)){
		$rt[] = $row[$fieldName];
	}
	
	return ("'". implode("', '", $rt) ."'");
}
function getUserID($username){
	$query = "SELECT * FROM ".FORUM_DB_PREFIX."user WHERE username='{$username}'";
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
	$row = mysql_fetch_array($result, MYSQL_ASSOC);
	return $row["userid"];
}
function getUserName($userid){
	$query = "SELECT * FROM ".FORUM_DB_PREFIX."user WHERE userid='{$userid}'";
	$result = mysql_query($query, $GLOBALS["link"]);
	checkQuerySuccess($result);
	$row = mysql_fetch_array($result, MYSQL_ASSOC);
	return $row["username"];
}
?>
<?php
function randomNumberString($n){
	$rt = "";
	for($i=0; $i<$n; $i++){
		$rt .= rand(0, 9);;
	}
	return $rt;
}
?>