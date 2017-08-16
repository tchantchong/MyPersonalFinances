package com.mpf.mypersonalfinances.features.finances;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mpf.mypersonalfinances.R;
import com.mpf.mypersonalfinances.models.Expense;
import com.mpf.mypersonalfinances.models.ExpenseCategories;
import com.mpf.mypersonalfinances.models.Income;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class AddIncomeActivity extends AppCompatActivity {

    //Constants
    private Calendar TODAY = Calendar.getInstance();
    private DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    private DateFormat PERIOD_FORMAT = new SimpleDateFormat("MMyy");

    //Database Declarations
    private String _userId;
    private DatabaseReference _userFinancesDatabase;

    //UI Declarations
    private EditText _descriptionEditText;
    private EditText _valueEditText;
    private Button _addIncomeButton;
    private Button _dateButton;
    private Button _frequencyButton;
    private Date _selectedDate;
    private DatePickerDialog _datePickerDialog;

    //Misc Declarations
    private String _selectedPeriod;
    private boolean _isOneTimeFrequency = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        //Popup Initialization
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.5));

        //UI Initialization
        _descriptionEditText = (EditText) findViewById(R.id.add_income_description_editText);
        _valueEditText = (EditText) findViewById(R.id.add_income_value_editText);
        _addIncomeButton = (Button) findViewById(R.id.add_income_add_button);
        _dateButton = (Button) findViewById(R.id.add_income_date_button);
        _frequencyButton = (Button) findViewById(R.id.add_income_frequency_button);
        _selectedDate = Calendar.getInstance().getTime();

        //Misc Initializations
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        if (year < 1900) {
            year += 1900;
        }
        _selectedPeriod = String.format("%s%s", month, year - 2000);
        if (_selectedPeriod.length() < 4) {
            _selectedPeriod = String.format("0%s", _selectedPeriod);
        }
        _frequencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _isOneTimeFrequency = !_isOneTimeFrequency;
                _frequencyButton.setText(_isOneTimeFrequency ? "One Time" : "Monthly");
            }
        });

        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year -= 1900;
            if (year < 1900) {
                year += 1900;
            }
            Date selectedDate = new Date(year, monthOfYear, dayOfMonth);
            if (DATE_FORMAT.format(_selectedDate) != DATE_FORMAT.format(selectedDate)) {
                _selectedDate = selectedDate;
                _selectedPeriod = PERIOD_FORMAT.format(_selectedDate);
                if (IsTodaySelected(year, monthOfYear, dayOfMonth)) {
                    _dateButton.setText("TODAY");
                }
                else {
                    _dateButton.setText(DATE_FORMAT.format(_selectedDate));
                }
            }
            }
        };
        _datePickerDialog = new DatePickerDialog(this, datePickerListener, TODAY.get(Calendar.YEAR),TODAY.get(Calendar.MONTH),TODAY.get(Calendar.DAY_OF_MONTH));

        //Database Initialization
        _userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        _addIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (!IsFieldsFilled()) {
                Toast.makeText(AddIncomeActivity.this, "All fields must be filled", Toast.LENGTH_LONG).show();
                return;
            }
            if (_descriptionEditText.getText().toString().trim().length() > 15) {
                Toast.makeText(AddIncomeActivity.this, "Please make the description lower than 15 characters.", Toast.LENGTH_LONG).show();
                return;
            }
            String stringValue = _valueEditText.getText().toString().trim().replace(" ", "").replace(",", ".");
            if (!IsValueValid(stringValue)) {
                Toast.makeText(AddIncomeActivity.this, "Please input a valid value.", Toast.LENGTH_LONG).show();
                return;
            }
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            Double doubleValue = Double.parseDouble(stringValue);
            stringValue = decimalFormat.format(doubleValue);
            doubleValue = Double.parseDouble(stringValue);
            if (doubleValue <= 0) {
                Toast.makeText(AddIncomeActivity.this, "Value cannot be 0 neither negative.", Toast.LENGTH_LONG).show();
                return;
            }

            _userFinancesDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(_userId)
                .child("finances").child(_selectedPeriod).child("incomes").push();
            String key = _userFinancesDatabase.getKey();
            String description = _descriptionEditText.getText().toString().trim();
            Income income = new Income(description, key, DATE_FORMAT.format(_selectedDate), doubleValue, doubleValue);
            Map<String, Object> incomeValues = income.toMap();
            _userFinancesDatabase.updateChildren(incomeValues);

            if (!_isOneTimeFrequency) {
                _userFinancesDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(_userId)
                    .child("finances").child("monthly").child("incomes").push();
                String monthlyKey = _userFinancesDatabase.getKey();
                Income monthlyIncome = new Income(description, monthlyKey, DATE_FORMAT.format(_selectedDate), doubleValue, doubleValue);
                Map<String, Object> monthlyIncomeValues = monthlyIncome.toMap();
                _userFinancesDatabase.updateChildren(monthlyIncomeValues);
            }
            Toast.makeText(AddIncomeActivity.this, "Income successfully added.", Toast.LENGTH_LONG).show();
            finish();
            }
        });

        _dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _datePickerDialog.show();
            }
        });
    }

    private boolean IsValueValid(String value_) {
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
                !TextUtils.isEmpty(_valueEditText.getText());
    }

    private boolean IsTodaySelected(int year, int month, int day) {
        if (year < 1900) {
            year += 1900;
        }
        return year == TODAY.get(Calendar.YEAR) &&
                month == TODAY.get(Calendar.MONTH) &&
                day == TODAY.get(Calendar.DAY_OF_MONTH);
    }
}
