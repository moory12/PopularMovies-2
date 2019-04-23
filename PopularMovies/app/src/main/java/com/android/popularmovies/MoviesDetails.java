package com.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MoviesDetails extends AppCompatActivity {

    FloatingActionButton more;
    FloatingActionButton fav;
    FloatingActionButton trailer;
    FloatingActionButton reviews;
    Movies movies;
    int counter = 0;
    Context context;

    private AppDatabase database;
    private boolean exsit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);
        context = this;


        database = AppDatabase.getInstance(getApplicationContext());

        final Intent intent = getIntent();

        movies = intent.getParcelableExtra("Movies");

        DatabaseTask databaseTask = new DatabaseTask();
        databaseTask.execute();


        TextView title = findViewById(R.id.title);
        TextView rating = findViewById(R.id.rating);
        ImageView poster = findViewById(R.id.poster);
        TextView plot_synopsis = findViewById(R.id.plot_synopsis);
        TextView release_date = findViewById(R.id.relaese_date);
        more = findViewById(R.id.fab_more);
        fav  = findViewById(R.id.fab_favorite);
        trailer = findViewById(R.id.fab_trailer);
        reviews = findViewById(R.id.fab_reviews);




        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter == 0){
                 fav.animate().translationY(-250).start();
                 trailer.animate().translationY(-500).start();
                 reviews.animate().translationY(-750).start();
                 counter++;
                }else{
                    fav.animate().translationY(0).start();
                    trailer.animate().translationY(0).start();
                    reviews.animate().translationY(0).start();
                    counter = 0;
                }
            }
        });

        trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context,TrailerActivity.class);
                intent1.putExtra("id",movies.id);
                fav.animate().translationY(0).start();
                trailer.animate().translationY(0).start();
                reviews.animate().translationY(0).start();
                counter = 0;
                startActivity(intent1);
            }
        });

        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context,ReviewsActivity.class);
                intent1.putExtra("id",movies.id);
                fav.animate().translationY(0).start();
                trailer.animate().translationY(0).start();
                reviews.animate().translationY(0).start();
                counter = 0;
                startActivity(intent1);
            }
        });

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseO databaseO = new DatabaseO();
                databaseO.execute();
            }
        });



        title.setText(movies.getTitle());
        rating.setText(movies.getVote_average());
        plot_synopsis.setText(movies.getOverview());
        release_date.setText(movies.getRelease_date());
        Glide.with(this).
                load("http://image.tmdb.org/t/p/w185/"+movies.getUrlToPoster()).
                into(poster);


    }

    public class DatabaseTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            Movies temp = database.movieDao().getMovie(movies.movieId);
            if(temp!=null){
                movies =  temp;
                exsit = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(exsit){
                fav.setImageResource(R.drawable.ic_star_white_24dp);
            }
        }
    }

    public  class  DatabaseO extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            if(exsit){
                Movies temp = database.movieDao().getMovie(movies.movieId);
                database.movieDao().deleteMovie(temp);
                exsit =false;
            }
            else{
                Movies temp = new Movies(movies.movieId,movies.title,movies.urlToPoster,movies.overview,movies.vote_average,movies.release_date);
                database.movieDao().insertMovie(temp);
                movies = temp;
                exsit = true;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(exsit){
                fav.setImageResource(R.drawable.ic_star_white_24dp);

            }
            else {
                fav.setImageResource(R.drawable.ic_star_border_white_24dp);
            }

        }
    }




}
