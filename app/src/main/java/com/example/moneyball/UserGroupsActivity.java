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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                        Log.d("tag2", data.toString());
                        Log.d("tag2", usersGroups.toString());
                    }
                }
                Log.d("tag3",dataMap.toString());
                if(objData!=null) {
                    Log.d("tag", objData.toString());
                    for (String key : objData.keySet()) {
                        Object data = objData.get(key);
                        if(usersGroups.contains(key)){
                            HashMap<String, Object> groupData = (HashMap<String, Object>) data;
                            String heading = groupData.get("heading").toString();
                            String description = groupData.get("description").toString();
                            String groupCreator = groupData.get("groupCreator").toString();
                            //Long picture = (Long)wagerData.get("picture");
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


        /*
        Wager flops =new Wager(id, "Harden Flops", "Mamba Gang", R.drawable.kobe_jersey, "Over/Under: \n" + "6 flops in tonight’s game vs the Thunder");
        id++;
        Wager sun = new Wager(id, "Sun Disappears", "Weather Watchers", R.drawable.weather, "Bruh the Sun just disappeared what happened");
        id++;
        wagers.add(flops);
        wagers.add(sun);
        wagers.add(flops);
        wagers.add(flops);
        wagers.add(flops);
         */
        groupAdapter = new GroupAdapter(groups);
        groupList.setAdapter(groupAdapter);
        ((GroupAdapter) groupAdapter).setClickListener(this);

        // Adding a group
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addGroup = new Intent(getApplicationContext(), CreateGroupActivity.class);

                startActivityForResult(addGroup, ADD_WAGER_REQUEST);
            }
        });

    }



    @Override
    // https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
    public void onItemClick(View view, int position) {
        String msg = ((GroupAdapter) groupAdapter).getItem(position).getDescription();
        Intent viewGroup = new Intent(getApplicationContext(), GroupActivity.class);
        viewGroup.putExtra("groupId",((GroupAdapter) groupAdapter).getItem(position).getId());
        viewGroup.putExtra("heading",((GroupAdapter) groupAdapter).getItem(position).getHeading());
        viewGroup.putExtra("description",((GroupAdapter) groupAdapter).getItem(position).getDescription());
        startActivity(viewGroup);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_WAGER_REQUEST){
            if(resultCode == NEW_GROUP){
                String heading = data.getStringExtra("headingText");
                String description = data.getStringExtra("descriptionText");
                String groupCreator = data.getStringExtra("groupCreator");

                DatabaseReference ref = database.getReference();
                DatabaseReference groupRef = ref.child("groups").push();
                String key = groupRef.getKey();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String UID = "";
                if(user!=null){
                    UID = user.getUid();
                }

                DatabaseReference userGroupsRef = ref.child("users").child(UID).child("groups").push();
                userGroupsRef.setValue(groupRef.getKey());

                Group newGroup = new Group(key, heading, description, groupCreator);
                groupRef.setValue(newGroup);
                //wagers.add(newWager);
                //wagerAdapter.notifyDataSetChanged();

            }
            if(resultCode == ADD_GROUP){
                Log.d("joingroup", "MADE IT HERE");
                String heading = data.getStringExtra("headingText");
                String description = data.getStringExtra("descriptionText");
                String groupCreator = data.getStringExtra("groupCreator");
                String id = data.getStringExtra("id");
                Log.d("joingroup", "ID IS" + id);
                DatabaseReference ref = database.getReference();
                //DatabaseReference groupRef = ref.child("groups").push();
                //String key = groupRef.getKey();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String UID = "";
                if(user!=null){
                    UID = user.getUid();
                }

                DatabaseReference userGroupsRef = ref.child("users").child(UID).child("groups").push();
                userGroupsRef.setValue(id);

                //Group newGroup = new Group(key, heading, description, groupCreator);
                //groupRef.setValue(newGroup);
                //wagers.add(newWager);
                //wagerAdapter.notifyDataSetChanged();

            }

        }
        if (requestCode == FROM_LOGIN){
            String UID = data.getStringExtra("UID");
            Log.d("tag", UID);
        }
    }
}
