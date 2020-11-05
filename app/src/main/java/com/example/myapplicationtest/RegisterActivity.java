package com.example.myapplicationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplicationtest.module.Backend;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText editEmail, editPassword, editRepassword, editUsername;
    Button buttonRegister, buttonBack;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    final static Map<String, String> param = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editEmail = findViewById(R.id.edit_email2);
        editPassword = findViewById(R.id.edit_password2);
        editRepassword = findViewById(R.id.edit_repassword);
        buttonBack = findViewById(R.id.buttonBack);
        buttonRegister = findViewById(R.id.button_login2);
        editUsername = findViewById(R.id.edit_username);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        final String stringEmail = editEmail.getText().toString().trim();
        final String stringUsername = editUsername.getText().toString().trim();
        final String stringPassword = editPassword.getText().toString().trim();
        Log.d("requestd", "" + stringEmail + stringPassword + stringUsername);

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Backend.register_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.INVISIBLE);

                try {
                    JSONObject jsonObject = new JSONObject(response); //Get the JSON array from php
                    Toast.makeText(RegisterActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show(); //show message
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() { //Error listener, if error happens
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError { //override the parameters with title
                Map<String, String> params = new HashMap<>();
                params.put("username", stringUsername);
                params.put("password", stringPassword);
                params.put("email", stringEmail);
                Log.d("params", "" + params);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
    }
}