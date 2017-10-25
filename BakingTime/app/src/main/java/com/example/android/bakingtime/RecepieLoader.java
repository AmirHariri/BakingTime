package com.example.android.bakingtime;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;

import java.util.List;

/**
 * Created by Amir on 10/10/2017.
 */

class RecepieLoader extends AsyncTaskLoader<List<Recepie>> {

    private String mUrl;
    public RecepieLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Recepie> loadInBackground() {
        if (mUrl == null) {
            return null;
        } else{
            // Perform the network request, parse the response, and extract a list of movies.
            return QueryUtils.fetchRecepieData(mUrl);

        }
    }
}
