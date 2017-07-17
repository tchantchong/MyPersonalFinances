package com.mpf.mypersonalfinances;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    //Constants

    //Declarations
    private DatabaseReference _dataBase;
    private FirebaseAuth _auth;
    private FirebaseAuth.AuthStateListener _authStateListener;
    private Button _logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _logoutButton = (Button) findViewById(R.id.logout_button);
        _dataBase = FirebaseDatabase.getInstance().getReference();
        _auth = FirebaseAuth.getInstance();

        _authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged (@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }
            }
        };
        _logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _auth.signOut();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        _auth.addAuthStateListener(_authStateListener);
    }
}
