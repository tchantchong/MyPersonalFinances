package com.mpf.mypersonalfinances.models.recommendations;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lucas on 10/11/2017.
 */

public class Recommendation {
    public String symbol;
    public String date;
    public String author;
    public String title;
    public String url;
    public int followers;
    public Double score;

    public Recommendation() {
        //Default constructor for DataSnapshot.getValue(Recommendation.class)
    }

    public Recommendation(String symbol, String date, String author, String title, String url,
                          int followers, Double score) {
        this.symbol = symbol;
        this.date = date;
        this.author = author;
        this.title = title;
        this.url = url;
        this.followers = followers;
        this.score = score;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("symbol", symbol);
        result.put("date", date);
        result.put("author", author);
        result.put("title", title);
        result.put("url", url);
        result.put("followers", followers);
        result.put("score", score);

        return result;
    }
}
