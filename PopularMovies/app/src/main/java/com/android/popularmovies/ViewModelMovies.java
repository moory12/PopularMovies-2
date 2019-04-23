package com.android.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class ViewModelMovies extends AndroidViewModel {

    private LiveData<List<Movies>> listLiveData;

    public ViewModelMovies(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(this.getApplication());
        listLiveData = appDatabase.movieDao().moviesList();
    }

    public LiveData<List<Movies>> getListLiveData() {
        return listLiveData;
    }
}
