<?php
    if ($_SERVER['REQUEST_METHOD'] == 'POST') {
        $phone      = filter_var($_POST['phone'],FILTER_SANITIZE_STRING,FILTER_FLAG_STRIP_HIGH);

        require_once('config.php');

        $sql = "SELECT * FROM users WHERE phone = '$phone'";
        $response = mysqli_query($db_handle,$sql);
        $result = array();
        $result['login'] = array();

        if (mysqli_num_rows($response) == 1) {
            $row = mysqli_fetch_assoc($response);

            if (password_verify($password, $row['password']))
              {

                $result['success'] = "1";
                $result['message'] = "success";
                echo json_encode($result);

                mysqli_close($db_handle);
              }
        } else{
            $result['success'] = "0";
            $result['message'] = "error";
            echo json_encode($result);

            mysqli_close($db_handle);
        }
    }
 ?>
