<?PHP
	session_start();
    $path = "/iqarena/source_server/";
    $uri = $_SERVER['DOCUMENT_ROOT'];
	require $uri.$path.'include/config.php';
    require $uri.$path.'include/mysql.php';
    require $uri.$path.'modules/models/questions.php';
    $record_per_page = 30;

    if (!isset($_SESSION['current_page'])){
        $_SESSION['current_page'] = 1;
    }

    MySQL::connect();
    $objQuestionManger = new QuestionManger();

    $sub = $_GET ['sub'];
    switch ($sub) {
        case 'add' :
            $objQuestionManger->add ();
            break;
        case 'get' :
            $objQuestionManger->get ();
            break;
        case 'edit' :
            $objQuestionManger->edit ();
            break;
        case 'delete' :
            $objQuestionManger->delete ();
            break;
        case 'logout':
            session_destroy();
            header('Location: login.php');
            break;
        default :
            $objQuestionManger->get ();
            break;
    }


    //function view, add, delete, edit;

	class QuestionManger{
        public function get(){
            global $record_per_page, $smarty;
            $result = Question::getAllQuestions();
            $total_records = mysql_num_rows($result);
            $total_page = (int)($total_records/$record_per_page +1);

            // Xac dinh trang hien tai yeu cau begin
            if (!isset($_GET['page'])){
                if ($_SESSION['current_page'] > $total_page){
                    $_SESSION['current_page'] = $total_page;
                }
                else if ($_SESSION['current_page'] < 1){
                    $_SESSION['current_page'] = 1;
                }
                $current_page = $_SESSION['current_page'];
            }
            else{
                if ($_GET['page'] < 1){
                    $current_page = 1;
                }
                else if ($_GET['page'] > $total_page){
                    $current_page = $total_page;
                }
                else{
                    $current_page = $_GET['page'];
                }
            }
            $_SESSION['current_page'] = $current_page;
            // Xac dinh trang hien tai yeu cau end

            // xac dinh row bat dau trang
            $page_begin = ($current_page - 1) * $record_per_page;

            $data = Question::getQuestionByLimit($page_begin, $record_per_page);
            while($row = mysql_fetch_assoc($data)){
                $data_return[] = $row;
            }

            if (isset($_GET['perform'])){
                $perform = true;
            }
            $smarty->assign('result', $perform);
            $smarty->assign('page', $total_page);   // tong so trang
            $smarty->assign('current_page', $_SESSION['current_page']); // trang hien tai
            $smarty->assign('question_data', $data_return); // du lieu
            $smarty->display('questions.html');
        }

        public function edit(){
            global $smarty;

            // kiem tra co submit hay reset ko
            if (isset($_POST['btnEdit'])){
                $question_id = $_GET['question_id'];
                $question_name = $_POST['question_name'];
                $question_type_id = $_POST['question_type_id'];
                $answer_a = $_POST['answer_a'];
                $answer_b = $_POST['answer_b'];
                $answer_c = $_POST['answer_c'];
                $answer_d = $_POST['answer_d'];
                $answer = $_POST['answer'];
                $describle_answer = $_POST['describle_answer'];
                $result = Question::updateQuestion($question_id, $question_name, $question_type_id, $answer_a, $answer_b, $answer_c, $answer_d, $answer, $describle_answer);
                if ($result){
                    header ( "Location: questions.php?sub=get&perform=true" );
                }
                else{
                    
                }
            }
            else if (isset($_POST['btnEditReset'])){
                $this->edit();
            }
            else{
                // lay ve id cua question
                if (isset($_GET['question_id'])){
                    $question_id = $_GET['question_id'];
                }
                else{

                }
                if (isset($question_id)){
                    // lay ve du lieu cau hoi
                    $result = Question::getQuestionById($question_id);
                    while($row = mysql_fetch_assoc($result)){
                        $data[] = $row;
                    }
                    $levels = array(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);
                    $smarty->assign("question_data", $data);
                    $smarty->assign("levels", $levels);
                    $smarty->display('edit_question.html');

                }
            }

        }

        public function add(){
            global $smarty;
            if (isset($_POST['btnAdd'])){
                $question_name = $_POST['question_name'];
                $question_type_id = $_POST['question_type_id'];
                $answer_a = $_POST['answer_a'];
                $answer_b = $_POST['answer_b'];
                $answer_c = $_POST['answer_c'];
                $answer_d = $_POST['answer_d'];
                $answer = $_POST['answer'];
                $describle_answer = $_POST['describle_answer'];
                $result = Question::insertQuestion( $question_name, $question_type_id, $answer_a, $answer_b, $answer_c, $answer_d, $answer, $describle_answer);
                if ($result){
                    header ( "Location: questions.php?sub=get&perform=true" );
                }
                else{
                    $smarty->assign('message', mysql_error());
                    $smarty->display('add_question.html');
                }
            }
            else{
            $smarty->display('add_question.html');
            }
        }
        
        public function delete(){
            if (isset($_GET['question_id'])){
                $result = Question::deleteQuestion($_GET['question_id']);
                if ($result){
                    header ( "Location: questions.php?sub=get&perform=true" );
                }
            }
        }
	}
?>