package com.example.android.bakingtime.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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

import static java.lang.System.load;

/**
 * Created by Amir on 10/14/2017.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private Context mContext;
    private ArrayList<Recepie.Steps> mSteps;

    // Define listener member variable
    private OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public StepAdapter(Activity context, ArrayList<Recepie.Steps> steps) {

        mContext = context;
        mSteps = steps;
    }
    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View recepieView = inflater.inflate(R.layout.steps_list_item,parent,false);
        return new StepViewHolder(recepieView);
    }
    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        Recepie.Steps currentstep = mSteps.get(position);
        holder.shortDscrpText.setText(currentstep.getShortDescription());
        // for accessibility
        holder.shortDscrpText.setContentDescription(currentstep.getShortDescription());
        String positionString = String.valueOf(position) + ": ";
        holder.stepNumberText.setText(positionString);

        String currentStepImageURL = currentstep.getThumbnaileURL();
        if (currentStepImageURL.isEmpty() || currentStepImageURL.endsWith(".mp4")) {
            holder.stepImageView.setImageResource(R.drawable.placeholder_category);
        } else{
            Picasso.with(mContext).load(currentStepImageURL).into(holder.stepImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_short_dscrp) TextView shortDscrpText;
        @BindView(R.id.tv_step_number) TextView stepNumberText;
        @BindView(R.id.iv_thumbnail) ImageView stepImageView;

        public StepViewHolder(final View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }

}


