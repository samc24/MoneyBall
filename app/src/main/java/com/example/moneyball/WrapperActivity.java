package com.example.moneyball;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WrapperActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Button registerBtn, loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrapper);

        mAuth = FirebaseAuth.getInstance();

        initializeViews();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WrapperActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser currUser = mAuth.getCurrentUser();
                startActivity(new Intent(WrapperActivity.this, LoginActivity.class));
//                if(currUser == null) {//if user not logged in bring to the login page, if already logged in then bring the users to the dashboard
//                    startActivity(new Intent(WrapperActivity.this, LoginActivity.class));
//                }
//                else {
//                    startActivity(new Intent(WrapperActivity.this, DashboardActivity.class));
//                }
            }
        });
    }

    private void initializeViews() {
        registerBtn = findViewById(R.id.register);
        loginBtn = findViewById(R.id.login);
    }
}
