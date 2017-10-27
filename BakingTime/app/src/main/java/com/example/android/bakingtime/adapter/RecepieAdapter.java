package com.example.android.bakingtime.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import static android.media.CamcorderProfile.get;

/**
 * Created by Amir on 10/10/2017.
 */

public class RecepieAdapter extends RecyclerView.Adapter<RecepieAdapter.RecepieViewHolder> {

    ArrayList<Recepie> mRecepies;
    Context mContext;
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

    public RecepieAdapter( Activity context, ArrayList<Recepie> recepies) {
        mContext = context;
        mRecepies = recepies;
    }



    @Override
    public RecepieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View recepieView = inflater.inflate(R.layout.list_item,parent,false);
        return new RecepieViewHolder(recepieView);
    }

    @Override
    public void onBindViewHolder(RecepieViewHolder holder, int position) {
        holder.mRecepieNameTextView.setText(mRecepies.get(position).getName());

        String currentRecepieImageUrl = mRecepies.get(position).getImage();
        if (!currentRecepieImageUrl.equals("")) {
            Picasso.with(mContext).load(currentRecepieImageUrl).into(holder.mRecepieImageView);
        }else{
            holder.mRecepieImageView.setImageResource(R.drawable.placeholder_for_recepie);
        }
    }

    @Override
    public int getItemCount() {
        return mRecepies.size();
    }


    public class RecepieViewHolder extends RecyclerView.ViewHolder{

        TextView mRecepieNameTextView;
        ImageView mRecepieImageView;

        public RecepieViewHolder(final View itemView) {
            super(itemView);
            mRecepieNameTextView = (TextView) itemView.findViewById(R.id.tv_recepie_name);
            mRecepieImageView = (ImageView) itemView.findViewById(R.id.iv_recepie);

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

    public List<Recepie> swapListRecepie(List<Recepie> c) {
        // check if this List<Recepie> is the same as the previous List<Recepie>
        if (mRecepies == c) {
            return null; // bc nothing has changed
        }
        ArrayList<Recepie> temp = mRecepies;
        mRecepies = (ArrayList<Recepie>) c; // new List<Recepie> value assigned

        //check if this is a valid List<Recepie>, then update the List<Recepie>
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }
}
