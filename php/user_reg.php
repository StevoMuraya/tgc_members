<?php 

require "password.php";
if ($_SERVER['REQUEST_METHOD'] =='GET'){
	$name				= filter_var($_GET['name'],FILTER_SANITIZE_STRING,FILTER_FLAG_STRIP_HIGH);
	$phone				= filter_var($_GET['phone'],FILTER_SANITIZE_STRING,FILTER_FLAG_STRIP_HIGH);
	$password			= filter_var($_GET['password'],FILTER_SANITIZE_STRING,FILTER_FLAG_STRIP_HIGH);
	
	require_once('config.php');

	$CODE = $name.' '.$phone; 
	$encrypted_password = password_hash($password, PASSWORD_DEFAULT);
	$hashedQRCode = password_hash($CODE, PASSWORD_DEFAULT);

	//echo $encrypted_password;

	$sql = "INSERT INTO users(name,phone,password,date_time,qr_code) VALUES ('$name','$phone','$encrypted_password',now(),'$hashedQRCode')";
	if (mysqli_query($db_handle,$sql)) {
		echo "Successfully Registered";
	}else{
		echo "Failed to Register";
	}
	mysqli_close($db_handle);
}else{
	echo 'Error in capturing data';
} 
?>