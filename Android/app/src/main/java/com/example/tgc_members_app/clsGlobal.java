package com.example.tgc_members_app;

public class clsGlobal {

    public static String protocol               = "http://";
    public static String hostname               = "10.0.2.2/tgc_members/php";

    //10.0.2.2


    public static String REGISTER_USER              = protocol + hostname + "/user_reg.php";
    public static String LOGIN_USER                 = protocol + hostname + "/user_login.php";
    public static String CHECK_USER                 = protocol + hostname + "/check_account.php";
}
