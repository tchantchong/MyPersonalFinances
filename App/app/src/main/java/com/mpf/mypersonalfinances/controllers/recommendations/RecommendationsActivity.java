package com.mpf.mypersonalfinances.controllers.recommendations;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mpf.mypersonalfinances.R;
import com.mpf.mypersonalfinances.models.recommendations.Recommendation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecommendationsActivity extends AppCompatActivity {

    //Database Declarations
    private String _userId;
    private DatabaseReference _analysesDatabase;

    //UI Declarations
    private EditText _tickerSymbolEditText;
    private Button _searchButton;
    private TextView _tickerNotFoundTextView;
    private TableLayout _tickerAnalysesTableLayout;

    //Misc Declarations
    List<Recommendation> _recommendationsList = new ArrayList<Recommendation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);

        //Database Initialization
        _userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        _analysesDatabase = FirebaseDatabase.getInstance().getReference().child("analyses");
        _analysesDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Recommendation recommendation = dataSnapshot.getValue(Recommendation.class);
                _recommendationsList.add(recommendation);
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

        //UI Initializations
        _tickerSymbolEditText = (EditText) findViewById(R.id.recommendations_ticker_symbol_editText);
        _searchButton = (Button) findViewById(R.id.recommendations_search_button);
        _tickerNotFoundTextView = (TextView) findViewById(R.id.recommendations_ticker_not_found_text_view);
        _tickerAnalysesTableLayout = (TableLayout) findViewById(R.id.recommendations_analyses_table);

        _tickerNotFoundTextView.setVisibility(View.GONE);
        _tickerAnalysesTableLayout.setVisibility(View.GONE);

        _searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _tickerAnalysesTableLayout.setVisibility(View.GONE);
                List<Recommendation> recommendationsFound = new ArrayList<Recommendation>();
                for (Recommendation recommendation : _recommendationsList) {
                    if (recommendation.symbol.equals(_tickerSymbolEditText.getText().toString().toUpperCase().trim())) {
                        recommendationsFound.add(recommendation);
                    }
                }
                if (recommendationsFound.isEmpty()) {
                    _tickerNotFoundTextView.setVisibility(View.VISIBLE);
                    return;
                }
                _tickerNotFoundTextView.setVisibility(View.GONE);
                Collections.sort(recommendationsFound, new Comparator<Recommendation>() {
                    @Override
                    public int compare(Recommendation rec2, Recommendation rec1) {
                        return rec1.score.compareTo(rec2.score);
                    }
                });
                CreateAnalysesTable(recommendationsFound);

            }
        });
    }

    private void CreateAnalysesTable(List<Recommendation> recommendationsList) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        Double scoreAverage = 0.00;
        DecimalFormat scoreFormat = new DecimalFormat("#");
        String rec = "";
        _tickerAnalysesTableLayout.removeAllViews();

        //Header
        TableRow header = new TableRow(this);
        TableRow.LayoutParams headerParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        header.setLayoutParams(headerParams);
        TextView headerView = new TextView(this);
        for (Recommendation recommendation : recommendationsList) {
            scoreAverage += recommendation.score;
        }
        scoreAverage = scoreAverage/_recommendationsList.size();
        if (scoreAverage > 0.2) {
            rec = "BUY";
        }
        else if (scoreAverage < -0.2) {
            rec = "SELL";
        }
        else {
            rec = "HOLD";
        }

        headerView.setText("Found " + _recommendationsList.size() + " analyses for " +  _tickerSymbolEditText.getText().toString().toUpperCase().trim()
                            + "\nScore Average: " + scoreFormat.format(scoreAverage*100)
                            + "\nRecommandation: " + rec);
        header.addView(headerView);
        _tickerAnalysesTableLayout.addView(header);

        for (final Recommendation recommendation : recommendationsList) {

            //Rows
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            TableRow row1 = new TableRow(this);
            row1.setLayoutParams(lp);
            TextView textView = new TextView(this);
            textView.setText("\nTitle: " + recommendation.title
                            + "\nScore: " + scoreFormat.format(recommendation.score*100)
                            + "\nPublication Date: " + recommendation.date
                            + "\nAuthor: " + recommendation.author);
            textView.setWidth((int)(width*.9));
            row1.addView(textView);
            TableRow row2 = new TableRow(this);
            row2.setLayoutParams(lp);
            TextView urlTextView = new TextView(this);
            textView.setClickable(true);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            String urlText = "URL: <a href='" + recommendation.url + "'>" + recommendation.url + "</a>";
            urlTextView.setText(Html.fromHtml(urlText));
            urlTextView.setWidth((int) (width*.9));
            row2.addView(urlTextView);
            row2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(recommendation.url));
                    startActivity(i);
                }
            });

            _tickerAnalysesTableLayout.addView(row1);
            _tickerAnalysesTableLayout.addView(row2);
        }
        _tickerAnalysesTableLayout.setVisibility(View.VISIBLE);
    }
}
