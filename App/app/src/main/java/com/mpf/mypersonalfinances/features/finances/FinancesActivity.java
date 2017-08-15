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
import com.google.firebase.database.ValueEventListener;
import com.mpf.mypersonalfinances.R;
import com.mpf.mypersonalfinances.models.Expense;
import com.mpf.mypersonalfinances.models.Income;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FinancesActivity extends AppCompatActivity {

    //Database Declarations
    private String _userId;
    private DatabaseReference _currentExpensesDatabaseReference;
    private DatabaseReference _currentIncomesDatabaseReference;

    //UI Declarations
    private double _currentExpensesTotal;
    private double _currentIncomesTotal;
    private TextView _financesPeriodTextView;
    private TextView _currentExpensesTotalView;
    private TextView _currentIncomesTotalView;
    private Button _financesAddExpenseButton;
    private Button _financesRemoveExpenseButton;
    private Button _financesAddIncomeButton;
    //private Button _financesRemoveIncomeButton;

    //Misc Declarations
    private String _currentPeriod;
    private boolean _periodInitialized = false;
    private boolean _monthlyExpensesInitialized = false;
    private boolean _monthlyIncomesInitialized = false;
    private List<Expense> _expensesList = new ArrayList<Expense>();
    private List<Income> _incomesList = new ArrayList<Income>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finances);

        //UI Initializations
        _financesPeriodTextView = (TextView) findViewById(R.id.finances_period_text_view);
        _currentExpensesTotalView = (TextView) findViewById(R.id.current_expenses_total_text_view);
        _currentIncomesTotalView = (TextView) findViewById(R.id.current_incomes_total_text_view);
        _financesAddExpenseButton = (Button) findViewById(R.id.finances_add_expense_button);
        _financesRemoveExpenseButton = (Button) findViewById(R.id.finances_remove_expense_button);
        _financesAddIncomeButton = (Button) findViewById(R.id.finances_add_income_button);

        //Misc Initializations
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        if (year < 1900) {
            year += 1900;
        }
        _currentPeriod = String.format("%s%s", month, year - 2000);
        if (_currentPeriod.length() < 4) {
            _currentPeriod = String.format("0%s", _currentPeriod);
        }
        _financesPeriodTextView.setText(String.format("Period: %s/%s", month, year));

        //DataBase Initializations
        _userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Initializing Period
        FirebaseDatabase.getInstance().getReference().child("users").child(_userId).child("finances").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                _periodInitialized = dataSnapshot.hasChild(_currentPeriod);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (!_periodInitialized) {
            DatabaseReference monthlyDB = FirebaseDatabase.getInstance().getReference().child("users").child(_userId).child("finances").child("monthly");

            monthlyDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    _monthlyExpensesInitialized = dataSnapshot.hasChild("expenses");
                    _monthlyIncomesInitialized = dataSnapshot.hasChild("incomes");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            if (_monthlyExpensesInitialized) {
                monthlyDB.child("expenses").child("monthly").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Expense expense = dataSnapshot.getValue(Expense.class);
                        _expensesList.add(expense);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            if (_monthlyIncomesInitialized) {
                monthlyDB.child("incomes").child("monthly").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Income income = dataSnapshot.getValue(Income.class);
                        _incomesList.add(income);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            FirebaseDatabase.getInstance().getReference().child("users").child(_userId).child("finances").child(_currentPeriod).child("initialized").setValue(true);
        }

        _currentExpensesDatabaseReference  = FirebaseDatabase.getInstance().getReference().child("users").child(_userId).child("finances").child(_currentPeriod).child("expenses");
        _currentIncomesDatabaseReference  = FirebaseDatabase.getInstance().getReference().child("users").child(_userId).child("finances").child(_currentPeriod).child("incomes");

        _currentExpensesDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Expense expense =  dataSnapshot.getValue(Expense.class);
                _expensesList.add(expense);
                _currentExpensesTotal += expense.value;
                OnCurrentExpensesTotalChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Expense expense = dataSnapshot.getValue(Expense.class);
                if (expense.oldValue != expense.value && (expense.value != 0 || expense.oldValue != 0)) {
                    _currentExpensesTotal -= expense.oldValue;
                    _currentExpensesTotal += expense.value;
                    OnCurrentExpensesTotalChanged();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Expense expense = dataSnapshot.getValue(Expense.class);
                _currentExpensesTotal -= 2*expense.value;
                OnCurrentExpensesTotalChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        _currentIncomesDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Income income = dataSnapshot.getValue(Income.class);
                _incomesList.add(income);
                _currentIncomesTotal += income.value;
                OnCurrentIncomesTotalChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Income income = dataSnapshot.getValue(Income.class);
                if (income.oldValue != income.value && (income.value != 0 || income.oldValue != 0)) {
                    _currentIncomesTotal -= income.oldValue;
                    _currentIncomesTotal += income.value;
                    OnCurrentIncomesTotalChanged();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Income income = dataSnapshot.getValue(Income.class);
                _currentIncomesTotal -= income.value;
                OnCurrentIncomesTotalChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        _financesAddExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FinancesActivity.this, AddExpenseActivity.class));
            }
        });
        _financesRemoveExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FinancesActivity.this, RemoveExpenseActivity.class));
            }
        });
        _financesAddIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FinancesActivity.this, AddIncomeActivity.class));
            }
        });
        //_financesRemoveIncomeButton.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
                //startActivity(new Intent(FinancesActivity.this, RemoveIncomeActivity.class));
        //    }
        //});
    }

    private void OnCurrentExpensesTotalChanged() {
        _currentExpensesTotalView.setText(String.format("This month total expenses: %1$,.2f", _currentExpensesTotal));
    }

    private void OnCurrentIncomesTotalChanged() {
        _currentIncomesTotalView.setText(String.format("This month total incomes: %1$,.2f", _currentIncomesTotal));
    }
}