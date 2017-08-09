package com.mpf.mypersonalfinances;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mpf.mypersonalfinances.auth.LoginActivity;
import com.mpf.mypersonalfinances.features.MenuActivity;

public class MainActivity extends AppCompatActivity {

    //Declarations
    private FirebaseAuth _auth;
    private FirebaseAuth.AuthStateListener _authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _auth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference("testing").setValue("te");
        //FirebaseAuth.getInstance().signOut();

        _authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged (@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    return;
                }
                startActivity(new Intent(MainActivity.this, MenuActivity.class));
                return;
            }
        };
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
