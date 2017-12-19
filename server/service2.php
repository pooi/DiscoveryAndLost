<?php

	function replaceSqlString($s1){

		$s1 = str_replace("\"", "\\\\\"", $s1);
		$s1 = str_replace("'", "\\'", $s1);
		$s1 = str_replace("\\n", "\\\\n", $s1);

		return $s1;
	}

	function getMilliseconds($year, $month, $day){
		if(strlen($month) <2){
				$month = "0".$month;
		}
		if(strlen($day) <2){
				$day = "0".$day;
		}
		$dob = strtotime($year.'-'.$month.'-'.$day) * 1000;
		return $dob;
	}

	ini_set('max_execution_time','600');
	ini_set('max_input_time','600');
	ini_set('memory_limit','100');
	ini_set('post_max_size','10M');
	ini_set('upload_max_filesize','10M');

	require_once('dbConnect2.php');

  $service = $_POST['service'];

	if($service == null){
		echo "error";
	}else{

		if($service == "login_user"){

		  $login_id = $_POST['login_id'];
		  $login_pw = $_POST['login_pw'];

			$sql = "SELECT * FROM users WHERE student_id='$login_id' AND pw='$login_pw';";
			// echo $sql;

		  $ret = mysqli_query($con, $sql);
		  if($ret){
		    $count = mysqli_num_rows($ret);
		  }else{
		    exit();
		  }

		  echo "{\"status\":\"OK\",\"num_result\":\"$count\",\"db_version\":\"1\",\"result\":[";

		  $i=0;

		  while($row = mysqli_fetch_array($ret)){

		    $id = $row['id'];
		    $student_id = $row['student_id'];
		    $pw = $row['pw'];
		    $email = $row['email'];
		    $first_name = $row['first_name'];
		    $last_name = $row['last_name'];
		    $phone = $row['phone'];
		    $registered_date = $row['registered_date'];

		    echo "{\"id\":\"$id\",
		    \"student_id\":\"$student_id\",
		    \"pw\":\"$pw\",
		    \"email\":\"$email\",
		    \"first_name\":\"$first_name\",
		    \"last_name\":\"$last_name\",
		    \"phone\":\"$phone\",
		    \"registered_date\":\"$registered_date\"
		    }";

		    if($i<$count-1){
		      echo ",";
		    }

		    $i++;

		  }

		  echo "]}";

		}

	}



	mysqli_close($con);
?>
