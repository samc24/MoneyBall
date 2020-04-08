package com.example.moneyball;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currUser = mAuth.getCurrentUser();

        assert currUser != null;
        Toast.makeText(getApplicationContext(), "you are logged in as " + currUser.getEmail(), Toast.LENGTH_LONG).show();
    }
}