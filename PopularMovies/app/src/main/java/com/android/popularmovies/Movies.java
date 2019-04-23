package com.android.popularmovies;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;


@Entity(tableName = "Movies")
public class Movies implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    int id;
    int movieId;
    String title;
    String urlToPoster;
    String overview;
    String vote_average;
    String release_date;


    @Ignore
    public Movies() {
    }


    public Movies(int id, int movieId, String title, String urlToPoster, String overview, String vote_average, String release_date) {
        this.id = id;
        this.movieId = movieId;
        this.title = title;
        this.urlToPoster = urlToPoster;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
    }

    @Ignore
    public Movies(int movieId, String title, String urlToPoster, String overview, String vote_average, String release_date) {
        this.movieId = movieId;
        this.title = title;
        this.urlToPoster = urlToPoster;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
    }

    protected Movies(Parcel in) {
        id = in.readInt();
        movieId = in.readInt();
        title = in.readString();
        urlToPoster = in.readString();
        overview = in.readString();
        vote_average = in.readString();
        release_date = in.readString();
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlToPoster() {
        return urlToPoster;
    }

    public void setUrlToPoster(String urlToPoster) {
        this.urlToPoster = urlToPoster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(movieId);
        dest.writeString(title);
        dest.writeString(urlToPoster);
        dest.writeString(overview);
        dest.writeString(vote_average);
        dest.writeString(release_date);
    }
}
