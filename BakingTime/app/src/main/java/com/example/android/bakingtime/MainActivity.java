package com.example.android.bakingtime;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.bakingtime.adapter.RecepieAdapter;
import com.example.android.bakingtime.widget.BakingWidgetProvider;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity implements
                                        LoaderManager.LoaderCallbacks<List<Recepie>>{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int RECEPIE_LOADER_ID = 0;
    private static final String RECEPIE_LISTING_URL_STRING =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private RecepieAdapter mRecepieAdapter;
    private RecyclerView mRecepieRecyclerView;
    private Recepie ingredientForWidget;
    public static final String SHARED_PREFS_KEY_NAME = "SHARED_PREFS_KEY_NAME";
    public static final String SHARED_PREFS_KEY_INGREDIENT = "SHARED_PREFS_KEY_INGREDIENT";
    ArrayList<Recepie> recepies;
    int TABLET_COLUMN = 2;
    int SMART_PONE_CULOMN =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recepies = new ArrayList<>();
        mRecepieAdapter = new RecepieAdapter(this, recepies);
        mRecepieRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_recepies);
        mRecepieRecyclerView.setAdapter(mRecepieAdapter);
        mRecepieRecyclerView.setHasFixedSize(true);
        mRecepieRecyclerView.setLayoutManager
                (new GridLayoutManager(this,returnGridViewColumnBaseOnScreenSize()));
        mRecepieAdapter.setOnItemClickListener(new RecepieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Recepie currentRecepie = recepies.get(position);
                Intent recepieIntent = new Intent(MainActivity.this, RecepieDetailActivity.class);
                recepieIntent.putExtra("CurrentRecepie", currentRecepie);
                ingredientForWidget = currentRecepie;
                startActivity(recepieIntent);

                sendDataToWidget();
            }
        });

        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(RECEPIE_LOADER_ID, null, this).forceLoad();





    }

    @Override
    public Loader<List<Recepie>> onCreateLoader(int i, Bundle bundle) {

        return new RecepieLoader(this, RECEPIE_LISTING_URL_STRING);
    }

    @Override
    public void onLoadFinished(Loader<List<Recepie>> loader, List<Recepie> data) {

        mRecepieAdapter.swapListRecepie(data);
        recepies = (ArrayList<Recepie>) data;

/*
        mRecepieRecyclerView.clear();
        // If there is a valid list of {@link Recepie}s, then add them to the adapter's
        // data set. This will trigger the GridView to update.
        if (recepies != null && !recepies.isEmpty()) {
            mRecepieAdapter.addAll(recepies);
        }*/

    }

    @Override
    public void onLoaderReset(Loader<List<Recepie>> loader) {
        mRecepieAdapter.swapListRecepie(null);
    }


    public void sendDataToWidget() {
        ArrayList<Recepie.Ingredient> mIngredientArrayList = null;

        String recepieName = ingredientForWidget.getName();
        mIngredientArrayList = ingredientForWidget.getIngredient();

        Gson gson = new Gson();
        String jsonIngredient = gson.toJson(mIngredientArrayList);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SHARED_PREFS_KEY_NAME, recepieName).apply();
        editor.putString(SHARED_PREFS_KEY_INGREDIENT, jsonIngredient).apply();
        Log.e(LOG_TAG," the name that has passed in to widget is : " + recepieName+ jsonIngredient);
        //refresh the widget after openning the app
        BakingWidgetProvider.sendRefreshBroadcast(this);
    }
    public int returnGridViewColumnBaseOnScreenSize(){
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=6.5){
            return TABLET_COLUMN;
        }else{
            return SMART_PONE_CULOMN;
        }
    }
}
