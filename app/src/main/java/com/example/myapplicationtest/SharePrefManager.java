package com.example.myapplicationtest;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SharePrefManager {
    private static SharePrefManager instance;
    private static Context ctx;
    private static final String SHARE = "123";
    private static final String KEY_USERNMAE = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";


    private SharePrefManager(Context context) {
        ctx = context;
    }

    public static synchronized SharePrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharePrefManager(context);
        }
        return instance;
    }

    public boolean userLogin (int id, String userName, String email){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARE, Context.MODE_PRIVATE); //Retrieve the data
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_PASSWORD,id);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_USERNMAE,userName);

        editor.apply();

        return true;
    }

    public boolean checkUserIsLogin () {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARE, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_USERNMAE, null) != null) { //sharedPreferences.getString(KEY_USERNMAE, null), IF KEY_USERNAME NOT AVAILABLE RETURN NULL
            return true;
        }
        return false;
    }

    public boolean logOut (){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
    
//    public boolean location (int id, String latitude, double longitude) {
//        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARE, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//    }

}

