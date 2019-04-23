package com.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Reviews> reviewsArrayList;

    public ReviewsAdapter(Context context, ArrayList<Reviews> reviewsArrayList) {
        this.context = context;
        this.reviewsArrayList = reviewsArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.reviews_cardview,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.author.setText(reviewsArrayList.get(position).getAuthor());
        holder.content.setText(reviewsArrayList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviewsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView content;

        public MyViewHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.r_author_name);
            content = itemView.findViewById(R.id.r_content);
        }
    }
}
