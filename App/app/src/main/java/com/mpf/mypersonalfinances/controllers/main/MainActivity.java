package com.mpf.mypersonalfinances.controllers.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.mpf.mypersonalfinances.R;
import com.mpf.mypersonalfinances.controllers.auth.LoginActivity;

public class MainActivity extends AppCompatActivity {

    //Declarations
    private FirebaseAuth _auth;
    private FirebaseAuth.AuthStateListener _authStateListener;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _auth = FirebaseAuth.getInstance();
        //FirebaseAuth.getInstance().signOut();
        /*DatabaseReference aa = FirebaseDatabase.getInstance().getReference("message");
        aa.setValue("Testing Connectivity").addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_LONG);
                }
            }
        });

        DatabaseReference bb = FirebaseDatabase.getInstance().getReference("connection");
        bb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String a = dataSnapshot.getValue(String.class);
                Toast.makeText(MainActivity.this, a, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String e = databaseError.getMessage();
                Toast.makeText(MainActivity.this, e, Toast.LENGTH_LONG).show();
            }
        });*/

        _authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged (@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                else {
                    startActivity(new Intent(MainActivity.this, MenuActivity.class));
                }
            }
        };
        FirebaseAuth.getInstance().addAuthStateListener(_authStateListener);
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
