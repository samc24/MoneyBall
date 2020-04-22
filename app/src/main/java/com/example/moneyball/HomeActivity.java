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

public class HomeActivity extends AppCompatActivity implements WagerAdapter.ItemClickListener {
    private RecyclerView.Adapter wagerAdapter;
    public static final int ADD_WAGER_REQUEST = 1;
    public static final int FROM_LOGIN = 2;
    int id = 1; // just added an ID field, not sure if it'll be used
    ArrayList<Wager> wagers;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RecyclerView wagerList = findViewById(R.id.wagerList);
        int numOfColumns = 2;
        RecyclerView.LayoutManager recyclerManager = new GridLayoutManager(getApplicationContext(), numOfColumns);
        wagerList.setLayoutManager(recyclerManager);
        wagers = new ArrayList<>();
        ref = ref.child("wagers");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                wagers.clear();
                HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                Log.d("tag", dataMap.toString());
                for(String key : dataMap.keySet()){
                    Object data = dataMap.get(key);
                    HashMap<String, Object> wagerData = (HashMap<String, Object>) data;
                    String groupName = wagerData.get("group").toString();
                    String heading = wagerData.get("heading").toString();
                    String description = wagerData.get("description").toString();
                    String picture = wagerData.get("picture").toString();
                    String wagerCreator = wagerData.get("wagerCreator").toString();
                    ArrayList<String> usersList = (ArrayList<String>)wagerData.get("usersList");
                    Boolean openStatus = (Boolean)wagerData.get("openStatus");
                    Wager newWager = new Wager(key, heading, groupName, picture, description, wagerCreator, usersList, openStatus);
                    wagers.add(newWager);
                    wagerAdapter.notifyDataSetChanged();
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
        wagerAdapter = new WagerAdapter(wagers);
        wagerList.setAdapter(wagerAdapter);
        ((WagerAdapter) wagerAdapter).setClickListener(this);

        // Proposing a wager
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addWager = new Intent(getApplicationContext(), CreateWagerActivity.class);
                startActivityForResult(addWager, ADD_WAGER_REQUEST);
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });

    }
    @Override
    // https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
    public void onItemClick(View view, int position) {
        String description = ((WagerAdapter) wagerAdapter).getItem(position).getDescription();
        String heading = ((WagerAdapter) wagerAdapter).getItem(position).getHeading();
        String group = ((WagerAdapter) wagerAdapter).getItem(position).getGroup();
        String pic = ((WagerAdapter) wagerAdapter).getItem(position).getPicture();
        //long id = ((WagerAdapter) wagerAdapter).getItem(position).getId();
//        Toast.makeText(getApplicationContext(),  description, Toast.LENGTH_SHORT).show();
        Intent openWager = new Intent(getApplicationContext(), WagerActivity.class);
        openWager.putExtra("description", description);
        openWager.putExtra("heading", heading);
        openWager.putExtra("group", group);
        openWager.putExtra("id", id);
        openWager.putExtra("pic", pic);
        startActivity(openWager);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_WAGER_REQUEST){
            if(resultCode == RESULT_OK){
                String imageUri = (data.getStringExtra("pic"));
                String heading = data.getStringExtra("headingText");
                String description = data.getStringExtra("descriptionText");
                String group = data.getStringExtra("groupNameText");


                DatabaseReference ref = database.getReference();
                DatabaseReference wagerRef = ref.child("wagers").push();
                String key = wagerRef.getKey();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String UID = "";
                if(user!=null){
                    UID = user.getUid();
                }
                ArrayList<String> usersList = new ArrayList<String>(); //new list of users that have entered the wager
                usersList.add(UID); //auto add the creator of the wager
                final Wager newWager = new Wager(key, heading, group, imageUri, description, UID, usersList, true); //create wager // TODO: add functionality in Wager class for when a uri (imageUri) is passed to be used as prof pic instead of drawable
                wagerRef.setValue(newWager);
                //wagers.add(newWager);
                //wagerAdapter.notifyDataSetChanged();

            }
            else if (resultCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(),  "New Wager Canceled", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == FROM_LOGIN){
            String UID = data.getStringExtra("UID");
            Log.d("tag", UID);
        }
    }
}
