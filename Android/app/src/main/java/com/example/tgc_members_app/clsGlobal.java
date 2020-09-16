package com.example.tgc_members_app;

public class clsGlobal {

    public static String protocol               = "http://";
    public static String hostname               = "192.168.43.187/tgc_members/php";

    //10.0.2.2

    public static String REGISTER_USER              = protocol + hostname + "/user_reg.php";
    public static String LOGIN_USER                 = protocol + hostname + "/user_login.php";
    public static String CHECK_USER                 = protocol + hostname + "/check_account.php";


    public static String shared_user_id;
    public static String shared_name;
    public static String shared_phone;
    public static String shared_date_time;
    public static String shared_qr_code;
}
