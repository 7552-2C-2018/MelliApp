package com.a7552_2c_2018.melliapp.activity;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.model.ChatMessage;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.achatLl) LinearLayout layout;
    @BindView(R.id.achatRl) RelativeLayout layout_2;
    @BindView(R.id.sendButton) ImageView sendButton;
    @BindView(R.id.messageArea) EditText messageArea;
    @BindView(R.id.achatScrollView) ScrollView scrollView;
    @BindView(R.id.cfTitle) TextView chatTitle;

    DatabaseReference reference1;
    private static FirebaseDatabase firebaseDatabase;
    String chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Chats");

        if (firebaseDatabase == null) {
            firebaseDatabase=FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
        }

        chatId = getIntent().getStringExtra("chatId");
        chatTitle.setText(getIntent().getStringExtra("title"));

        reference1 = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(getString(R.string.remote_chats) +
                        "chats/" + chatId + "/messages");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    ChatMessage msg = new ChatMessage();
                    msg.setText(messageText);
                    msg.setSenderId(SingletonUser.getInstance().getUser().getFacebookID());
                    Long tsLong = System.currentTimeMillis()/1000;
                    msg.setTimestamp(tsLong);
                    reference1.push().setValue(msg);
                    messageArea.setText("");
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage chat = dataSnapshot.getValue(ChatMessage.class);
                String message = chat.getText();
                String userName = chat.getSenderId();

                if(userName.equals(SingletonUser.getInstance().getUser().getFacebookID())){
                    addMessageBox(message, 1);
                }
                else{
                    addMessageBox(message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(ChatActivity.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if(type == 1) {
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        else{
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
