package com.mpf.mypersonalfinances.features.finances;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mpf.mypersonalfinances.R;
import com.mpf.mypersonalfinances.models.ExpenseCategories;

import java.text.DecimalFormat;

public class AddExpenseActivity extends AppCompatActivity {

    //Database Declarations
    private FirebaseAuth _auth;
    private DatabaseReference _userFinancesDatabase;

    //UI Declarations
    private EditText _descriptionEditText;
    private Spinner _categorySpinner;
    private EditText _valueEditText;
    private Button _addExpenseButton;
    private String _selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        //Popup Initialization
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.5));

        //UI Initialization
        _descriptionEditText = (EditText) findViewById(R.id.add_expense_description_editText);
        _categorySpinner = (Spinner) findViewById(R.id.add_expense_category_spinner);
        _valueEditText = (EditText) findViewById(R.id.add_expense_value_editText);
        _addExpenseButton = (Button) findViewById(R.id.add_expense_add_button);

        //Database Initialization
        _auth = FirebaseAuth.getInstance();

        ArrayAdapter<ExpenseCategories> adapter = new ArrayAdapter<ExpenseCategories>(this, android.R.layout.simple_list_item_1, ExpenseCategories.values());
        _categorySpinner.setAdapter(adapter);
        _categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _selectedCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        _addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!IsFieldsFilled()) {
                    Toast.makeText(AddExpenseActivity.this, "All fields must be filled", Toast.LENGTH_LONG).show();
                    return;
                }
                if (_descriptionEditText.getText().toString().trim().length() > 15) {
                    Toast.makeText(AddExpenseActivity.this, "Please make the description lower than 15 characters.", Toast.LENGTH_LONG).show();
                    return;
                }
                String stringValue = _valueEditText.getText().toString().trim().replace(" ", "").replace(",", ".");
                if (!IsValueValid(stringValue)) {
                    Toast.makeText(AddExpenseActivity.this, "Please input a valid value.", Toast.LENGTH_LONG).show();
                    return;
                }
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                Double doubleValue = Double.parseDouble(stringValue);
                stringValue = decimalFormat.format(doubleValue);
                doubleValue = Double.parseDouble(stringValue);
                if (doubleValue <= 0) {
                    Toast.makeText(AddExpenseActivity.this, "Value cannot be 0 neither negative.", Toast.LENGTH_LONG).show();
                    return;
                }
                String userId = _auth.getCurrentUser().getUid();
                _userFinancesDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(userId)
                    .child("finances").child("actual").child("expenses").push();
                String key = _userFinancesDatabase.getKey();
                _userFinancesDatabase.child("id").setValue(key);
                _userFinancesDatabase.child("description").setValue(_descriptionEditText.getText().toString().trim());
                _userFinancesDatabase.child("category").setValue(_selectedCategory);
                _userFinancesDatabase.child("value").setValue(doubleValue);
                _userFinancesDatabase.child("oldValue").setValue(doubleValue);
                Toast.makeText(AddExpenseActivity.this, "Expense successfully added.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private Boolean IsValueValid(String value_) {
        int lastIndex = 0;
        String findStr = ".";
        int count = 0;
        while (lastIndex != -1) {
            lastIndex = value_.indexOf(findStr, lastIndex);

            if (lastIndex != -1) {
                count ++;
                lastIndex += findStr.length();
            }
        }
        return count <= 1;
    }

    private boolean IsFieldsFilled() {
        return !TextUtils.isEmpty(_descriptionEditText.getText()) &&
                !TextUtils.isEmpty(_selectedCategory) &&
                !TextUtils.isEmpty(_valueEditText.getText());
    }
}
