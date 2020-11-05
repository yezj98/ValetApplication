package com.example.myapplicationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editEmail, editPassword;
    Button buttonLogin, buttonRegister;
    String stringEmail, stringPassword;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    final String login_url = "http://192.168.1.31/login.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        buttonLogin = findViewById(R.id.button_login);
        buttonRegister = findViewById(R.id.button_register);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        buttonRegister.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
    }

    public void logIn() {
        final String email = editEmail.getText().toString().trim();
        final String password = editPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Backend.login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("error")) { //If json "error" is false
                        SharePrefManager.getInstance(MainActivity.this).userLogin(jsonObject.getInt("id"),
                                jsonObject.getString("username"), jsonObject.getString("email"));
                        Toast.makeText(MainActivity.this, "Welcome back " + jsonObject.getString("username"), Toast.LENGTH_SHORT).show();

                        String id = jsonObject.getString("id");
                        Log.d("getid", "" + jsonObject.getString("id"));
                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                        intent.putExtra("ID", id);
                        startActivity(intent);
                    } else { // else print json "message"
                        Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("email", email);
                param.put("password", password);
                return param;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View view) {
        if (view == buttonRegister) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        } else if (view == buttonLogin) {
            logIn();
        }
    }
}