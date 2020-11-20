package com.example.myapplicationtest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import dagger.internal.MapBuilder;

public class MenuActivity extends AppCompatActivity {

    ImageButton carButton, driverButton, moreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        carButton = findViewById(R.id.car);
        driverButton = findViewById(R.id.driver);
        moreButton = findViewById(R.id.more);

        carButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }
}