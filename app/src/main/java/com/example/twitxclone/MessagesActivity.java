package com.example.twitxclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.twitxclone.model.Message;
import com.example.twitxclone.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {

    private final List<Message> messageList = new ArrayList<>();
    private MessageAdapter adapter;
    EditText messageField;
    TextView userTextView;
    TextView dobTextView;
    ListView listView;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageField = findViewById(R.id.message_input);
        userTextView = findViewById(R.id.username_tv);
        dobTextView = findViewById(R.id.dob_tv);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new MessageAdapter(this, messageList);
        listView.setAdapter(adapter);

        // Get Intent and populate user and DOB

        Intent intent = getIntent();
        if(intent != null){
            String user = intent.getStringExtra(User.U_KEY);
            String dob = intent.getStringExtra(User.D_KEY);
            userTextView.setText(user);
            dobTextView.setText(dob);
        }
        database = FirebaseDatabase.getInstance();

        DatabaseReference mRef = database.getReference("messages");
        // Read messages form database
        mRef.orderByChild("publishedAt").limitToLast(100).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot message : snapshot.getChildren()){
                   Message m = message.getValue(Message.class);
                   messageList.add(m);
               }
                Collections.reverse(messageList);
                listView.invalidateViews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void submitMessage(View view) {
        String message = messageField.getText().toString();
        String username = userTextView.getText().toString();

        Message newMessage = new Message();
        newMessage.setAuthor(username);
        newMessage.setMessage(message);
        newMessage.setPublishedAt(new GregorianCalendar().getTimeInMillis());
        DatabaseReference mDatabase =  database.getReference("messages");
        String key = mDatabase.push().getKey();
        if(key != null) {
            mDatabase.child(key).setValue(newMessage);
            Toast.makeText(this, key + " " + newMessage.getMessage(), Toast.LENGTH_SHORT).show();
        }
        messageField.getText().clear();
    }
}