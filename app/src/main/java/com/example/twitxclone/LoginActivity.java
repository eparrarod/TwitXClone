package com.example.twitxclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button signupButton;
    Button loginButton;

    FirebaseAuth firebaseAuth;

    View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText editText = findViewById(R.id.user_field);
            final String username = editText.getText().toString();

            editText = findViewById(R.id.pass_field);
            final String password = editText.getText().toString();

            firebaseAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Intent intent = new Intent(getApplicationContext(), MessagesActivity.class);
                        intent.putExtra("USERNAME",user.getEmail());
                        startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(getApplicationContext(), "Invalid Login", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signupButton = findViewById(R.id.signup_button);
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(loginListener);
        firebaseAuth = FirebaseAuth.getInstance();
    }


    public void signUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}