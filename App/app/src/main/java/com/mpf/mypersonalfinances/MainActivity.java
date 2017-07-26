package com.mpf.mypersonalfinances;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;
import com.mpf.mypersonalfinances.auth.LoginActivity;
import com.mpf.mypersonalfinances.features.MenuActivity;
import com.mpf.mypersonalfinances.models.User;

public class MainActivity extends AppCompatActivity {

    //Constants

    //Declarations
    private DatabaseReference _dataBase;
    private FirebaseAuth _auth;
    private FirebaseAuth.AuthStateListener _authStateListener;
    private String _userName;
    private String _userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _dataBase = FirebaseDatabase.getInstance().getReference();
        _auth = FirebaseAuth.getInstance();

        _authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged (@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        };
        _userId = _auth.getCurrentUser().getUid();
        _dataBase.child("users").child(_userId)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    _userName = user.name;
                    Toast.makeText(MainActivity.this, String.format("Bem vindo %s", _userName), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this, MenuActivity.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }

    @Override
    protected void onStart() {
        super.onStart();
        _auth.addAuthStateListener(_authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (_authStateListener != null) {
            _auth.removeAuthStateListener(_authStateListener);
        }
    }
}
