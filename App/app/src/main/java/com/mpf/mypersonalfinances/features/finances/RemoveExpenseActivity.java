package com.mpf.mypersonalfinances.features.finances;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
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

import java.util.ArrayList;
import java.util.Map;

public class RemoveExpenseActivity extends AppCompatActivity {

    //Constants
    String SpinnerItemFormat = "%s / %s / %s";

    //Database Declarations
    private String _userId;
    private DatabaseReference _userFinancesDatabase;

    //UI Declarations
    private Spinner _expensesSpinner;
    private Button _removeExpenseButton;

    //Misc Declarations
    private ArrayList<Expense> _expensesList;
    private ArrayList<String> _spinnerItemsList;
    private String _selectedExpenseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_expense);

        //Popup Initialization
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.5));

        //UI Initialization
        _expensesSpinner = (Spinner) findViewById(R.id.remove_expenses_spinner);
        _removeExpenseButton = (Button) findViewById(R.id.remove_expense_button);

        //Database Initialization
        _userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        _userFinancesDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(_userId).child("finances").child("actual").child("expenses");

        //Misc Initialization
        _expensesList = new ArrayList<Expense>();
        _spinnerItemsList = new ArrayList<String>();

        _userFinancesDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Expense expense = dataSnapshot.getValue(Expense.class);
                _expensesList.add(expense);
                String spinnerItem = String.format(SpinnerItemFormat, expense.category, expense.description, Double.toString(expense.value));
                _spinnerItemsList.add(spinnerItem);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RemoveExpenseActivity.this, android.R.layout.simple_spinner_item, _spinnerItemsList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                _expensesSpinner.setAdapter(adapter);
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
        _expensesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinnerItem = parent.getItemAtPosition(position).toString();
                String category = spinnerItem.split("/")[0].trim();
                String description  = spinnerItem.split("/")[1].trim();
                String value = spinnerItem.split("/")[2].trim();
                for (Expense expense : _expensesList) {
                    if (expense.category.equals(category) &&
                        expense.description.equals(description) &&
                        Double.toString(expense.value).equals(value)) {
                            _selectedExpenseId = expense.id;
                            return;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _removeExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query expenseToBeRemoved = _userFinancesDatabase.child(_selectedExpenseId);
                expenseToBeRemoved.addListenerForSingleValueEvent(new ValueEventListener() {
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
                Toast.makeText(RemoveExpenseActivity.this, "Expense successfully removed.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
