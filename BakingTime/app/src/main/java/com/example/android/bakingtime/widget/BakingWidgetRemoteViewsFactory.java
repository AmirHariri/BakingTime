package com.example.android.bakingtime.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingtime.MainActivity;
import com.example.android.bakingtime.R;
import com.example.android.bakingtime.Recepie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Amir on 10/24/2017.
 */

public class BakingWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String LOG_TAG = BakingWidgetRemoteViewsFactory.class.getSimpleName();
    private Context mContext;
    ArrayList<Recepie.Ingredient> mIngredientList;
    String mRecepieName;

    public BakingWidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
    }

    //get the info from SharedPreferences
    @Override
    public void onDataSetChanged() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        mRecepieName = preferences.getString(MainActivity.SHARED_PREFS_KEY_NAME, "");
        String jsonIngredient = preferences.getString(MainActivity.SHARED_PREFS_KEY_INGREDIENT, "");
        if (!jsonIngredient.equals("")) {
            Gson gson = new Gson();
            mIngredientList = gson.fromJson(jsonIngredient, new TypeToken<ArrayList<Recepie.Ingredient>>() {
            }.getType());
            Log.e(LOG_TAG, " Recepie names are : " + mRecepieName);
        }
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        if (mIngredientList.size() != 0) {
            return mIngredientList.size();
        } else {
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Recepie.Ingredient ingredient = mIngredientList.get(position);
        String measure = ingredient.getMeasure();
        String quantityStr = String.valueOf(ingredient.getQuantity());
        String ingredientName = ingredient.getSingleIngredient();

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.recepie_widget_list_item);
        rv.setTextViewText(R.id.ingredient_quantity, quantityStr);
        rv.setTextViewText(R.id.ingredient_measure, measure);
        rv.setTextViewText(R.id.ingredient_name, ingredientName);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

