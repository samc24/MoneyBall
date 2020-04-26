package com.example.moneyball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class JoinWagerActivity extends AppCompatActivity {
    Button btnYes, btnNo;
    int WAGER_JOINED = 321;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_wager);
        final TextView tvWagerTitle = findViewById(R.id.tvWagerTitle);
        final TextView tvWagerDescription = findViewById(R.id.tvWagerDescription);

        Intent wagerInfo = getIntent();
        final String wagerTitle = wagerInfo.getStringExtra("heading");
        final String wagerDescription = wagerInfo.getStringExtra("description");
        final String userID = wagerInfo.getStringExtra("userID");
        final String groupId = wagerInfo.getStringExtra("groupID");
        final String wagerId = wagerInfo.getStringExtra("wagerID");
        final ArrayList<String> votesList = wagerInfo.getStringArrayListExtra("votesList");
        tvWagerDescription.setText(wagerDescription);
        tvWagerTitle.setText(wagerTitle);
        btnYes = findViewById(R.id.btnYes);
        btnNo = findViewById(R.id.btnNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wagerIntent = new Intent(getApplicationContext(), GroupActivity.class);
                wagerIntent.putExtra("vote", "Y");
                setResult(RESULT_OK, wagerIntent);
                finish();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wagerIntent = new Intent(getApplicationContext(), GroupActivity.class);
                wagerIntent.putExtra("vote", "N");
                setResult(RESULT_OK, wagerIntent);
                finish();
            }
        });

    }
}
