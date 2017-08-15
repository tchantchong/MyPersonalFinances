package com.mpf.mypersonalfinances.features;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mpf.mypersonalfinances.R;
import com.mpf.mypersonalfinances.auth.LoginActivity;
import com.mpf.mypersonalfinances.features.finances.AddExpenseActivity;
import com.mpf.mypersonalfinances.features.finances.FinancesActivity;
import com.mpf.mypersonalfinances.models.User;

public class MenuActivity extends AppCompatActivity {

    //Database Declarations
    private FirebaseAuth _auth;

    //Declarations
    private Button _quickAddButton;
    private Button _financesButton;
    private Button _investmentsButton;
    private Button _recommendationsButton;
    private Button _logoutButton;
    private TextView _welcomeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //UI Initialization
        _quickAddButton = (Button) findViewById(R.id.quick_add_expense_button);
        _financesButton= (Button) findViewById(R.id.finances_menu_button);
        _investmentsButton = (Button) findViewById(R.id.investments_menu_button);
        _recommendationsButton = (Button) findViewById(R.id.recommendations_menu_button);
        _logoutButton = (Button) findViewById(R.id.logout_menu_button);
        _welcomeView = (TextView) findViewById(R.id.welcome_view);

        //Database Initialization
        _auth = FirebaseAuth.getInstance();
        String userId = _auth.getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String name = user.name;
                _welcomeView.setText(String.format("Welcome %s!", name.split(" ")[0].trim()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        _quickAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, AddExpenseActivity.class));
            }
        });
        _financesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, FinancesActivity.class));
            }
        });
        _investmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MenuActivity.this, InvestmentsActivity.class));
            }
        });
        _recommendationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MenuActivity.this, RecommendationsActivity.class));
            }
        });
        _logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MenuActivity.this, LoginActivity.class));
            }
        });
    }
}
