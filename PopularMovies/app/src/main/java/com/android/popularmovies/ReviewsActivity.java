package com.android.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class ReviewsActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ReviewsAdapter reviewsAdapter;
    ArrayList<Reviews> reviewsArrayList;
    TextView no_Reviews;
    int movieID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        reviewsArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.reviewsRecyclerView);
        no_Reviews   = findViewById(R.id.no_reviews);
        reviewsAdapter = new ReviewsAdapter(this, reviewsArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(reviewsAdapter);
        ReviewsAsyncTask reviewsAsyncTask = new ReviewsAsyncTask();

        movieID = getIntent().getIntExtra("id", 0);

        if (isOnline()) {
            Uri uri = Uri.parse("https://api.themoviedb.org/3/movie/" + movieID + "/reviews?language=en-US&api_key=" + getResources().getString(R.string.tmdb_api_key));
            try {
                URL url = new URL(uri.toString());
                reviewsAsyncTask.execute(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public class ReviewsAsyncTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String ReviewsResults = null;

            try {
                ReviewsResults = getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ReviewsResults;
        }


        @Override
        protected void onPostExecute(String ReviewsResults) {
            super.onPostExecute(ReviewsResults);

            if (ReviewsResults != null && !ReviewsResults.equals("")) {



                try {
                    JSONObject jsonObject = new JSONObject(ReviewsResults);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    if(jsonArray.length()>0){
                        no_Reviews.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        System.out.println(json);
                        reviewsArrayList.add(new Reviews(json.optString("author"),json.optString("content")));
                        reviewsAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    private String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


}