package com.mpf.mypersonalfinances.controllers.investments;

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
import com.mpf.mypersonalfinances.models.investments.InvestmentBase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InvestmentsActivity extends AppCompatActivity {

    //Constants
    private DecimalFormat PRICE_FORMAT = new DecimalFormat("#.00");

    //Database Declarations
    private String _userId;
    private DatabaseReference _investmentsDatabaseReference;

    //UI Declarations
    private double _investmentsTotal;
    private TextView _investmentsTotalTextView;
    private Button _investmentsAddInvestmentButton;
    private Button _investmentsRemoveInvestmentButton;
    private TableLayout _investmentsTableLayout;

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
        _investmentsTableLayout = (TableLayout) findViewById(R.id.investments_table);

        //UI Listeners Initializations
        _investmentsAddInvestmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InvestmentsActivity.this, AddInvestmentActivity.class));
            }
        });

        _investmentsRemoveInvestmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(InvestmentsActivity.this, RemoveInvestmentActivity.class), 1);
            }
        });

        //Database Initializations
        _userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        _investmentsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(_userId).child("investments");
        _investmentsDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null) {
                    InvestmentBase investment = dataSnapshot.getValue(InvestmentBase.class);
                    _investmentsList.add(investment);
                    UpdateTotal();
                }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String key = data.getStringExtra("RemovedInvestmentKey");
                if (key != null && !key.trim().isEmpty()) {
                    Iterator<InvestmentBase> i = _investmentsList.iterator();
                    while (i.hasNext()) {
                        InvestmentBase investmentToRemove = i.next();
                        if (investmentToRemove.id.equals(key)) {
                            i.remove();
                            UpdateTotal();
                        }
                    }
                }
            }
        }
    }

    private void UpdateTotal() {
        _investmentsTotal = 0;
        for (InvestmentBase investment : _investmentsList) {
            _investmentsTotal += investment.buyUnitPrice * investment.quantity;
        }
        _investmentsTotalTextView.setText(String.format("Total Investments: %1$,.2f", _investmentsTotal));
        CreateInvestmentsTable();
    }

    private void CreateInvestmentsTable() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        TableRow.LayoutParams headerParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        _investmentsTableLayout.removeAllViews();

        //Expenses
        TableRow investmentHeader = new TableRow(this);
        investmentHeader.setLayoutParams(headerParams);
        TextView investmentHeaderView = new TextView(this);
        investmentHeaderView.setText("You have " + _investmentsList.size() + " Investment(s)");
        investmentHeader.addView(investmentHeaderView);
        _investmentsTableLayout.addView(investmentHeader);

        for (InvestmentBase investment: _investmentsList) {
            TableRow investmentRow = new TableRow(this);
            investmentRow.setLayoutParams(rowParams);
            TextView investmentView = new TextView(this);
            investmentView.setText("\n - " + investment.name
                    + " - " + investment.investmentType
                    + " - " + PRICE_FORMAT.format(investment.buyUnitPrice)
                    + " * " + PRICE_FORMAT.format(investment.quantity)
                    + " = " + PRICE_FORMAT.format(investment.buyUnitPrice * investment.quantity));
            investmentView.setWidth((int)(width*.9));
            investmentRow.addView(investmentView);
            _investmentsTableLayout.addView(investmentRow);
        }
    }
}
