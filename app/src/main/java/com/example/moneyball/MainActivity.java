package com.example.moneyball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button tempButtonHome = findViewById(R.id.button_home);
        Button tempButtonGroup = findViewById(R.id.button_group);
        final Intent tempIntentHome = new Intent(MainActivity.this,HomeActivity.class);
        final Intent tempIntentGroup = new Intent(MainActivity.this, GroupActivity.class);
        tempButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(tempIntentHome);
            }
        });
        tempButtonGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(tempIntentGroup);
            }
        });
    }
}
