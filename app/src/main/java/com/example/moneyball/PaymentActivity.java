package com.example.moneyball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PaymentActivity extends AppCompatActivity {

    ImageButton venmoButton;
    ImageButton googlepayButton;
    TextView Val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment); // initializing layout and views
        venmoButton = findViewById(R.id.venmoButton);
        googlepayButton = findViewById(R.id.googlepayButton);
        Val = findViewById(R.id.betValueButton);

        Val.setText("Pay The Winner of Wager");   //Sets text to TextView


        venmoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //on click listener for venmo option
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.venmo"); //retrieves package details for venmo and stores in variable

                if (launchIntent != null){  // if variable is not empty, then this means venmo is installed in user's phone.
                    startActivity(launchIntent); // launches venmo app
                }else{
                    Toast.makeText(PaymentActivity.this,"Venmo Download Required",Toast.LENGTH_LONG).show(); //if variable empty, display toast telling user to download venmo

                }

            }
        });

        googlepayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //on click listener for venmo option
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.apps.walletnfcrel"); //retrieves package details for googlePay and stores in variable


                if (launchIntent != null){   // if variable is not empty, then this means googlepay app is installed in user's phone.
                    startActivity(launchIntent); //launches googlepay app
                }else{
                    Toast.makeText(PaymentActivity.this,"Google Pay Download Required",Toast.LENGTH_LONG).show(); ////if variable empty, display toast telling user to download googlePay app

                }

            }
        });


    }

}
