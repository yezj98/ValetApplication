package com.example.myapplicationtest.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplicationtest.RequestHandler;
import com.example.myapplicationtest.module.Backend;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class storeLocation {

    public void storeLocation(final String id, final String longitude, final String latitude, final Object object) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Backend.location_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText((Context) object, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            Toast.makeText((Context) object, error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("hhh", " " + error.getMessage());
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("id", id);
                param.put("longitude", longitude);
                param.put("latitude", latitude);

                Log.d("php", "" + param);
                return param;
            }
        };
        RequestHandler.getInstance((Context) object).addToRequestQueue(stringRequest);
    }

    public void overWriteLocation(final String id, final String latitude, final String longitude, final Object object) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Backend.overwrite_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
//                   Toast.makeText(MapsActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText((Context) object, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("id", id);
                param.put("latitude", latitude);
                param.put("longitude", longitude);

                return param;
            }
        };
        RequestHandler.getInstance((Context) object).addToRequestQueue(stringRequest);
    }


}



