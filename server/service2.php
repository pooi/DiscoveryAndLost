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
		else if($service == 'registerLost'){
			$rgt_user = $_POST['rgt_user'];
			$category = $_POST['category'];
			$color = $_POST['color'];
			$brand = $_POST['brand'];
			$building = $_POST['building'];
			$room = $_POST['room'];
			$tags = $_POST['tags'];
			$description = $_POST['description'];
			// echo $dob;
			$photos = $_POST['photos'];

			// echo '<br>';
			$current_time = time() * 1000;
			// echo 'timestamp : ' . $timestamp;
			// echo '<br>';

			$sql = "INSERT INTO
					lost(category,color,brand,building,room,tags,description,photos,rgt_user,registered_date)
					VALUES('$category','$color','$brand','$building', '$room','$tags','$description', '$photos', '$rgt_user','$current_time');";
			//echo $sql;
			$ret = mysqli_query($con, $sql);
			if($ret == '1'){
				echo json_encode(array('status'=>'success', 'message'=>"save success"));
			}else{
				echo json_encode(array('status'=>'fail', 'message'=>"save fail"));
			}

		}
		else if($service == 'Request'){
			$lost_id = $_POST['lost_id'];
			$user_id = $_POST['user_id'];
			$category = $_POST['category'];
			$color = $_POST['color'];
			$brand = $_POST['brand'];
			$building = $_POST['building'];
			$room = $_POST['room'];
			$tags = $_POST['tags'];
			$lost_date = $_POST['lost_date'];
			// echo $dob;
			$match = $_POST['match'];
			// echo '<br>';
			$current_time = time() * 1000;
			// echo 'timestamp : ' . $timestamp;
			// echo '<br>';

			$sql = "INSERT INTO item_matching(lost_id,user_id,category,color,brand,building,room,tags,match_ratio,registered_date,lost_date) VALUES('$lost_id','$user_id','$category','$color','$brand','$building', '$room', '$tags', '$match','$current_time','0');";
			//echo $sql;
			$ret = mysqli_query($con, $sql);
			$item_matching_id = mysqli_insert_id($con);
			if($ret == '1'){
				$sql2= "INSERT INTO request(lost_id,item_matching_id,match_ratio,request_user,request_date) VALUES('$lost_id','$item_matching_id','$match','$user_id','$current_time');";
				$ret2 = mysqli_query($con, $sql2);
				if($ret2 == '1'){
						echo json_encode(array('status'=>'success', 'message'=>"request success"));
				}
				else{
					echo json_encode(array('status'=>'fail', 'message'=>"request fail"));
				}
			}else{
				echo json_encode(array('status'=>'fail', 'message'=>"save fail"));
			}

		}
		else if($service == 'registerRequest'){
			$lost_id = $_POST['lost_id'];
			$item_matching_id = $_POST['item_matching_id'];
			$match = $_POST['match'];
			$request_date = $_POST['request_date'];
			// echo '<br>';
			$current_time = time() * 1000;
			// echo 'timestamp : ' . $timestamp;
			// echo '<br>';

			$sql = "INSERT INTO
					request(lost_id,item_matching_id,match_ratio,request_date,request_date)
					VALUES('$lost_id','$item_matching_id','$match','$current_time','$current_time');";
			// echo $sql;
			$ret = mysqli_query($con, $sql);
			if($ret == '1'){
				echo json_encode(array('status'=>'success', 'message'=>"save success"));
			}else{
				echo json_encode(array('status'=>'fail', 'message'=>"save fail"));
			}

		}
		else if($service == "searchLost"){
			$category = $_POST['category'];
			$color = $_POST['color'];
			$brand = $_POST['brand'];
			$building = $_POST['building'];
			$room = $_POST['room'];


			$sql = "SELECT * FROM lost WHERE (category like '%$category%' and color like '%$color%') and (brand like '%$brand%' or building like '%$building%' or room like '%$room%');";
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
				$category = $row['category'];
				$color = $row['color'];
				$brand = $row['brand'];
				$building = $row['building'];
				$room = $row['room'];
				$tags = $row['tags'];
				$description = $row['description'];
				$photos = $row['photos'];
				$rgt_user = $row['rgt_user'];
				$rcv_user = $row['rcv_user'];
				$registered_date = $row['registered_date'];
				$received_date = $row['$received_date'];

                echo "{\"id\":\"$id\",
				\"category\":\"$category\",
				\"color\":\"$color\",
				\"brand\":\"$brand\",
				\"building\":\"$building\",
				\"room\":\"$room\",
				\"tags\":\"$tags\",
				\"description\":\"$description\",
				\"photos\":\"$photos\",
				\"rgt_user\":\"$rgt_user\",
				\"rcv_user\":\"$rcv_user\",
				\"registered_date\":\"$registered_date\",
				\"received_date\":\"$received_date\"
				}";

                if($i<$count-1){
                    echo ",";
                }

                $i++;

            }

            echo "]}";

		}
		else if($service == "itemMatching"){
			$lost_id = $_POST['lost_id'];
			$id = $_POST['id'];
			$sql = "SELECT * FROM lost WHERE category like '%$category%' or color like '%$color%' or brand like '%$brand%' or building like '%$building%' or room like '%$room%';";
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
				$category = $row['category'];
				$color = $row['color'];
				$brand = $row['brand'];
				$building = $row['building'];
				$room = $row['room'];
				$tags = $row['tags'];
				$description = $row['description'];
				$photos = $row['photos'];
				$rgt_user = $row['rgt_user'];
				$rcv_user = $row['rcv_user'];
				$registered_date = $row['registered_date'];
				$received_date = $row['$received_date'];

                echo "{\"id\":\"$id\",
				\"category\":\"$category\",
				\"color\":\"$color\",
				\"brand\":\"$brand\",
				\"building\":\"$building\",
				\"room\":\"$room\",
				\"tags\":\"$tags\",
				\"description\":\"$description\",
				\"photos\":\"$photos\",
				\"rgt_user\":\"$rgt_user\",
				\"rcv_user\":\"$rcv_user\",
				\"registered_date\":\"$registered_date\",
				\"received_date\":\"$received_date\"
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
