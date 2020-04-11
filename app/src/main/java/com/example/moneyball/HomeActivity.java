package com.example.moneyball;

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

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements WagerAdapter.ItemClickListener {
    private RecyclerView.Adapter wagerAdapter;
    public static final int ADD_WAGER_REQUEST = 1;
    int id = 1; // just added an ID field, not sure if it'll be used
    ArrayList<Wager> wagers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RecyclerView wagerList = findViewById(R.id.wagerList);
        int numOfColumns = 2;
        RecyclerView.LayoutManager recyclerManager = new GridLayoutManager(getApplicationContext(), numOfColumns);
        wagerList.setLayoutManager(recyclerManager);
        wagers = new ArrayList<>();
        Wager flops =new Wager(id, "Harden Flops", "Mamba Gang", R.drawable.kobe_jersey, "Over/Under: \n" + "6 flops in tonight’s game vs the Thunder");
        id++;
        Wager sun = new Wager(id, "Sun Disappears", "Weather Watchers", R.drawable.weather, "Bruh the Sun just disappeared what happened");
        id++;
        wagers.add(flops);
        wagers.add(sun);
        wagers.add(flops);
        wagers.add(flops);
        wagers.add(flops);
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
        String msg = ((WagerAdapter) wagerAdapter).getItem(position).getDescription();
        Toast.makeText(getApplicationContext(),  msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_WAGER_REQUEST){
            if(resultCode == RESULT_OK){
                Uri imageUri = Uri.parse(data.getStringExtra("pic"));
                String heading = data.getStringExtra("headingText");
                String description = data.getStringExtra("descriptionText");
                String group = data.getStringExtra("groupNameText");

                Wager newWager = new Wager(id, heading, group, R.drawable.weather, description); // TODO: add functionality in Wager class for when a uri (imageUri) is passed to be used as prof pic instead of drawable
                wagers.add(newWager);
                wagerAdapter.notifyDataSetChanged();

            }
        }
    }
}
