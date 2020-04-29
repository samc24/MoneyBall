package com.example.moneyball;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.ConversationActions;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>
{
    private List<Messages> userMessagesList;

    //constructor
    public MessageAdapter(List<Messages> userMessagesList){
        this.userMessagesList = userMessagesList;
    }



    public class MessageViewHolder extends RecyclerView.ViewHolder{

        public TextView messageText; //used to hold message TextViews (message_layout.xml)

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.textMessage); // intialized the message_layout.xml (message bubble)

        }
    }

    //creates new views(invoked by layout manager)
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.message_layout,viewGroup,false);
        return new MessageViewHolder(view);
    }


    //puts content on Edit text (message bubble)
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i) {
        //String messageSenderId = myAuthorization.getCurrentUser().getUid();
        Messages messages = userMessagesList.get(i);


        //set message information on message layout (message_layout.xml)
        messageViewHolder.messageText.setBackgroundResource(R.drawable.bubble); //creates chat bubble
        messageViewHolder.messageText.setTextColor(Color.BLACK);
        messageViewHolder.messageText.setText(messages.getName() + ":              " + messages.getTime() +" | " + messages.getDate() + "\n\n"  + messages.getMessage()); ///write message to bubble

    }

    // Return the size of your dataset (invoked by the layout manager)
   @Override
    public int getItemCount() {
       return userMessagesList.size();
    }


}
