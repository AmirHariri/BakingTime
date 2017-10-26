package com.example.android.bakingtime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.bakingtime.adapter.IngredientAdapter;
import com.example.android.bakingtime.adapter.StepAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amir on 10/13/2017.
 */

public class RecepieDetailListFragment extends Fragment {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    Recepie mRecepie;
    OnStepClickListener mCallback;
    // Mandatory empty constructor
    public RecepieDetailListFragment(){

    }
    public interface OnStepClickListener{
        void onStepSelected(int position);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        mRecepie = (Recepie) intent.getSerializableExtra("RecepieData");

        View rootView = inflater.inflate(R.layout.fragment_recepie_detail_list,container,false);

        ArrayList<Recepie.Ingredient> ingredients = mRecepie.getIngredient();
        IngredientAdapter ingredientAdapter = new IngredientAdapter(this.getActivity() , ingredients);
        ListView ingredientsListView =(ListView) rootView.findViewById(R.id.lv_ingredients);
        ingredientsListView.setAdapter(ingredientAdapter);

        ArrayList<Recepie.Steps> steps = mRecepie.getSteps();
        StepAdapter stepAdapter = new StepAdapter(this.getActivity(), steps);
        ListView stepsListView =(ListView) rootView.findViewById(R.id.lv_steps);
        stepsListView.setAdapter(stepAdapter);

        stepsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mCallback.onStepSelected(position);
            }
        });

        for (int i= 0; i<ingredients.size();i++) {
            Log.v(LOG_TAG, "Ingredients are : " + ingredients.get(i).getSingleIngredient());
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback =(OnStepClickListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"Please implement OnStepClickListener");
        }
    }
}
