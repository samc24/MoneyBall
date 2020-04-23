package com.example.moneyball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class WagerActivity extends AppCompatActivity {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wager);
        Bundle extras = getIntent().getExtras();
        String description = extras.getString("description");
        String groupDesc = extras.getString("groupDescription");
        String heading = extras.getString("heading");
        String group = extras.getString("group");
        String wagerCreator = extras.getString("wagerCreator");
        final String id = extras.getString("id");
        final String groupId = extras.getString("groupId");
        final ArrayList<String> usersList = extras.getStringArrayList("usersList");
        double betVal = extras.getDouble("betVal");
        final ArrayList<String> challengeList = extras.getStringArrayList("challengeList");
        Log.d("BET", "wager onCreate: " + betVal);
        final EditText potentialChallengeText = findViewById(R.id.potentialChallenge);
        final int position = extras.getInt("position");

        //setting the group picture holder
        Uri groupPicUri = Uri.parse(extras.getString("groupPic"));
        ImageView groupPicHolder = findViewById(R.id.groupPic);
        Picasso.get().load(groupPicUri).into(groupPicHolder);

        //setting the picture holder with picture url from internet
        Uri picUri = Uri.parse(extras.getString("pic"));//gets the picture from the bundle as a URL
        ImageView wagerPic = findViewById(R.id.wagerPic);
        Picasso.get().load(picUri).into(wagerPic);//use PICASSSOOOO for loading picture urls into imageView or whatever holders u need.

        TextView groupName, groupDescription, wagerName, wagerDescription, betValTV;
        groupDescription = findViewById(R.id.groupDescription);
        groupDescription.setText(groupDesc);
        groupName = findViewById(R.id.groupName);
        groupName.setText(group);
        wagerName = findViewById(R.id.wagerName);
        wagerName.setText(heading);
        wagerDescription = findViewById(R.id.wagerDescription);
        wagerDescription.setText(description);
        betValTV = findViewById(R.id.betValTV);
        betValTV.setText("Value: $"+betVal);


        final Button bet, challenge, invite, btn_closeWager;
        bet = findViewById(R.id.bet);
        challenge = findViewById(R.id.challenge);
        invite = findViewById(R.id.invite);
        btn_closeWager = findViewById(R.id.btn_closeWager);


//        btn_closeWager.setVisibility(INVISIBLE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String UID = "";
        if(user!=null){
            UID = user.getUid();
        }
        if(UID.equals(wagerCreator)){
            btn_closeWager.setText("Close Wager");
//            btn_closeWager.setVisibility(VISIBLE);
        }
        else if(usersList!=null && usersList.contains(UID)) {
            btn_closeWager.setVisibility(View.GONE);
            potentialChallengeText.setVisibility(View.GONE);
        }

        if(btn_closeWager.getText().toString().equalsIgnoreCase("Close Wager")) {
            potentialChallengeText.setVisibility(INVISIBLE);
            Log.d("TEST", "admin");
        }
        btn_closeWager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sets the wager openStatus to false (closed)
                if(btn_closeWager.getText().toString().equalsIgnoreCase("Close Wager")) {
                    DatabaseReference ref = database.getReference(); //get db reference
                    final DatabaseReference openStatusRef = ref.child("groups").child(groupId).child("wagers").child(id).child("openStatus");
                    openStatusRef.setValue(false);
                    Toast.makeText(getApplicationContext(), "Wager Closed", Toast.LENGTH_LONG).show();
                    btn_closeWager.setVisibility(View.GONE);
                }
                else{
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String UID = "";
                    if(user!=null){
                        UID = user.getUid();
                    }
                    if(usersList.contains(UID)==false) {
                        usersList.add(UID);
                    }
                    DatabaseReference ref = database.getReference(); //get db reference
                    final DatabaseReference usersListRef = ref.child("groups").child(groupId).child("wagers").child(id).child("usersList");
                    usersListRef.setValue(usersList);
                    Intent intent = new Intent();
                    String potentialChallenge = potentialChallengeText.getText().toString();
                    if(potentialChallenge.equals(""))
                        Toast.makeText(getApplicationContext(),  "Enter one potential challenge!", Toast.LENGTH_SHORT).show();
                    else {
                        intent.putExtra("potentialChallenge", potentialChallenge);
                        Log.d("CHL", potentialChallenge);
                        intent.putExtra("position", position);
                        Toast.makeText(getApplicationContext(), "Joined Wager", Toast.LENGTH_SHORT).show();
                        btn_closeWager.setVisibility(View.GONE);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });

        bet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Izzy link ur payment stuff here
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                String UID = "";
//                if(user!=null){
//                    UID = user.getUid();
//                }
//                if(usersList.contains(UID)==false) {
//                    usersList.add(UID);
//                }
//                DatabaseReference ref = database.getReference(); //get db reference
//                final DatabaseReference usersListRef = ref.child("groups").child(groupId).child("wagers").child(id).child("usersList");
//                usersListRef.setValue(usersList);
                Toast.makeText(getApplicationContext(),  "Opening Bet Payment Stuff", Toast.LENGTH_LONG).show();
            }
        });


        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(WagerActivity.this,  DareActivity.class);
                Toast.makeText(getApplicationContext(),"Spin for DARE", Toast.LENGTH_LONG).show();
                myIntent.putExtra("challengeList", challengeList);
                startActivity(myIntent);
            }
        });

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));

                sendIntent.putExtra("sms_body", "Hey! I'd like to invite you to my group. Use this code to join it on the MoneyBall app! Code: " + groupId);
                startActivity(sendIntent);
                Toast.makeText(getApplicationContext(),  "Invite your friends!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
