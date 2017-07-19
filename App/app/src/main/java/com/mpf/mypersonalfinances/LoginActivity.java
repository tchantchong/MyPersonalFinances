package com.mpf.mypersonalfinances;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Field;

public class LoginActivity extends AppCompatActivity {

    //Constants

    //Declarations
    private EditText _emailField;
    private EditText _passwordField;
    private Button _signInButton;
    private Button _registerButton;
    private FirebaseAuth _auth;
    private FirebaseAuth.AuthStateListener _authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _emailField = (EditText) findViewById(R.id.email_field);
        _passwordField = (EditText) findViewById(R.id.password_field);
        _signInButton = (Button) findViewById(R.id.sign_in_button);
        _registerButton = (Button) findViewById(R.id.register_button);

        _auth = FirebaseAuth.getInstance();

        _signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });
    }

    private void startSignIn() {
        String email = _emailField.getText().toString();
        String password = _passwordField.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Both fields cannot be empty", Toast.LENGTH_LONG).show();
        } else {
            _auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Sign In Problem. Make sure you entered correct e-mail and password", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Sign In Succesful", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
