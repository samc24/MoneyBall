package com.example.moneyball;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupActivity extends AppCompatActivity implements WagerAdapter.ItemClickListener {
    private RecyclerView.Adapter wagerAdapter;
    ArrayList<Wager> wagers;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    TextView heading;
    TextView description;
    Button backButton;
    String groupId;
    String groupHeading;
    String groupDescription;
    public static final int ADD_WAGER_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);


        Intent intent = getIntent(); //get intent that was passed from the previous page
        //get data passed from intent
        final String groupId = intent.getStringExtra("groupId");
        groupDescription = intent.getStringExtra("description");
        groupHeading = intent.getStringExtra("heading");

        //initialize views
        heading = findViewById(R.id.tvGroup_Title);
        description = findViewById(R.id.tvGroup_Description);
        backButton = findViewById(R.id.btnGroup_Back);
        RecyclerView wagerList = findViewById(R.id.group_bets);

        //set the heading and description text for the group
        heading.setText(groupHeading);
        description.setText(groupDescription);
        int numOfColumns = 2;
        RecyclerView.LayoutManager recyclerManager = new GridLayoutManager(getApplicationContext(), numOfColumns);
        wagerList.setLayoutManager(recyclerManager);

        wagers = new ArrayList<>(); //this will be used to store the groups wagers
        ref = ref.child("groups").child(groupId).child("wagers"); //get the reference to the wagers for this specific group in the database
        ref.addValueEventListener(new ValueEventListener() {
            //read the wager data from the database
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                wagers.clear(); //clear the wagers arraylist so to avoid adding duplicates
                HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue(); //get the database data as a hashmap
                if(dataMap!=null) { //check if its null to avoid errors
                    for (String key : dataMap.keySet()) {   //loop through the wagers
                        Object data = dataMap.get(key);
                        HashMap<String, Object> wagerData = (HashMap<String, Object>) data;

                        //separate the data by groupname, heading, and description
                        String groupName = wagerData.get("group").toString();
                        String heading = wagerData.get("heading").toString();
                        String description = wagerData.get("description").toString();

                        Wager newWager = new Wager(key, heading, groupName, R.drawable.kobe_jersey, description);  //create the new wager using the data from above
                        wagers.add(newWager); //add this wager to a list of wagers
                        wagerAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        wagerAdapter = new WagerAdapter(wagers);
        wagerList.setAdapter(wagerAdapter);
        ((WagerAdapter) wagerAdapter).setClickListener(this);

        // Proposing a wager
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addWager = new Intent(getApplicationContext(), CreateWagerActivity.class);
                addWager.putExtra("groupId", groupId);
                startActivityForResult(addWager, ADD_WAGER_REQUEST);
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });

        //button to return to the users groups page
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToGroupsPage = new Intent(getApplicationContext(), UserGroupsActivity.class);
                startActivity(backToGroupsPage);
            }
        });

    }
    @Override
    // https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
    public void onItemClick(View view, int position) {
        //Get wager data to be passed to wager activity
        String description = ((WagerAdapter) wagerAdapter).getItem(position).getDescription();
        String heading = ((WagerAdapter) wagerAdapter).getItem(position).getHeading();
        int pic = ((WagerAdapter) wagerAdapter).getItem(position).getPicture();

        Intent openWager = new Intent(getApplicationContext(), WagerActivity.class); //create the intent
        //pass data to intent
        openWager.putExtra("description", description);
        openWager.putExtra("heading", heading);
        openWager.putExtra("group", groupHeading);
        openWager.putExtra("groupDescription", groupDescription);
        openWager.putExtra("id", 0L); // change 0L to id
        openWager.putExtra("pic", pic);
        startActivity(openWager);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_WAGER_REQUEST){
            if(resultCode == RESULT_OK){
                //Get data from create wager activity to be used in adding the wager data to the database
                Uri imageUri = Uri.parse(data.getStringExtra("pic"));
                String heading = data.getStringExtra("headingText");
                String description = data.getStringExtra("descriptionText");
                String group = data.getStringExtra("groupNameText");


                DatabaseReference ref = database.getReference();    //Get database reference
                DatabaseReference wagerRef = ref.child("groups").child(group).child("wagers").push(); //Find specific spot in database to place data (push creates unique key)
                String key = wagerRef.getKey(); //get the key in order to store it in the wager class
                Wager newWager = new Wager(key, heading, group, R.drawable.weather, description); //create wager
                wagerRef.setValue(newWager); //set the value in the database to be that of the wager
            }
        }
    }
}