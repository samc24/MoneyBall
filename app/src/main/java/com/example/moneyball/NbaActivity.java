package com.example.moneyball;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class NbaActivity extends AppCompatActivity implements GameAdapter.ItemClickListener{
    private GameAdapter gameAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nba);


        RecyclerView wagerList = findViewById(R.id.gameList);
        int numOfColumns = 1;
        RecyclerView.LayoutManager recyclerManager = new GridLayoutManager(getApplicationContext(), numOfColumns);
        wagerList.setLayoutManager(recyclerManager);
        ArrayList<Game> Games = new ArrayList<>();


        // https://stackoverflow.com/questions/6343166/how-to-fix-android-os-networkonmainthreadexception
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        Response response = null;

        for (int i = 1; i <=10; i++) {
            Request request = new Request.Builder()
                    .url("https://free-nba.p.rapidapi.com/games/"+i)
                    .get()
                    .addHeader("x-rapidapi-host", "free-nba.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "5b1e3c5d43mshbff7c5d6ee6bd0bp1d7debjsnf76b534f6cc5")
                    .build();

            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String res = "";
            try {
                res = response.body().string();
                Log.d("NBA", res);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                JSONObject json = new JSONObject(res);
                String home = json.getJSONObject("home_team").getString("full_name");
                String visitor = json.getJSONObject("visitor_team").getString("full_name");
                String score = json.getString("home_team_score") + " - " + json.getString("visitor_team_score");
                Game g =new Game( home, visitor, score);
                Games.add(g);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        gameAdapter = new GameAdapter(Games);
        wagerList.setAdapter(gameAdapter);
        ((GameAdapter) gameAdapter).setClickListener(this);

    }

    @Override
    public void onItemClick(View view, int position) {
        String msg = ((GameAdapter) gameAdapter).getItem(position).getScore();
        Toast.makeText(getApplicationContext(),  msg, Toast.LENGTH_LONG).show();
    }
}
