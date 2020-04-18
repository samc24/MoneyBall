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
    public static final int ADD_WAGER_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Intent intent = getIntent();
        final String groupId = intent.getStringExtra("groupId");
        String groupDescription = intent.getStringExtra("description");
        String groupHeading = intent.getStringExtra("heading");
        heading = findViewById(R.id.tvGroup_Title);
        description = findViewById(R.id.tvGroup_Description);
        backButton = findViewById(R.id.btnGroup_Back);
        heading.setText(groupHeading);
        description.setText(groupDescription);
        RecyclerView wagerList = findViewById(R.id.group_bets);
        int numOfColumns = 2;
        RecyclerView.LayoutManager recyclerManager = new GridLayoutManager(getApplicationContext(), numOfColumns);
        wagerList.setLayoutManager(recyclerManager);
        wagers = new ArrayList<>();
        ref = ref.child("groups").child(groupId).child("wagers");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                wagers.clear();
                HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                if(dataMap!=null) {
                    Log.d("tag", dataMap.toString());
                    for (String key : dataMap.keySet()) {
                        Object data = dataMap.get(key);
                        HashMap<String, Object> wagerData = (HashMap<String, Object>) data;
                        String groupName = wagerData.get("group").toString();
                        String heading = wagerData.get("heading").toString();
                        String description = wagerData.get("description").toString();
                        //Long picture = (Long)wagerData.get("picture");
                        Wager newWager = new Wager(key, heading, groupName, R.drawable.kobe_jersey, description);
                        wagers.add(newWager);
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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToGroupsPage = new Intent(getApplicationContext(), UserGroupsActivity.class);
                startActivity(backToGroupsPage);
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });

    }
    @Override
    // https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
    public void onItemClick(View view, int position) {
        String msg = ((WagerAdapter) wagerAdapter).getItem(position).getDescription();
        Toast.makeText(getApplicationContext(),  msg, Toast.LENGTH_SHORT).show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_WAGER_REQUEST){
            if(resultCode == RESULT_OK){
                Uri imageUri = Uri.parse(data.getStringExtra("pic"));
                String heading = data.getStringExtra("headingText");
                String description = data.getStringExtra("descriptionText");
                String group = data.getStringExtra("groupNameText");


                DatabaseReference ref = database.getReference();
                DatabaseReference wagerRef = ref.child("groups").child(group).child("wagers").push();
                String key = wagerRef.getKey();
                Wager newWager = new Wager(key, heading, group, R.drawable.weather, description); // TODO: add functionality in Wager class for when a uri (imageUri) is passed to be used as prof pic instead of drawable
                wagerRef.setValue(newWager);
                //wagers.add(newWager);
                //wagerAdapter.notifyDataSetChanged();

            }
        }
    }
}