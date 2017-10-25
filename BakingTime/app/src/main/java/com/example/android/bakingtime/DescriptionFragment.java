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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * Created by Amir on 10/16/2017.
 */

public class DescriptionFragment extends Fragment {
    public DescriptionFragment(){}

    int stepNumber;
    ArrayList<Recepie.Steps> steps;
    OnChangeStepClickListener mCallback;
    public Button previousButton;
    public Button nextButton;
    public TextView textView;
    public boolean singlePane = true;
    private static final String LOG_TAG = RecepieDetailActivity.class.getSimpleName();

    public interface OnChangeStepClickListener{
        void onStepChanged(int newStep);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(singlePane) {
            Intent i = getActivity().getIntent();
            steps = (ArrayList<Recepie.Steps>) getActivity().getIntent().getSerializableExtra("RecepieSteps");
            stepNumber = i.getIntExtra("StepNumber", 0);
        }
        Log.e(LOG_TAG,"steps are :" + steps.get(stepNumber).getDescription());
        String currentStepDscrp = steps.get(stepNumber).getDescription();
        View rootViewDescriotion = inflater.inflate(R.layout.fragment_description,container, false);

        textView = rootViewDescriotion.findViewById(R.id.tv_description);
        textView.setText(currentStepDscrp);
        textView.setContentDescription(currentStepDscrp);

        previousButton = rootViewDescriotion.findViewById(R.id.btn_previous);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newNumber = previousStepNumber();
                textView.setText(getNewDescription(newNumber));
                textView.setContentDescription(getNewDescription(newNumber));
                mCallback.onStepChanged(newNumber);
            }
        });

        nextButton = rootViewDescriotion.findViewById(R.id.btn_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newNumber = nextStepNumber();
                textView.setText(getNewDescription(newNumber));
                textView.setContentDescription(getNewDescription(newNumber));
                mCallback.onStepChanged(newNumber);
            }
        });

        if(!singlePane){
            nextButton.setVisibility(View.INVISIBLE);
            previousButton.setVisibility(View.INVISIBLE);
        }
        return rootViewDescriotion;
    }

    public int nextStepNumber(){
        if (stepNumber < steps.size()-1) {
            return ++stepNumber;
        }else{
            Toast.makeText(getActivity(), "You are done! This is the last step",Toast.LENGTH_SHORT).show();
            return stepNumber;
        }
    }
    public int previousStepNumber(){
        if (stepNumber>0){
            return --stepNumber;
        }else{
            Toast.makeText(getActivity(), "This is the first step",Toast.LENGTH_SHORT).show();
            return stepNumber;
        }

   }
    public String getNewDescription(int stepNumber){
        String newDescription = steps.get(stepNumber).getDescription();
        return newDescription;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback =(OnChangeStepClickListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"Please implement OnChangeStepClickListener");
        }
    }

}
