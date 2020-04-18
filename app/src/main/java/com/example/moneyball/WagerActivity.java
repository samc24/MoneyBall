package com.example.moneyball;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wager);
        Bundle extras = getIntent().getExtras();
        String description = extras.getString("description");
        String heading = extras.getString("heading");
        String group = extras.getString("group");
        long id = extras.getLong("id");
        int pic = extras.getInt("pic");
        TextView groupName, groupDescription, wagerName, wagerDescription;
        groupName = findViewById(R.id.groupName);
        groupName.setText(group);
        wagerName = findViewById(R.id.wagerName);
        wagerName.setText(heading);
        wagerDescription = findViewById(R.id.wagerDescription);
        wagerDescription.setText(description);

        Button bet, challenge, invite;
        bet = findViewById(R.id.bet);
        challenge = findViewById(R.id.challenge);
        invite = findViewById(R.id.invite);

        bet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Izzy link ur payment stuff here
                Toast.makeText(getApplicationContext(),  "Opening Bet Payment Stuff", Toast.LENGTH_LONG).show();
            }
        });


        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Abdul link ur challenge stuff here
                Toast.makeText(getApplicationContext(),  "Opening Challenge Payment Stuff", Toast.LENGTH_LONG).show();
            }
        });

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send invite link of group to ppl
                Toast.makeText(getApplicationContext(),  "Invite your friends!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
