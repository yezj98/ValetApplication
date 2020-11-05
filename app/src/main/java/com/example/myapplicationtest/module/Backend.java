package com.example.myapplicationtest.module;

import java.net.URL;

public class Backend {
    private static final String root_url = "http://172.19.192.1/android/operation/";

    public static final String register_url = root_url + "register.php";
    public static final String login_url = root_url + "userlogin.php";
    public static final String location_url = root_url + "locationOperator.php";
    public static final String overwrite_url = root_url + "locationOverwrite.php";
}