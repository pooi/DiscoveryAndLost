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

		}else if($service == "login_director"){

			$login_id = $_POST['login_id'];
			$login_pw = $_POST['login_pw'];

			$sql = "SELECT * FROM director WHERE director_id='$login_id' AND director_pw='$login_pw';";

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
			$director_id = $row['director_id'];
			$director_pw = $row['director_pw'];
			$director_phone = $row['director_phone'];

			echo "{\"id\":\"$id\",
			\"director_id\":\"$director_id\",
			\"director_pw\":\"$director_pw\",
			\"director_phone\":\"$director_phone\"
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

			$save_photo = $_POST['save_photo'];
			if($save_photo == "1"){

				$target_path = dirname(__FILE__).'/pic/';

				$imageString = "image";

				if (isset($_FILES[$imageString]['name'])) {

					$path = $_FILES[$imageString]['name'];
					$ext = pathInfo($path, PATHINFO_EXTENSION);

					$path = "dal_" . $rgt_user . "" . $current_time . "." . $ext;

					$target_path2 = $target_path.$path;

					try{

						if (move_uploaded_file($_FILES[$imageString]['tmp_name'], $target_path2)) {
							// File successfully uploaded
							$picture = "http://nearby.cf/pic/" . $path;

							// $sql_photo = "INSERT INTO patient_photo(main_record_id, patient_id, url, registered_date)
							// 			VALUES('$main_record_id', '$patient_id', '$picture', '$registered_date');";

							// $ret_photo = mysqli_query($con, $sql_photo);
							// if($ret_photo != '1'){
							// 	$isFail = $isFail."/photo".$sql_photo;
							// }

						}else{
							// File upload failed
							// $isFail = $isFail."/photo file upload failed";
							echo json_encode(array('status'=>'fail', 'message'=>$target_path));
							exit();
						}

					}catch(Exception $e){
						// $isFail = $isFail."/photo " .  $e->getMessage();
						echo json_encode(array('status'=>'fail', 'message'=>$e->getMessage()));
						exit();
					}

				} else {
					// File parameter is missing
					// $isFail = $isFail."/photo Not received any file";
					echo json_encode(array('status'=>'fail', 'message'=>'Not received any file'));
					exit();
				}

			}

			$sql = "INSERT INTO
					lost(category,color,brand,building,room,tags,description,photos,rgt_user,registered_date)
					VALUES('$category','$color','$brand','$building', '$room','$tags','$description', '$picture', '$rgt_user','$current_time');";
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
		else if($service == 'inquirySell'){

			$page = $_POST['page'];
			$page = $page*30;

				//$lost_id = $_POST['lost_id'];
			//$rgt_user = $_POST['rgt_user'];
				//$buy_user = $_POST['buy_user'];
				//$sql = "SELECT * FROM Patient WHERE location_id='$location_id'";

			$sql = "SELECT A.*, B.category as lost_category, B.color as lost_color, B.brand as lost_brand, B.building as lost_building, B.room as lost_room, B.tags as lost_tags, B.description as lost_description, B.photos as lost_photos, B.rgt_user as lost_rgt_user, B.rcv_user as lost_rcv_user, B.registered_date as lost_registered_date, B.received_date as lost_received_date
				, C.student_id as rgt_student_id, C.email as rgt_email, C.first_name as rgt_first_name, C.last_name as rgt_last_name, C.phone as rgt_phone, C.registered_date as rgt_registered_date
				, D.student_id as rgt_student_id, D.email as rgt_email, D.first_name as rgt_first_name, D.last_name as rgt_last_name, D.phone as rgt_phone, D.registered_date as rgt_registered_date
				FROM sell as A
				LEFT OUTER JOIN (
				SELECT *
				FROM lost
				GROUP BY id) as B
				ON (B.id = A.lost_id)
				LEFT OUTER JOIN (
				SELECT *
				FROM users
				GROUP BY id) as C
				ON (C.id = A.rgt_user)
				LEFT OUTER JOIN (
				SELECT *
				FROM users
				GROUP BY id) as D
				ON (D.id = A.buy_user)
				WHERE A.lost_id like '%'
				GROUP BY A.id LIMIT $page, 30;";

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
						$lost_id = $row['lost_id'];
						$price = $row['price'];
						$rgt_user = $row['rgt_user'];
						$buy_user = $row['buy_user'];
						$registered_date = $row['registered_date'];
						$bought_date = $row['bought_date'];
						$lost_category = $row['lost_category'];
						$lost_color = $row['lost_color'];
						$lost_brand = $row['lost_brand'];
						$lost_building = $row['lost_building'];
						$lost_room = $row['lost_room'];
						$lost_tags = $row['lost_tags'];
						$lost_description = $row['lost_description'];
						$lost_photos = $row['lost_photos'];
						$lost_rgt_user = $row['lost_rgt_user'];
						$lost_rcv_user = $row['lost_rcv_user'];
						$lost_registered_date = $row['lost_registered_date'];
						$lost_received_date = $row['lost_received_date'];
						$rgt_student_id = $row['rgt_student_id'];
						$rgt_email = $row['rgt_email'];
						$rgt_first_name = $row['rgt_first_name'];
						$rgt_last_name = $row['rgt_last_name'];
						$rgt_phone = $row['rgt_phone'];
						$rgt_registered_date = $row['rgt_registered_date'];
						$buy_student_id = $row['buy_student_id'];
						$buy_email = $row['buy_email'];
						$buy_first_name = $row['buy_first_name'];
						$buy_last_name = $row['buy_last_name'];
						$buy_phone = $row['buy_phone'];
						$buy_registered_date = $row['buy_registered_date'];


						echo "{\"id\":\"$id\",
		\"lost_id\":\"$lost_id\",
		\"price\":\"$price\",
		\"rgt_user\":\"$rgt_user\",
		\"buy_user\":\"$buy_user\",
		\"registered_date\":\"$registered_date\",
		\"bought_date\":\"$bought_date\",
		\"lost_category\":\"$lost_category\",
		\"lost_color\":\"$lost_color\",
		\"lost_brand\":\"$lost_brand\",
		\"lost_building\":\"$lost_building\",
		\"lost_room\":\"$lost_room\",
		\"lost_tags\":\"$lost_tags\",
		\"lost_description\":\"$lost_description\",
		\"lost_photos\":\"$lost_photos\",
		\"lost_rgt_user\":\"$lost_rgt_user\",
		\"lost_rcv_user\":\"$lost_rcv_user\",
		\"lost_registered_date\":\"$lost_registered_date\",
		\"lost_received_date\":\"$lost_received_date\",
		\"rgt_student_id\":\"$rgt_student_id\",
		\"rgt_email\":\"$rgt_email\",
		\"rgt_first_name\":\"$rgt_first_name\",
		\"rgt_last_name\":\"$rgt_last_name\",
		\"rgt_phone\":\"$rgt_phone\",
		\"rgt_registered_date\":\"$rgt_registered_date\",
		\"buy_student_id\":\"$buy_student_id\",
		\"buy_email\":\"$buy_email\",
		\"buy_first_name\":\"$buy_first_name\",
		\"buy_last_name\":\"$buy_last_name\",
		\"buy_phone\":\"$buy_phone\",
		\"buy_registered_date\":\"$buy_registered_date\"
		}";

						if($i<$count-1){
								echo ",";
						}

						$i++;

				}

				echo "]}";

		}else if($service == "inquiryLost"){

			$isRequestOnly = $_POST['isRequestOnly'];

			$page = $_POST['page'];
			$page = $page*30;

			if($isRequestOnly == ''){

				$sql = "SELECT A.*
				, C.student_id as rgt_student_id, C.email as rgt_email, C.first_name as rgt_first_name, C.last_name as rgt_last_name, C.phone as rgt_phone, C.registered_date as rgt_registered_date
				, D.student_id as rcv_student_id, D.email as rcv_email, D.first_name as rcv_first_name, D.last_name as rcv_last_name, D.phone as rcv_phone, D.registered_date as rcv_registered_date
				FROM lost as A
				LEFT OUTER JOIN (
				SELECT *
				FROM users
				GROUP BY id) as C
				ON (C.id = A.rgt_user)
				LEFT OUTER JOIN (
				SELECT *
				FROM users
				GROUP BY id) as D
				ON (D.id = A.rcv_user)
				WHERE rcv_user is null
				ORDER BY id DESC 
				 LIMIT $page, 30;";

			}else{

				$sql = "SELECT A.*
				, C.student_id as rgt_student_id, C.email as rgt_email, C.first_name as rgt_first_name, C.last_name as rgt_last_name, C.phone as rgt_phone, C.registered_date as rgt_registered_date
				, D.student_id as rcv_student_id, D.email as rcv_email, D.first_name as rcv_first_name, D.last_name as rcv_last_name, D.phone as rcv_phone, D.registered_date as rcv_registered_date
				FROM lost as A
				LEFT OUTER JOIN (
				SELECT *
				FROM users
				GROUP BY id) as C
				ON (C.id = A.rgt_user)
				LEFT OUTER JOIN (
				SELECT *
				FROM users
				GROUP BY id) as D
				ON (D.id = A.rcv_user)
				WHERE rcv_user is null AND (SELECT count(*) FROM request WHERE lost_id=A.id) >= 1 
				ORDER BY id DESC 
				 LIMIT $page, 30;";

			}
			//  echo $sql;

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

				$rgt_student_id = $row['rgt_student_id'];
				$rgt_email = $row['rgt_email'];
				$rgt_first_name = $row['rgt_first_name'];
				$rgt_last_name = $row['rgt_last_name'];
				$rgt_phone = $row['rgt_phone'];
				$rgt_registered_date = $row['rgt_registered_date'];

				$rcv_student_id = $row['rcv_student_id'];
				$rcv_email = $row['rcv_email'];
				$rcv_first_name = $row['rcv_first_name'];
				$rcv_last_name = $row['rcv_last_name'];
				$rcv_phone = $row['rcv_phone'];
				$rcv_registered_date = $row['rcv_registered_date'];

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
				\"rgt_student_id\":\"$rgt_student_id\",
				\"rgt_email\":\"$rgt_email\",
				\"rgt_first_name\":\"$rgt_first_name\",
				\"rgt_last_name\":\"$rgt_last_name\",
				\"rgt_phone\":\"$rgt_phone\",
				\"rgt_registered_date\":\"$rgt_registered_date\",
				\"rcv_user\":\"$rcv_user\",
				\"rcv_student_id\":\"$rcv_student_id\",
				\"rcv_email\":\"$rcv_email\",
				\"rcv_first_name\":\"$rcv_first_name\",
				\"rcv_last_name\":\"$rcv_last_name\",
				\"rcv_phone\":\"$rcv_phone\",
				\"rcv_registered_date\":\"$rcv_registered_date\",
				\"registered_date\":\"$registered_date\",
				\"received_date\":\"$received_date\"
				}";

                if($i<$count-1){
                    echo ",";
                }

                $i++;

            }

            echo "]}";

		}else if($service == "inquiry_request_user"){

			$lost_id = $_POST['lost_id'];

			$sql = "SELECT A.*
			, C.student_id as rgt_student_id, C.email as rgt_email, C.first_name as rgt_first_name, C.last_name as rgt_last_name, C.phone as rgt_phone, C.registered_date as rgt_registered_date
			from request as A
			LEFT OUTER JOIN (
			SELECT *
			FROM users
			GROUP BY id) as C
			ON (C.id = A.request_user)
			WHERE lost_id='$lost_id';";

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
				$lost_id = $row['lost_id'];
				$item_matching_id = $row['item_matching_id'];
				$match_ratio = $row['match_ratio'];
				$registered_date = $row['registered_date'];
				$request_user = $row['request_user'];

				$rgt_student_id = $row['rgt_student_id'];
				$rgt_email = $row['rgt_email'];
				$rgt_first_name = $row['rgt_first_name'];
				$rgt_last_name = $row['rgt_last_name'];
				$rgt_phone = $row['rgt_phone'];
				$rgt_registered_date = $row['rgt_registered_date'];

				echo "{\"id\":\"$id\",
				\"lost_id\":\"$lost_id\",
				\"item_matching_id\":\"$item_matching_id\",
				\"match_ratio\":\"$match_ratio\",
				\"request_user\":\"$request_user\",
				\"rgt_user\":\"$rgt_user\",
				\"rgt_student_id\":\"$rgt_student_id\",
				\"rgt_email\":\"$rgt_email\",
				\"rgt_first_name\":\"$rgt_first_name\",
				\"rgt_last_name\":\"$rgt_last_name\",
				\"rgt_phone\":\"$rgt_phone\",
				\"rgt_registered_date\":\"$rgt_registered_date\",
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
