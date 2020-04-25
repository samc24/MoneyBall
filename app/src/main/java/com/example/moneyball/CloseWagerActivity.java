package com.example.moneyball;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CloseWagerActivity extends AppCompatActivity {
    Button yes, no;
    TextView heading, description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_wager);
        yes = findViewById(R.id.btnYes);
        no = findViewById(R.id.btnNo);
        heading = findViewById(R.id.tvWagerTitle);
        description = findViewById(R.id.tvWagerDescription);
        Bundle extras = getIntent().getExtras();
        heading.setText(extras.getString("heading"));
        description.setText(extras.getString("description"));

        final String id = extras.getString("id");
        final String groupId = extras.getString("groupId");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(); //get db reference
        final DatabaseReference wagerResultRef = ref.child("groups").child(groupId).child("wagers").child(id).child("wagerResult");
        wagerResultRef.setValue("");

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wagerResultRef.setValue("Y");
                Toast.makeText(getApplicationContext(), "Wager Result is YES", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wagerResultRef.setValue("N");
                Toast.makeText(getApplicationContext(), "Wager Result is NO", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
