package com.mpf.mypersonalfinances.controllers.investments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.mpf.mypersonalfinances.models.investments.InvestmentBase;
import com.mpf.mypersonalfinances.models.investments.InvestmentTypes;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class AddInvestmentActivity extends AppCompatActivity {

    //Constants
    private Calendar TODAY = Calendar.getInstance();
    private DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    //Database Declarations
    private String _userId;
    private DatabaseReference _userInvestmentsDatabase;

    //UI Declarations
    private EditText _nameEditText;
    private Spinner _typeSpinner;
    private String _selectedType;
    private Button _buyDateButton;
    private Date _selectedBuyDate;
    private EditText _priceEditText;
    private EditText _quantityEditText;
    private EditText _yieldEditText;
    private Button _maturityButton;
    private Date _selectedMaturityDate;
    private Button _addInvestmentButton;
    private DatePickerDialog _buyDatePickerDialog;
    private DatePickerDialog _maturityPickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_investment);

        //UI Initialization
        _nameEditText = (EditText) findViewById(R.id.add_investment_name_editText);
        _typeSpinner = (Spinner) findViewById(R.id.add_investment_type_spinner);
        _buyDateButton = (Button) findViewById(R.id.add_investment_date_button);
        _priceEditText = (EditText) findViewById(R.id.add_investment_price_editText);
        _quantityEditText = (EditText) findViewById(R.id.add_investment_quantity_editText);
        _yieldEditText = (EditText) findViewById(R.id.add_investment_yield_editText);
        _maturityButton = (Button) findViewById(R.id.add_investment_maturity_button);
        _addInvestmentButton = (Button) findViewById(R.id.add_investment_add_button);
        _selectedBuyDate = Calendar.getInstance().getTime();
        _selectedMaturityDate = Calendar.getInstance().getTime();
        ArrayAdapter<InvestmentTypes> adapter = new ArrayAdapter<InvestmentTypes>(this, android.R.layout.simple_list_item_1, InvestmentTypes.values());
        _typeSpinner.setAdapter(adapter);
        _typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _selectedType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Misc Initializations

        DatePickerDialog.OnDateSetListener buyDatePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                year -= 1900;
                if (year < 1900) {
                    year += 1900;
                }
                Date selectedDate = new Date(year, monthOfYear, dayOfMonth);
                if (DATE_FORMAT.format(_selectedBuyDate) != DATE_FORMAT.format(selectedDate)) {
                    _selectedBuyDate = selectedDate;
                    if (IsTodaySelected(year, monthOfYear, dayOfMonth)) {
                        _buyDateButton.setText("TODAY");
                    }
                    else {
                        _buyDateButton.setText(DATE_FORMAT.format(_selectedBuyDate));
                    }
                }
            }
        };
        _buyDatePickerDialog = new DatePickerDialog(this, buyDatePickerListener, TODAY.get(Calendar.YEAR),TODAY.get(Calendar.MONTH),TODAY.get(Calendar.DAY_OF_MONTH));
        _buyDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _buyDatePickerDialog.show();
            }
        });

        DatePickerDialog.OnDateSetListener maturityPickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                year -= 1900;
                if (year < 1900) {
                    year += 1900;
                }
                Date selectedDate = new Date(year, monthOfYear, dayOfMonth);
                if (DATE_FORMAT.format(_selectedMaturityDate) != DATE_FORMAT.format(selectedDate)) {
                    _selectedMaturityDate = selectedDate;
                    _maturityButton.setText(DATE_FORMAT.format(_selectedMaturityDate));
                }
            }
        };
        _maturityPickerDialog = new DatePickerDialog(this, maturityPickerListener, TODAY.get(Calendar.YEAR),TODAY.get(Calendar.MONTH),TODAY.get(Calendar.DAY_OF_MONTH));
        _maturityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_maturityButton.getText() != "NONE") {
                    _maturityButton.setText("NONE");
                }
                else {
                    _maturityPickerDialog.show();
                }
            }
        });

        //Database Initialization
        _userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        _userInvestmentsDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(_userId).child("investments");

        _addInvestmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_nameEditText.getText().toString().trim().length() > 15) {
                    Toast.makeText(AddInvestmentActivity.this, "Please make name lower than 15 characters.", Toast.LENGTH_LONG).show();
                    return;
                }
                String stringPrice = _priceEditText.getText().toString().trim().replace(" ", "").replace(",", ".");
                if (!IsValueValid(stringPrice)) {
                    Toast.makeText(AddInvestmentActivity.this, "Please input a valid Price.", Toast.LENGTH_LONG).show();
                    return;
                }
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                Double doublePrice = Double.parseDouble(stringPrice);
                stringPrice = decimalFormat.format(doublePrice);
                doublePrice = Double.parseDouble(stringPrice);
                if (doublePrice <= 0) {
                    Toast.makeText(AddInvestmentActivity.this, "Price cannot be 0 neither negative.", Toast.LENGTH_LONG).show();
                    return;
                }
                String stringQuantity = _quantityEditText.getText().toString().trim().replace(" ", "").replace(",", ".");
                if (!IsValueValid(stringQuantity)) {
                    Toast.makeText(AddInvestmentActivity.this, "Please input a valid Quantity.", Toast.LENGTH_LONG).show();
                    return;
                }
                Double doubleQuantity = Double.parseDouble(stringQuantity);
                stringQuantity = decimalFormat.format(doubleQuantity);
                doubleQuantity = Double.parseDouble(stringQuantity);
                if (doubleQuantity <= 0) {
                    Toast.makeText(AddInvestmentActivity.this, "Quantity cannot be neither 0 or negative.", Toast.LENGTH_LONG).show();
                    return;
                }
                Double doubleYield = 0.0;
                if (!_yieldEditText.getText().toString().isEmpty()) {
                    String stringYield = _yieldEditText.getText().toString().trim().replace(" ", "").replace(",", ".");
                    if (!IsValueValid(stringYield)) {
                        Toast.makeText(AddInvestmentActivity.this, "Please input a valid Yield.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    stringYield = decimalFormat.format(doubleYield);
                    doubleYield = Double.parseDouble(stringYield);
                }

                if (doubleQuantity <= 0) {
                    Toast.makeText(AddInvestmentActivity.this, "Yield cannot be 0 neither negative.", Toast.LENGTH_LONG).show();
                    return;
                }

                _userInvestmentsDatabase = _userInvestmentsDatabase.push();
                String key = _userInvestmentsDatabase.getKey();
                String name = _nameEditText.getText().toString().trim();
                InvestmentBase investmentBase = new InvestmentBase(key, name, DATE_FORMAT.format(_selectedBuyDate), DATE_FORMAT.format(_selectedMaturityDate), _selectedType, doublePrice, doubleQuantity, doubleYield);
                Map<String, Object> investmentValues = investmentBase.toMap();
                _userInvestmentsDatabase.updateChildren(investmentValues);
                Toast.makeText(AddInvestmentActivity.this, "Investment successfully added.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(AddInvestmentActivity.this, InvestmentsActivity.class));
            }
        });
    }

    private boolean IsTodaySelected(int year, int month, int day) {
        if (year < 1900) {
            year += 1900;
        }
        return year == TODAY.get(Calendar.YEAR) &&
                month == TODAY.get(Calendar.MONTH) &&
                day == TODAY.get(Calendar.DAY_OF_MONTH);
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
}
