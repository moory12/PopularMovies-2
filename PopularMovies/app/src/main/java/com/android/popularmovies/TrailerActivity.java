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

public class TrailerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TrailerAdapter trailerAdapter;
    ArrayList<Trailer> trailerArrayList;
    int movieID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);


        trailerArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.trailer_recyclerView);
        trailerAdapter  = new TrailerAdapter(this, trailerArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(trailerAdapter);
        TrailerAsyncTask trailerAsyncTask = new TrailerAsyncTask();

        movieID = getIntent().getIntExtra("id",0);

        if (isOnline()){
            Uri uri = Uri.parse("https://api.themoviedb.org/3/movie/"+movieID+"/videos?language=en-US&api_key="+getResources().getString(R.string.tmdb_api_key));
            try {
                URL url = new URL(uri.toString());
               trailerAsyncTask.execute(url);
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



    public class TrailerAsyncTask extends AsyncTask<URL,Void,String>{


        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String TrailerResults = null;

            try {
                TrailerResults = getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return TrailerResults;
        }

        @Override
        protected void onPostExecute(String TrailerResults) {
            super.onPostExecute(TrailerResults);

            if (TrailerResults != null && !TrailerResults.equals("")) {



                try {
                    JSONObject jsonObject = new JSONObject(TrailerResults);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        trailerArrayList.add(new Trailer(json.optString("name"),json.optString("key")));
                        trailerAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
}
