package com.android.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MoviesRecyclerAdapter moviesRecyclerAdapter;
    ArrayList<Movies> moviesArrayList;
    ArrayList<Movies> databaseMovies;
    MoviesRecyclerAdapter databaseAdapter;

    TextView n_error;
    QueryTask queryTask;
    FloatingActionButton whatshot;
    FloatingActionButton arrow;
    FloatingActionButton sort;
    FloatingActionButton fav;
    int counter = 0;
    Context context;
    String oldjson;
    String lastCall;
    String sortby = "";
    private AppDatabase database;



    @Override
    protected void onPause() {
        super.onPause();
        if(counter ==1) {
            whatshot.animate().translationY(0).start();
            arrow.animate().translationY(0).start();
            fav.animate().translationY(0).start();
            counter = 0;
        }


        
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        databaseMovies = new ArrayList<>();
        databaseAdapter =  new MoviesRecyclerAdapter(this,databaseMovies);

        database = AppDatabase.getInstance(getApplicationContext());
        n_error = findViewById(R.id.n_error);
        queryTask = new QueryTask();
        oldjson = "";
        context = this;
        lastCall = "https://api.themoviedb.org/3/movie/now_playing?api_key="+getResources().getString(R.string.tmdb_api_key)+"&language=en-US";

        moviesArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        moviesRecyclerAdapter = new MoviesRecyclerAdapter(this,moviesArrayList);
        recyclerView.setAdapter(moviesRecyclerAdapter);


        if(isOnline()){
            Uri uri = Uri.parse("https://api.themoviedb.org/3/movie/now_playing?api_key="+getResources().getString(R.string.tmdb_api_key)+"&language=en-US");
            lastCall = "https://api.themoviedb.org/3/movie/now_playing?api_key="+getResources().getString(R.string.tmdb_api_key)+"&language=en-US";
            try {
                URL url = new URL(uri.toString());
                queryTask.execute(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            n_error.setVisibility(View.GONE);

        }
        else{

            n_error.setText(R.string.no_internet_connection);
        }



        sort = findViewById(R.id.sort);
        whatshot = findViewById(R.id.whatshot);
         arrow = findViewById(R.id.arrow);
         fav  = findViewById(R.id.main_fav);


        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter ==0) {
                    whatshot.animate().translationY(-250).start();
                    arrow.animate().translationY(-500).start();
                    fav.animate().translationY(-750).start();
                    counter++;
                }
                else{
                    whatshot.animate().translationY(0).start();
                    arrow.animate().translationY(0).start();
                    fav.animate().translationY(0).start();
                    counter = 0;
                }

            }
        });

        whatshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                whatshot.animate().translationY(0).start();
                arrow.animate().translationY(0).start();
                fav.animate().translationY(0).start();
                counter = 0;

                queryTask = new QueryTask();
                if(isOnline()){
                    Uri uri = Uri.parse("https://api.themoviedb.org/3/movie/top_rated?api_key="+getResources().getString(R.string.tmdb_api_key)+"&language=en-US");
                    lastCall = "https://api.themoviedb.org/3/movie/top_rated?api_key="+getResources().getString(R.string.tmdb_api_key)+"&language=en-US";
                    try {
                        URL url = new URL(uri.toString());
                        queryTask.execute(url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    n_error.setVisibility(View.GONE);
                }
                else{

                    n_error.setText(R.string.no_internet_connection);
                }


            }
        });

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                whatshot.animate().translationY(0).start();
                arrow.animate().translationY(0).start();
                fav.animate().translationY(0).start();

                counter = 0;

                queryTask = new QueryTask();
                if(isOnline()){
                    Uri uri = Uri.parse("https://api.themoviedb.org/3/movie/popular?api_key="+getResources().getString(R.string.tmdb_api_key)+"&language=en-US");
                    lastCall = "https://api.themoviedb.org/3/movie/popular?api_key="+getResources().getString(R.string.tmdb_api_key)+"&language=en-US";
                    try {
                        URL url = new URL(uri.toString());
                        queryTask.execute(url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    n_error.setVisibility(View.GONE);
                }
                else{
                    n_error.setText(R.string.no_internet_connection);
                }


            }
        });


        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                whatshot.animate().translationY(0).start();
                arrow.animate().translationY(0).start();
                fav.animate().translationY(0).start();
                recyclerView.swapAdapter(databaseAdapter,false);

            }
        });



        SetupViewModel();

    }





    //This method were copied from  https://stackoverflow.com/a/4009133
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void SetupViewModel(){
        ViewModelMovies viewModelMovies = ViewModelProviders.of(this).get(ViewModelMovies.class);
        viewModelMovies.getListLiveData().observe(this, new Observer<List<Movies>>() {
            @Override
            public void onChanged(@Nullable List<Movies> movies) {
                databaseMovies.clear();
                databaseMovies.addAll(movies);
                databaseAdapter.notifyDataSetChanged();

            }
        });




    }






    public class QueryTask extends AsyncTask<URL, Void, String> {


        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String movieURL = null;

            try {
                movieURL = getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieURL;
        }


        @Override
        protected void onPostExecute(String MovieResults) {

                    if (MovieResults != null && !MovieResults.equals("")) {

                        moviesArrayList.clear();
                        recyclerView.swapAdapter(moviesRecyclerAdapter,false);
                        moviesRecyclerAdapter.notifyDataSetChanged();

                        try {
                            JSONObject jsonObject = new JSONObject(MovieResults);
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json = jsonArray.getJSONObject(i);
                                moviesArrayList.add(new Movies(json.getInt("id")
                                        ,json.getString("title"),
                                        json.getString("poster_path"),
                                        json.getString("overview"),
                                        json.getString("vote_average"),
                                        json.getString("release_date")));
                                moviesRecyclerAdapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

        }



        public  String getResponseFromHttpUrl(URL url) throws IOException {
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
