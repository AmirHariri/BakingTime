package com.example.android.bakingtime.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.Recepie;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;

/**
 * Created by Amir on 10/14/2017.
 */

public class StepAdapter extends ArrayAdapter<Recepie.Steps> {

    private Context mContext;
    private ArrayList<Recepie.Steps> mSteps;

    public StepAdapter(Activity context, ArrayList<Recepie.Steps> steps) {
        super(context, 0, steps);
        mContext = context;
        mSteps = steps;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        StepViewHolder stepViewHolder;
        View listItemView = convertView;
        if(listItemView != null){
            stepViewHolder = (StepViewHolder) listItemView.getTag();
        }else{
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.steps_list_item, parent, false);
            stepViewHolder = new StepViewHolder(listItemView);
            listItemView.setTag(stepViewHolder);
        }
        Recepie.Steps currentstep = mSteps.get(position);
        stepViewHolder.shortDscrpText.setText(currentstep.getShortDescription());
        // for accessibility
        stepViewHolder.shortDscrpText.setContentDescription(currentstep.getShortDescription());
        String positionString = String.valueOf(position) + ": ";
        stepViewHolder.stepNumberText.setText(positionString);

        String currentStepImageURL = currentstep.getThumbnaileURL();

        if (currentStepImageURL.isEmpty() || currentStepImageURL.endsWith(".mp4")) {
            stepViewHolder.stepImageView.setImageResource(R.drawable.placeholder_category);
        } else{
            Picasso.with(getContext()).load(currentStepImageURL).into(stepViewHolder.stepImageView);
        }

        return listItemView;
    }
    static class StepViewHolder{
        @BindView(R.id.tv_short_dscrp) TextView shortDscrpText;
        @BindView(R.id.tv_step_number) TextView stepNumberText;
        @BindView(R.id.iv_thumbnail) ImageView stepImageView;
        public StepViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

}

//Usual way :
/*
View listItemView = convertView;
        if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(
                R.layout.steps_list_item, parent, false);
                }
                StepViewHolder stepViewHolder = new StepViewHolder();
                Recepie.Steps currentstep = mSteps.get(position);
                stepViewHolder.shortDscrpText = listItemView.findViewById(R.id.tv_short_dscrp);
                stepViewHolder.stepNumberText = listItemView.findViewById(R.id.tv_step_number);
                stepViewHolder.shortDscrpText.setText(currentstep.getShortDescription());
                String positionString = String.valueOf(position) + ": ";
                stepViewHolder.stepNumberText.setText(positionString);

                listItemView.setTag(stepViewHolder);
                return listItemView;
                }
private static class StepViewHolder{
    TextView shortDscrpText;
    TextView stepNumberText;
}
 */