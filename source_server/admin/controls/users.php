<?PHP

    session_start();
    $path = "/iqarena/source_server/";
    $uri = $_SERVER['DOCUMENT_ROOT'];
    require $uri . $path . 'include/config.php';
    require $uri . $path . 'include/mysql.php';
    require $uri . $path . 'modules/models/user.php';
    $record_per_page = 30;

    if (!isset($_SESSION['current_page_user']))
    {
        $_SESSION['current_page_user'] = 1;
    }
    if (!isset($_SESSION['current_page_admin'])){
        $_SESSION['current_page_admin'] = 1;
    }

    if (!isset($_SESSION['mark_page_user'])){
        $_SESSION['mark_page_user'] = 'get';
    }

    MySQL::connect();
    $objUserManager = new UserManager();

    $sub = $_GET['sub'];
    switch ($sub)
    {
        case 'add':
            $objUserManager->add();
            break;
        case 'get':
            $objUserManager->get();
            break;
        case 'edit':
            $objUserManager->edit();
            break;
        case 'delete':
            $objUserManager->delete();
            break;
        case 'deleteall':
            $objUserManager->deleteall();
            break;
        case 'admin':
            $objUserManager->admin();
            break;
        case 'logout':
            session_destroy();
            header('Location: login.php');
            break;
        default:
            $objUserManager->get();
            break;
    }


    //function view, add, delete, edit;

    class UserManager
    {
        public function get()
        {
            global $record_per_page, $smarty;
            $_SESSION['mark_page_user'] = 'get';
            $result = User::getAllUser();
            $total_records = mysql_num_rows($result);
            $total_page = (int)($total_records / $record_per_page + 1);

            // Xac dinh trang hien tai yeu cau begin
            if (!isset($_GET['page']))
            {
                if ($_SESSION['current_page_user'] > $total_page)
                {
                    $_SESSION['current_page_user'] = $total_page;
                } else
                    if ($_SESSION['current_page_user'] < 1)
                    {
                        $_SESSION['current_page_user'] = 1;
                    }
                $current_page = $_SESSION['current_page_user'];
            } else
            {
                if ($_GET['page'] < 1)
                {
                    $current_page = 1;
                } else
                    if ($_GET['page'] > $total_page)
                    {
                        $current_page = $total_page;
                    } else
                    {
                        $current_page = $_GET['page'];
                    }
            }
            $_SESSION['current_page_user'] = $current_page;
            // Xac dinh trang hien tai yeu cau end

            // xac dinh row bat dau trang
            $page_begin = ($current_page - 1) * $record_per_page;

            $data = User::getUsersByLimit($page_begin, $record_per_page);
            while ($row = mysql_fetch_assoc($data))
            {
                $data_return[] = $row;
            }

            if (isset($_GET['perform']))
            {
                $perform = true;
            }
            $smarty->assign('result', $perform);
            $smarty->assign('page', $total_page); // tong so trang
            $smarty->assign('current_page', $_SESSION['current_page_user']); // trang hien tai
            $smarty->assign('user_data', $data_return); // du lieu
            $smarty->display('users.html');
        }

        public function edit()
        {
            global $smarty;

            // kiem tra co submit hay reset ko
            if (isset($_POST['btnEdit']))
            {
                $user_id = $_GET['user_id'];
                $power_user = $_POST['power_user'];
                $score_level = $_POST['score'];
                $money = $_POST['money'];

                if ($power_user == '' || $score_level == '' || $money == '')
                {
                    $smarty->assign('message', "You must enter full information");
                    // lay ve id cua question
                    if (isset($_GET['user_id']))
                    {
                        $user_id = $_GET['user_id'];
                    } else
                    {

                    }
                    if (isset($user_id))
                    {
                        // lay ve du lieu cau hoi
                        $result = User::getUserById($user_id);
                        while ($row = mysql_fetch_assoc($result))
                        {
                            $data[] = $row;
                        }
                        $smarty->assign("user_data", $data);
                        $smarty->display('edit_user.html');

                    }
                } else
                {
                    $result = User::updateUser($user_id, $power_user, $score_level, $money);
                    if ($result)
                    {
                        if ($_SESSION['mark_page_user'] == 'get')
                        {
                            header("Location: users.php?sub=get&perform=true");
                        } else
                            if ($_SESSION['mark_page_user'] == 'admin')
                            {
                                header("Location: users.php?sub=admin&perform=true");
                            }
                    } else
                    {

                    }
                }
            } else
            {
                // lay ve id cua question
                if (isset($_GET['user_id']))
                {
                    $user_id = $_GET['user_id'];
                } else
                {

                }
                if (isset($user_id))
                {
                    // lay ve du lieu cau hoi
                    $result = User::getUserById($user_id);
                    while ($row = mysql_fetch_assoc($result))
                    {
                        $data[] = $row;
                    }
                    $smarty->assign("user_data", $data);
                    $smarty->display('edit_user.html');

                }
            }

        }

        public function add()
        {
            global $smarty;
            if (isset($_POST['btnAdd']))
            {
                $username = $_POST['username'];
                $password = $_POST['password'];
                $re_password = $_POST['re_password'];
                $email = $_POST['email'];
                $power_user = $_POST['power_user'];

                $username = stripcslashes($username);
                $username = mysql_real_escape_string($username);
                $password = stripcslashes($password);
                $password = mysql_real_escape_string($password);
                $email = stripcslashes($email);
                $email = mysql_real_escape_string($email);
                $power_user = stripcslashes($power_user);
                $power_user = mysql_real_escape_string($power_user);

                if ($username == '' || $password == '' || $email == '' || $power_user == '')
                {
                    $smarty->assign('message', "You must enter full information");
                    $smarty->display('add_user.html');
                    exit();
                } else if ($password != $re_password){
                    $smarty->assign('message', "Two password do not the same");
                    $smarty->display('add_user.html');
                    exit();
                }
                else if (!$this->checkEmail($email)){
                    $smarty->assign('message', "Email incorrect");
                    $smarty->display('add_user.html');
                    exit();
                }
                else {
                    $checkUserExist = User::getUserByUserName($username);
                    if (mysql_num_rows($checkUserExist) > 0){
                        $smarty->assign('message', "User exits");
                        $smarty->display('add_user.html');
                        exit();
                    }
                    else{
                        $result = User::insertUser($username, md5($password), $email, $power_user);
                        if ($result)
                        {
                            if ($_SESSION['mark_page_user'] == 'get')
                            {
                                header("Location: users.php?sub=get&perform=true");
                            }
                            else if ($_SESSION['mark_page_user'] == 'admin')
                            {
                                header("Location: users.php?sub=admin&perform=true");
                            }
                        } else
                        {
                            $smarty->assign('message', mysql_error());
                            $smarty->display('add_user.html');
                        }
                    }
                }
            } else
            {
                $smarty->display('add_user.html');
            }
        }

        public function delete()
        {
            if (isset($_GET['user_id']))
            {
                $result = User::deleteUser($_GET['user_id']);
                if ($result)
                {
                    if ($_SESSION['mark_page_user'] == 'get')
                    {
                        header("Location: users.php?sub=get&perform=true");
                    }
                    else if ($_SESSION['mark_page_user'] == 'admin')
                    {
                        header("Location: users.php?sub=admin&perform=true");
                    }
                }
            }
        }

        public function deleteall()
        {
            foreach ($_POST['need_delete'] as $id => $value)
            {
                User::deleteUser($id);
            }
            if ($_SESSION['mark_page_user'] == 'get')
            {
                header("Location: users.php?sub=get&perform=true");
            } else
                if ($_SESSION['mark_page_user'] == 'admin')
                {
                    header("Location: users.php?sub=admin&perform=true");
                }
        }

        public function admin(){

            global $record_per_page, $smarty;
            $_SESSION['mark_page_user'] = 'admin';
            $result = User::getAllAdmin();
            $total_records = mysql_num_rows($result);
            $total_page = (int)($total_records / $record_per_page + 1);

            // Xac dinh trang hien tai yeu cau begin
            if (!isset($_GET['page']))
            {
                if ($_SESSION['current_page_admin'] > $total_page)
                {
                    $_SESSION['current_page_admin'] = $total_page;
                } else
                    if ($_SESSION['current_page_admin'] < 1)
                    {
                        $_SESSION['current_page_admin'] = 1;
                    }
                $current_page = $_SESSION['current_page_admin'];
            } else
            {
                if ($_GET['page'] < 1)
                {
                    $current_page = 1;
                } else
                    if ($_GET['page'] > $total_page)
                    {
                        $current_page = $total_page;
                    } else
                    {
                        $current_page = $_GET['page'];
                    }
            }
            $_SESSION['current_page_admin'] = $current_page;
            // Xac dinh trang hien tai yeu cau end

            // xac dinh row bat dau trang
            $page_begin = ($current_page - 1) * $record_per_page;

            $data = User::getAdminsByLimit($page_begin, $record_per_page);
            while ($row = mysql_fetch_assoc($data))
            {
                $data_return[] = $row;
            }

            if (isset($_GET['perform']))
            {
                $perform = true;
            }
            $smarty->assign('result', $perform);
            $smarty->assign('page', $total_page); // tong so trang
            $smarty->assign('current_page', $_SESSION['current_page_admin']); // trang hien tai
            $smarty->assign('user_data', $data_return); // du lieu
            $smarty->display('admin.html');
        }

        function checkEmail($email)
        {
            if (preg_match("/^([a-zA-Z0-9])+([a-zA-Z0-9\._-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9\._-]+)+$/", $email))
            {
                list($username, $domain) = split('@', $email);
                if (!checkdnsrr($domain, 'MX'))
                {
                    return false;
                }
                return true;
            }
            return false;
        }
  }

?>