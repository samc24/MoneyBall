package com.example.moneyball;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class GroupActivity extends AppCompatActivity implements WagerAdapter.ItemClickListener {
    private RecyclerView.Adapter wagerAdapter;
    ArrayList<Wager> wagers;
    final FirebaseStorage storage = FirebaseStorage.getInstance();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    TextView heading;
    TextView description;
    ImageButton backButton;
    String groupId;
    String groupHeading;
    String groupDescription;
    Uri groupPicUri;
    ImageView groupPic;
    String groupIdToPass;
    public static final int ADD_WAGER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

//        Bundle extras = getIntent().getExtras();
        Intent intent = getIntent(); //get intent that was passed from the previous page
        //get data passed from intent
        final String groupId = intent.getStringExtra("groupId");
        groupIdToPass = groupId;
        groupDescription = intent.getStringExtra("description");
        groupHeading = intent.getStringExtra("heading");
        groupPicUri = Uri.parse(intent.getStringExtra("groupPic"));

        //initialize views
        heading = findViewById(R.id.tvGroup_Title);
        description = findViewById(R.id.tvGroup_Description);
        backButton = findViewById(R.id.btnGroup_Back);
        RecyclerView wagerList = findViewById(R.id.group_bets);
        groupPic = findViewById(R.id.groupPic);

        //sets the groupPic within this activity:
        Picasso.get().load(groupPicUri).into(groupPic);

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

                        //separate the data by groupname, heading, and description and pic
                        String groupName = wagerData.get("group").toString();
                        String heading = wagerData.get("heading").toString();
                        String description = wagerData.get("description").toString();
                        String pic = wagerData.get("picture").toString();
                        String wagerCreator = wagerData.get("wagerCreator").toString();
                        ArrayList<String> usersList = (ArrayList<String>)wagerData.get("usersList");
                        Boolean openStatus = (Boolean)wagerData.get("openStatus");

                        Log.d("KOBE", pic);

                        Wager newWager = new Wager(key, heading, groupName, pic, description, wagerCreator, usersList, openStatus);  //create the new wager using the data from above
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

        Button invite;
        invite = findViewById(R.id.invite);

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

    @Override
    // https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
    public void onItemClick(View view, int position) {
        //Get wager data to be passed to wager activity
        ArrayList<String> usersList = ((WagerAdapter) wagerAdapter).getItem(position).getUsersList();
        String description = ((WagerAdapter) wagerAdapter).getItem(position).getDescription();
        String heading = ((WagerAdapter) wagerAdapter).getItem(position).getHeading();
        String pic = ((WagerAdapter) wagerAdapter).getItem(position).getPicture();
        String id = ((WagerAdapter) wagerAdapter).getItem(position).getId();
        String wagerCreator = ((WagerAdapter) wagerAdapter).getItem(position).getWagerCreator();

        Intent openWager = new Intent(getApplicationContext(), WagerActivity.class); //create the intent
        //pass data to intent

        openWager.putExtra("usersList", usersList);
        openWager.putExtra("description", description);
        openWager.putExtra("heading", heading);
        openWager.putExtra("wagerCreator", wagerCreator);
        openWager.putExtra("group", groupHeading);
        openWager.putExtra("groupDescription", groupDescription);
        openWager.putExtra("groupId", groupIdToPass);
        openWager.putExtra("id", id); // change 0L to id
        openWager.putExtra("pic", pic);//THIS IS THE WAGER PICTURE
        openWager.putExtra("groupPic", groupPicUri.toString());
        startActivity(openWager);

    }

    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == ADD_WAGER_REQUEST){
            if(resultCode == RESULT_OK){
                //Get data from create wager activity to be used in adding the wager data to the database
                String imageUri = data.getStringExtra("pic");
                final String heading = data.getStringExtra("headingText");
                final String description = data.getStringExtra("descriptionText");
                final String group = data.getStringExtra("groupIdToPass");

                final DatabaseReference ref = database.getReference();    //Get database reference
                final DatabaseReference wagerRef = ref.child("groups").child(group).child("wagers").push();//Find specific spot in database to place data (push creates unique key)
                final String key = wagerRef.getKey(); //get the key in order to store it in the wager class

                if(!imageUri.equals("")) {//can't be null as no images selected when pressing means empty string
                   final StorageReference imageStorageReference = storage.getReference().child("images/" + key + ".png");
                   imageStorageReference.putFile(Uri.parse(imageUri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           Toast.makeText(getApplicationContext(), "upload image success!", Toast.LENGTH_SHORT).show();
                           //get metadata and path from storage
                           StorageMetadata snapshotMetadata = taskSnapshot.getMetadata();
                           Task<Uri> downloadUrl = imageStorageReference.getDownloadUrl();
                           downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {
                                   String imageReference = uri.toString();
                                   FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                   String UID = "";
                                   if(user!=null){
                                       UID = user.getUid();
                                   }
                                   ArrayList<String> usersList = new ArrayList<String>(); //new list of users that have entered the wager
                                   usersList.add(UID); //auto add the creator of the wager
                                   Wager newWager = new Wager(key, heading, group, imageReference, description, UID, usersList, true); //create wager
                                       wagerRef.setValue(newWager); //set the value in the database to be that of the wager

                               }
                           });
                       }
                   });
                }
                else{//this accounts for when the user doesn't select an image for the wager, imageUri will then be empty string.
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String UID = "";
                    if(user!=null){
                        UID = user.getUid();
                    }
                    ArrayList<String> usersList = new ArrayList<String>(); //new list of users that have entered the wager
                    usersList.add(UID); //auto add the creator of the wager
                   final Wager newWager = new Wager(key, heading, group, "", description, UID, usersList, true); //create wager
                   wagerRef.setValue(newWager); //set the value in the database to be that of the wager
                }
            }
            else if (resultCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(),  "New Wager Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}