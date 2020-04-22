package com.example.moneyball;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class CreateGroupActivity extends AppCompatActivity {
    private final int PICK_IMAGE = 1;
    private final int NEW_GROUP = 123;
    private final int ADD_GROUP = 321;
    Drawable bg;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    private FirebaseAuth mAuth;
    private ImageButton uploadGP;
    private RelativeLayout groupPicHolder;
    private Uri selectedImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        final EditText heading = findViewById(R.id.groupHeading);
        final EditText description = findViewById(R.id.groupDescription);
        final EditText join_group = findViewById(R.id.etJoin_Group);
        Button btn_join_group = findViewById(R.id.btnJoin_group);
        Button exit = findViewById(R.id.exit);
        Button done = findViewById(R.id.done);

        groupPicHolder = findViewById(R.id.groupPicHolder);
        final TextView uploadTV = findViewById(R.id.uploadTV);
        uploadGP = findViewById(R.id.uploadGroupPic);
        uploadGP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadTV.setVisibility(View.GONE);

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK);
                pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });

        final Intent intent = new Intent();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String headingText = heading.getText().toString();
                String descriptionText = description.getText().toString();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String UID = "";
                String picUriStr = "";
                if(user!=null){
                    UID = user.getUid();
                }

                if(headingText.equals("")|| descriptionText.equals("")){
                    Toast.makeText(getApplicationContext(),  "Please enter group information for all text fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (selectedImageUri == null || selectedImageUri.toString().equals("")) {
                        picUriStr = ""; //Toast.makeText(getApplicationContext(),  "add pic", Toast.LENGTH_SHORT).show();
                    } else {
                        picUriStr = selectedImageUri.toString();
                    }
                    Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
                    intent.putExtra("groupCreator", UID);
                    intent.putExtra("headingText", headingText);
                    intent.putExtra("descriptionText", descriptionText);
                    intent.putExtra("pic", picUriStr);
                    setResult(NEW_GROUP, intent);
                    finish();
                }

            }
        });

        //Button for joining a group
        btn_join_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String group = join_group.getText().toString();
                ref = ref.child("groups");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Boolean validGroupName = false;
                        HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                        if(dataMap!=null) {

                            for (String key : dataMap.keySet()) {
                                Object data = dataMap.get(key);
                                HashMap<String, Object> groupData = (HashMap<String, Object>) data;
                                String heading = groupData.get("heading").toString();
                                String description = groupData.get("description").toString();
                                String groupCreator = groupData.get("groupCreator").toString();
                                String groupId = groupData.get("id").toString();
                                Log.d("joingroup", heading);
                                Log.d("joingroup", group);
                                if(groupId.equals(group)){
                                    Log.d("joingroup", "EQUALS!");
                                    intent.putExtra("groupCreator", groupCreator);
                                    intent.putExtra("headingText", heading);
                                    intent.putExtra("descriptionText", description);
                                    intent.putExtra("id", groupId);
                                    setResult(ADD_GROUP, intent);
                                    finish();
                                }

                                //Long picture = (Long)wagerData.get("picture");

                            }
                            //Toast.makeText(getApplicationContext(),  "Not a valid group", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });


            }
        });


        // TODO
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICK_IMAGE){
            if(resultCode==RESULT_OK){
                selectedImageUri = data.getData();
                uploadGP.setVisibility(View.GONE);
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
}
