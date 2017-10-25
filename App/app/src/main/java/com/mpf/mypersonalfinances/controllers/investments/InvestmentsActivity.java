package com.mpf.mypersonalfinances.controllers.investments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mpf.mypersonalfinances.R;
import com.mpf.mypersonalfinances.models.investments.InvestmentBase;

import java.util.ArrayList;
import java.util.List;

public class InvestmentsActivity extends AppCompatActivity {

    //Database Declarations
    private String _userId;
    private DatabaseReference _investmentsDatabaseReference;

    //UI Declarations
    private double _investmentsTotal;
    private TextView _investmentsTotalTextView;
    private Button _investmentsAddInvestmentButton;
    private Button _investmentsRemoveInvestmentButton;

    //Misc Declarations
    private List<InvestmentBase> _investmentsList = new ArrayList<InvestmentBase>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investments);

        //UI Initializations
        _investmentsTotalTextView = (TextView) findViewById(R.id.investments_total_text_view);
        _investmentsAddInvestmentButton = (Button) findViewById(R.id.investments_add_investment_button);
        _investmentsRemoveInvestmentButton = (Button) findViewById(R.id.investments_remove_investment_button);

        //UI Listeners Initializations
        _investmentsAddInvestmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        _investmentsRemoveInvestmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        //Database Initializations
        _userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        _investmentsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(_userId).child("investments");
        _investmentsDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                InvestmentBase investment = dataSnapshot.getValue(InvestmentBase.class);
                _investmentsList.add(investment);
                UpdateTotal();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                InvestmentBase investment = dataSnapshot.getValue(InvestmentBase.class);
                _investmentsList.remove(investment);
                UpdateTotal();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void UpdateTotal() {
        _investmentsTotal = 0;
        for (InvestmentBase investment : _investmentsList) {
            _investmentsTotal += investment.buyUnitPrice * investment.quantity;
        }
        _investmentsTotalTextView.setText(String.format("Total Investments: %1$,.2f", _investmentsTotal));
    }
}
