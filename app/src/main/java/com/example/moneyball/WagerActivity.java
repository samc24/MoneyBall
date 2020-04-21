package com.example.moneyball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class WagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wager);
        Bundle extras = getIntent().getExtras();
        String description = extras.getString("description");
        String groupDesc = extras.getString("groupDescription");
        String heading = extras.getString("heading");
        String group = extras.getString("group");
        long id = extras.getLong("id");

        //setting the picture holder with picture url from internet
        Uri picUri = Uri.parse(extras.getString("pic"));//gets the picture from the bundle as a URL
        ImageView wagerPic = findViewById(R.id.wagerPic);
        Picasso.get().load(picUri).into(wagerPic);//use PICASSSOOOO for loading picture urls into imageView or whatever holders u need.

        TextView groupName, groupDescription, wagerName, wagerDescription;
        groupDescription = findViewById(R.id.groupDescription);
        groupDescription.setText(groupDesc);
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
                Intent myIntent = new Intent(WagerActivity.this,  DareActivity.class);
                Toast.makeText(getApplicationContext(),"Spin for DARE", Toast.LENGTH_LONG).show();
                startActivity(myIntent);
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
