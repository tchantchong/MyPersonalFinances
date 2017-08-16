package com.mpf.mypersonalfinances.features.finances;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mpf.mypersonalfinances.R;
import com.mpf.mypersonalfinances.models.Expense;
import com.mpf.mypersonalfinances.models.Income;

import java.util.ArrayList;
import java.util.Calendar;

public class RemoveIncomeActivity extends AppCompatActivity {

    //Constants
    private String SPINNER_ITEM_FORMAT = "%s / %s";

    //Database Declarations
    private String _userId;
    private DatabaseReference _userFinancesDatabase;

    //UI Declarations
    private Spinner _incomesSpinner;
    private Button _removeIncomeButton;

    //Misc Declarations
    private ArrayList<Income> _incomesList;
    private ArrayList<String> _spinnerItemsList;
    private String _selectedIncomeId;
    private String _currentPeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_income);

        //Popup Initialization
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.5));

        //UI Initialization
        _incomesSpinner = (Spinner) findViewById(R.id.remove_incomes_spinner);
        _removeIncomeButton = (Button) findViewById(R.id.remove_income_button);

        //Misc Initialization
        _incomesList = new ArrayList<Income>();
        _spinnerItemsList = new ArrayList<String>();
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        if (year < 1900) {
            year += 1900;
        }
        _currentPeriod = String.format("%s%s", month, year - 2000);
        if (_currentPeriod.length() < 4) {
            _currentPeriod = String.format("0%s", _currentPeriod);
        }

        //Database Initialization
        _userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        _userFinancesDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(_userId).child("finances").child(_currentPeriod).child("incomes");

        _userFinancesDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Income income = dataSnapshot.getValue(Income.class);
                _incomesList.add(income);
                String spinnerItem = String.format(SPINNER_ITEM_FORMAT, income.description, Double.toString(income.value));
                _spinnerItemsList.add(spinnerItem);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RemoveIncomeActivity.this, android.R.layout.simple_spinner_item, _spinnerItemsList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                _incomesSpinner.setAdapter(adapter);
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
        _incomesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinnerItem = parent.getItemAtPosition(position).toString();
                String description  = spinnerItem.split("/")[0].trim();
                String value = spinnerItem.split("/")[1].trim();
                for (Income income : _incomesList) {
                    if (income.description.equals(description) &&
                            Double.toString(income.value).equals(value)) {
                        _selectedIncomeId = income.id;
                        return;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _removeIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query incomeToBeRemoved = _userFinancesDatabase.child(_selectedIncomeId);
                incomeToBeRemoved .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childrenDataSnapshot: dataSnapshot.getChildren()) {
                            childrenDataSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Toast.makeText(RemoveIncomeActivity.this, "Income successfully removed.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
