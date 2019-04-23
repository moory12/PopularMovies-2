package com.android.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;




import java.util.List;


@Dao
public interface MovieDao {

    @Query("SELECT * FROM Movies")
    LiveData<List<Movies>> moviesList();

    @Insert
    void insertMovie(Movies movies);

    @Delete
    void deleteMovie(Movies movies);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movies moives);


    @Query("SELECT * FROM Movies WHERE movieId = :id")
    Movies getMovie(int id);


}
