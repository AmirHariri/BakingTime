package com.example.android.bakingtime.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Movie;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.Recepie;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.resource;

/**
 * Created by Amir on 10/10/2017.
 */

public class RecepieAdapter extends ArrayAdapter<Recepie> {

    ArrayList<Recepie> mRecepies;

    public RecepieAdapter( Activity context, ArrayList<Recepie> recepies) {
        super(context,0, recepies);
        mRecepies = recepies;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
            TextView recepieNameTextView = (TextView) listItemView.findViewById(R.id.tv_recepie_name);


            recepieNameTextView.setText(mRecepies.get(position).getName());
            recepieNameTextView.setContentDescription(mRecepies.get(position).getName());
        }
        Recepie currentRecepie = getItem(position);
        return listItemView;

    }
}
