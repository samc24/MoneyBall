package com.example.moneyball;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity implements WagerAdapter.ItemClickListener {
    private RecyclerView.Adapter wagerAdapter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        RecyclerView wagerList = findViewById(R.id.group_bets);
        int numOfColumns = 2;
        RecyclerView.LayoutManager recyclerManager = new GridLayoutManager(getApplicationContext(), numOfColumns);
        wagerList.setLayoutManager(recyclerManager);
        ArrayList<Wager> wagers = new ArrayList<>();
        Wager flops =new Wager(1, "Harden Flops", "Mamba Gang", R.drawable.kobe_jersey, "Over/Under: \n" + "6 flops in tonight’s game vs the Thunder");
        wagers.add(flops);
        wagers.add(flops);
        wagers.add(flops);
        wagers.add(flops);
        wagerAdapter = new WagerAdapter(wagers);
        wagerList.setAdapter(wagerAdapter);
        ((WagerAdapter) wagerAdapter).setClickListener(this);

    }
    @Override
    // https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
    public void onItemClick(View view, int position) {
        String msg = ((WagerAdapter) wagerAdapter).getItem(position).getDescription();
        Toast.makeText(getApplicationContext(),  msg, Toast.LENGTH_SHORT).show();
    }
}