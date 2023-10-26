package com.example.twitxclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.twitxclone.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText usernameText;
    EditText passwordText;
    EditText dobText;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        usernameText = findViewById(R.id.user_field);
        passwordText = findViewById(R.id.pass_field);
        dobText = findViewById(R.id.dob_field);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }


    public void onSignUp(View view) {
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String dob = dobText.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, save new user in Freebase
                    DatabaseReference databaseRef = database.getReference("users");
                    String uid = databaseRef.push().getKey();
                    User user = new User();
                    user.setUsername(username);
                    user.setDob(dob);
                    Toast.makeText(SignUpActivity.this, uid, Toast.LENGTH_SHORT).show();
                    databaseRef.child(uid).setValue(user);
                    Intent intent = new Intent(SignUpActivity.this, MessagesActivity.class);
                    startActivity(intent);
                } else {
                    // If sign in fails, display a message to the user.
                    Exception e = task.getException();
                    Log.d("EXCE", e.toString());
                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}