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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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

import java.util.ArrayList;
import java.util.HashMap;

public class UserGroupsActivity extends AppCompatActivity implements GroupAdapter.ItemClickListener {
    private RecyclerView.Adapter groupAdapter;
    public static final int ADD_WAGER_REQUEST = 1;
    public static final int FROM_LOGIN = 2;
    private final int NEW_GROUP = 123;
    private final int ADD_GROUP = 321;
    int id = 1; // just added an ID field, not sure if it'll be used
    ArrayList<Group> groups;
    ArrayList<String> usersGroups;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseReference ref = database.getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_group);
        RecyclerView groupList = findViewById(R.id.groupList);
        int numOfColumns = 2;
        RecyclerView.LayoutManager recyclerManager = new GridLayoutManager(getApplicationContext(), numOfColumns);
        groupList.setLayoutManager(recyclerManager);
        groups = new ArrayList<>();
        usersGroups = new ArrayList<>();




        DatabaseReference groupRef = ref;
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groups.clear();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String UID = "";
                if(user!=null){
                    UID = user.getUid();
                }
                HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                HashMap<String, Object> objData = (HashMap<String, Object>) dataMap.get("groups");
                HashMap<String, Object> userData = (HashMap<String, Object>) dataMap.get("users");
                HashMap<String, Object> userSpecificData = (HashMap<String, Object>) userData.get(UID);
                HashMap<String, Object> userGroupData = (HashMap<String, Object>) userSpecificData.get("groups");
                if(userGroupData!=null) {
                    for (String key : userGroupData.keySet()) {
                        Object data = userGroupData.get(key);
                        usersGroups.add(data.toString());
                    }
                }

                if(objData!=null) {
                    for (String key : objData.keySet()) {
                        Object data = objData.get(key);
                        if(usersGroups.contains(key)){
                            HashMap<String, Object> groupData = (HashMap<String, Object>) data;
                            String heading = groupData.get("heading").toString();
                            String description = groupData.get("description").toString();
                            String groupCreator = groupData.get("groupCreator").toString();
                            Group newGroup = new Group(key, heading, description, groupCreator);
                            groups.add(newGroup);
                            groupAdapter.notifyDataSetChanged();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        groupAdapter = new GroupAdapter(groups);
        groupList.setAdapter(groupAdapter);
        ((GroupAdapter) groupAdapter).setClickListener(this);

        //Click on the floating button to add or join a group
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //leads to create group activity
                Intent addGroup = new Intent(getApplicationContext(), CreateGroupActivity.class);
                startActivityForResult(addGroup, ADD_WAGER_REQUEST);
            }
        });

    }



    @Override
    // https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
    public void onItemClick(View view, int position) {
        //View the group you click on
        Intent viewGroup = new Intent(getApplicationContext(), GroupActivity.class);
        //put group data into intent to be passed to group activity
        viewGroup.putExtra("groupId",((GroupAdapter) groupAdapter).getItem(position).getId());
        viewGroup.putExtra("heading",((GroupAdapter) groupAdapter).getItem(position).getHeading());
        viewGroup.putExtra("description",((GroupAdapter) groupAdapter).getItem(position).getDescription());
        startActivity(viewGroup);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_WAGER_REQUEST){
            if(resultCode == NEW_GROUP){
                //This is called if a user creates a new group
                //get data passed from create group activity
                final String heading = data.getStringExtra("headingText");
                final String description = data.getStringExtra("descriptionText");
                final String groupCreator = data.getStringExtra("groupCreator");
                String picUri = data.getStringExtra("pic");

                DatabaseReference ref = database.getReference(); //get db reference
                final DatabaseReference groupRef = ref.child("groups").push(); //get the path to store the data
                final String key = groupRef.getKey(); //get the key for storing in a users database

                //get current users ID
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String UID = "";
                if(user!=null){
                    UID = user.getUid();
                }

                final DatabaseReference userGroupsRef = ref.child("users").child(UID).child("groups").push(); //get path to store groupID under a users data
                userGroupsRef.setValue(groupRef.getKey()); //store the group ID (so we know what groups a user is in)=

                if (!picUri.equals("")) {//can't be null as no images selected when pressing means empty string
                    final StorageReference imageStorageReference = storage.getReference().child("images/groups/" + key + ".png");
                    imageStorageReference.putFile(Uri.parse(picUri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                                    Group newGroup = new Group(key, heading, description, groupCreator);
                                    groupRef.setValue(newGroup);
                                }
                            });
                        }
                    });
                }
                else {
                    Group newGroup = new Group(key, heading, description, groupCreator); //create the group
                    groupRef.setValue(newGroup); //set the db value
                }
            }
            if(resultCode == ADD_GROUP){
                //This is called if a user adds an existing group
                //To add the groupID field to a users data in the database
                String id = data.getStringExtra("id"); //get the group ID
                DatabaseReference ref = database.getReference(); //get the db reference

                //get the ID of the current user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String UID = "";
                if(user!=null){
                    UID = user.getUid();
                }

                DatabaseReference userGroupsRef = ref.child("users").child(UID).child("groups").push(); //get the correct path to write the data
                userGroupsRef.setValue(id); //write the data


            }

        }
        if (requestCode == FROM_LOGIN){
            String UID = data.getStringExtra("UID");
            Log.d("tag", UID);
        }
    }
}
