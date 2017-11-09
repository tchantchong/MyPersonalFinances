package com.mpf.mypersonalfinances.controllers.auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mpf.mypersonalfinances.R;
import com.mpf.mypersonalfinances.controllers.main.MenuActivity;

public class RegisterActivity extends AppCompatActivity {

    //Database Declarations
    private FirebaseAuth _auth;
    private DatabaseReference _database;

    //UI Declarations
    private EditText _nameField;
    private EditText _emailField;
    private EditText _passwordField;
    private EditText _confirmPasswordField;
    private Button _registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //UI Initialization
        _nameField = (EditText) findViewById(R.id.register_name_field);
        _emailField = (EditText) findViewById(R.id.register_email_field);
        _passwordField = (EditText) findViewById(R.id.register_password_field);
        _confirmPasswordField= (EditText) findViewById(R.id.register_confirm_password_field);
        _registerButton = (Button) findViewById(R.id.register_button);

        //Database Initialization
        _auth = FirebaseAuth.getInstance();
        _database = FirebaseDatabase.getInstance().getReference().child("users");

        _registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUser();
            }
        });
    }

    private void CreateUser() {
        if (!IsFieldsFilled()) {
            Toast.makeText(RegisterActivity.this, "All fields must be filled", Toast.LENGTH_LONG).show();
            return;
        }
        if(!TextUtils.equals(_passwordField.getText().toString(), _confirmPasswordField.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "Both passwords must be the same", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(RegisterActivity.this, "Creating new user...", Toast.LENGTH_LONG).show();
        _auth.createUserWithEmailAndPassword(_emailField.getText().toString().trim(), _passwordField.getText().toString().trim())
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
                else {
                    String userId = _auth.getCurrentUser().getUid();
                    DatabaseReference currentUserDataBase = _database.child(userId);

                    //Initializing Database Structure
                    currentUserDataBase.child("name").setValue(_nameField.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {//Finally
                                Toast.makeText(RegisterActivity.this, String.format("Registration Completed. Logged in as %s",
                                    _nameField.getText().toString().split(" ")[0].trim()),Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegisterActivity.this, MenuActivity.class));
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();;
                            }
                        }
                    });


                }
                }
            });
    }

    private boolean IsFieldsFilled() {
        return !TextUtils.isEmpty(_nameField.getText()) &&
               !TextUtils.isEmpty(_emailField.getText()) &&
               !TextUtils.isEmpty(_passwordField.getText()) &&
               !TextUtils.isEmpty(_confirmPasswordField.getText());
    }
}
