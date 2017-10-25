package com.example.android.bakingtime;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.bakingtime.adapter.RecepieAdapter;
import com.example.android.bakingtime.widget.BakingWidgetProvider;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
                                        LoaderManager.LoaderCallbacks<List<Recepie>>{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int RECEPIE_LOADER_ID = 0;
    private static final String RECEPIE_LISTING_URL_STRING =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private RecepieAdapter mRecepieAdapter;
    private GridView mRecepieGridView;
    private Recepie ingredientForWidget;
    public static final String SHARED_PREFS_KEY_NAME = "SHARED_PREFS_KEY_NAME";
    public static final String SHARED_PREFS_KEY_INGREDIENT = "SHARED_PREFS_KEY_INGREDIENT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Recepie> recepies = new ArrayList<>();
        mRecepieAdapter = new RecepieAdapter(this, recepies);
        mRecepieGridView = (GridView) findViewById(R.id.grid_view_recepies);
        mRecepieGridView.setAdapter(mRecepieAdapter);

        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(RECEPIE_LOADER_ID, null, this).forceLoad();

        mRecepieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //find the current Recepie that was clicked on
                Recepie currentRecepie = mRecepieAdapter.getItem(position);
                Intent recepieIntent = new Intent(MainActivity.this, RecepieDetailActivity.class);
                recepieIntent.putExtra("CurrentRecepie", currentRecepie);
                ingredientForWidget = currentRecepie;
                startActivity(recepieIntent);

                sendDataToWidget();
            }
        });




    }

    @Override
    public Loader<List<Recepie>> onCreateLoader(int i, Bundle bundle) {

        return new RecepieLoader(this, RECEPIE_LISTING_URL_STRING);
    }

    @Override
    public void onLoadFinished(Loader<List<Recepie>> loader, List<Recepie> recepies) {

        mRecepieAdapter.clear();
        // If there is a valid list of {@link Recepie}s, then add them to the adapter's
        // data set. This will trigger the GridView to update.
        if (recepies != null && !recepies.isEmpty()) {
            mRecepieAdapter.addAll(recepies);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Recepie>> loader) {
        mRecepieAdapter.clear();
    }


    private void sendDataToWidget() {
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
}
