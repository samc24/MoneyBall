package com.example.moneyball;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class ProfilePreferencesPage extends AppCompatActivity {
    private final int PICK_IMAGE = 1;

    Drawable bg;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final FirebaseStorage storage = FirebaseStorage.getInstance();
    final DatabaseReference db_ref = database.getReference();
    final private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ImageButton uploadProfilePic;
    private RelativeLayout profilePicHolder;
    private TextView emailTV;
    private EditText usernameET;
    private Uri profilePicUri;
    private Button notifyButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        emailTV = findViewById(R.id.emailTV);
        if(user.getEmail() != null){
            emailTV.setText(user.getEmail());
        }
        else{
            emailTV.setText("Anonymous");
        }

        StorageReference ref = storage.getReference();
        ref.child("profile_pictures/" + user.getUid() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                profilePicHolder = findViewById(R.id.profilePicHolder);

                Picasso.get().load(uri).into(new Target(){
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        profilePicHolder.setBackground(new BitmapDrawable(getApplicationContext().getResources(), bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        Log.e("TAG", "Failed");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        Log.e("TAG", "Prepare Load");
                    }
                });
            }
        });

        final TextView uploadTV = findViewById(R.id.uploadTV);
        uploadProfilePic = findViewById(R.id.uploadProfilePic);
        uploadProfilePic.setOnClickListener(new View.OnClickListener() {
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

        final DatabaseReference username_ref = db_ref.child("users/" + user.getUid()).child("profile").child("username");
        usernameET = findViewById(R.id.usernameInput);
        username_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = (String) dataSnapshot.getValue();//we can do this because we are already at the username key and we are just getting access to what "username" holds
                if (username != null) {
                    usernameET.setText(username);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                String username = usernameET.getText().toString();
                if (!username.equals("")){
                    username_ref.setValue(username);
                }

                final StorageReference ref = storage.getReference();
                if (profilePicUri != null) {
                    final StorageReference profilePicRef = ref.child("profile_pictures/" + user.getUid() + ".png");
                    profilePicRef.putFile(profilePicUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    profilePicUri = uri;
                                }
                            });
                        }

                    });
                }
                finish();
            }
        });

//        notifyButton = findViewById(R.id.notifications);
//        notifyButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
//
////for Android 5-7
//                intent.putExtra("app_package", getPackageName());
//                intent.putExtra("app_uid", getApplicationInfo().uid);
////                intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
//
//                startActivity(intent);
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                profilePicUri = data.getData();
                RelativeLayout layout = findViewById(R.id.profilePicHolder);
                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(profilePicUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bg = Drawable.createFromStream(inputStream, profilePicUri.toString());
                layout.setBackground(bg);
            }
        }
    }
}
