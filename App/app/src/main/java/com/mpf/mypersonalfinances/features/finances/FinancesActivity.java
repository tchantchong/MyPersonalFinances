package com.mpf.mypersonalfinances.features.finances;

import android.content.Intent;
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
import com.mpf.mypersonalfinances.R;
import com.mpf.mypersonalfinances.models.Expense;
import com.mpf.mypersonalfinances.models.Income;

public class FinancesActivity extends AppCompatActivity {

    //Database Declarations
    private FirebaseAuth _auth;
    private DatabaseReference _financesActualExpensesDatabaseReference;
    private DatabaseReference _financesActualIncomesDatabaseReference;

    //UI Declarations
    private double _actualExpensessTotal;
    private double _actualIncomesTotal;
    private TextView _actualExpensesTotalView;
    private TextView _actualIncomesTotalView;
    private Button _actualAddExpenseButton;
    private Button _actualRemoveExpenseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finances);

        //UI Initializations
        _actualExpensesTotalView = (TextView) findViewById(R.id.actual_expenses_total_text_view);
        _actualIncomesTotalView = (TextView) findViewById(R.id.actual_incomes_total_text_view);
        _actualAddExpenseButton = (Button) findViewById(R.id.actual_add_expense_button);
        _actualRemoveExpenseButton = (Button) findViewById(R.id.actual_remove_expense_button);

        //DataBase Initializations
        _auth = FirebaseAuth.getInstance();
        String userId = _auth.getCurrentUser().getUid();
        _financesActualExpensesDatabaseReference  = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("finances").child("actual").child("expenses");
        _financesActualIncomesDatabaseReference  = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("finances").child("actual").child("incomes");

        _financesActualExpensesDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Expense expense =  dataSnapshot.getValue(Expense.class);
                _actualExpensessTotal += expense.value;
                OnActualExpensesTotalChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Expense expense = dataSnapshot.getValue(Expense.class);
                if (expense.oldValue != expense.value && (expense.value != 0 || expense.oldValue != 0)) {
                    _actualExpensessTotal -= expense.oldValue;
                    _actualExpensessTotal += expense.value;
                    OnActualExpensesTotalChanged();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Expense expense = dataSnapshot.getValue(Expense.class);
                _actualExpensessTotal -= 2*expense.value;
                OnActualExpensesTotalChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        _financesActualIncomesDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Income income = dataSnapshot.getValue(Income.class);
                _actualIncomesTotal += income.value;
                OnActualIncomesTotalChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Income income = dataSnapshot.getValue(Income.class);
                _actualIncomesTotal -= income.oldValue;
                _actualIncomesTotal += income.value;
                OnActualIncomesTotalChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Income income = dataSnapshot.getValue(Income.class);
                _actualIncomesTotal -= income.value;
                OnActualIncomesTotalChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        _actualAddExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FinancesActivity.this, AddExpenseActivity.class));
            }
        });
        _actualRemoveExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FinancesActivity.this, RemoveExpenseActivity.class));
            }
        });
    }

    private void OnActualExpensesTotalChanged() {
        _actualExpensesTotalView.setText(String.format("This month total expenses: %1$,.2f", _actualExpensessTotal));
    }

    private void OnActualIncomesTotalChanged() {
        _actualIncomesTotalView.setText(String.format("This month total incomes: %1$,.2f", _actualIncomesTotal));
    }
}
