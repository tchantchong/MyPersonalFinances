package com.mpf.mypersonalfinances.controllers.investments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mpf.mypersonalfinances.R;
import com.mpf.mypersonalfinances.models.investments.Investment;
import com.mpf.mypersonalfinances.models.investments.InvestmentBase;

import java.util.ArrayList;

public class RemoveInvestmentActivity extends AppCompatActivity {

    //Constants
    private String SPINNER_ITEM_FORMAT = "%s / %s x %s";

    //Database Declarations
    private String _userId;
    private DatabaseReference _userInvestmentsDatabase;

    //UI Declarations
    private Spinner _investmentsSpinner;
    private Button _removeInvestmentButton;

    //Misc Declarations
    private ArrayList<InvestmentBase> _investmentsList;
    private ArrayList<String> _spinnerItemsList;
    private String _selectedInvestmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_investment);

        //Popup Initialization
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.5));

        //UI Initialization
        _investmentsSpinner = (Spinner) findViewById(R.id.remove_investments_spinner);
        _removeInvestmentButton = (Button) findViewById(R.id.remove_investment_button);

        //Misc Initialization
        _investmentsList = new ArrayList<InvestmentBase>();
        _spinnerItemsList = new ArrayList<String>();

        //Database Initialization
        _userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        _userInvestmentsDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(_userId).child("investments");

        _userInvestmentsDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                InvestmentBase investment = dataSnapshot.getValue(InvestmentBase.class);
                _investmentsList.add(investment);
                String spinnerItem = String.format(SPINNER_ITEM_FORMAT, investment.name, Double.toString(investment.buyUnitPrice*investment.quantity));
                _spinnerItemsList.add(spinnerItem);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RemoveInvestmentActivity.this, android.R.layout.simple_spinner_item, _spinnerItemsList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                _investmentsSpinner.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        _investmentsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinnerItem = parent.getItemAtPosition(position).toString();
                String name  = spinnerItem.split("/")[0].trim();
                String quantity = spinnerItem.split("/")[1].trim().split("x")[0].trim();
                String buyUnitPrice = spinnerItem.split("/")[1].trim().split("x")[1].trim();
                for (InvestmentBase investment : _investmentsList) {
                    if (investment.name.equals(name) &&
                        Double.toString(investment.quantity).equals(quantity) &&
                        Double.toString(investment.buyUnitPrice).equals(buyUnitPrice)) {
                        _selectedInvestmentId = investment.id;
                        return;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
