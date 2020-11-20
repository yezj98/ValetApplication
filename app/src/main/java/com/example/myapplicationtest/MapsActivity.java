package com.example.myapplicationtest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplicationtest.module.Backend;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    static FusedLocationProviderClient mfusedLocationProviderClient;
    private String FINELOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private String COARSELOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private boolean locationGranted = true;
    private double currentLatitude, currentLongitude, getCurrentLatitude1, getCurrentLongitude1;
    static String userID;
    Spinner spinner;
    PlacesClient placesClient;
    Boolean backButtonTwice = false;
    Button confirm, confirm1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        spinner = findViewById(R.id.snipper);
        confirm = findViewById(R.id.confirm_button);
        confirm1 = findViewById(R.id.confirm_button1);
        mapFragment.getMapAsync(this);



        getLocation();

        googlePlace();

        dropList();
        
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {


                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                final Task location1 = mfusedLocationProviderClient.getLastLocation();


                location1.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Location currentLocation = (Location) task.getResult();

                        getCurrentLatitude1 = currentLocation.getLatitude();
                        getCurrentLongitude1 = currentLocation.getLongitude();

                        String latitude = String.valueOf(getCurrentLatitude1);
                        String longitude = String.valueOf(getCurrentLongitude1);

                        LatLng latLng = new LatLng(getCurrentLatitude1, getCurrentLongitude1);

                        Intent intent = getIntent();
                        String userID = intent.getStringExtra("ID");

                        overWriteLocation(userID, latitude, longitude);
                        


                    }
                });
            }
        }, 0, 10000);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    public void getLocation() {

        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (locationGranted) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            final Task location = mfusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    Location currentLocation = (Location) task.getResult();

                    currentLatitude = currentLocation.getLatitude();
                    currentLongitude = currentLocation.getLongitude();

                    final String latitude = String.valueOf(currentLatitude);
                    final String longitude = String.valueOf(currentLongitude);

                    Intent intent = getIntent();
                    userID = intent.getStringExtra("ID");

                    storeLocation(userID, longitude, latitude);

                    LatLng latLng = new LatLng(currentLatitude, currentLongitude);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18), 1, null);

                }
            });

        } else {
            Toast.makeText(this, "Unable to allocate location", Toast.LENGTH_SHORT).show();
        }

    }

    public void storeLocation(final String id, final String longitude, final String latitude) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Backend.location_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(MapsActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("hhh", " " + error.getMessage());
            }
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
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void timer(int sec) {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {


            }
        }, 0, 10000);
    }

    private void overWriteLocation(final String id, final String latitude, final String longitude) {
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
                Toast.makeText(MapsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

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
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void dropList() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Current location");
        arrayList.add("Specify location");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrayList);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (spinner.getSelectedItemPosition() == 0) {
                    AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.googlefragment1);
                    autocompleteSupportFragment.getView().setVisibility(View.INVISIBLE);
                    AutocompleteSupportFragment autocompleteSupportFragment1 = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.googlefragment);
                    autocompleteSupportFragment1.setHint("To where");

                    confirm1.setVisibility(View.INVISIBLE);
                    confirm.setVisibility(View.VISIBLE);

                }
                else if (spinner.getSelectedItemPosition() == 1) {
                    AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.googlefragment1);
                    autocompleteSupportFragment.getView().setVisibility(View.VISIBLE);
                    autocompleteSupportFragment.setHint("To where");

                    AutocompleteSupportFragment autocompleteSupportFragment1 = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.googlefragment);
                    autocompleteSupportFragment1.setHint("From where");

                    confirm1.setVisibility(View.VISIBLE);
                    confirm.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void googlePlace () {
        String api = "AIzaSyAOe6TknXHJNTITfnH-NMMKuTr5kzgyjwk";
        final AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.googlefragment);

        AutocompleteSupportFragment autocompleteSupportFragment1 = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.googlefragment1);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(),api);
        }
        placesClient = Places.createClient(this);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.v("Tag", "Place" + place.getName() + "," + place.getId());
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.d("Tag", "Error");
                Toast.makeText(MapsActivity.this,""+ status, Toast.LENGTH_SHORT).show();
                Log.d("tafff", "" + status);
            }
        });

        autocompleteSupportFragment1.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));
        autocompleteSupportFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        if (backButtonTwice) {
            super.onBackPressed();
            return;
     
        }
        else {
            backButtonTwice = true;
            Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backButtonTwice = false;
                }
            }, 2000);
        }
    }
}