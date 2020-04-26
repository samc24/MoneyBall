package com.example.moneyball;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class CreateWagerActivity extends AppCompatActivity {
    private final int PICK_IMAGE = 1, REQUEST_READ_STORAGE = 100;
    ImageButton upload;
    Drawable bg;
    Uri selectedImageUri;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    String uriStr, valBet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wager);
        upload = findViewById(R.id.uploadGroupPic);
        final TextView uploadTV = findViewById(R.id.uploadTV);
        final EditText heading = findViewById(R.id.heading);
        final EditText description = findViewById(R.id.description);
//        final EditText groupName = findViewById(R.id.groupName);
        final EditText betValText = findViewById(R.id.betVal);
        final EditText potentialChallengeText = findViewById(R.id.potentialChallenge);
        Button exit = findViewById(R.id.exit);
        Button btnYes = findViewById(R.id.btnYesCreate);
        Button btnNo = findViewById(R.id.btnNoCreate);
        Intent groupInfo = getIntent();
        final String groupId = groupInfo.getStringExtra("groupId");

        betValText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                valBet = betValText.getText().toString();
                Log.d("BET", "afterTextChanged, before .00: " +valBet);
                if(!valBet.contains("."))
                    valBet=valBet+".00";
                Log.d("BET", "afterTextChanged, before .00: " +valBet);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    if (ActivityCompat.shouldShowRequestPermissionRationale(CreateWagerActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Toast.makeText(getApplicationContext(),  "We need permission to upload pictures to associate with your wager", Toast.LENGTH_LONG).show();
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(CreateWagerActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE);
                    }
                }
                else {
                    uploadTV.setVisibility(View.GONE);

                    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getIntent.setType("image/*");

                    Intent pickIntent = new Intent(Intent.ACTION_PICK);
                    pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

                    Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                    startActivityForResult(chooserIntent, PICK_IMAGE);
                }
            }
        });

        final Intent intent = new Intent();
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String headingText = heading.getText().toString();
                String descriptionText = description.getText().toString();
                String groupIdToPass = groupId;
                String voteVal = "Y";
                double betVal = Double.parseDouble(valBet);
                Log.d("BET", "done onClick: "+betVal);
                String potentialChallenge = potentialChallengeText.getText().toString();
                Log.d("tag", groupIdToPass);
                if(headingText.equals("")|| descriptionText.equals("")){
                    Toast.makeText(getApplicationContext(),  "Please enter wager information for all text fields", Toast.LENGTH_SHORT).show();
                }

                else if (potentialChallenge.equals(""))
                    Toast.makeText(getApplicationContext(),  "Please enter one potential challenge", Toast.LENGTH_SHORT).show();

                else {
                    if(selectedImageUri==null || selectedImageUri.toString().equals("")){
                        uriStr = ""; //Toast.makeText(getApplicationContext(),  "add pic", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        uriStr = selectedImageUri.toString();
                    }
                    Toast.makeText(getApplicationContext(),  "Done!", Toast.LENGTH_SHORT).show();
                    intent.putExtra("pic", uriStr);
                    intent.putExtra("headingText", headingText);
                    intent.putExtra("descriptionText", descriptionText);
                    intent.putExtra("groupIdToPass", groupIdToPass);
                    intent.putExtra("betVal", betVal);
                    intent.putExtra("potentialChallenge", potentialChallenge);
                    intent.putExtra("voteVal", voteVal);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String headingText = heading.getText().toString();
                String descriptionText = description.getText().toString();
                String groupIdToPass = groupId;
                String voteVal = "N";
                double betVal = Double.parseDouble(valBet);
                Log.d("BET", "done onClick: "+betVal);
                String potentialChallenge = potentialChallengeText.getText().toString();
                Log.d("tag", groupIdToPass);
                if(headingText.equals("")|| descriptionText.equals("")){
                    Toast.makeText(getApplicationContext(),  "Please enter wager information for all text fields", Toast.LENGTH_SHORT).show();
                }

                else if (potentialChallenge.equals(""))
                    Toast.makeText(getApplicationContext(),  "Please enter one potential challenge", Toast.LENGTH_SHORT).show();

                else {
                    if(selectedImageUri==null || selectedImageUri.toString().equals("")){
                        uriStr = ""; //Toast.makeText(getApplicationContext(),  "add pic", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        uriStr = selectedImageUri.toString();
                    }
                    Toast.makeText(getApplicationContext(),  "Done!", Toast.LENGTH_SHORT).show();
                    intent.putExtra("pic", uriStr);
                    intent.putExtra("headingText", headingText);
                    intent.putExtra("descriptionText", descriptionText);
                    intent.putExtra("groupIdToPass", groupIdToPass);
                    intent.putExtra("betVal", betVal);
                    intent.putExtra("potentialChallenge", potentialChallenge);
                    intent.putExtra("voteVal", voteVal);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            if(resultCode==RESULT_OK){
                selectedImageUri = data.getData();
                upload.setVisibility(View.GONE);
                RelativeLayout layout = findViewById(R.id.groupPicHolder);
                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(selectedImageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bg = Drawable.createFromStream(inputStream, selectedImageUri.toString());
                layout.setBackground(bg);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Toast.makeText(getApplicationContext(),  "Thanks! Please click again", Toast.LENGTH_LONG).show();
                } else {
                    // permission denied
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
        }
    }
}
