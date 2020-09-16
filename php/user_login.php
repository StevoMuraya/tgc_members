<?php
    if ($_SERVER['REQUEST_METHOD'] == 'POST') {
        $phone      = filter_var($_POST['phone'],FILTER_SANITIZE_STRING,FILTER_FLAG_STRIP_HIGH);
        $password   = filter_var($_POST['password'],FILTER_SANITIZE_STRING,FILTER_FLAG_STRIP_HIGH);

        require_once('config.php');

        $login = FALSE;

        $sql = "SELECT * FROM users WHERE phone = '$phone'";
        $response = mysqli_query($db_handle,$sql);
        $result = array();
        $result['login'] = array();

        if (mysqli_num_rows($response) == 1) {
            $row = mysqli_fetch_assoc($response);

            if (password_verify($password, $row['password']))
              {
                /* The password is correct. */
                $login = TRUE;

                $index['user_id'] = $row['user_id'];
                $index['name'] = $row['name'];
                $index['phone'] = $row['phone'];
                $index['date_time'] = $row['date_time'];
                $index['qr_code'] = $row['qr_code'];

                array_push($result['login'], $index);
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
