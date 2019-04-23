package com.android.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Trailer> trailerArrayList;

    public TrailerAdapter(Context context, ArrayList<Trailer> trailerArrayList) {
        this.context = context;
        this.trailerArrayList = trailerArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.trailer_cardview,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.Trailer_name.setText(trailerArrayList.get(position).getName());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerArrayList.get(position).getKey()));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + trailerArrayList.get(position).getKey()));
                try {
                    context.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    context.startActivity(webIntent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return trailerArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView Trailer_name;
        public MyViewHolder(View itemView) {
            super(itemView);
            Trailer_name     = itemView.findViewById(R.id.trailer_name);
            cardView         = itemView.findViewById(R.id.trailer_cardview);
        }
    }
}
