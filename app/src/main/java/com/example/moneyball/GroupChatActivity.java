package com.example.moneyball;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GroupChatActivity extends AppCompatActivity {

    private Button sendButton;
    private EditText userMessageInput;
    private RecyclerView userMessagesList;
    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;


    private FirebaseAuth myAuthorization;
    private DatabaseReference UsersRef, GroupMessageKeyRef,currentGroupRef;



    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();

    private String currentGroupID, currentUserID, currentUserName, currentDate, currentTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        InitializeFields(); //calls function that initializes all view on layout.

        myAuthorization = FirebaseAuth.getInstance(); // used as reference
        UsersRef = FirebaseDatabase.getInstance().getReference().child("users"); //used to reference current user in database
        currentUserID = myAuthorization.getUid(); // collect current user's ID using reference
        currentGroupID = getIntent().getExtras().get("groupId").toString(); //collects intent from GroupActivity which passes group id.
        currentGroupRef = ref.child("groups").child(currentGroupID).child("chatId"); // stores reference to current group that user is in


        //used to collect current username of current user using reference to database
        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    currentUserName = dataSnapshot.child("profile").child("username").getValue().toString(); //collect username for display in chat
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //on click for the send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage(); ///sends message to db and posts it on UI

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        //collects current message data and displays on screen
        currentGroupRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    Messages messages = dataSnapshot.getValue(Messages.class);

                    messagesList.add(messages);
                    messageAdapter.notifyDataSetChanged();

                    userMessagesList.smoothScrollToPosition(userMessagesList.getAdapter().getItemCount());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void InitializeFields(){
        sendButton = (Button) findViewById(R.id.send_button); //button to send message
        userMessageInput = findViewById(R.id.input_message); //user input for message
        messageAdapter = new MessageAdapter(messagesList);//list of messages for adapter
        userMessagesList = (RecyclerView)findViewById(R.id.message_display);//initialized recycler view used for messages
        linearLayoutManager = new LinearLayoutManager(this);
        userMessagesList.setLayoutManager(linearLayoutManager);
        userMessagesList.setAdapter(messageAdapter);

    }


    ///stores message info in database
    private void SendMessage(){

        String messageText = userMessageInput.getText().toString(); //converts message to text
        String messageKey = currentGroupRef.push().getKey(); //pushes new message into database and stores its unique key
        userMessageInput.setText(""); //clears EditText view on UI after user send message

        if (TextUtils.isEmpty(messageText)){
            Toast.makeText(this,"Write your message", Toast.LENGTH_LONG);

        }
        ///sets message in db
        else{
            //collects Date information
            Calendar callforDate = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            currentDate = currentDateFormat.format(callforDate.getTime());

            //collects Time information
            Calendar callforTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = currentTimeFormat.format(callforTime.getTime());

            HashMap<String, Object> groupMessageKey = new HashMap<>();
            currentGroupRef.updateChildren(groupMessageKey);

            GroupMessageKeyRef = currentGroupRef.child(messageKey); // references unique message key in database
            HashMap<String, Object> messageInfoMap = new HashMap<>(); //put all message values in hash
            messageInfoMap.put("name", currentUserName);
            messageInfoMap.put("message", messageText);
            messageInfoMap.put("date", currentDate);
            messageInfoMap.put("time", currentTime);

            GroupMessageKeyRef.updateChildren(messageInfoMap); // insert values in database.

        }
    }

}
