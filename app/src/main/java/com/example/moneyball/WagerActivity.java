package com.example.moneyball;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class WagerActivity extends AppCompatActivity {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final int JOIN_WAGER_REQUEST = 123;
    final int CLOSE_WAGER_REQUEST = 1000;
    final int JOINED_WAGER = 321;
    String descriptionToPass;
    String headingToPass;
    ArrayList<String> votesListToPass;
    String groupID, wagerID;
    ArrayList<String> usersList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wager);
        Bundle extras = getIntent().getExtras();
        final String description = extras.getString("description");
        String groupDesc = extras.getString("groupDescription");
        final String heading = extras.getString("heading");
        String group = extras.getString("group");
        final String wagerCreator = extras.getString("wagerCreator");
        final String id = extras.getString("id");
        final String groupId = extras.getString("groupId");
        groupID = groupId;
        wagerID = id;
        usersList = extras.getStringArrayList("usersList");
        final ArrayList<String> votesList = extras.getStringArrayList("votesList");
        Log.d("tag", votesList.toString());
        double betVal = extras.getDouble("betVal");
        final ArrayList<String> challengeList = extras.getStringArrayList("challengeList");
        Log.d("BET", "wager onCreate: " + betVal);
        final EditText potentialChallengeText = findViewById(R.id.potentialChallenge);
        final int position = extras.getInt("position");
        descriptionToPass = description;
        headingToPass = heading;
        votesListToPass = votesList;
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

        final TextView userResult, winners, losers;
        userResult = findViewById(R.id.userResult);
        winners = findViewById(R.id.winners);
        losers = findViewById(R.id.losers);

        final Button bet, challenge, invite, btn_closeWager;
        bet = findViewById(R.id.bet);
        challenge = findViewById(R.id.challenge);
        invite = findViewById(R.id.invite);
        btn_closeWager = findViewById(R.id.btn_closeWager);

        DatabaseReference ref = database.getReference(); //get db reference
        final DatabaseReference openStatusRef = ref;//.child(id);//.child("openStatus");
        openStatusRef.addValueEventListener(new ValueEventListener() {
            //read the wager data from the database
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue(); //get the database data as a hashmap
                HashMap<String, Object> groupsData = (HashMap<String, Object>) dataMap.get("groups");
                HashMap<String, Object> groupData = (HashMap<String, Object>) groupsData.get(groupId);
                HashMap<String, Object> wagersData = (HashMap<String, Object>) groupData.get("wagers");
                HashMap<String, Object> usersData = (HashMap<String, Object>) dataMap.get("users");
                Log.d("db", dataMap.toString());
                if(dataMap!=null) { //check if its null to avoid errors
                    for (String key : wagersData.keySet()) {   //loop through the wagers
                        Object data = wagersData.get(key);
                        HashMap<String, Object> wagerData = (HashMap<String, Object>) data;
                        String wagerId = wagerData.get("id").toString();
                        if(!wagerId.equalsIgnoreCase(id))
                            continue;

                        Boolean openStatus = (Boolean)wagerData.get("openStatus");
                        if(!openStatus){
                            btn_closeWager.setVisibility(View.GONE);
                            invite.setVisibility(View.GONE);
                            userResult.setVisibility(VISIBLE);
                            winners.setVisibility(VISIBLE);
                            losers.setVisibility(VISIBLE);
                            ArrayList<String> votesList = (ArrayList<String>)wagerData.get("userVotes");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String UID = "";
                            if(user!=null){
                                UID = user.getUid();
                            }
                            ArrayList<String> usersList = (ArrayList<String>)wagerData.get("usersList");
                            String userVote = votesList.get(usersList.indexOf(UID));
                            String wagerResult = wagerData.get("wagerResult").toString();
                            ArrayList<String> winnerList = new ArrayList<>(), loserList = new ArrayList<>();
                            for(int i =0; i <votesList.size(); i++){
                                String res = votesList.get(i);
                                HashMap<String, Object> userData = (HashMap<String, Object>)usersData.get(usersList.get(i));
                                HashMap<String, Object> profData = (HashMap<String, Object>)userData.get("profile");
                                String username = (String)profData.get("username");
                                if(res.equalsIgnoreCase(wagerResult)){
                                    winnerList.add(username);
                                }
                                else
                                    loserList.add(username);
                            }

                            String winnersString = "Winners: "+winnerList.toString(), losersString = "Losers: "+loserList.toString();; // TODO: needs to be replaced with usernames
                            winners.setText(winnersString);
                            losers.setText(losersString);
                            String userResultString;
                            if(userVote.equalsIgnoreCase(wagerResult)){
                                userResultString = "You Win!";
                                userResult.setText(userResultString);
                            }
                            else{
                                userResultString = "You Lose...";
                                userResult.setText(userResultString);
                            }
                        }
//                        Toast.makeText(getApplicationContext(), openStatus+", id: "+id, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });




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
                    Intent intent = new Intent(getApplicationContext(), CloseWagerActivity.class);
                    intent.putExtra("heading", headingToPass);
                    intent.putExtra("description", descriptionToPass);
                    intent.putExtra("groupId", groupId);
                    intent.putExtra("id", id);
                    btn_closeWager.setVisibility(View.GONE);
                    startActivityForResult(intent, CLOSE_WAGER_REQUEST);
                }
                else{
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String UID = "";
                    if(user!=null){
                        UID = user.getUid();
                    }
                    DatabaseReference ref = database.getReference();
                    Intent intent = new Intent(getApplicationContext(), JoinWagerActivity.class);
                    String potentialChallenge = potentialChallengeText.getText().toString();
                    if(potentialChallenge.equals(""))
                        Toast.makeText(getApplicationContext(),  "Enter one potential challenge!", Toast.LENGTH_SHORT).show();
                    else {
                        challengeList.add(potentialChallenge);
                        final DatabaseReference challengeListRef = ref.child("groups").child(groupId).child("wagers").child(id).child("challengeList");
                        challengeListRef.setValue(challengeList);
                        Toast.makeText(getApplicationContext(), "Joined Wager", Toast.LENGTH_SHORT).show();
                        btn_closeWager.setVisibility(View.GONE);
                        potentialChallengeText.setVisibility(View.GONE);
                        Log.d("tag", votesListToPass.toString());
                        Log.d("tag", descriptionToPass);
                        Log.d("tag", headingToPass);
                        intent.putExtra("description", descriptionToPass);
                        intent.putExtra("heading", headingToPass);
                        intent.putExtra("userID", UID);
                        intent.putExtra("groupID", groupId);
                        intent.putExtra("wagerID", id);
                        intent.putExtra("votesList", votesListToPass);
                        startActivityForResult(intent, JOIN_WAGER_REQUEST);
                    }
                }
            }
        });

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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.simple_test_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.profile:
                Toast.makeText(getApplicationContext(), "opening profile page!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ProfilePreferencesPage.class));
                return true;
//            case R.id.someID4:
//                Toast.makeText(getApplicationContext(), "someID2!", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(this, Help.class));
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == JOIN_WAGER_REQUEST) {
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String UID = "";
                if (user != null) {
                    UID = user.getUid();
                }
                if (usersList.contains(UID) == false) {
                    usersList.add(UID);
                }
                DatabaseReference ref = database.getReference(); //get db reference
                final DatabaseReference usersListRef = ref.child("groups").child(groupID).child("wagers").child(wagerID).child("usersList");
                usersListRef.setValue(usersList);

                //Intent voteData = getIntent();
                String vote = data.getStringExtra("vote");
                final DatabaseReference wagerVoteRef = ref.child("groups").child(groupID).child("wagers").child(wagerID).child("userVotes");
                votesListToPass.add(vote);
                wagerVoteRef.setValue(votesListToPass);
            }
        } else if (requestCode == CLOSE_WAGER_REQUEST) {
            if (resultCode == RESULT_OK) {

            }
        }
    }
}
