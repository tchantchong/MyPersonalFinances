package com.mpf.mypersonalfinances.controllers.finances;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mpf.mypersonalfinances.R;
import com.mpf.mypersonalfinances.models.finances.Expense;
import com.mpf.mypersonalfinances.models.finances.Income;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FinancesActivity extends AppCompatActivity {

    //Constants
    private DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    private DecimalFormat PRICE_FORMAT = new DecimalFormat("#.00");

    //Database Declarations
    private String _userId;
    private DatabaseReference _monthlyDB;
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
    private Button _financesRemoveIncomeButton;
    private TableLayout _financesTableLayout;

    //Misc Declarations
    private String _currentPeriod;
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
        _financesRemoveIncomeButton = (Button) findViewById(R.id.finances_remove_income_button);
        _financesTableLayout = (TableLayout) findViewById(R.id.finances_table);

        //Misc Initializations
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
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
        _monthlyDB = FirebaseDatabase.getInstance().getReference().child("users").child(_userId).child("finances").child("monthly");

        InitializePeriod();

        //UI Listener Initializations
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
        _financesRemoveIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FinancesActivity.this, RemoveIncomeActivity.class));
            }
        });
    }

    private void InitializePeriod() {
        FirebaseDatabase.getInstance().getReference().child("users").child(_userId).child("finances").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(_currentPeriod)) {
                    _monthlyDB.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild("expenses")) {
                                _monthlyDB.child("expenses").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        Expense expense = dataSnapshot.getValue(Expense.class);
                                        _expensesList.add(expense);
                                        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("users").child(_userId)
                                                .child("finances").child(_currentPeriod).child("expenses").push();
                                        Map<String, Object> expenseValues = expense.toMap();
                                        mRef.updateChildren(expenseValues);
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
                            }

                            if (dataSnapshot.hasChild("incomes")) {
                                _monthlyDB.child("incomes").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        Income income = dataSnapshot.getValue(Income.class);
                                        _incomesList.add(income);
                                        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("users").child(_userId)
                                                .child("finances").child(_currentPeriod).child("incomes").push();
                                        Map<String, Object> incomeValues = income.toMap();
                                        mRef.updateChildren(incomeValues);
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
                            }
                            InitializeDatabase();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    FirebaseDatabase.getInstance().getReference().child("users").child(_userId).child("finances").child(_currentPeriod).child("initialized").setValue(true);
                }
                else {
                    InitializeDatabase();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void InitializeDatabase() {
        _currentExpensesDatabaseReference  = FirebaseDatabase.getInstance().getReference().child("users").child(_userId).child("finances").child(_currentPeriod).child("expenses");
        _currentIncomesDatabaseReference  = FirebaseDatabase.getInstance().getReference().child("users").child(_userId).child("finances").child(_currentPeriod).child("incomes");

        _currentExpensesDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Expense expense =  dataSnapshot.getValue(Expense.class);
                _expensesList.add(expense);
                OnExpensesListChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                if (key != null && !key.trim().isEmpty()) {
                    Iterator<Expense> i = _expensesList.iterator();
                    while (i.hasNext()) {
                        Expense expenseIterator = i.next();
                        if (expenseIterator.id.equals(key)) {
                            i.remove();
                            OnExpensesListChanged();
                        }
                    }
                }
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
                OnIncomesListChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                if (key != null && !key.trim().isEmpty()) {
                    Iterator<Income> i = _incomesList.iterator();
                    while (i.hasNext()) {
                        Income incomeIterator = i.next();
                        if (incomeIterator.id.equals(key)) {
                            i.remove();
                            OnIncomesListChanged();
                        }
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void OnExpensesListChanged() {
        _currentExpensesTotal = 0;
        for (Expense expense : _expensesList) {
            _currentExpensesTotal += expense.value;
        }
        _currentExpensesTotalView.setText(String.format("This month total expenses: %1$,.2f", _currentExpensesTotal));
        CreateFinancesTable();
    }

    private void OnIncomesListChanged() {
        _currentIncomesTotal = 0;
        for (Income income : _incomesList) {
            _currentIncomesTotal += income.value;
        }
        _currentIncomesTotalView.setText(String.format("This month total incomes: %1$,.2f", _currentIncomesTotal));
        CreateFinancesTable();
    }

    private void CreateFinancesTable() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        TableRow.LayoutParams headerParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        _financesTableLayout.removeAllViews();

        //Expenses
        TableRow expenseHeader = new TableRow(this);
        expenseHeader.setLayoutParams(headerParams);
        TextView expenseHeaderView = new TextView(this);
        expenseHeaderView.setText(_expensesList.size() + " Expenses this month");
        expenseHeader.addView(expenseHeaderView);
        _financesTableLayout.addView(expenseHeader);

        for (Expense expense : _expensesList) {
            TableRow expenseRow = new TableRow(this);
            expenseRow.setLayoutParams(rowParams);
            TextView expenseView = new TextView(this);
            expenseView.setText("\n - " + expense.description
                                + " - " + expense.category
                                + " - " + PRICE_FORMAT.format(expense.value));
            expenseView.setWidth((int)(width*.9));
            expenseRow.addView(expenseView);
            _financesTableLayout.addView(expenseRow);
        }

        //Incomes
        TableRow incomeHeader = new TableRow(this);
        incomeHeader.setLayoutParams(headerParams);
        TextView incomeHeaderView = new TextView(this);
        incomeHeaderView.setText("\n" + _incomesList.size() + " Incomes this month");
        incomeHeader.addView(incomeHeaderView);
        _financesTableLayout.addView(incomeHeader);

        for (Income income : _incomesList) {
            TableRow incomeRow = new TableRow(this);
            incomeRow.setLayoutParams(rowParams);
            TextView incomeView = new TextView(this);
            incomeView.setText("\n - " + income.description
                               + " - " + PRICE_FORMAT.format(income.value));
            incomeView.setWidth((int)(width*.9));
            incomeRow.addView(incomeView);
            _financesTableLayout.addView(incomeRow);
        }
    }
}