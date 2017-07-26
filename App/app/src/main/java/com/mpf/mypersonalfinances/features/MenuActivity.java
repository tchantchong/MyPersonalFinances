package com.mpf.mypersonalfinances.features;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.mpf.mypersonalfinances.R;
import com.mpf.mypersonalfinances.auth.LoginActivity;

public class MenuActivity extends AppCompatActivity {

    //Constants

    //Declarations
    private Button _quickAddButton;
    private Button _financesButton;
    private Button _investmentsButton;
    private Button _recommendationsButton;
    private Button _logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        _quickAddButton = (Button) findViewById(R.id.quick_add_button);
        _financesButton= (Button) findViewById(R.id.finances_menu_button);
        _investmentsButton = (Button) findViewById(R.id.investments_menu_button);
        _recommendationsButton = (Button) findViewById(R.id.recommendations_menu_button);
        _logoutButton = (Button) findViewById(R.id.logout_menu_button);

        _quickAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MenuActivity.this, QuickAddActivity.class));
            }
        });
        _financesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MenuActivity.this, FinancesActivity.class));
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
