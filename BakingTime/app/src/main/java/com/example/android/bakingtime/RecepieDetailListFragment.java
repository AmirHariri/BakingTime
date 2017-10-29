package com.example.android.bakingtime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingtime.adapter.IngredientAdapter;
import com.example.android.bakingtime.adapter.StepAdapter;

import java.util.ArrayList;

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
        RecyclerView ingredientsRecyclerView =(RecyclerView) rootView.findViewById(R.id.recycler_view_ingredients);
        ingredientsRecyclerView.setAdapter(ingredientAdapter);
        ingredientsRecyclerView.setHasFixedSize(true);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final ArrayList<Recepie.Steps> steps = mRecepie.getSteps();
        StepAdapter stepAdapter = new StepAdapter(this.getActivity(), steps);
        RecyclerView stepsRecyclerView =(RecyclerView) rootView.findViewById(R.id.recycler_view_steps);
        stepsRecyclerView.setAdapter(stepAdapter);
        stepsRecyclerView.setHasFixedSize(true);
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        stepAdapter.setOnItemClickListener(new StepAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
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
